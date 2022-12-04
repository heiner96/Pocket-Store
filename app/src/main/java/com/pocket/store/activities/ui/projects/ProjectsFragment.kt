package com.pocket.store.activities.ui.projects

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
import com.pocket.store.adatper.ProjectAdapter
import com.pocket.store.databinding.FragmentProjectsBinding
import com.pocket.store.model.Project
import com.pocket.store.viewmodel.ProjectViewModel


class ProjectsFragment : Fragment()
{
    private var _binding: FragmentProjectsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    private lateinit var projectsViewModel: ProjectViewModel
    private lateinit var listado: List<Project>
    private val usuario = Firebase.auth.currentUser?.email.toString()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        projectsViewModel =
            ViewModelProvider(this).get(ProjectViewModel::class.java)

        _binding = FragmentProjectsBinding.inflate(inflater, container, false)

        binding.btAddProject.setOnClickListener{
            findNavController().navigate(R.id.action_projectsFragment_to_addProjectFragment)
        }

        val projectAdapter = ProjectAdapter()
        val reciclador = binding.recicladorProject
        reciclador.adapter = projectAdapter
        reciclador.layoutManager = LinearLayoutManager(requireContext())
        projectsViewModel.getProjects.observe(viewLifecycleOwner) { projects->
            projectAdapter.setData(projects)
            listado = projects
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
                val projectAdapter = ProjectAdapter()
                val reciclador = binding.recicladorProject
                reciclador.adapter = projectAdapter
                reciclador.layoutManager = LinearLayoutManager(requireContext())
                deleteProject(listado.get(position))
                projectsViewModel.getProjects.observe(viewLifecycleOwner) { projects->
                    projectAdapter.setData(projects)
                    listado = projects
                }
            }
        }).attachToRecyclerView(reciclador)

        return binding.root
    }
    private fun deleteProject(project: Project) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.msg_delete_project))
        builder.setMessage(getString(R.string.msg_seguro_borrado_project) +" ${project.nombre}?")
        builder.setNegativeButton(getString(R.string.msg_no)){ _, _ ->}
        builder.setPositiveButton(getString(R.string.msg_si)){ _, _ ->
            projectsViewModel.deleteProject(project)
            Toast.makeText(requireContext(),getString(R.string.msg_project_deleted), Toast.LENGTH_SHORT).show()
        }
        builder.show()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}