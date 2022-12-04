package com.pocket.store.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.pocket.store.data.ObjetoDao
import com.pocket.store.data.ProjectObjectsDao
import com.pocket.store.model.Objeto
import com.pocket.store.repository.ObjetoRepository
import com.pocket.store.repository.ProjectObjectsRepository
import kotlinx.coroutines.launch

class ProjectObjectsViewModel(application: Application) : AndroidViewModel(application)
{
    private val repository : ProjectObjectsRepository = ProjectObjectsRepository(ProjectObjectsDao())
    lateinit var getObjetos : MutableLiveData<List<Objeto>>

    fun obtenerObjetos(email: String, colecion2: String, document: String) {
        getObjetos = repository.getObjetos(email,colecion2, document)
    }


    fun saveObjetos(objeto: Objeto, email: String, colecion2: String, document: String)
    {
        viewModelScope.launch { repository.saveObjecto(objeto, email,colecion2, document) }
    }

    fun deleteObjeto(objeto: Objeto, email: String, colecion2: String, document: String)
    {
        viewModelScope.launch { repository.deleteObjecto(objeto,email,colecion2, document) }
    }

}