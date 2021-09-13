package com.souzavaltenis.payintime.util.adapaters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.souzavaltenis.payintime.R
import com.souzavaltenis.payintime.model.ContaModel
import com.souzavaltenis.payintime.util.GeralUtil
import com.souzavaltenis.payintime.util.interfaces.CallbackMenuConta

class HomeContasAdapterRV(
        var contasNormais: ArrayList<ContaModel>,
        private val callbackMenuConta: CallbackMenuConta
    ): RecyclerView.Adapter<HomeContasAdapterRV.ViewHolder>() {

    class ViewHolder(view: View, private val callbackMenuConta: CallbackMenuConta) : RecyclerView.ViewHolder(view) {

        private val ivStatusConta: ImageView = view.findViewById(R.id.ivStatusConta)
        private val tvNomeConta: TextView = view.findViewById(R.id.tvNomeConta)
        private val tvVencimentoConta: TextView = view.findViewById(R.id.tvVencimentoConta)
        private val tvValorConta: TextView = view.findViewById(R.id.tvValorConta)
        private val btSubMenuConta: Button = view.findViewById(R.id.btSubMenuConta)

        fun bind(contaNormal: ContaModel){
            ivStatusConta.setImageResource(GeralUtil.getResourceFromStatusConta(contaNormal.status!!))
            tvNomeConta.text = contaNormal.descricao
            tvVencimentoConta.text = GeralUtil.convertDateToStr(contaNormal.vencimento!!, "dd/MM")
            tvValorConta.text = GeralUtil.formatarValor(contaNormal.valor)
            btSubMenuConta.setOnClickListener{callbackMenuConta.notify(contaNormal.id)}
        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_conta, viewGroup, false)
        return ViewHolder(view, callbackMenuConta)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(contasNormais[position])
    }

    override fun getItemCount() = contasNormais.size

}