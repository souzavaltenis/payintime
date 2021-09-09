package com.souzavaltenis.payintime.model

import com.souzavaltenis.payintime.model.enums.StatusConta

data class ContaFixaModel(
    var id: String = "",
    var descricao: String = "",
    var valor: Double = 0.0,
    var diaVencimento: Int = -1,
    var pagamentos: Map<String, StatusConta> = hashMapOf()
)