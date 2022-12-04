package com.pocket.store.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.ktx.Firebase
import com.pocket.store.model.Objeto
import com.pocket.store.model.Project

class ProjectObjectsDao
{
    private val coleccion1 = "PocketStoreApp"
    private val usuario = Firebase.auth.currentUser?.email.toString()
    private val projectObjectsColection = "projectObjects"
    private val projectsColection = "projects"
    private val objetosColection = "misObjetos"

    private var firestore : FirebaseFirestore =
        FirebaseFirestore.getInstance()


    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings
            .Builder().build()
    }

    fun getProjects() : MutableLiveData<List<Project>> {
        val listaProjects = MutableLiveData<List<Project>>()
        firestore
            .collection(coleccion1)
            .document(usuario)
            .collection(projectsColection)//proyectos
            .addSnapshotListener{ instantenea, error->

                if(error!=null){//hubo un error en la recureparion de los datos
                    return@addSnapshotListener
                }
                if(instantenea != null){
                    //Se logró recuperar la info y hay informacion
                    val lista = ArrayList<Project>()
                    instantenea.documents.forEach{
                        var projects = it.toObject(Project::class.java)
                        if(projects!=null)//Si se pudo convertir en objecto
                        {
                            lista.add(projects)
                        }
                    }
                    listaProjects.value = lista
                }

            }

        return listaProjects
    }
    fun getObjects() : MutableLiveData<List<Objeto>> {
        val listaObjetos = MutableLiveData<List<Objeto>>()

        firestore
            .collection(coleccion1)
            .document(usuario)
            .collection(objetosColection)
            .addSnapshotListener{ instantenea, error->

                if(error!=null){//hubo un error en la recureparion de los datos
                    return@addSnapshotListener
                }
                if(instantenea != null){
                    //Se logró recuperar la info y hay informacion
                    val lista = ArrayList<Objeto>()
                    instantenea.documents.forEach{
                        var objetos = it.toObject(Objeto::class.java)
                        if(objetos!=null)//Si se pudo convertir en objecto
                        {
                            lista.add(objetos)
                        }
                    }
                    listaObjetos.value = lista
                }

            }

        return listaObjetos
    }

    fun saveObjectProject(confirmProject: Project, confirmObject: Objeto)
    {
        val documento : DocumentReference
        if(confirmProject.id.isEmpty())
        {
            documento = firestore
                .collection(coleccion1)
                .document(usuario)
                .collection(projectObjectsColection)//proyectos
                .document(confirmProject.nombre)//nombre del proyecto
                .collection(confirmObject.nombre)
                .document()
            confirmProject.id = documento.id
        }
        else{
            documento = firestore
                .collection(coleccion1)
                .document(usuario)
                .collection(projectObjectsColection)//proyectos
                .document(confirmProject.nombre)//nombre del proyecto
                .collection(confirmObject.nombre)
                .document(confirmProject.id)
        }

        documento.set(confirmProject)
            .addOnSuccessListener {
                Log.d("SaveObjecto","Objecto agregado o modifico")
            }
            .addOnCanceledListener {
                Log.e("SaveObjecto","Objecto NO agregado o modificado")
            }
    }
}