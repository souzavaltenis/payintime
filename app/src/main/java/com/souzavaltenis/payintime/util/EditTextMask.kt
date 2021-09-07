package com.souzavaltenis.payintime.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.text.NumberFormat
import java.util.*

object EditTextMask {

    fun insertCurrency(editText: EditText): TextWatcher {
        return object : TextWatcher {

            var current: String = ""

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                if(s.toString() != current){
                    editText.removeTextChangedListener(this)
                    val cleanString: String = s.toString().replace("[R$,.]".toRegex(), "").replace("\\s".toRegex(),"")
                    val value: Double = cleanString.toDouble()
                    var formatted: String = doubleToStrBRL(value, true)
                    if(value == 0.0) formatted = ""
                    current = formatted
                    editText.setText(formatted)
                    editText.setSelection(formatted.length)
                    editText.addTextChangedListener(this)
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
        }
    }

    fun doubleToStrBRL(value: Double, divider: Boolean): String {
        return NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
            .format(if (divider) value / 100 else value)
    }

    fun unMaskBRL(brl: String): String {
        return brl.replace("[R$.]".toRegex(), "")
            .replace("\\s".toRegex(), "")
            .replace("[,]".toRegex(), ".")
    }
}