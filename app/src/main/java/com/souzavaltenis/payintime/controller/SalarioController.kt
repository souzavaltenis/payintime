package com.souzavaltenis.payintime.controller

import com.google.android.gms.tasks.Task
import com.souzavaltenis.payintime.singleton.UsuarioSingleton

class SalarioController {

    private var usuarioController = UsuarioController()

    fun atualizarSalario(salario: Double): Task<Void> {
        val email: String = UsuarioSingleton.usuario.email
        return usuarioController.updateFieldSalario(email, salario).addOnSuccessListener {
            UsuarioSingleton.usuario.salario = salario
        }
    }

}