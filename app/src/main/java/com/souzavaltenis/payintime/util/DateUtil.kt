package com.souzavaltenis.payintime.util

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class DateUtil {

    companion object {

        fun extractMonthName(date: LocalDate, textStyle: TextStyle = TextStyle.FULL): String {
            return date.month.getDisplayName(textStyle, Locale("pt", "BR"))
        }

        fun getNameMonthUpper(date: LocalDate): String {
            return extractMonthName(date).replaceFirstChar { c: Char -> c.uppercase() }
        }

        @SuppressLint("SimpleDateFormat")
        fun dateIsGreaterOrLessThanOneYear(date: LocalDate): Boolean {

            val isBeforeOneYear: Boolean = date.isBefore(LocalDate.now().minusYears(1))
            val isAfterOneYear: Boolean = date.isAfter(LocalDate.now().plusYears(1))

            return isBeforeOneYear ||  isAfterOneYear
        }

        fun getKeyFromDate(date: LocalDate): String{
            return extractMonthName(date, TextStyle.SHORT) + date.year
        }

    }

}