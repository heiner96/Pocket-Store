package com.pocket.store.viewmodel

import android.app.Application
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
    val getObjetos : MutableLiveData<List<Objeto>>

    init {
        getObjetos = repository.getObjetos
    }

    fun saveObjetos(objeto: Objeto)
    {
        viewModelScope.launch { repository.saveObjecto(objeto) }
    }

    fun deleteObjeto(objeto: Objeto)
    {
        viewModelScope.launch { repository.deleteObjecto(objeto) }
    }




}