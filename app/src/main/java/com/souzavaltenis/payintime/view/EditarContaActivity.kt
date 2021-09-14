package com.souzavaltenis.payintime.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.souzavaltenis.payintime.R
import com.souzavaltenis.payintime.controller.ContaController
import com.souzavaltenis.payintime.controller.ContaFixaController
import com.souzavaltenis.payintime.model.enums.StatusConta
import com.souzavaltenis.payintime.singleton.EditContaSingleton
import com.souzavaltenis.payintime.singleton.UsuarioSingleton
import com.souzavaltenis.payintime.util.DateUtil
import com.souzavaltenis.payintime.util.EditTextMask
import com.souzavaltenis.payintime.util.GeralUtil
import java.time.LocalDate

class EditarContaActivity : AppCompatActivity() {

    companion object {
        const val RESULT_OK_EDIT_CONTA_NORMAL = 11
        const val RESULT_OK_EDIT_CONTA_FIXA = 22
    }

    private var isContaFixa: Boolean = false
    private lateinit var contaController: ContaController
    private lateinit var contaFixaController: ContaFixaController

    private lateinit var etDescContaEdit: EditText
    private lateinit var etValorContaEdit: EditText
    private lateinit var etDiaVencimentoContaEdit: EditText
    private lateinit var btEditarConta: Button

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_conta)

        val btBack: Button = findViewById(R.id.btBack)
        btBack.setOnClickListener{ finish() }

        isContaFixa = intent.getBooleanExtra("isContaFixa", false)

        val tvTitleEditarConta: TextView = findViewById(R.id.tvTitleEditarConta)
        btEditarConta = findViewById(R.id.btEditarConta)

        if(!isContaFixa){
            val date: LocalDate = UsuarioSingleton.dataSelecionada
            tvTitleEditarConta.text = "${DateUtil.getNameMonthUpper(date)}  ${date.year}"
        }else{
            tvTitleEditarConta.text = "Conta Fixa"
            val tvTitleDescEditConta: TextView = findViewById(R.id.tvTitleDescEditConta)
            tvTitleDescEditConta.text = tvTitleDescEditConta.text.toString().plus(" fixa")
            btEditarConta.text = btEditarConta.text.toString().plus(" Fixa")

            val tvDescCriarConta: TextView = findViewById(R.id.tvDescCriarConta)
            tvDescCriarConta.text = tvDescCriarConta.text.toString().plus(" fixa")
            btEditarConta.text = btEditarConta.text.toString().plus(" Fixa")
        }

        val emailUsuario: String = UsuarioSingleton.usuario.email

        contaController = ContaController(emailUsuario)
        contaFixaController = ContaFixaController(emailUsuario)

        etDescContaEdit = findViewById(R.id.etDescContaEdit)
        etValorContaEdit = findViewById(R.id.etValorContaEdit)
        etDiaVencimentoContaEdit = findViewById(R.id.etDiaVencimentoContaEdit)
        etValorContaEdit.addTextChangedListener(EditTextMask.insertCurrency(etValorContaEdit))

        setDataInputs()

        btEditarConta.setOnClickListener { atualizarConta() }
    }

    fun setDataInputs(){
        etDescContaEdit.setText(EditContaSingleton.contaNormal.descricao)
        etValorContaEdit.setText(EditTextMask.doubleToStrBRL(EditContaSingleton.contaNormal.valor))
        etDiaVencimentoContaEdit.setText(DateUtil.getDayOfDate(EditContaSingleton.contaNormal.vencimento!!).toString())
    }

    fun atualizarConta() {

        //Values of EditTexts
        val descricaoConta: String = etDescContaEdit.text.toString()
        val valorConta: Double = EditTextMask.unMaskBRL(etValorContaEdit.text.toString()).toDouble()
        val diaVencimentoConta: Int = etDiaVencimentoContaEdit.text.toString().toInt()

        //Update Conta Fixa
        if(isContaFixa){

            EditContaSingleton.contaFixa.descricao = descricaoConta
            EditContaSingleton.contaFixa.valor = valorConta
            EditContaSingleton.contaFixa.diaVencimento = diaVencimentoConta
            GeralUtil.updateStatusContaFixa(EditContaSingleton.contaFixa, UsuarioSingleton.keyDate())

            setResultAndFinish(RESULT_OK_EDIT_CONTA_FIXA)

        //Update Conta Normal
        }else{

            val vencimento: LocalDate = DateUtil.getLocalDateFromDayInActualMonth(diaVencimentoConta)

            val status: StatusConta =
                if(vencimento.isBefore(LocalDate.now())) StatusConta.VENCIDA
                else StatusConta.PENDENTE

            EditContaSingleton.contaNormal.descricao = descricaoConta
            EditContaSingleton.contaNormal.valor = valorConta
            EditContaSingleton.contaNormal.vencimento = DateUtil.convertLocalDateToDate(vencimento)
            if(EditContaSingleton.contaNormal.status != StatusConta.PAGA){
                EditContaSingleton.contaNormal.status =status
            }

            setResultAndFinish(RESULT_OK_EDIT_CONTA_NORMAL)
        }

    }

    fun setResultAndFinish(resultCode: Int){
        setResult(resultCode)
        finish()
    }

}