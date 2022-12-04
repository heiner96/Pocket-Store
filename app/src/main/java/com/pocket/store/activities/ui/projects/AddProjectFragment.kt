package com.pocket.store.activities.ui.projects

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.pocket.store.R
import com.pocket.store.databinding.FragmentAddProjectBinding
import com.pocket.store.model.Project
import com.pocket.store.utiles.AudioUtiles
import com.pocket.store.utiles.ImagenUtiles
import com.pocket.store.viewmodel.ProjectViewModel


class AddProjectFragment : Fragment() {
    private var _binding: FragmentAddProjectBinding? = null
    private val binding get() = _binding!!
    private lateinit var projectViewModel: ProjectViewModel

    //para grabar audios
    private lateinit var audioUtiles: AudioUtiles

    //para capturar la imagen del lugar...
    private lateinit var tomarFotoActivity: ActivityResultLauncher<Intent>
    private lateinit var imagenUtiles: ImagenUtiles
    private val usuario = Firebase.auth.currentUser?.email.toString()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        projectViewModel = ViewModelProvider(this).get(ProjectViewModel::class.java)
        _binding = FragmentAddProjectBinding.inflate(inflater, container, false)
        binding.btAddProject.setOnClickListener {
            binding.progressBar.visibility = ProgressBar.VISIBLE
            binding.msgMensaje.visibility = View.VISIBLE
            binding.msgMensaje.text = getString(R.string.msg_subiendo_audio)

            subeAudio()
        }
        activaGPS()
        //se inicializa el objeto audioUtiles
        audioUtiles = AudioUtiles(
            requireActivity(),
            requireContext(),
            binding.btAccion,
            binding.btPlay,
            binding.btDelete,
            getString(R.string.msg_graba_audio),
            getString(R.string.msg_detener_audio)
        )
        //se inicializa el activity para tomar foto
        tomarFotoActivity = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                imagenUtiles.actualizaFoto()
            }
        }
        //se inicializa para gestionar foto del lugar
        imagenUtiles = ImagenUtiles(
            requireContext(),
            binding.btPhoto,
            binding.btRotaL,
            binding.btRotaR,
            binding.imagen,
            tomarFotoActivity
        )
        return binding.root
    }

    private fun subeAudio() {
        val audioFile = audioUtiles.audioFile
        if (audioFile.exists() && audioFile.isFile && audioFile.canRead()) {
            val rutaLocal = Uri.fromFile(audioFile)
            val rutaNube = "objetosAPP/${Firebase.auth.currentUser?.email}/audios/${audioFile.name}"
            val referencia: StorageReference = Firebase.storage.reference.child(rutaNube)
            referencia.putFile(rutaLocal)
                .addOnSuccessListener {
                    referencia.downloadUrl.addOnSuccessListener {
                        //si ya genero la ruta publica para este archivo
                        val rutaPublicaAudio = it.toString()
                        subeImagen(rutaPublicaAudio)
                    }
                }
                .addOnCanceledListener {
                    subeImagen("")
                }
        }
        else
        {
            subeImagen("")
        }
    }

    private fun subeImagen(rutaPublicaAudio: String) {
        binding.msgMensaje.text = getText(R.string.msg_subiendo_imagen)
        val imagenFile = imagenUtiles.imagenFile
        if (imagenFile.exists() && imagenFile.isFile && imagenFile.canRead()) {
            val rutaLocal = Uri.fromFile(imagenFile)
            val rutaNube = "objetosAPP/${Firebase.auth.currentUser?.email}/imagenes/${imagenFile.name}"
            val referencia: StorageReference = Firebase.storage.reference.child(rutaNube)
            referencia.putFile(rutaLocal)
                .addOnSuccessListener {
                    referencia.downloadUrl.addOnSuccessListener {
                        //si ya genero la ruta publica para este archivo
                        val rutaPublicaImagen = it.toString()
                        subeProjects(rutaPublicaAudio,rutaPublicaImagen)
                    }
                }
                .addOnCanceledListener {
                    subeProjects(rutaPublicaAudio,"")
                }
        }
        else
        {
            subeProjects(rutaPublicaAudio,"")
        }
    }

    private fun activaGPS() {
        //se solicita los permisos para la llamada
        if (requireActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED &&
            requireActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED
        ) //se debe perdir los permisos
        {//no tiene permisos otorgados se deben pedir
            //pedir permisos
            requireActivity().requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), 105
            )
        } else {
            //se tienen los permisos oara activar el GPS
            val ubicacion: FusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(requireContext())
            ubicacion.lastLocation.addOnSuccessListener {
                if (it != null) {
                    binding.tvLatitud.text = "${it.latitude}"
                    binding.tvLongitud.text = "${it.longitude}"
                    binding.tvAltura.text = "${it.altitude}"
                } else {
                    binding.tvLatitud.text = "0.0"
                    binding.tvLongitud.text = "0.0"
                    binding.tvAltura.text = "0.0"
                }
            }
        }
    }

    private fun subeProjects(rutaPublicaAudio: String, rutaPublicaImagen: String) {
        val nombre = binding.etNombreProject.text.toString().toUpperCase()
        if (nombre.isNotEmpty()) {//se puede agregar un project
            binding.msgMensaje.text = getString(R.string.subiendo_proyecto)
            val nombreCliente = binding.etNombreCliente.text.toString()
            val latitud = binding.tvLatitud.text.toString().toDouble()
            val longitud = binding.tvLongitud.text.toString().toDouble()
            val altura = binding.tvAltura.text.toString().toDouble()
            val objeto = Project("", nombre, nombreCliente,rutaPublicaImagen, latitud, longitud, altura, rutaPublicaAudio)
            projectViewModel.saveProject(objeto)
            Toast.makeText(requireContext(), getText(R.string.msg_project_added), Toast.LENGTH_SHORT).show()
            binding.msgMensaje.visibility = View.GONE
            findNavController().navigate(R.id.action_addProjectFragment_to_projectsFragment)
        } else {//sino no se puede agregar el project


            Toast.makeText(requireContext(), getText(R.string.msg_data), Toast.LENGTH_LONG).show()

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}