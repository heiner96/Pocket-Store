package com.pocket.store.activities.ui.shared

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
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.pocket.store.R
import com.pocket.store.databinding.FragmentSharedObjectBinding
import com.pocket.store.viewmodel.ObjectViewModel


class SharedObjectFragment : Fragment()
{
    private var _binding: FragmentSharedObjectBinding? = null
    private val binding get() = _binding!!
    private lateinit var objectViewModel: ObjectViewModel
    private val usuario = Firebase.auth.currentUser?.email.toString()

    //Defino un argumento para obtener los argumentos pasados las fragmento
    private val args by navArgs<SharedObjectFragmentArgs>()

    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        objectViewModel = ViewModelProvider(this).get(ObjectViewModel::class.java)
        _binding = FragmentSharedObjectBinding.inflate(inflater, container, false)

        binding.etNombre.setText(args.sharedObject.nombre.toUpperCase())
        binding.etCorreoTienda.setText(args.sharedObject.correo)
        binding.etPrecioUpdate.setText(args.sharedObject.precio.toString())
        binding.etTelefonoTienda.setText(args.sharedObject.telefono)
        binding.etWeb.setText(args.sharedObject.web)
        binding.tvLongitud.text = args.sharedObject.longitud.toString()
        binding.tvLatitud.text = args.sharedObject.latitud.toString()
        binding.tvAltura.text = args.sharedObject.altura.toString()
        binding.btEmail.setOnClickListener{
            escribirCorreo()
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
        binding.btWase.setOnClickListener{
            if(isPackageInstalled("com.waze",requireContext().packageManager))
            {
                usarWase(args.sharedObject.latitud,args.sharedObject.longitud)
            }
            else
            {
                Toast.makeText(requireContext(),getString(R.string.requiere_wase_instalado), Toast.LENGTH_LONG ).show()
            }

        }
        if(args.sharedObject.ruta_audio?.isNotEmpty() == true){
            mediaPlayer = MediaPlayer()
            mediaPlayer.setDataSource(args.sharedObject.ruta_audio)
            mediaPlayer.prepare()
            binding.btPlay.isEnabled = true
        }
        else{
            binding.btPlay.isEnabled = false
        }

        if(args.sharedObject.ruta_imagen?.isNotEmpty() == true){
            Glide.with(requireContext())
                .load(args.sharedObject.ruta_imagen)
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
    private fun isPackageInstalled(packageName: String, packageManager: PackageManager): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
    private fun mensajeWhastApps()
    {
        if(!isPackageInstalled("com.whatsapp", requireContext().packageManager )){
            Toast.makeText(requireContext(),getString(R.string.requiere_whatss_instalado), Toast.LENGTH_LONG).show()
            return
        }
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
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}