package com.souzavaltenis.payintime.util.adapaters

import android.graphics.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.souzavaltenis.payintime.R
import com.souzavaltenis.payintime.model.ContaFixaModel
import com.souzavaltenis.payintime.model.enums.StatusConta
import com.souzavaltenis.payintime.singleton.UsuarioSingleton
import com.souzavaltenis.payintime.util.DateUtil
import com.souzavaltenis.payintime.util.GeralUtil
import java.util.*

class HomeContasFixaAdapterRV(
        var contasFixas: ArrayList<ContaFixaModel>
    ): RecyclerView.Adapter<HomeContasFixaAdapterRV.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val ivStatusConta: ImageView = view.findViewById(R.id.ivStatusConta)
        private val tvNomeConta: TextView = view.findViewById(R.id.tvNomeConta)
        private val tvVencimentoConta: TextView = view.findViewById(R.id.tvVencimentoConta)
        private val tvValorConta: TextView = view.findViewById(R.id.tvValorConta)
        private val btSubMenuConta: Button = view.findViewById(R.id.btSubMenuConta)
        private val viewColorStatus: View = view.findViewById(R.id.viewColorStatus)

        fun bind(contaFixa: ContaFixaModel){

            val keyDate: String = UsuarioSingleton.keyDate()
            val vencimento: Date = DateUtil.getDateFromDayInMonthSpecific(contaFixa.diaVencimento, UsuarioSingleton.dataSelecionada)
            val status: StatusConta = contaFixa.pagamentos[keyDate] ?: StatusConta.PENDENTE

            val idColorStatus: Int =GeralUtil.getColorFromStatusConta(status)
            val idImageStatus: Int = GeralUtil.getResourceFromStatusConta(status)
            setColorStatusAndIcon(idColorStatus, idImageStatus)

            tvNomeConta.text = contaFixa.descricao
            tvVencimentoConta.text = GeralUtil.convertDateToStr(vencimento, "dd/MM")
            tvValorConta.text = GeralUtil.formatarValor(contaFixa.valor)
            btSubMenuConta.setOnClickListener {
                //Logica
            }
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
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_conta, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val contaFixa: ContaFixaModel = contasFixas[position]
        viewHolder.bind(contaFixa)
    }

    override fun getItemCount() = contasFixas.size

}