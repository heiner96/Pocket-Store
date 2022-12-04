package com.pocket.store.activities.ui.projectObjects

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.pocket.store.adatper.ProjectObjectsViewAdapter
import com.pocket.store.databinding.FragmentProjectObjectsViewBinding
import com.pocket.store.model.Objeto
import com.pocket.store.viewmodel.ProjectObjectsViewModel


class ProjectObjectsViewFragment : Fragment()
{
    private var _binding: FragmentProjectObjectsViewBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    private lateinit var projectObjectViewModel: ProjectObjectsViewModel
    private lateinit var listado: List<Objeto>
    private val args by navArgs<ProjectObjectsViewFragmentArgs>()

    private val usuario = Firebase.auth.currentUser?.email.toString()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        projectObjectViewModel =
            ViewModelProvider(this).get(ProjectObjectsViewModel::class.java)

        _binding = FragmentProjectObjectsViewBinding.inflate(inflater, container, false)

        val projectAdapterView = ProjectObjectsViewAdapter()
        val reciclador = binding.recicladorProjectObjects
        reciclador.adapter = projectAdapterView
        reciclador.layoutManager = LinearLayoutManager(requireContext())
        projectObjectViewModel.getObjects.observe(viewLifecycleOwner) { objects->
            projectAdapterView.setData(objects,args.projectView)
            listado = objects
        }

        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}