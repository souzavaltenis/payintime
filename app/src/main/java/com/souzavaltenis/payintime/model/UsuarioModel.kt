package com.souzavaltenis.payintime.model

import java.time.LocalDate
import java.util.*

data class UsuarioModel (
    var nome: String = "",
    var email: String = "",
    var salario: Double = 0.0,
    var dataCriacao: Date? = null
)