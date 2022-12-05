package com.pocket.store.activities.ui.projects

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.pocket.store.R
import com.pocket.store.adatper.ObjetosAdapter
import com.pocket.store.databinding.FragmentViewObjectsProjectBinding
import com.pocket.store.model.Objeto
import com.pocket.store.model.Project
import com.pocket.store.viewmodel.ProjectObjectsViewModel

class ViewObjectsProjectFragment : Fragment()
{
    private var _binding: FragmentViewObjectsProjectBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    private lateinit var projectObjectsViewModel: ProjectObjectsViewModel
    private lateinit var listado: List<Objeto>
    private val usuario = Firebase.auth.currentUser?.email.toString()

    //Defino un argumento para obtener los argumentos pasados las fragmento
    private val args by navArgs<ViewObjectsProjectFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        projectObjectsViewModel =
            ViewModelProvider(this).get(ProjectObjectsViewModel::class.java)

        _binding = FragmentViewObjectsProjectBinding.inflate(inflater, container, false)



        val objetosAdapter = ObjetosAdapter()
        val reciclador = binding.recicladorProject
        reciclador.adapter = objetosAdapter
        reciclador.layoutManager = LinearLayoutManager(requireContext())
        var project : String = args.proyectViewObjects.nombre
        projectObjectsViewModel.obtenerObjetosProject(project)
        projectObjectsViewModel.getObjetosProject.observe(viewLifecycleOwner) { objetos->
            objetosAdapter.setData(objetos,"projectObjects")
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
                val reciclador = binding.recicladorProject
                reciclador.adapter = objetosAdapter
                reciclador.layoutManager = LinearLayoutManager(requireContext())
                deleteObject(listado.get(position),args.proyectViewObjects)
                var project : String = args.proyectViewObjects.nombre
                projectObjectsViewModel.obtenerObjetosProject(project)
                projectObjectsViewModel.getObjetosProject.observe(viewLifecycleOwner) { objetos->
                    objetosAdapter.setData(objetos,"projectObjects")
                    listado = objetos
                }
            }
        }).attachToRecyclerView(reciclador)

        return binding.root
    }
    private fun deleteObject(objecto: Objeto, project: Project) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.msg_delete_objeto))
        builder.setMessage(getString(R.string.msg_seguro_borrado_from_project) +" ${objecto.nombre} del proyecto: ${args.proyectViewObjects.nombre} ?")
        builder.setNegativeButton(getString(R.string.msg_no)){ _, _ ->}
        builder.setPositiveButton(getString(R.string.msg_si)){ _, _ ->
            projectObjectsViewModel.deleteObjetoFromProject(objecto,project)
            Toast.makeText(requireContext(),getString(R.string.msg_object_deleted), Toast.LENGTH_SHORT).show()
        }
        builder.show()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}