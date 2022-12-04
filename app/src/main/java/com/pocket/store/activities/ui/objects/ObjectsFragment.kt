package com.pocket.store.activities.ui.objects

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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.pocket.store.R
import com.pocket.store.adatper.ObjetosAdapter
import com.pocket.store.databinding.FragmentObjectsBinding
import com.pocket.store.model.Objeto
import com.pocket.store.viewmodel.ObjectViewModel

class ObjectsFragment : Fragment() {

    private var _binding: FragmentObjectsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    private lateinit var objectsViewModel: ObjectViewModel
    private lateinit var listado: List<Objeto>
    private val usuario = Firebase.auth.currentUser?.email.toString()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        objectsViewModel =
            ViewModelProvider(this).get(ObjectViewModel::class.java)

        _binding = FragmentObjectsBinding.inflate(inflater, container, false)

        binding.btAddObject.setOnClickListener{
            findNavController().navigate(R.id.action_nav_objects_to_addObjectFragment)
        }

        val objetosAdapter = ObjetosAdapter()
        val reciclador = binding.reciclador
        reciclador.adapter = objetosAdapter
        reciclador.layoutManager = LinearLayoutManager(requireContext())
        objectsViewModel.obtenerObjetos(usuario,"misObjetos")
        objectsViewModel.getObjetos.observe(viewLifecycleOwner) { objetos->
            objetosAdapter.setData(objetos,"misObjetos")
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
                val objetosAdapter = ObjetosAdapter()
                val reciclador = binding.reciclador
                reciclador.adapter = objetosAdapter
                reciclador.layoutManager = LinearLayoutManager(requireContext())
                deleteObject(listado.get(position))
                objectsViewModel.obtenerObjetos(usuario,"misObjetos")
                objectsViewModel.getObjetos.observe(viewLifecycleOwner) { objetos->
                    objetosAdapter.setData(objetos,"misObjetos")
                    listado = objetos
                }
            }
        }).attachToRecyclerView(reciclador)

        return binding.root
    }
    private fun deleteObject(objecto: Objeto) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.msg_delete_objeto))
        builder.setMessage(getString(R.string.msg_seguro_borrado) +" ${objecto.nombre}?")
        builder.setNegativeButton(getString(R.string.msg_no)){_,_ ->}
        builder.setPositiveButton(getString(R.string.msg_si)){_,_ ->
            objectsViewModel.deleteObjeto(objecto,usuario,"misObjetos")
            Toast.makeText(requireContext(),getString(R.string.msg_object_deleted), Toast.LENGTH_SHORT).show()
        }
        builder.show()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}