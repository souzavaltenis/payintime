package com.souzavaltenis.payintime.model.dtos

data class ContaFixaDto (
    var id: String?,
    var nomeContaFixa: String?,
    var valorContaFixa: Double?,
    var diaVencimentoContaFixa: Int?,
    var pagamentos: Map<String, String>?
)