package com.souzavaltenis.payintime.singleton

import com.souzavaltenis.payintime.model.ContaFixaModel
import com.souzavaltenis.payintime.model.ContaModel

object EditContaSingleton {
    var contaNormal: ContaModel = ContaModel()
    var contaFixa: ContaFixaModel = ContaFixaModel()
}



