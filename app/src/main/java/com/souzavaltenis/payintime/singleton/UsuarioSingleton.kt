package com.souzavaltenis.payintime.singleton

import com.souzavaltenis.payintime.model.UsuarioModel
import java.time.LocalDate

object UsuarioSingleton {
    var dataSelecionada : LocalDate = LocalDate.now()
    lateinit var usuario: UsuarioModel
}



