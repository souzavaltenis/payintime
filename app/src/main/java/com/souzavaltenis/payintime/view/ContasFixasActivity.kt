package com.souzavaltenis.payintime.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.souzavaltenis.payintime.R
import com.souzavaltenis.payintime.singleton.ContaSingleton
import com.souzavaltenis.payintime.util.ContasFixaAdapterRV
import com.souzavaltenis.payintime.util.GeralUtil

class ContasFixasActivity : AppCompatActivity() {

    private lateinit var rvContasFixas: RecyclerView
    private lateinit var contasFixasAdapterRV: ContasFixaAdapterRV
    private lateinit var tvInfoConta: TextView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contas_fixas)

        val btBack: Button = findViewById(R.id.btBack)
        btBack.setOnClickListener{onBackPressed()}

        val ivAddConta: ImageView = findViewById(R.id.ivAddConta)
        ivAddConta.setOnClickListener{criarContaFixa()}

        val tvTipoConta: TextView = findViewById(R.id.tvTipoConta)
        tvTipoConta.text = "Contas Fixas"

        tvInfoConta = findViewById(R.id.tvInfoConta)

        rvContasFixas = findViewById(R.id.rvContasFixas)
        rvContasFixas.layoutManager = LinearLayoutManager(this)
        rvContasFixas.itemAnimator = DefaultItemAnimator()
        rvContasFixas.setHasFixedSize(true)

        contasFixasAdapterRV = ContasFixaAdapterRV(ContaSingleton.contasFixas)

        rvContasFixas.adapter = contasFixasAdapterRV

        atualizarInformacoesContasFixas()
    }

    fun criarContaFixa() {
        val intent = Intent(this, CriarContaActivity::class.java)
        intent.putExtra("isContaFixa", true)
        startActivityForResult(intent, CriarContaActivity.RESULT_OK_CONTA_FIXA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == CriarContaActivity.RESULT_OK_CONTA_FIXA){
            val indexAdded: Int = data?.getIntExtra("indexAdded", -1)!!
            Toast.makeText(this,"Conta Fixa Adicionada com Sucesso!", Toast.LENGTH_SHORT).show()
            contasFixasAdapterRV.notifyItemInserted(indexAdded)
            atualizarInformacoesContasFixas()
        }
    }

    fun atualizarInformacoesContasFixas() {

        val totalContasFixas: Int = ContaSingleton.contasFixas.size
        val somaValorContasFixas: Double = ContaSingleton.contasFixas
            .stream()
            .mapToDouble { contaFixa -> contaFixa.valor }
            .sum()

        val textoInformacoes = "Total: $totalContasFixas (${
            GeralUtil.formatarValor(somaValorContasFixas)
        })"

        tvInfoConta.text = textoInformacoes
    }
}