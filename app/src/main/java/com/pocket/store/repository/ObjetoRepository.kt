package com.pocket.store.repository

import androidx.lifecycle.MutableLiveData
import com.pocket.store.data.ObjetoDao
import com.pocket.store.model.Objeto

class ObjetoRepository (private val lugarDao: ObjetoDao)
{

    fun saveObjecto(objeto: Objeto)
    {
        lugarDao.saveObjeto(objeto)
    }

    fun deleteObjecto(objeto: Objeto)
    {
        lugarDao.deleteObjeto(objeto)
    }


    val getObjetos : MutableLiveData<List<Objeto>> =  lugarDao.getObjetos()


}