package com.pocket.store.adatper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pocket.store.activities.ui.projectObjects.ProjectObjectsFragmentDirections
import com.pocket.store.databinding.ProjectFilaBinding
import com.pocket.store.model.Project

class ProjectObjectsAdapter  : RecyclerView.Adapter<ProjectObjectsAdapter.ProjectObjetsViewHolder>()
{
    private var listaProjects = emptyList<Project>()


    inner class ProjectObjetsViewHolder(private val itemBinding: ProjectFilaBinding) :
        RecyclerView.ViewHolder(itemBinding.root){
        fun bind(project : Project){
            itemBinding.tvNombreProject.text = project.nombre
            itemBinding.tvClienteProject.text = project.nombreCliente
            itemBinding.imgProjectEye.setVisibility(View.GONE)

            Glide.with(itemBinding.root.context)
                .load(project.ruta_imagen)
                .circleCrop()
                .into(itemBinding.imagenProject)
            itemBinding.vistaFilaProject.setOnClickListener {
                val accion = ProjectObjectsFragmentDirections
                    .actionNavProjectObjectsToProjectObjectsViewFragment(project)
                itemView.findNavController().navigate(accion)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectObjetsViewHolder {
        //creo un elemeto en memoria de una "cajita" vista_fila
        val itemBinding = ProjectFilaBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        //retorno la cajita en memoria
        return ProjectObjetsViewHolder(itemBinding)

    }

    override fun onBindViewHolder(holder: ProjectObjetsViewHolder, position: Int) {
        //Obtengo el objeto que debo "dibujar" en la fila del recyclerView que "voy"
        val projectActual = listaProjects[position]

        holder.bind(projectActual) //LLamo a la función que efectivamente "pinta" la inflate

    }

    override fun getItemCount(): Int {
        return listaProjects.size
    }

    fun setData(project : List<Project>){
        this.listaProjects = project
        notifyDataSetChanged()
    }


}