package com.souzavaltenis.payintime.model

import com.souzavaltenis.payintime.model.enums.StatusConta
import java.time.LocalDate

data class ContaModel (
    var id: String = "",
    var descricao: String = "",
    var vencimento: LocalDate? = null,
    var valor: Double = 0.0,
    var fixa: Boolean = false,
    var status: StatusConta = StatusConta.PENDENTE
)