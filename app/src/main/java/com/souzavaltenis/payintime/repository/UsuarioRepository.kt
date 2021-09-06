package com.souzavaltenis.payintime.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.souzavaltenis.payintime.model.UsuarioModel
import com.souzavaltenis.payintime.repository.interfaces.IUsuarioRepository

class UsuarioRepository: IUsuarioRepository {

    private val firestore: FirebaseFirestore = Firebase.firestore

    override fun save(usuarioModel: UsuarioModel): Task<Void> {
        return firestore.collection("users").document(usuarioModel.email).set(usuarioModel)
    }

    override fun findByEmail(email: String): Task<DocumentSnapshot> {
        return firestore.collection("users").document(email).get()
    }
}