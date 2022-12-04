package com.pocket.store.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pocket.store.data.ProjectDao
import com.pocket.store.model.Project
import com.pocket.store.repository.ProjectRepository
import kotlinx.coroutines.launch

class ProjectViewModel(application: Application) : AndroidViewModel(application)
{
    private val repository : ProjectRepository = ProjectRepository(ProjectDao())
    var getProjects : MutableLiveData<List<Project>> = repository.getProjects()

    fun saveProject(project: Project)
    {
        viewModelScope.launch { repository.saveProject(project) }
    }

    fun deleteProject(project: Project)
    {
        viewModelScope.launch { repository.deleteProject(project) }
    }
}