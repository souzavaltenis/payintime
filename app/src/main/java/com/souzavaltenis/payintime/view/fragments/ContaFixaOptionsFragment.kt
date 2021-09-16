package com.souzavaltenis.payintime.view.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.button.MaterialButtonToggleGroup
import com.souzavaltenis.payintime.R
import com.souzavaltenis.payintime.controller.ContaFixaController
import com.souzavaltenis.payintime.model.ContaFixaModel
import com.souzavaltenis.payintime.model.enums.StatusConta
import com.souzavaltenis.payintime.singleton.ContaSingleton
import com.souzavaltenis.payintime.singleton.EditContaSingleton
import com.souzavaltenis.payintime.singleton.UsuarioSingleton
import com.souzavaltenis.payintime.util.GeralUtil
import com.souzavaltenis.payintime.util.enums.ActionConta
import com.souzavaltenis.payintime.util.interfaces.CallbackChangesContaFixa

class ContaFixaOptionsFragment(
        private val idConta: String,
        private val callbackChangesContaFixa: CallbackChangesContaFixa
    ) : DialogFragment() {

    private lateinit var contaFixaModelAntiga: ContaFixaModel
    private lateinit var contaFixaModelNova: ContaFixaModel

    private lateinit var statusConta: StatusConta
    private var actionConta: ActionConta = ActionConta.NENHUMA

    private lateinit var tvNomeContaFixaSubMenu: TextView
    private lateinit var tvValorContaFixaSubMenu: TextView

    private lateinit var tbGroupSubMenuFixa: MaterialButtonToggleGroup

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        loadDataConta()

        val dialogview = LayoutInflater.from(activity).inflate(R.layout.fragment_conta_fixa_options, null, false)
        configDataView(dialogview)

        val alert = AlertDialog.Builder(activity)
        alert.setView(dialogview)

        return alert.create()
    }

    private fun loadDataConta(){

        contaFixaModelAntiga = ContaSingleton.contasFixas.find { it.id == idConta }!!.copy()

        contaFixaModelNova = contaFixaModelAntiga.copy(pagamentos = HashMap(contaFixaModelAntiga.pagamentos))

        EditContaSingleton.contaFixa = contaFixaModelNova

        val keyDate: String = UsuarioSingleton.keyDate()
        statusConta = contaFixaModelNova.pagamentos[keyDate]!!
    }

    private fun configDataView(view: View) {

        val ivSairSubMenuFixa: ImageView = view.findViewById(R.id.ivSairSubMenuFixa)
        ivSairSubMenuFixa.setOnClickListener { dismiss() }

        tvNomeContaFixaSubMenu = view.findViewById(R.id.tvNomeContaFixaSubMenu)
        tvValorContaFixaSubMenu = view.findViewById(R.id.tvValorContaFixaSubMenu)
        tbGroupSubMenuFixa = view.findViewById(R.id.tbGroupSubMenuFixa)

        setData()

        tbGroupSubMenuFixa.addOnButtonCheckedListener{ _, checkedId, isChecked ->
            if (isChecked) {
                when(checkedId){
                    R.id.btPendenteSubMenuFixa -> { setStatusConta(StatusConta.PENDENTE) }
                    R.id.btPagaSubMenuFixa -> { setStatusConta(StatusConta.PAGA) }
                }
            }
        }

    }

    private fun setData(){
        tvNomeContaFixaSubMenu.text = contaFixaModelNova.descricao
        tvValorContaFixaSubMenu.text = GeralUtil.formatarValor(contaFixaModelNova.valor)

        val keyDate: String = UsuarioSingleton.keyDate()
        tbGroupSubMenuFixa.check(GeralUtil.getIdButtonFromStatusConta(contaFixaModelNova.pagamentos[keyDate]!!, true))
    }

    private fun setStatusConta(statusConta: StatusConta){
        this.statusConta = statusConta
        Toast.makeText(context, "Novo status: $statusConta", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy(){
        super.onDestroy()
        updateInfo()
    }

    private fun updateInfo(){

        val keyDate: String = UsuarioSingleton.keyDate()
        contaFixaModelNova.pagamentos[keyDate] = statusConta

        if(actionConta == ActionConta.NENHUMA && contaFixaModelNova != contaFixaModelAntiga){
            actionConta = ActionConta.EDITADA
        }

        if(actionConta == ActionConta.EDITADA){

            val contaFixaController = ContaFixaController(UsuarioSingleton.usuario.email)
            contaFixaController.update(contaFixaModelNova).addOnCompleteListener {

                updateContaInList()
                callbackChangesContaFixa.onStatusUpdate()
            }
        }

    }

    private fun updateContaInList(){
        val index: Int = ContaSingleton.contasFixas.indexOfFirst { it.id == idConta }
        ContaSingleton.contasFixas[index] = contaFixaModelNova
    }

}