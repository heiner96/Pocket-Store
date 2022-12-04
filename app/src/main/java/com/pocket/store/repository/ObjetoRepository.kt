package com.pocket.store.repository

import androidx.lifecycle.MutableLiveData
import com.pocket.store.data.ObjetoDao
import com.pocket.store.model.Objeto

class ObjetoRepository (private val objetoDao: ObjetoDao)
{

    fun saveObjecto(objeto: Objeto, email: String, coleccion2: String)
    {
        objetoDao.saveObjeto(objeto,email, coleccion2)
    }

    fun deleteObjecto(objeto: Objeto, email: String, coleccion2: String)
    {
        objetoDao.deleteObjeto(objeto, email, coleccion2)
    }

    fun getObjetos(email : String, coleccion2 : String): MutableLiveData<List<Objeto>> {
        return objetoDao.getObjetos(email,coleccion2)
    }



}