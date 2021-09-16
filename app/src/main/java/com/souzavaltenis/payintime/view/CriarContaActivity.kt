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
import com.souzavaltenis.payintime.model.ContaFixaModel
import com.souzavaltenis.payintime.model.ContaModel
import com.souzavaltenis.payintime.model.enums.StatusConta
import com.souzavaltenis.payintime.singleton.ContaSingleton
import com.souzavaltenis.payintime.singleton.UsuarioSingleton
import com.souzavaltenis.payintime.util.DateUtil
import com.souzavaltenis.payintime.util.EditTextMask
import java.time.LocalDate
import java.util.*

class CriarContaActivity : AppCompatActivity() {

    companion object {
        const val RESULT_OK_CONTA_NORMAL = 1
        const val RESULT_OK_CONTA_FIXA = 2
    }

    private var isContaFixa: Boolean = false
    private lateinit var contaController: ContaController
    private lateinit var contaFixaController: ContaFixaController

    private lateinit var etDescConta: EditText
    private lateinit var etValorConta: EditText
    private lateinit var etDiaVencimentoConta: EditText
    private lateinit var btCadastrarConta: Button

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_criar_conta)

        val btBack: Button = findViewById(R.id.btBack)
        btBack.setOnClickListener{ finish() }

        isContaFixa = intent.getBooleanExtra("isContaFixa", false)

        val tvTitleCriarConta: TextView = findViewById(R.id.tvTitleEditarConta)
        btCadastrarConta = findViewById(R.id.btCadastrarConta)

        if(!isContaFixa){
            val date: LocalDate = UsuarioSingleton.dataSelecionada
            tvTitleCriarConta.text = "${DateUtil.getNameMonthUpper(date)}  ${date.year}"
        }else{
            tvTitleCriarConta.text = "Conta Fixa"

            val tvDescCriarConta: TextView = findViewById(R.id.tvDescCriarConta)
            tvDescCriarConta.text = tvDescCriarConta.text.toString().plus(" fixa")
            btCadastrarConta.text = btCadastrarConta.text.toString().plus(" Fixa")
        }

        val emailUsuario: String = UsuarioSingleton.usuario.email

        contaController = ContaController(emailUsuario)
        contaFixaController = ContaFixaController(emailUsuario)

        etDescConta = findViewById(R.id.etDescConta)
        etValorConta = findViewById(R.id.etValorConta)
        etDiaVencimentoConta = findViewById(R.id.etDiaVencimentoConta)

        etValorConta.addTextChangedListener(EditTextMask.insertCurrency(etValorConta))

        btCadastrarConta.setOnClickListener { cadastrarConta() }
    }

    fun cadastrarConta() {

        //Values of EditTexts
        val idConta: String = UUID.randomUUID().toString()
        val descricaoConta: String = etDescConta.text.toString()
        val valorConta: Double = EditTextMask.unMaskBRL(etValorConta.text.toString()).toDouble()
        val diaVencimentoConta: Int = etDiaVencimentoConta.text.toString().toInt()

        //Create Conta Fixa
        if(isContaFixa){

            val pagamentos: HashMap<String, StatusConta> = hashMapOf()
            val contaFixa = ContaFixaModel(idConta, descricaoConta, valorConta, diaVencimentoConta, pagamentos, Date())

            contaFixaController.save(contaFixa).addOnCompleteListener {
                ContaSingleton.contasFixas.add(contaFixa)
                setResultAndFinish(RESULT_OK_CONTA_FIXA)
            }

        //Create Conta Normal
        }else{


            val vencimento: LocalDate = DateUtil.getLocalDateFromDayInMonthSpecific(
                diaVencimentoConta,
                UsuarioSingleton.dataSelecionada
            )

            val status: StatusConta =
                if(vencimento.isBefore(LocalDate.now())) StatusConta.VENCIDA
                else StatusConta.PENDENTE

            val conta = ContaModel(
                idConta,
                descricaoConta,
                valorConta,
                DateUtil.convertLocalDateToDate(vencimento),
                status,
                Date()
            )

            val keyDate = DateUtil.getKeyFromDate(UsuarioSingleton.dataSelecionada)

            contaController.save(keyDate, conta) {

                val contasNormais: HashMap<String, ArrayList<ContaModel>> = ContaSingleton.contasNormais

                if(!contasNormais.containsKey(keyDate)){
                    contasNormais[keyDate] = arrayListOf(conta)
                }else{
                    contasNormais[keyDate]?.add(conta)
                }

                setResultAndFinish(RESULT_OK_CONTA_NORMAL)

            }
        }

    }

    fun setResultAndFinish(resultCode: Int){
        setResult(resultCode)
        finish()
    }

}