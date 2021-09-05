package com.souzavaltenis.payintime.repository.interfaces

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.souzavaltenis.payintime.model.UsuarioModel

interface IUsuarioRepository {
    fun save(usuarioModel: UsuarioModel): Task<Void>
    fun findByEmail(email: String): Task<DocumentSnapshot>
}