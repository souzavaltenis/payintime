package com.souzavaltenis.payintime.util.adapaters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.souzavaltenis.payintime.R
import com.souzavaltenis.payintime.model.ContaFixaModel
import com.souzavaltenis.payintime.util.GeralUtil
import com.souzavaltenis.payintime.util.interfaces.CallbackOptionsContaFixa

class ContasFixaAdapterRV(
        var contasFixas: ArrayList<ContaFixaModel>,
        private val callbackOptionsContaFixa: CallbackOptionsContaFixa
    ): RecyclerView.Adapter<ContasFixaAdapterRV.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val tvNomeContaFixa: TextView = view.findViewById(R.id.tvNomeContaFixa)
        private val tvValorContaFixa: TextView = view.findViewById(R.id.tvValorContaFixa)
        private val tvDiaVencimentoContaFixa: TextView = view.findViewById(R.id.tvDiaVencimentoContaFixa)
        val btSubMenu: Button = view.findViewById(R.id.btSubMenu)

        @SuppressLint("SetTextI18n")
        fun bind(contaFixa: ContaFixaModel){
            tvNomeContaFixa.text = contaFixa.descricao
            tvValorContaFixa.text = GeralUtil.formatarValor(contaFixa.valor)
            tvDiaVencimentoContaFixa.text = "Dia do Vencimento: ${contaFixa.diaVencimento}"

        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_conta_fixa, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val contaFixa = contasFixas[position]
        viewHolder.bind(contaFixa)

        viewHolder.btSubMenu.setOnClickListener {

            val popup = PopupMenu(viewHolder.btSubMenu.context, viewHolder.btSubMenu)
            popup.inflate(R.menu.submenu_conta_fixa_items)

            popup.setOnMenuItemClickListener {

                when(it.itemId){
                    R.id.opEditarContaFixa -> callbackOptionsContaFixa.onClickEdit(position)
                    R.id.opApagarContaFixa -> callbackOptionsContaFixa.onClickDelete(position)
                }

                true
            }

            popup.show()
        }
    }

    override fun getItemCount() = contasFixas.size
}