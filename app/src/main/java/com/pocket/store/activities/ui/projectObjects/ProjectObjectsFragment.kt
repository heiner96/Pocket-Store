package com.pocket.store.activities.ui.projectObjects

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.pocket.store.R
import com.pocket.store.adatper.ProjectObjectsAdapter
import com.pocket.store.databinding.FragmentProjectObjectsBinding
import com.pocket.store.model.Objeto
import com.pocket.store.viewmodel.ProjectObjectsViewModel

class ProjectObjectsFragment : Fragment()
{
    private var _binding: FragmentProjectObjectsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    private lateinit var projectObjectViewModel: ProjectObjectsViewModel
    private lateinit var listado: List<Objeto>
    private val usuario = Firebase.auth.currentUser?.email.toString()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        projectObjectViewModel =
            ViewModelProvider(this).get(ProjectObjectsViewModel::class.java)

        _binding = FragmentProjectObjectsBinding.inflate(inflater, container, false)


        val objetosAdapter = ProjectObjectsAdapter()
        val reciclador = binding.recicladorProjectObjects
        reciclador.adapter = objetosAdapter
        reciclador.layoutManager = LinearLayoutManager(requireContext())
        projectObjectViewModel.obtenerObjetos(usuario,"objetos_proyectos","casa_escazu")
        projectObjectViewModel.getObjetos.observe(viewLifecycleOwner) { objetos->
            objetosAdapter.setData(objetos)
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
                val objetosAdapter = ProjectObjectsAdapter()
                val reciclador = binding.recicladorProjectObjects
                reciclador.adapter = objetosAdapter
                reciclador.layoutManager = LinearLayoutManager(requireContext())
                deleteObject(listado.get(position))
                projectObjectViewModel.obtenerObjetos(usuario,"objetos_proyectos","casa_escazu")
                projectObjectViewModel.getObjetos.observe(viewLifecycleOwner) { objetos->
                    objetosAdapter.setData(objetos)
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
        builder.setNegativeButton(getString(R.string.msg_no)){ _, _ ->}
        builder.setPositiveButton(getString(R.string.msg_si)){ _, _ ->
            projectObjectViewModel.deleteObjeto(objecto,usuario,"objetos_proyectos","casa_escazu")
            Toast.makeText(requireContext(),getString(R.string.msg_object_deleted), Toast.LENGTH_SHORT).show()
        }
        builder.show()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}