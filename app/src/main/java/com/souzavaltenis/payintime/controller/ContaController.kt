package com.souzavaltenis.payintime.controller

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.souzavaltenis.payintime.model.ContaModel
import com.souzavaltenis.payintime.repository.ContaRepository

class ContaController(emailUsuario: String) {

    private var contaRepository: ContaRepository = ContaRepository(emailUsuario)

    fun save(keyDate: String, conta: ContaModel, callback: () -> Any): Task<DocumentSnapshot> {
        return contaRepository.save(keyDate, conta, callback)
    }

    fun findAllByKeyDate(keyDate: String): Task<DocumentSnapshot> {
        return contaRepository.findAllByKeyDate(keyDate)
    }
}