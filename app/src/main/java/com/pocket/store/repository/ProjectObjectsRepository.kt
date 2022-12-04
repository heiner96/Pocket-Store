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

    val getProjects: MutableLiveData<List<Project>> = projectObjectsDao.getProjects()

    val getObjects: MutableLiveData<List<Objeto>> = projectObjectsDao.getObjects()

}