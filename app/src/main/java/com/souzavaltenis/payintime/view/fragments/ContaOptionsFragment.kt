package com.souzavaltenis.payintime.view.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.button.MaterialButtonToggleGroup
import com.souzavaltenis.payintime.R
import com.souzavaltenis.payintime.controller.ContaController
import com.souzavaltenis.payintime.model.ContaModel
import com.souzavaltenis.payintime.model.enums.StatusConta
import com.souzavaltenis.payintime.singleton.ContaSingleton
import com.souzavaltenis.payintime.singleton.EditContaSingleton
import com.souzavaltenis.payintime.singleton.UsuarioSingleton
import com.souzavaltenis.payintime.util.GeralUtil
import com.souzavaltenis.payintime.util.enums.ActionConta
import com.souzavaltenis.payintime.util.interfaces.CallbackChangesConta
import com.souzavaltenis.payintime.view.EditarContaActivity

class ContaOptionsFragment(
        private val idConta: String,
        private val callbackChangesConta: CallbackChangesConta) : DialogFragment() {

    private lateinit var contaModelAntiga: ContaModel
    private lateinit var contaModelNova: ContaModel
    private lateinit var statusConta: StatusConta
    private var actionConta: ActionConta = ActionConta.NENHUMA

    private lateinit var tbGroupSubMenu: MaterialButtonToggleGroup

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        loadDataConta()

        val dialogview = LayoutInflater.from(activity).inflate(R.layout.fragment_conta_options, null, false)
        configDataView(dialogview)

        val alert = AlertDialog.Builder(activity)
        alert.setView(dialogview)

        return alert.create()
    }

    private fun loadDataConta(){
        //Chave para acessar as contas do mês selecionado pelo usuário
        val keyDate: String = UsuarioSingleton.keyDate()

        //Conta antiga = preservação do estado da conta antes de realizar possíveis alterações
        contaModelAntiga = ContaSingleton.contasNormais[keyDate]?.find { it.id == idConta }!!.copy()

        //Salvando uma cópia da conta em um singleton
        EditContaSingleton.contaNormal = contaModelAntiga.copy()

        //Apontando contaModelNova para a cópia criada no singleton
        contaModelNova = EditContaSingleton.contaNormal

        //Coletando o status da conta
        statusConta = contaModelNova.status!!
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

        contaModelNova.status = statusConta

        if(actionConta == ActionConta.NENHUMA && contaModelNova != contaModelAntiga){
            actionConta = ActionConta.EDITADA
        }

        if(actionConta != ActionConta.NENHUMA) {

            val contaController = ContaController(UsuarioSingleton.usuario.email)

            when (actionConta) {

                ActionConta.EDITADA -> {
                    contaController.update(UsuarioSingleton.keyDate(), contaModelAntiga, contaModelNova) {
                        updateContaInList()
                        callbackChangesConta.onUpdate()
                    }

                }

                ActionConta.APAGADA -> {
                    contaController.delete(UsuarioSingleton.keyDate(), contaModelAntiga) {
                        deleteContaInList()
                        callbackChangesConta.onDelete()
                    }
                }

                else -> {}
            }
        }

    }

    private fun getIndexOfContaById(keyDate: String): Int{
        return ContaSingleton.contasNormais[keyDate]?.indexOfFirst { it.id == idConta }!!
    }

    private fun updateContaInList(){
        val keyDate: String = UsuarioSingleton.keyDate()
        val index: Int = getIndexOfContaById(keyDate)
        ContaSingleton.contasNormais[keyDate]?.set(index, contaModelNova)
    }

    private fun deleteContaInList(){
        val keyDate: String = UsuarioSingleton.keyDate()
        val index: Int = getIndexOfContaById(keyDate)
        ContaSingleton.contasNormais[keyDate]?.removeAt(index)
    }

    private fun configDataView(view: View){

        val ivSairSubMenu: ImageView = view.findViewById(R.id.ivSairSubMenu)
        ivSairSubMenu.setOnClickListener { dismiss() }

        val tvNomeContaSubMenu: TextView = view.findViewById(R.id.tvNomeContaSubMenu)
        tvNomeContaSubMenu.text = contaModelAntiga.descricao

        tbGroupSubMenu = view.findViewById(R.id.tbGroupSubMenu)
        tbGroupSubMenu.check(GeralUtil.getIdButtonFromStatusConta(contaModelAntiga.status!!))

        tbGroupSubMenu.addOnButtonCheckedListener{ _, checkedId, isChecked ->
            if (isChecked) {
                when(checkedId){
                    R.id.btPendenteSubMenu -> { setStatusConta(StatusConta.PENDENTE) }
                    R.id.btPagaSubMenu -> { setStatusConta(StatusConta.PAGA) }
                }
            }
        }

        val btEditarContaSubMenu: Button = view.findViewById(R.id.btEditarContaSubMenu)
        btEditarContaSubMenu.setOnClickListener {
            startActivityForResult(Intent(context, EditarContaActivity::class.java),
                EditarContaActivity.RESULT_OK_EDIT_CONTA_NORMAL)
        }

        val btApagarContaSubMenu: Button = view.findViewById(R.id.btApagarContaSubMenu)
        btApagarContaSubMenu.setOnClickListener {
            //Algum popup para confirmação
            actionConta = ActionConta.APAGADA
            dismiss()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == EditarContaActivity.RESULT_OK_EDIT_CONTA_NORMAL){
            statusConta = EditContaSingleton.contaNormal.status!!
            tbGroupSubMenu.check(GeralUtil.getIdButtonFromStatusConta(statusConta))
        }
    }

}