package com.souzavaltenis.payintime.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.souzavaltenis.payintime.model.ContaModel
import com.souzavaltenis.payintime.repository.interfaces.IContaRepository

class ContaRepository(val emailUsuario: String): IContaRepository {

    private val firestore: FirebaseFirestore = Firebase.firestore

    override fun save(keyDate: String, conta: ContaModel, callback: () -> Any): Task<DocumentSnapshot> {

        return firestore.collection("users")
            .document(emailUsuario)
            .collection("contas")
            .document(keyDate)
            .get().addOnSuccessListener { doc ->

                //Se não existe, crie o documento já com o array "all" contendo a nova conta
                if(!doc.exists()){
                    doc.reference.set(hashMapOf("all" to listOf<Any>(conta)))
                        .addOnCompleteListener { callback.invoke() }

                //Se existe, apenas adicione a nova conta como um novo elemento do array
                }else{
                    doc.reference.update("all", FieldValue.arrayUnion(conta))
                        .addOnCompleteListener { callback.invoke() }
                }
            }
    }

    override fun findAllByKeyDate(keyDate: String): Task<DocumentSnapshot> {
        return firestore.collection("users")
            .document(emailUsuario)
            .collection("contas")
            .document(keyDate)
            .get()
    }

}