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
import com.pocket.store.model.Project
import com.pocket.store.viewmodel.ProjectObjectsViewModel

class ProjectObjectsFragment : Fragment()
{
    private var _binding: FragmentProjectObjectsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    private lateinit var projectObjectViewModel: ProjectObjectsViewModel
    private lateinit var listado: List<Project>

    private val usuario = Firebase.auth.currentUser?.email.toString()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        projectObjectViewModel =
            ViewModelProvider(this).get(ProjectObjectsViewModel::class.java)

        _binding = FragmentProjectObjectsBinding.inflate(inflater, container, false)

        val projectAdapter = ProjectObjectsAdapter()
        val reciclador = binding.recicladorProjectObjects
        reciclador.adapter = projectAdapter
        reciclador.layoutManager = LinearLayoutManager(requireContext())
        projectObjectViewModel.getProjects.observe(viewLifecycleOwner) { project->
            projectAdapter.setData(project)
            listado = project
        }

        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}