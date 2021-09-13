package com.souzavaltenis.payintime.view.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.google.android.material.button.MaterialButtonToggleGroup
import com.souzavaltenis.payintime.R
import com.souzavaltenis.payintime.model.ContaModel
import com.souzavaltenis.payintime.model.enums.StatusConta
import com.souzavaltenis.payintime.singleton.ContaSingleton
import com.souzavaltenis.payintime.singleton.UsuarioSingleton
import com.souzavaltenis.payintime.util.GeralUtil

class ContaOptionsFragment(val idConta: String) : DialogFragment() {

    private lateinit var contaModel: ContaModel
    private lateinit var statusConta: StatusConta

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        //Buscando a conta com id repassado no construtor
        val keyDate: String = UsuarioSingleton.keyDate()
        contaModel = ContaSingleton.contasNormais[keyDate]?.find { it.id == idConta }!!
        statusConta = contaModel.status!!

        val dialogview = LayoutInflater.from(activity).inflate(R.layout.fragment_conta_options, null, false)
        configDataView(dialogview)

        val alert = AlertDialog.Builder(activity)
        alert.setView(dialogview)

        return alert.create()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("luz", "$statusConta")

        //Logica para salvar o que foi alterado durante esse dialog
    }

    fun configDataView(view: View){

        val ivSairSubMenu: ImageView = view.findViewById(R.id.ivSairSubMenu)
        ivSairSubMenu.setOnClickListener { dismiss() }

        val tvNomeContaSubMenu: TextView = view.findViewById(R.id.tvNomeContaSubMenu)
        tvNomeContaSubMenu.text = contaModel.descricao

        val tbGroupSubMenu: MaterialButtonToggleGroup = view.findViewById(R.id.tbGroupSubMenu)

        tbGroupSubMenu.check(GeralUtil.getIdButtonFromStatusConta(contaModel.status!!))

        tbGroupSubMenu.addOnButtonCheckedListener{ _, checkedId, isChecked ->
            if (isChecked) {
                when(checkedId){
                    R.id.btPendenteSubMenu -> { statusConta = StatusConta.PENDENTE }
                    R.id.btPagaSubMenu -> { statusConta = StatusConta.PAGA }
                    R.id.btVencidaSubMenu -> { statusConta = StatusConta.VENCIDA }
                }
            }
        }

//        val btEditarContaSubMenu: Button = view.findViewById(R.id.btEditarContaSubMenu)
//        val btApagarContaSubMenu: Button = view.findViewById(R.id.btApagarContaSubMenu)

    }

}