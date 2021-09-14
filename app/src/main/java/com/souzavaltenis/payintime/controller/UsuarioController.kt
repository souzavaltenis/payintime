package com.souzavaltenis.payintime.controller

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.souzavaltenis.payintime.model.UsuarioModel
import com.souzavaltenis.payintime.repository.UsuarioRepository

class UsuarioController {

    private var usuarioRepository: UsuarioRepository = UsuarioRepository()

    fun save(usuarioModel: UsuarioModel): Task<Void> {
        return usuarioRepository.save(usuarioModel)
    }

    fun findByEmail(email: String): Task<DocumentSnapshot> {
        return usuarioRepository.findByEmail(email)
    }

    fun updateFieldSalario(email: String, salario: Double): Task<Void> {
        return usuarioRepository.updateField(email, "salario", salario)
    }
}