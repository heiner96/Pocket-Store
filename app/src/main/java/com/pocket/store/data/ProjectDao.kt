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

class ProjectDao
{
    private val coleccion1 = "PocketStoreApp"
    private val usuario = Firebase.auth.currentUser?.email.toString()
    private val coleccion2 = "projects"

    private var firestore : FirebaseFirestore =
        FirebaseFirestore.getInstance()


    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings
            .Builder().build()
    }

    fun saveProject(project: Project){

        val documento : DocumentReference
        if(project.id.isEmpty())
        {
            documento = firestore
                .collection(coleccion1)
                .document(usuario)
                .collection(coleccion2)
                .document()
            project.id = documento.id
        }
        else{
            documento = firestore
                .collection(coleccion1)
                .document(usuario)
                .collection(coleccion2)
                .document(project.id)
        }

        documento.set(project)
            .addOnSuccessListener {
                Log.d("SaveProject","Project agregado o modifico")
            }
            .addOnCanceledListener {
                Log.e("SaveProject","Project NO agregado o modificado")
            }
    }


    fun deleteProject(project: Project){

        if(project.id.isNotEmpty()){
            firestore
                .collection(coleccion1)
                .document(usuario)
                .collection(coleccion2)
                .document(project.id)
                .delete()

                .addOnSuccessListener {
                    Log.d("DeleteProject","Project eliminado")
                }
                .addOnCanceledListener {
                    Log.e("DeleteProject","Project NO eliminado")

                }
        }
    }

    fun getProjects() : MutableLiveData<List<Project>> {
        val listaProjectos = MutableLiveData<List<Project>>()

        firestore
            .collection(coleccion1)
            .document(usuario)
            .collection(coleccion2)
            .addSnapshotListener{ instantenea, error->

                if(error!=null){//hubo un error en la recureparion de los datos
                    return@addSnapshotListener
                }
                if(instantenea != null){
                    //Se logró recuperar la info y hay informacion
                    val lista = ArrayList<Project>()
                    instantenea.documents.forEach{
                        var project = it.toObject(Project::class.java)
                        if(project!=null)//Si se pudo convertir en objecto
                        {
                            lista.add(project)
                        }
                    }
                    listaProjectos.value = lista
                }

            }

        return listaProjectos
    }
}