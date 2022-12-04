package com.pocket.store.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.pocket.store.data.ProjectObjectsDao
import com.pocket.store.model.Objeto
import com.pocket.store.model.Project
import com.pocket.store.repository.ProjectObjectsRepository
import kotlinx.coroutines.launch

class ProjectObjectsViewModel(application: Application) : AndroidViewModel(application)
{
    private val repository : ProjectObjectsRepository = ProjectObjectsRepository(ProjectObjectsDao())
    var getProjects : MutableLiveData<List<Project>>
    var getObjects : MutableLiveData<List<Objeto>>

    init {
        getProjects = repository.getProjects
        getObjects = repository.getObjects
    }

    fun saveObjectProject(confirmProject: Project, confirmObject: Objeto) {
        repository.saveObjectProject(confirmProject,confirmObject)
    }
}