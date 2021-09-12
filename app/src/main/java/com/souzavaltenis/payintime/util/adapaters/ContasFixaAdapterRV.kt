package com.souzavaltenis.payintime.util.adapaters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.souzavaltenis.payintime.R
import com.souzavaltenis.payintime.model.ContaFixaModel
import com.souzavaltenis.payintime.util.GeralUtil

class ContasFixaAdapterRV(
        var contasFixas: ArrayList<ContaFixaModel>
    ): RecyclerView.Adapter<ContasFixaAdapterRV.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val tvNomeContaFixa: TextView = view.findViewById(R.id.tvNomeContaFixa)
        private val tvValorContaFixa: TextView = view.findViewById(R.id.tvValorContaFixa)
        private val tvDiaVencimentoContaFixa: TextView = view.findViewById(R.id.tvDiaVencimentoContaFixa)
        private val btSubMenu: Button = view.findViewById(R.id.btSubMenu)

        @SuppressLint("SetTextI18n")
        fun bind(contaFixa: ContaFixaModel){
            tvNomeContaFixa.text = contaFixa.descricao
            tvValorContaFixa.text = GeralUtil.formatarValor(contaFixa.valor)
            tvDiaVencimentoContaFixa.text = "Dia do Vencimento: ${contaFixa.diaVencimento}"
            btSubMenu.setOnClickListener {
                //Logica
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_conta_fixa, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val contaFixa = contasFixas[position]
        viewHolder.bind(contaFixa)
    }

    override fun getItemCount() = contasFixas.size
}