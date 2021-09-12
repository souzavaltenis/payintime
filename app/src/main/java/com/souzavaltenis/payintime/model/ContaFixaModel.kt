package com.souzavaltenis.payintime.model

import com.souzavaltenis.payintime.model.enums.StatusConta
import java.util.*
import kotlin.collections.HashMap

data class ContaFixaModel(
    var id: String = "",
    var descricao: String = "",
    var valor: Double = 0.0,
    var diaVencimento: Int = -1,
    var pagamentos: HashMap<String, StatusConta> = hashMapOf(),
    var dataCriacao: Date? = null
)