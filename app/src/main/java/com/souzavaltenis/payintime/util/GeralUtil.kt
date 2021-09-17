package com.souzavaltenis.payintime.util

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.text.SpannableStringBuilder
import androidx.core.text.bold
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
                StatusConta.PENDENTE -> R.drawable.ic_pending_48
                StatusConta.PAGA -> R.drawable.ic_checkbox_48
                StatusConta.VENCIDA -> R.drawable.ic_warning_48
            }
        }

        fun getColorFromStatusConta(status: StatusConta): Int{
            return when (status) {
                StatusConta.PENDENTE -> R.color.text
                StatusConta.PAGA -> R.color.green
                StatusConta.VENCIDA -> R.color.yellow
            }
        }

        fun getIdButtonFromStatusConta(status: StatusConta, isFixa: Boolean = false): Int{
            return when (status) {
                StatusConta.PENDENTE -> if(isFixa) R.id.btPendenteSubMenuFixa else R.id.btPendenteSubMenu
                StatusConta.PAGA -> if(isFixa) R.id.btPagaSubMenuFixa else R.id.btPagaSubMenu
                else -> -1
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

        fun showDialogConfirmDelete(context: Context, nomeConta: String, isFixa: Boolean = false,
                                    callbackYes: () -> Unit){

            val messageDialog = SpannableStringBuilder()
                .append("Você deseja")
                .bold { append(" APAGAR ") }
                .append("a conta" + (if(isFixa) " fixa" else "") + " (")
                .bold { append(nomeConta) }
                .append(") ?")

            val builder = AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar)
            builder.setTitle("Excluir Conta" + if(isFixa) " Fixa" else "")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage(messageDialog)
                .setCancelable(false)
                .setPositiveButton("Sim") { dialog, id ->
                    callbackYes.invoke()
                }
                .setNegativeButton("Não") { dialog, id ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }

    }
}