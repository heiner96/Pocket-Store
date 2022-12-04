package com.pocket.store.activities.ui.projectObjects

import android.Manifest
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
import com.pocket.store.activities.ui.objects.UpdateObjectFragmentArgs
import com.pocket.store.databinding.FragmentConfirmProjectObjectBinding
import com.pocket.store.databinding.FragmentUpdateObjetBinding
import com.pocket.store.model.Objeto
import com.pocket.store.viewmodel.ObjectViewModel
import com.pocket.store.viewmodel.ProjectObjectsViewModel

class ConfirmProjectObjectFragment : Fragment() {

    private var _binding: FragmentConfirmProjectObjectBinding? = null
    private val binding get() = _binding!!
    private lateinit var projectObjectViewModel: ProjectObjectsViewModel
    private val usuario = Firebase.auth.currentUser?.email.toString()

    //Defino un argumento para obtener los argumentos pasados las fragmento
    private val args by navArgs<ConfirmProjectObjectFragmentArgs>()

    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        projectObjectViewModel =
            ViewModelProvider(this).get(ProjectObjectsViewModel::class.java)
        _binding = FragmentConfirmProjectObjectBinding.inflate(inflater, container, false)

        binding.tvNombreProject.setText(args.confirmProject.nombre.toUpperCase())
        binding.tvClienteProject.setText(args.confirmProject.nombreCliente)
        binding.tvNombre.setText(args.confirmObject.nombre.toString())
        binding.tvTelefono.setText(args.confirmObject.telefono)
        binding.tvCorreoLugarFila.setText(args.confirmObject.correo)
        Glide.with(requireContext())
            .load(args.confirmProject.ruta_imagen)
            .circleCrop()
            .into(binding.imagenProject)

        Glide.with(requireContext())
            .load(args.confirmObject.ruta_imagen)
            .circleCrop()
            .into(binding.imagen)

        binding.btConfirmObbjectToProject.setOnClickListener{
            projectObjectViewModel.saveObjectProject(args.confirmProject,args.confirmObject)
            Toast.makeText(requireContext(),getString(R.string.msg_project_object_confirm_success), Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_confirmProjectObjectFragment_to_projectObjectsViewFragment)
        }
        return binding.root

    }


}