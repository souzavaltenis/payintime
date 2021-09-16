package com.souzavaltenis.payintime.controller

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.souzavaltenis.payintime.model.ContaFixaModel
import com.souzavaltenis.payintime.repository.ContaFixaRepository

class ContaFixaController(emailUsuario: String) {

    private var contaFixaRepository: ContaFixaRepository = ContaFixaRepository(emailUsuario)

    fun save(contaFixa: ContaFixaModel): Task<Void> {
        return contaFixaRepository.save(contaFixa)
    }

    fun findAll(): Task<QuerySnapshot> {
        return contaFixaRepository.findAll()
    }

    fun update(conta: ContaFixaModel): Task<Void> {
        return contaFixaRepository.update(conta)
    }

    fun delete(idConta: String): Task<Void> {
        return contaFixaRepository.delete(idConta)
    }
}