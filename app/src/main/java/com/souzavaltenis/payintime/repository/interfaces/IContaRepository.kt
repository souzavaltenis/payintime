package com.souzavaltenis.payintime.repository.interfaces

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.souzavaltenis.payintime.model.ContaModel

interface IContaRepository {
    fun save(keyDate: String, conta: ContaModel, callback: () -> Any): Task<DocumentSnapshot>
    fun findAllByKeyDate(keyDate: String): Task<DocumentSnapshot>
}