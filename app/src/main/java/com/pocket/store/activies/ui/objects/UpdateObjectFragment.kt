package com.pocket.store.activies.ui.objects

import android.Manifest
import android.app.AlertDialog
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
import com.pocket.store.R
import com.pocket.store.databinding.FragmentUpdateObjetBinding
import com.pocket.store.model.Objeto
import com.pocket.store.viewmodel.ObjectViewModel


class UpdateObjectFragment : Fragment() {
    private var _binding: FragmentUpdateObjetBinding? = null
    private val binding get() = _binding!!
    private lateinit var objectViewModel: ObjectViewModel

    //Defino un argumento para obtener los argumentos pasados las fragmento
    private val args by navArgs<UpdateObjectFragmentArgs>()

    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        objectViewModel = ViewModelProvider(this).get(ObjectViewModel::class.java)
        _binding = FragmentUpdateObjetBinding.inflate(inflater, container, false)

        binding.etNombre.setText(args.objetoArgumento.nombre.toUpperCase())
        binding.etCorreoTienda.setText(args.objetoArgumento.correo)
        binding.etPrecioUpdate.setText(args.objetoArgumento.precio.toString())
        binding.etTelefonoTienda.setText(args.objetoArgumento.telefono)
        binding.etWeb.setText(args.objetoArgumento.web)
        binding.tvLongitud.text = args.objetoArgumento.longitud.toString()
        binding.tvLatitud.text = args.objetoArgumento.latitud.toString()
        binding.tvAltura.text = args.objetoArgumento.altura.toString()
        binding.btEmail.setOnClickListener{
            escribirCorreo()
        }
        binding.btUpdateObjeto.setOnClickListener{
            updateObject()
        }
        binding.btDeleteObjeto.setOnClickListener{
            deleteObject()
        }
        binding.btPhone.setOnClickListener{
            llamarLugar()
        }
        binding.btWhatsapp.setOnClickListener{
            mensajeWhastApps()
        }
        binding.btWeb.setOnClickListener{
            verWeb()
        }
        binding.btLocation.setOnClickListener{
            verMapa()
        }
        if(args.objetoArgumento.ruta_audio?.isNotEmpty() == true){
            mediaPlayer = MediaPlayer()
            mediaPlayer.setDataSource(args.objetoArgumento.ruta_audio)
            mediaPlayer.prepare()
            binding.btPlay.isEnabled = true
        }
        else{
            binding.btPlay.isEnabled = false
        }

        if(args.objetoArgumento.ruta_imagen?.isNotEmpty() == true){
            Glide.with(requireContext())
                .load(args.objetoArgumento.ruta_imagen)
                .fitCenter()
                .into(binding.imagen)
        }
        binding.btPlay.setOnClickListener{
            mediaPlayer.start()
        }
        return binding.root

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

    private fun verWeb()
    {
        val para = binding.etWeb.text.toString()
        if(para.isNotEmpty()){ //puedo enviar Mensaje WhatsApp
            val uri = Uri.parse("http://$para")
            val intent = Intent(Intent.ACTION_VIEW,uri)
            startActivity(intent)
        }
        else{
            Toast.makeText(requireContext(),getString(R.string.msg_data), Toast.LENGTH_LONG).show()
        }
    }

    private fun mensajeWhastApps()
    {
        val para = binding.etTelefonoTienda.text.toString()
        if(para.isNotEmpty()){ //puedo enviar Mensaje WhatsApp
            val intent = Intent(Intent.ACTION_VIEW)
            val uri = "whatsapp://send?phone506$para&text=" + getString(R.string.msg_saludos)
            intent.setPackage("com.whatsapp")
            intent.data = Uri.parse(uri)
            startActivity(intent)

        }
        else{
            Toast.makeText(requireContext(),getString(R.string.msg_data), Toast.LENGTH_LONG).show()
        }
    }

    private fun llamarLugar()
    {
        val para = binding.etTelefonoTienda.text.toString()
        if(para.isNotEmpty()){ //puedo enviar el correo
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:$para")
            //se solicita los permisos para la llamada
            if(requireActivity().checkSelfPermission(Manifest.permission.CALL_PHONE) !=
                PackageManager.PERMISSION_GRANTED) //se debe perdir los permisos
            {
                //pedir permisos
                requireActivity().requestPermissions(arrayOf(Manifest.permission.CALL_PHONE),105)
            }
            else{
                requireActivity().startActivity(intent)
            }

        }
        else{
            Toast.makeText(requireContext(),getString(R.string.msg_data), Toast.LENGTH_LONG).show()
        }
    }

    private fun escribirCorreo()
    {
        val para = binding.etCorreoTienda.text.toString()
        if(para.isNotEmpty()){ //puedo enviar el correo
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "message/rfc822"
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(para))
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.msg_saludos)+ binding.etNombre.text.toString())
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.msg_mensaje_correo))
            startActivity(intent)

        }
        else{
            Toast.makeText(requireContext(),getString(R.string.msg_data), Toast.LENGTH_LONG).show()
        }
    }

    private fun deleteObject() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.msg_delete_lugar))
        builder.setMessage(getString(R.string.msg_seguro_borrado) +" ${args.objetoArgumento.nombre}?")
        builder.setNegativeButton(getString(R.string.msg_no)){_,_ ->}
        builder.setPositiveButton(getString(R.string.msg_si)){_,_ ->
            objectViewModel.deleteObjeto(args.objetoArgumento)
            Toast.makeText(requireContext(),getString(R.string.msg_object_deleted), Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_nav_objects)
        }
        builder.show()
    }

    private fun updateObject() {
        val nombre = binding.etNombre.text.toString().toUpperCase()
        if(nombre.isNotEmpty()){//se puede agregar un lugar
            val correo = binding.etCorreoTienda.text.toString()
            val precio = binding.etPrecioUpdate.text.toString().toDouble()
            val telefono = binding.etTelefonoTienda.text.toString()
            val web = binding.etWeb.text.toString()
            val objeto = Objeto(args.objetoArgumento.id,
                nombre,correo,web,telefono,
                args.objetoArgumento.latitud,args.objetoArgumento.longitud,args.objetoArgumento.altura,precio,
                args.objetoArgumento.ruta_audio,args.objetoArgumento.ruta_imagen)
            objectViewModel.saveObjetos(objeto)
            Toast.makeText(requireContext(),getText(R.string.msg_object_updated), Toast.LENGTH_SHORT)
            findNavController().navigate(R.id.action_updateFragment_to_nav_objects)
        }
        else{//sino no se puede modificar el lugar


            Toast.makeText(requireContext(),getText(R.string.msg_data), Toast.LENGTH_LONG)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}