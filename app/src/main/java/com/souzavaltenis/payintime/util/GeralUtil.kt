package com.souzavaltenis.payintime.util

class GeralUtil {

    companion object {

        fun formatarValor(valor: Double): String{
            return EditTextMask.doubleToStrBRL(valor, false)
        }

    }
}