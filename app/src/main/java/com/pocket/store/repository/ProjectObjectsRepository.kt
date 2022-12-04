package com.pocket.store.repository

import androidx.lifecycle.MutableLiveData
import com.pocket.store.data.ProjectObjectsDao
import com.pocket.store.model.Objeto

class ProjectObjectsRepository (private val projectObjectsDao: ProjectObjectsDao)
{

    fun saveObjecto(objeto: Objeto, email: String, coleccion2: String, document: String)
    {
        projectObjectsDao.saveObjeto(objeto,email, coleccion2, document)
    }

    fun deleteObjecto(objeto: Objeto, email: String, coleccion2: String, document: String)
    {
        projectObjectsDao.deleteObjeto(objeto, email, coleccion2, document)
    }

    fun getObjetos(email : String, coleccion2 : String, document: String): MutableLiveData<List<Objeto>> {
        return projectObjectsDao.getObjetos(email,coleccion2, document)
    }
}