package com.souzavaltenis.payintime.util

import android.annotation.SuppressLint
import com.souzavaltenis.payintime.R
import com.souzavaltenis.payintime.model.ContaFixaModel
import com.souzavaltenis.payintime.model.enums.StatusConta
import com.souzavaltenis.payintime.singleton.UsuarioSingleton
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class GeralUtil {

    companion object {

        fun formatarValor(valor: Double): String{
            return EditTextMask.doubleToStrBRL(valor, false)
        }

        fun getResourceFromStatusConta(status: StatusConta): Int{
            return when (status) {
                StatusConta.PENDENTE -> {
                    R.drawable.ic_pending_48
                }
                StatusConta.PAGA -> {
                    R.drawable.ic_checkbox_48
                }
                StatusConta.VENCIDA -> {
                    R.drawable.ic_warning_48
                }
            }
        }

        fun getIdButtonFromStatusConta(status: StatusConta): Int{
            return when (status) {
                StatusConta.PENDENTE -> {
                    R.id.btPendenteSubMenu
                }
                StatusConta.PAGA -> {
                    R.id.btPagaSubMenu
                }
                StatusConta.VENCIDA -> {
                    R.id.btVencidaSubMenu
                }
            }
        }

        @SuppressLint("SimpleDateFormat")
        fun convertDateToStr(date: Date, pattern: String): String {
            val sdf = SimpleDateFormat(pattern)
            return sdf.format(date)
        }

        fun updateStatusContaFixa(contaFixa: ContaFixaModel, keyDate: String): ContaFixaModel {

            val existeStatusPagamentoNaDataAtual: Boolean = contaFixa.pagamentos.containsKey(keyDate)
            val estaPendente: Boolean = contaFixa.pagamentos[keyDate] == StatusConta.PENDENTE
            val vencimento: LocalDate = DateUtil.getLocalDateFromDayInMonthSpecific(
                contaFixa.diaVencimento,
                UsuarioSingleton.dataSelecionada
            )
            val estaVencida: Boolean = vencimento.isBefore(LocalDate.now())

            if(existeStatusPagamentoNaDataAtual && estaPendente && estaVencida){
                contaFixa.pagamentos[keyDate] = StatusConta.VENCIDA
            }else if(!existeStatusPagamentoNaDataAtual){
                contaFixa.pagamentos[keyDate] =
                    if(estaVencida) StatusConta.VENCIDA
                    else StatusConta.PENDENTE
            }

            return contaFixa
        }

    }
}