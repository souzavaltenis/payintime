package com.souzavaltenis.payintime.repository.interfaces

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.souzavaltenis.payintime.model.ContaFixaModel

interface IContaFixaRepository {
    fun save(conta: ContaFixaModel): Task<Void>
    fun findAll(): Task<QuerySnapshot>
}