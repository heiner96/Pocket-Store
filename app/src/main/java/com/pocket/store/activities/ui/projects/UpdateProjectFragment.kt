package com.pocket.store.activities.ui.projects

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.pocket.store.R
import com.pocket.store.databinding.FragmentUpdateProjectBinding
import com.pocket.store.model.Project
import com.pocket.store.viewmodel.ProjectViewModel

class UpdateProjectFragment : Fragment()
{
    private var _binding: FragmentUpdateProjectBinding? = null
    private val binding get() = _binding!!
    private lateinit var projectUpdateViewModel: ProjectViewModel
    private val usuario = Firebase.auth.currentUser?.email.toString()

    //Defino un argumento para obtener los argumentos pasados las fragmento
    private val args by navArgs<UpdateProjectFragmentArgs>()

    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        projectUpdateViewModel = ViewModelProvider(this).get(ProjectViewModel::class.java)
        _binding = FragmentUpdateProjectBinding.inflate(inflater, container, false)

        binding.etNombreProject.setText(args.updateProject.nombre.toUpperCase())
        binding.etNombreClienteUpdate.setText(args.updateProject.nombreCliente)
        binding.tvLongitud.text = args.updateProject.longitud.toString()
        binding.tvLatitud.text = args.updateProject.latitud.toString()
        binding.tvAltura.text = args.updateProject.altura.toString()
        binding.btUpdateObjeto.setOnClickListener{
            updateProject()
        }
        binding.btLocation.setOnClickListener{
            verMapa()
        }
        binding.btWase.setOnClickListener{
            if(isPackageInstalled("com.waze",requireContext().packageManager))
            {
                usarWase(args.updateProject.latitud,args.updateProject.longitud)
            }
            else
            {
                Toast.makeText(requireContext(),getString(R.string.requiere_wase_instalado), Toast.LENGTH_LONG ).show()
            }

        }
        if(args.updateProject.ruta_audio?.isNotEmpty() == true){
            mediaPlayer = MediaPlayer()
            mediaPlayer.setDataSource(args.updateProject.ruta_audio)
            mediaPlayer.prepare()
            binding.btPlay.isEnabled = true
        }
        else{
            binding.btPlay.isEnabled = false
        }

        if(args.updateProject.ruta_imagen?.isNotEmpty() == true){
            Glide.with(requireContext())
                .load(args.updateProject.ruta_imagen)
                .fitCenter()
                .into(binding.imagen)
        }
        binding.btPlay.setOnClickListener{
            mediaPlayer.start()
        }

        return binding.root

    }

    private fun usarWase(latitude: Double?,lontitude: Double?) {

        try {
            var url: String? = ""

            url = java.lang.String.format(
                getString(R.string.wase_ir),
                latitude.toString(),
                lontitude.toString())
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)

        } catch (ex: ActivityNotFoundException) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.wase_descargar)))
            startActivity(intent)
        } catch (ex: Exception) {

        }

    }

    private fun verMapa()
    {
        val latitud = binding.tvLatitud.text.toString().toDouble()
        val longitud = binding.tvLongitud.text.toString().toDouble()
        if( latitud.isFinite() && longitud.isFinite() ){ //puedo ver el lugar en el mapa
            val uri = Uri.parse("geo:$latitud , $longitud?z14")
            val intent = Intent(Intent.ACTION_VIEW,uri)
            startActivity(intent)
        }
        else{
            Toast.makeText(requireContext(),getString(R.string.msg_data), Toast.LENGTH_LONG).show()
        }
    }


    private fun isPackageInstalled(packageName: String, packageManager: PackageManager): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    private fun updateProject() {
        val nombre = binding.etNombreProject.text.toString().toUpperCase()
        if(nombre.isNotEmpty()){//se puede agregar un lugar
            val nombreCliente = binding.etNombreClienteUpdate.text.toString()

            val project = Project(args.updateProject.id,
                nombre,nombreCliente,args.updateProject.ruta_imagen,
                args.updateProject.latitud,args.updateProject.longitud,args.updateProject.altura,
                args.updateProject.ruta_audio,)
            projectUpdateViewModel.saveProject(project)
            Toast.makeText(requireContext(),getText(R.string.msg_project_updated), Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateProjectFragment_to_projectsFragment)
        }
        else{//sino no se puede modificar el project


            Toast.makeText(requireContext(),getText(R.string.msg_data), Toast.LENGTH_LONG).show()

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}