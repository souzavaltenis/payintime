package com.souzavaltenis.payintime.model

import com.souzavaltenis.payintime.model.enums.StatusConta
import java.util.*

data class ContaModel(
    var id: String = "",
    var descricao: String = "",
    var valor: Double = 0.0,
    var vencimento: Date? = null,
    var status: StatusConta? = StatusConta.PENDENTE,
    var dataCriacao: Date? = null
)