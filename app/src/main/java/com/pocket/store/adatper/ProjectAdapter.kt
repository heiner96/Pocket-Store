package com.pocket.store.adatper

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pocket.store.activities.ui.projects.ProjectsFragmentDirections
import com.pocket.store.databinding.ActivityLoginBinding.inflate
import com.pocket.store.databinding.ProjectFilaBinding
import com.pocket.store.model.Project

class ProjectAdapter : RecyclerView.Adapter<ProjectAdapter.ProjectsViewHolder>()
{
    private var listaProjects = emptyList<Project>()


    inner class ProjectsViewHolder(private val itemBinding: ProjectFilaBinding) :
        RecyclerView.ViewHolder(itemBinding.root){
        fun bind(project : Project){
            itemBinding.tvNombreProject.text = project.nombre
            itemBinding.tvClienteProject.text = project.nombreCliente

            Glide.with(itemBinding.root.context)
                .load(project.ruta_imagen)
                .circleCrop()
                .into(itemBinding.imagenProject)

            itemBinding.vistaFilaProject.setOnClickListener {
                val accion = ProjectsFragmentDirections
                    .actionProjectsFragmentToUpdateProjectFragment(project)
                itemView.findNavController().navigate(accion)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectsViewHolder {
        //creo un elemeto en memoria de una "cajita" vista_fila
        val itemBinding = ProjectFilaBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        //retorno la cajita en memoria
        return ProjectsViewHolder(itemBinding)

    }

    override fun onBindViewHolder(holder: ProjectsViewHolder, position: Int) {
        //Obtengo el objeto que debo "dibujar" en la fila del recyclerView que "voy"
        val objetoActual = listaProjects[position]

        holder.bind(objetoActual) //LLamo a la función que efectivamente "pinta" la inflate

    }

    override fun getItemCount(): Int {
        return listaProjects.size
    }

    fun setData(project : List<Project>){
        this.listaProjects = project
        notifyDataSetChanged()
    }
}