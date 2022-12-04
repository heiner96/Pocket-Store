package com.pocket.store.repository

import androidx.lifecycle.MutableLiveData
import com.pocket.store.data.ProjectDao
import com.pocket.store.model.Project

class ProjectRepository(private val projectDao: ProjectDao)
{
    fun saveProject(project: Project)
    {
        projectDao.saveProject(project)
    }

    fun deleteProject(project: Project)
    {
        projectDao.deleteProject(project)
    }

    fun getProjects(): MutableLiveData<List<Project>> {
        return projectDao.getProjects()
    }
}