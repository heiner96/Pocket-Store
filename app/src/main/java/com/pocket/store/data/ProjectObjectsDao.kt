package com.pocket.store.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.pocket.store.model.Objeto

class ProjectObjectsDao
{
    private val coleccion1 = "PocketStoreApp"
    //private val usuario = Firebase.auth.currentUser?.email.toString()
    private val coleccion3 = "projectObjects"

    private var firestore : FirebaseFirestore =
        FirebaseFirestore.getInstance()


    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings
            .Builder().build()
    }

    fun saveObjeto(objeto: Objeto, email: String, coleccion2: String, document: String){

        val documento : DocumentReference
        if(objeto.id.isEmpty())
        {
            documento = firestore
                .collection(coleccion1)
                .document(email)
                .collection(coleccion2)//proyectos
                .document(document)//nombre del proyecto
                .collection(coleccion3)
                .document()
            objeto.id = documento.id
        }
        else{
            documento = firestore
                .collection(coleccion1)
                .document(email)
                .collection(coleccion2)//proyectos
                .document(document)//nombre del proyecto
                .collection(coleccion3)
                .document(objeto.id)
        }

        documento.set(objeto)
            .addOnSuccessListener {
                Log.d("SaveObjecto","Objecto agregado o modifico")
            }
            .addOnCanceledListener {
                Log.e("SaveObjecto","Objecto NO agregado o modificado")
            }
    }


    fun deleteObjeto(objecto: Objeto, email: String, coleccion2: String, document: String){

        if(objecto.id.isNotEmpty()){
            firestore
                .collection(coleccion1)
                .document(email)
                .collection(coleccion2)//proyectos
                .document(document)//nombre del proyecto
                .collection(coleccion3)
                .document(objecto.id)
                .delete()

                .addOnSuccessListener {
                    Log.d("DeleteObjeto","Objeto eliminado")
                }
                .addOnCanceledListener {
                    Log.e("DeleteObjeto","Objeto NO eliminado")

                }
        }
    }

    fun getObjetos(email: String,coleccion2: String, document: String) : MutableLiveData<List<Objeto>> {
        val listaObjetos = MutableLiveData<List<Objeto>>()

        firestore
            .collection(coleccion1)
            .document(email)
            .collection(coleccion2)//proyectos
            .document(document)//nombre del proyecto
            .collection(coleccion3)
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
}