package com.souzavaltenis.payintime.singleton

import com.souzavaltenis.payintime.model.UsuarioModel
import com.souzavaltenis.payintime.util.DateUtil
import java.time.LocalDate

object UsuarioSingleton {
    var dataSelecionada : LocalDate = LocalDate.now()
    lateinit var usuario: UsuarioModel

    fun keyDate(): String = DateUtil.getKeyFromDate(dataSelecionada)
}



