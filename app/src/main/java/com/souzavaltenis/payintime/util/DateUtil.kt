package com.souzavaltenis.payintime.util

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import com.souzavaltenis.payintime.singleton.UsuarioSingleton
import java.time.LocalDate
import java.time.ZoneId
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

        fun getKeyFromDate(date: LocalDate = LocalDate.now()): String{
            return extractMonthName(date, TextStyle.SHORT).replace(".", "") + date.year
        }

        fun getLocalDateFromDayInActualMonth(day: Int): LocalDate {
            val localDateActual = UsuarioSingleton.dataSelecionada
            return LocalDate.of(
                localDateActual.year,
                localDateActual.month,
                day
            )
        }

        fun getLocalDateFromDayInMonthSpecific(day: Int, date: LocalDate): LocalDate {
            return LocalDate.of(date.year, date.month, day)
        }

        fun convertLocalDateToDate(dateToConvert: LocalDate): Date {
            return Date.from(
                    dateToConvert.atStartOfDay()
                    .atZone(ZoneId.of("America/Sao_Paulo"))
                    .toInstant()
            )
        }

        fun getDateFromDayInMonthSpecific(day: Int, date: LocalDate): Date {
            return convertLocalDateToDate(
                getLocalDateFromDayInMonthSpecific(day, date)
            )
        }

        fun convertDateToLocalDate(dateToConvert: Date): LocalDate {
            return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        }

        fun getDayOfDate(date: Date): Int {
            return convertDateToLocalDate(date).dayOfMonth
        }

    }

}