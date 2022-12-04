package com.pocket.store.adatper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pocket.store.activities.ui.objects.ObjectsFragmentDirections
import com.pocket.store.activities.ui.shared.SharedFragmentDirections
import com.pocket.store.databinding.ObjetoFilaBinding
import com.pocket.store.model.Objeto

class ObjetosAdapter : RecyclerView.Adapter<ObjetosAdapter.ObjetosViewHolder>()
{

    private var listaObjetos = emptyList<Objeto>()
    private var flag: String = ""

    inner class ObjetosViewHolder(private val itemBinding: ObjetoFilaBinding) :
        RecyclerView.ViewHolder(itemBinding.root){
        fun bind(objeto : Objeto){
            itemBinding.tvNombre.text = objeto.nombre
            itemBinding.tvCorreoLugarFila.text = objeto.correo
            itemBinding.tvTelefono.text = objeto.telefono

            Glide.with(itemBinding.root.context)
                .load(objeto.ruta_imagen)
                .circleCrop()
                .into(itemBinding.imagen)
            if(flag.equals("misObjetos")){
                itemBinding.btShare.setVisibility(View.VISIBLE)
                itemBinding.btShare.setOnClickListener{
                    val accion = ObjectsFragmentDirections
                        .actionNavObjectsToShareObjectFragment(objeto)
                    itemView.findNavController().navigate(accion)
                }
                itemBinding.vistaFila.setOnClickListener {
                    val accion = ObjectsFragmentDirections
                        .actionNavObjectsToUpdateFragment(objeto)
                    itemView.findNavController().navigate(accion)
                }
            }
            else if(flag.equals("compartidos")){
                itemBinding.btShare.setVisibility(View.GONE)
                itemBinding.vistaFila.setOnClickListener {
                    val accion = SharedFragmentDirections
                        .actionNavCompartidosToSharedObjectFragment(objeto)
                    itemView.findNavController().navigate(accion)
                }
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObjetosViewHolder {
        //creo un elemeto en memoria de una "cajita" vista_fila
        val itemBinding = ObjetoFilaBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        //retorno la cajita en memoria
        return ObjetosViewHolder(itemBinding)

    }

    override fun onBindViewHolder(holder: ObjetosViewHolder, position: Int) {
        //Obtengo el objeto que debo "dibujar" en la fila del recyclerView que "voy"
        val objetoActual = listaObjetos[position]

        holder.bind(objetoActual) //LLamo a la función que efectivamente "pinta" la inflate

    }

    override fun getItemCount(): Int {
        return listaObjetos.size
    }

    fun setData(lugares : List<Objeto>,flag: String){
        this.listaObjetos = lugares
        this.flag = flag
        notifyDataSetChanged()
    }



}