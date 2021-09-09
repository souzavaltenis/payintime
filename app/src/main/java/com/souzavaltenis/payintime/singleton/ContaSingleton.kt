package com.souzavaltenis.payintime.singleton

import com.souzavaltenis.payintime.model.ContaFixaModel
import com.souzavaltenis.payintime.model.ContaModel

object ContaSingleton {
    var contasFixas: ArrayList<ContaFixaModel> = arrayListOf()
    var contasNormais: HashMap<String, ArrayList<ContaModel>> = hashMapOf()
}



