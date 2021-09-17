package com.souzavaltenis.payintime.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.souzavaltenis.payintime.R
import com.souzavaltenis.payintime.controller.ContaFixaController
import com.souzavaltenis.payintime.model.ContaFixaModel
import com.souzavaltenis.payintime.singleton.ContaSingleton
import com.souzavaltenis.payintime.singleton.EditContaSingleton
import com.souzavaltenis.payintime.singleton.UsuarioSingleton
import com.souzavaltenis.payintime.util.GeralUtil
import com.souzavaltenis.payintime.util.adapaters.ContasFixaAdapterRV
import com.souzavaltenis.payintime.util.enums.ActionConta
import com.souzavaltenis.payintime.util.interfaces.CallbackOptionsContaFixa

class ContasFixasActivity : AppCompatActivity() {

    companion object {
        const val RESULT_OK_CONTAS_FIXAS = 4
    }

    private lateinit var rvContasFixas: RecyclerView
    private lateinit var contasFixasAdapterRV: ContasFixaAdapterRV
    private lateinit var tvInfoContaFixa: TextView
    private lateinit var contaFixaController: ContaFixaController

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contas_fixas)

        val btBack: Button = findViewById(R.id.btBack)
        btBack.setOnClickListener{
            setResult(RESULT_OK_CONTAS_FIXAS)
            finish()
        }

        val ivAddContaFixa: ImageView = findViewById(R.id.ivAddContaFixa)
        ivAddContaFixa.setOnClickListener{criarContaFixa()}

        tvInfoContaFixa = findViewById(R.id.tvInfoContaFixa)

        rvContasFixas = findViewById(R.id.rvContasFixas)
        rvContasFixas.layoutManager = LinearLayoutManager(this)
        rvContasFixas.itemAnimator = DefaultItemAnimator()
        rvContasFixas.setHasFixedSize(true)

        contasFixasAdapterRV = ContasFixaAdapterRV(ContaSingleton.contasFixas, callbackOptionsContaFixa())
        rvContasFixas.adapter = contasFixasAdapterRV

        contaFixaController = ContaFixaController(UsuarioSingleton.usuario.email)

        atualizarInformacoesContasFixas()
    }

    private fun callbackOptionsContaFixa(): CallbackOptionsContaFixa {
        return object : CallbackOptionsContaFixa {

            override fun onClickEdit(position: Int) {
                EditContaSingleton.contaFixa = ContaSingleton.contasFixas[position]
                val intent = Intent(applicationContext, EditarContaActivity::class.java)
                intent.putExtra("isContaFixa", true)
                startActivityForResult(intent, EditarContaActivity.RESULT_OK_EDIT_CONTA_FIXA)
            }

            override fun onClickDelete(position: Int) {
                val contaFixa: ContaFixaModel = ContaSingleton.contasFixas[position]

                GeralUtil.showDialogConfirmDelete(this@ContasFixasActivity, contaFixa.descricao, true){
                    contaFixaController.delete(contaFixa.id)
                        .addOnCompleteListener {
                            ContaSingleton.contasFixas.removeAt(position)
                            Toast.makeText(applicationContext, "Conta fixa removida", Toast.LENGTH_SHORT).show()
                            contasFixasAdapterRV.notifyItemRemoved(position)
                        }
                }

            }
        }
    }

    fun criarContaFixa() {
        val intent = Intent(this, CriarContaActivity::class.java)
        intent.putExtra("isContaFixa", true)
        startActivityForResult(intent, CriarContaActivity.RESULT_OK_CONTA_FIXA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(resultCode){

            CriarContaActivity.RESULT_OK_CONTA_FIXA -> {
                Toast.makeText(this,"Conta Fixa Adicionada com Sucesso!", Toast.LENGTH_SHORT).show()
                atualizarInformacoesContasFixas()
            }

            EditarContaActivity.RESULT_OK_EDIT_CONTA_FIXA -> {
                Toast.makeText(this,"Conta Fixa Atualizada com Sucesso!", Toast.LENGTH_SHORT).show()
                atualizarInformacoesContasFixas()
            }

        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun atualizarInformacoesContasFixas() {

        contasFixasAdapterRV.notifyDataSetChanged()

        val totalContasFixas: Int = ContaSingleton.contasFixas.size
        val somaValorContasFixas: Double = ContaSingleton.contasFixas.sumOf{c -> c.valor}
        val textoInformacoes = "Total: $totalContasFixas (${
            GeralUtil.formatarValor(somaValorContasFixas)
        })"

        tvInfoContaFixa.text = textoInformacoes
    }
}