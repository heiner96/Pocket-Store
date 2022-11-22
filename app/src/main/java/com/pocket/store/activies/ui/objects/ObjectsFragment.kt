package com.pocket.store.activies.ui.objects

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pocket.store.R
import com.pocket.store.adatper.ObjetosAdatper
import com.pocket.store.databinding.FragmentObjectsBinding
import com.pocket.store.viewmodel.ObjectViewModel

class ObjectsFragment : Fragment() {

    private var _binding: FragmentObjectsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    private lateinit var objetosViewModel: ObjectViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val objetosViewModel =
            ViewModelProvider(this).get(ObjectViewModel::class.java)

        _binding = FragmentObjectsBinding.inflate(inflater, container, false)

        binding.btAddObject.setOnClickListener{
            findNavController().navigate(R.id.action_nav_objects_to_addObjectFragment)
        }
        val objetosAdatper = ObjetosAdatper()
        val reciclador = binding.reciclador
        reciclador.adapter = objetosAdatper
        reciclador.layoutManager = LinearLayoutManager(requireContext())
        objetosViewModel.getObjetos.observe(viewLifecycleOwner) { objetos->
            objetosAdatper.setData(objetos)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}