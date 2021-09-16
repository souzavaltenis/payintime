package com.souzavaltenis.payintime.util.adapaters

import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.souzavaltenis.payintime.R.id
import com.souzavaltenis.payintime.R.layout
import com.souzavaltenis.payintime.model.ContaModel
import com.souzavaltenis.payintime.util.GeralUtil
import com.souzavaltenis.payintime.util.interfaces.CallbackMenuConta

class HomeContasAdapterRV(
        var contasNormais: ArrayList<ContaModel>,
        private val callbackMenuConta: CallbackMenuConta
    ): RecyclerView.Adapter<HomeContasAdapterRV.ViewHolder>() {

    class ViewHolder(view: View, private val callbackMenuConta: CallbackMenuConta) : RecyclerView.ViewHolder(view) {

        private val ivStatusConta: ImageView = view.findViewById(id.ivStatusConta)
        private val tvNomeConta: TextView = view.findViewById(id.tvNomeConta)
        private val tvVencimentoConta: TextView = view.findViewById(id.tvVencimentoConta)
        private val tvValorConta: TextView = view.findViewById(id.tvValorConta)
        private val btSubMenuConta: Button = view.findViewById(id.btSubMenuConta)
        private val viewColorStatus: View = view.findViewById(id.viewColorStatus)

        fun bind(contaNormal: ContaModel){

            val idColorStatus: Int =GeralUtil.getColorFromStatusConta(contaNormal.status!!)
            val idImageStatus: Int = GeralUtil.getResourceFromStatusConta(contaNormal.status!!)
            setColorStatusAndIcon(idColorStatus, idImageStatus)

            tvNomeConta.text = contaNormal.descricao
            tvVencimentoConta.text = GeralUtil.convertDateToStr(contaNormal.vencimento!!, "dd/MM")
            tvValorConta.text = GeralUtil.formatarValor(contaNormal.valor)
            btSubMenuConta.setOnClickListener{callbackMenuConta.notify(contaNormal.id)}
        }

        private fun setColorStatusAndIcon(idColor: Int, idIcon: Int){
            val color: Int = ContextCompat.getColor(viewColorStatus.context, idColor)
            viewColorStatus.setBackgroundColor(color)

            val myIcon = ContextCompat.getDrawable(ivStatusConta.context, idIcon)!!
            val filter: ColorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY)
            myIcon.colorFilter = filter
            ivStatusConta.setImageDrawable(myIcon)
        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(layout.card_conta, viewGroup, false)
        return ViewHolder(view, callbackMenuConta)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(contasNormais[position])
    }

    override fun getItemCount() = contasNormais.size

}