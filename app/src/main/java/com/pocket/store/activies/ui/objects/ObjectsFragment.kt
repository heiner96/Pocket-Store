package com.pocket.store.activies.ui.objects

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pocket.store.R
import com.pocket.store.adatper.ObjetosAdatper
import com.pocket.store.databinding.FragmentObjectsBinding
import com.pocket.store.model.Objeto
import com.pocket.store.viewmodel.ObjectViewModel

class ObjectsFragment : Fragment() {

    private var _binding: FragmentObjectsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    private lateinit var objetosViewModel: ObjectViewModel
    private lateinit var listado: List<Objeto>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        objetosViewModel =
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
            listado = objetos
        }
        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            // Called when a user swipes left or right on a ViewHolder
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                val position = viewHolder.adapterPosition
                val objetosAdatper = ObjetosAdatper()
                val reciclador = binding.reciclador
                reciclador.adapter = objetosAdatper
                reciclador.layoutManager = LinearLayoutManager(requireContext())
                deleteObject(listado.get(position))
                objetosViewModel.getObjetos.observe(viewLifecycleOwner) { objetos->
                    objetosAdatper.setData(objetos)
                    listado = objetos
                }
            }
        }).attachToRecyclerView(reciclador)

        return binding.root
    }
    private fun deleteObject(objecto: Objeto) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.msg_delete_lugar))
        builder.setMessage(getString(R.string.msg_seguro_borrado) +" ${objecto.nombre}?")
        builder.setNegativeButton(getString(R.string.msg_no)){_,_ ->}
        builder.setPositiveButton(getString(R.string.msg_si)){_,_ ->
            objetosViewModel.deleteObjeto(objecto)
            Toast.makeText(requireContext(),getString(R.string.msg_object_deleted), Toast.LENGTH_SHORT).show()
        }
        builder.show()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}