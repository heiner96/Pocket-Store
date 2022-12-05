package com.pocket.store.repository

import androidx.lifecycle.MutableLiveData
import com.pocket.store.data.ProjectObjectsDao
import com.pocket.store.model.Objeto
import com.pocket.store.model.Project

class ProjectObjectsRepository (private val projectObjectsDao: ProjectObjectsDao)
{
    fun saveObjectProject(confirmProject: Project, confirmObject: Objeto) {
        projectObjectsDao.saveObjectProject(confirmProject,confirmObject)
    }

    fun getObjetosProject(project_nombre: String): MutableLiveData<List<Objeto>>
    {
        return projectObjectsDao.getObjetosProject(project_nombre)
    }

    fun deleteObjetoFromProject(objecto: Objeto, project: Project) {
        return projectObjectsDao.deleteObjetoFromProject(objecto,project)
    }

    val getProjects: MutableLiveData<List<Project>> = projectObjectsDao.getProjects()

    val getObjects: MutableLiveData<List<Objeto>> = projectObjectsDao.getObjects()

}