package com.pocket.store.adatper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pocket.store.activities.ui.projectObjects.ProjectObjectsFragmentDirections
import com.pocket.store.activities.ui.projectObjects.ProjectObjectsViewFragmentDirections
import com.pocket.store.databinding.ObjetoFilaBinding
import com.pocket.store.databinding.ProjectFilaBinding
import com.pocket.store.model.Objeto
import com.pocket.store.model.Project

class ProjectObjectsViewAdapter: RecyclerView.Adapter<ProjectObjectsViewAdapter.ProjectObjetsViewHolder>()
{
    private var listaObjects = emptyList<Objeto>()
    lateinit var project: Project


    inner class ProjectObjetsViewHolder(private val itemBinding: ObjetoFilaBinding) :
        RecyclerView.ViewHolder(itemBinding.root){
        fun bind(objeto : Objeto){
            itemBinding.tvNombre.text = objeto.nombre
            itemBinding.tvCorreoLugarFila.text = objeto.correo
            itemBinding.tvTelefono.text = objeto.telefono

            Glide.with(itemBinding.root.context)
                .load(objeto.ruta_imagen)
                .circleCrop()
                .into(itemBinding.imagen)
            itemBinding.btShare.setVisibility(View.GONE)
            itemBinding.vistaFila.setOnClickListener {
                val accion = ProjectObjectsViewFragmentDirections
                    .actionProjectObjectsViewFragmentToConfirmProjectObjectFragment(objeto, project)
                itemView.findNavController().navigate(accion)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectObjetsViewHolder {
        //creo un elemeto en memoria de una "cajita" vista_fila
        val itemBinding = ObjetoFilaBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        //retorno la cajita en memoria
        return ProjectObjetsViewHolder(itemBinding)

    }

    override fun onBindViewHolder(holder: ProjectObjetsViewHolder, position: Int) {
        //Obtengo el objeto que debo "dibujar" en la fila del recyclerView que "voy"
        val projectActual = listaObjects[position]

        holder.bind(projectActual) //LLamo a la función que efectivamente "pinta" la inflate

    }

    override fun getItemCount(): Int {
        return listaObjects.size
    }

    fun setData(objeto : List<Objeto>, project: Project){
        this.listaObjects = objeto
        this.project = project
        notifyDataSetChanged()
    }


}