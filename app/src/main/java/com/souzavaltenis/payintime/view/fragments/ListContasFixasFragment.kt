package com.souzavaltenis.payintime.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.souzavaltenis.payintime.R
import com.souzavaltenis.payintime.singleton.ContaSingleton
import com.souzavaltenis.payintime.util.adapaters.HomeContasFixaAdapterRV
import com.souzavaltenis.payintime.util.interfaces.CallbackFragment
import com.souzavaltenis.payintime.util.interfaces.CallbackMenuConta

class ListContasFixasFragment(callbackMenuContaFixa: CallbackMenuConta) : Fragment(), CallbackFragment {

    private lateinit var rvTabContasNormais: RecyclerView
    private var homeContasFixaAdapterRV: HomeContasFixaAdapterRV = HomeContasFixaAdapterRV(ContaSingleton.contasFixas, callbackMenuContaFixa)

    private var isInit: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        isInit = true

        val viewOfLayout: View = inflater.inflate(R.layout.fragment_list_contas_fixas, container, false)

        rvTabContasNormais = viewOfLayout.findViewById(R.id.rvTabContasFixas)
        rvTabContasNormais.layoutManager = LinearLayoutManager(context)
        rvTabContasNormais.itemAnimator = DefaultItemAnimator()
        rvTabContasNormais.setHasFixedSize(true)
        rvTabContasNormais.adapter = homeContasFixaAdapterRV
        notifyUpdate()
        return viewOfLayout
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun notifyUpdate() {
        if(isInit) {
            homeContasFixaAdapterRV.contasFixas = ContaSingleton.contasFixas
            homeContasFixaAdapterRV.notifyDataSetChanged()
        }
    }

}