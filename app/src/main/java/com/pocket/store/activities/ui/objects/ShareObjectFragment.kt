package com.pocket.store.activities.ui.objects

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
import com.pocket.store.R
import com.pocket.store.databinding.FragmentShareObjectBinding
import com.pocket.store.model.Objeto
import com.pocket.store.viewmodel.ObjectViewModel


class ShareObjectFragment : Fragment() {
    private var _binding: FragmentShareObjectBinding? = null
    private val binding get() = _binding!!
    private lateinit var objetosViewModel: ObjectViewModel


    //Defino un argumento para obtener los argumentos pasados las fragmento
    private val args by navArgs<ShareObjectFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        objetosViewModel = ViewModelProvider(this).get(ObjectViewModel::class.java)
        _binding = FragmentShareObjectBinding.inflate(inflater, container, false)
        binding.btShareObjeto.setOnClickListener {
            shareWithFriend(args.shareObject)
        }
        binding.etNombre.setText(args.shareObject.nombre)
        binding.etPrecioUpdate.setText(args.shareObject.precio.toString())
        binding.etCorreoTienda.setText(args.shareObject.correo)
        binding.etWeb.setText(args.shareObject.web)
        if(args.shareObject.ruta_imagen?.isNotEmpty() == true){
            Glide.with(requireContext())
                .load(args.shareObject.ruta_imagen)
                .fitCenter()
                .into(binding.imagen)
        }
        binding.etCorreoShare.requestFocus()

        return binding.root
    }

    private fun shareWithFriend(objetoArgumento: Objeto) {
        if (binding.etCorreoShare.text.toString().trim().isNotEmpty()){
            subeObjetos(objetoArgumento,binding.etCorreoShare.text.toString())
        }
        else{
            Toast.makeText(requireContext(), getText(R.string.msg_share_friend_error_data), Toast.LENGTH_LONG).show()
        }
    }


    private fun subeObjetos(objetoArgumento: Objeto,friendEmail: String )
    {
        try {
            objetosViewModel.saveObjetos(objetoArgumento,friendEmail,"compartidos")
            Toast.makeText(requireContext(), getText(R.string.msg_shared_friend), Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_shareObjectFragment_to_nav_objects)
        }
        catch (ex: Exception){
            Toast.makeText(requireContext(),getText(R.string.msg_share_friend_error_not_registered),Toast.LENGTH_LONG).show()
            ex.printStackTrace()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}