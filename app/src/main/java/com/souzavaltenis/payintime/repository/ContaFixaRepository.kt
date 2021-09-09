package com.souzavaltenis.payintime.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.souzavaltenis.payintime.model.ContaFixaModel
import com.souzavaltenis.payintime.repository.interfaces.IContaFixaRepository

class ContaFixaRepository(val emailUsuario: String): IContaFixaRepository {

    private val firestore: FirebaseFirestore = Firebase.firestore

    override fun save(conta: ContaFixaModel): Task<Void> {
        return firestore.collection("users")
            .document(emailUsuario)
            .collection("contas_fixas")
            .document(conta.id)
            .set(conta)
    }

    override fun findAll(): Task<QuerySnapshot> {
        return firestore.collection("users")
            .document(emailUsuario)
            .collection("contas_fixas")
            .get()
    }

}