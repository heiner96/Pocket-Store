package com.pocket.store.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.pocket.store.data.ProjectObjectsDao
import com.pocket.store.model.Objeto
import com.pocket.store.model.Project
import com.pocket.store.repository.ProjectObjectsRepository


class ProjectObjectsViewModel(application: Application) : AndroidViewModel(application)
{
    private val repository : ProjectObjectsRepository = ProjectObjectsRepository(ProjectObjectsDao())
    var getProjects : MutableLiveData<List<Project>>
    var getObjects : MutableLiveData<List<Objeto>>
    lateinit var getObjetosProject : MutableLiveData<List<Objeto>>

    fun obtenerObjetosProject(project_nombre: String) {
        getObjetosProject = repository.getObjetosProject(project_nombre)
    }
    init {
        getProjects = repository.getProjects
        getObjects = repository.getObjects
    }

    fun saveObjectProject(confirmProject: Project, confirmObject: Objeto) {
        repository.saveObjectProject(confirmProject,confirmObject)
    }

    fun deleteObjetoFromProject(objecto: Objeto, project: Project) {
        repository.deleteObjetoFromProject(objecto,project)
    }
}