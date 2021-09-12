package com.souzavaltenis.payintime.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.souzavaltenis.payintime.R
import com.souzavaltenis.payintime.model.ContaModel
import com.souzavaltenis.payintime.model.enums.StatusConta
import com.souzavaltenis.payintime.singleton.ContaSingleton
import com.souzavaltenis.payintime.singleton.UsuarioSingleton
import com.souzavaltenis.payintime.util.GeralUtil
import com.souzavaltenis.payintime.util.interfaces.CallbackFragment

class ResumoFragment : Fragment(), CallbackFragment {

    private lateinit var tvValorAPagar: TextView
    private lateinit var tvValorJaPago: TextView
    private lateinit var tvValorSalRestante: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val viewOfLayout: View = inflater.inflate(R.layout.fragment_resumo, container, false)

        tvValorAPagar = viewOfLayout.findViewById(R.id.tvValorAPagar)
        tvValorJaPago = viewOfLayout.findViewById(R.id.tvValorJaPago)
        tvValorSalRestante = viewOfLayout.findViewById(R.id.tvValorSalRestante)

        notifyUpdate()

        return viewOfLayout
    }

    override fun notifyUpdate() {
        Log.d("fragment", "ResumoFragment.notifyUpdate")

        val keyDate: String = UsuarioSingleton.keyDate()

        val contasNormais: ArrayList<ContaModel> = ContaSingleton.contasNormais[keyDate]!!

        val (contasNormaisPagas, contasNormaisNaoPagas) = contasNormais
            .partition { it.status == StatusConta.PAGA }

        val (contasFixasPagas, contasFixasNaoPagas) = ContaSingleton.contasFixas
            .partition { it.pagamentos[keyDate] == StatusConta.PAGA }

        val aPagar: Double = contasNormaisNaoPagas.sumOf { it.valor } +
                contasFixasNaoPagas.sumOf { it.valor }

        val jaPago: Double = contasNormaisPagas.sumOf { it.valor } +
                contasFixasPagas.sumOf { it.valor }

        val salarioRestante: Double = UsuarioSingleton.usuario.salario - jaPago

        tvValorAPagar.text = GeralUtil.formatarValor(aPagar)
        tvValorJaPago.text = GeralUtil.formatarValor(jaPago)
        tvValorSalRestante.text = GeralUtil.formatarValor(salarioRestante)

    }
}