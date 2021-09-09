package com.souzavaltenis.payintime.util

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.souzavaltenis.payintime.R
import com.souzavaltenis.payintime.model.ContaFixaModel

class ContasFixaAdapterRV(private var contasFixas: ArrayList<ContaFixaModel>): RecyclerView.Adapter<ContasFixaAdapterRV.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNomeContaFixa: TextView = view.findViewById(R.id.tvNomeContaFixa)
        val tvValorContaFixa: TextView = view.findViewById(R.id.tvValorContaFixa)
        val tvDiaVencimentoContaFixa: TextView = view.findViewById(R.id.tvDiaVencimentoContaFixa)
        val btSubMenu: Button = view.findViewById(R.id.btSubMenu)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_conta_fixa, viewGroup, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val contaFixa = contasFixas[position]

        viewHolder.tvNomeContaFixa.text = contaFixa.descricao
        viewHolder.tvValorContaFixa.text = GeralUtil.formatarValor(contaFixa.valor)
        viewHolder.tvDiaVencimentoContaFixa.text = "Dia do Vencimento: ${contaFixa.diaVencimento}"
        viewHolder.btSubMenu.setOnClickListener {
            //Logica para quando clicar no submenu do card de uma conta fixa
        }
    }

    override fun getItemCount() = contasFixas.size
}