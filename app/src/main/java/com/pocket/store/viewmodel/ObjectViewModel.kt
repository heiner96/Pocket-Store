package com.pocket.store.viewmodel

import android.app.Application
import android.provider.ContactsContract
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pocket.store.data.ObjetoDao
import com.pocket.store.model.Objeto
import com.pocket.store.repository.ObjetoRepository
import kotlinx.coroutines.launch

class ObjectViewModel (application: Application) : AndroidViewModel(application)
{
    private val repository : ObjetoRepository = ObjetoRepository(ObjetoDao())
    lateinit var getObjetos : MutableLiveData<List<Objeto>>

    fun obtenerObjetos(email: String, colecion2: String) {
        getObjetos = repository.getObjetos(email,colecion2)
    }


    fun saveObjetos(objeto: Objeto, email: String, colecion2: String)
    {
        viewModelScope.launch { repository.saveObjecto(objeto, email,colecion2) }
    }

    fun deleteObjeto(objeto: Objeto,email: String, colecion2: String)
    {
        viewModelScope.launch { repository.deleteObjecto(objeto,email,colecion2) }
    }




}