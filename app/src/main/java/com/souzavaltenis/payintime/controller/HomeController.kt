package com.souzavaltenis.payintime.controller

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.souzavaltenis.payintime.model.UsuarioModel
import com.souzavaltenis.payintime.singleton.UsuarioSingleton

class HomeController {

    private var usuarioController = UsuarioController()

    fun initUsuario(): Task<DocumentSnapshot> {

        val authController = AuthController()
        val currentEmail: String? = authController.getEmailUser()

        return usuarioController.findByEmail(currentEmail!!).addOnSuccessListener { doc: DocumentSnapshot? ->
                UsuarioSingleton.usuario = doc?.toObject(UsuarioModel::class.java)!!
        }

    }
}