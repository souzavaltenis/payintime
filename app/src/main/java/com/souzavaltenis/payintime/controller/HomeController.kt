package com.souzavaltenis.payintime.controller

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.souzavaltenis.payintime.model.ContaFixaModel
import com.souzavaltenis.payintime.model.ContaModel
import com.souzavaltenis.payintime.model.UsuarioModel
import com.souzavaltenis.payintime.model.dtos.ContasDto
import com.souzavaltenis.payintime.singleton.ContaSingleton
import com.souzavaltenis.payintime.singleton.UsuarioSingleton
import com.souzavaltenis.payintime.util.GeralUtil

class HomeController {

    private var authController: AuthController = AuthController()
    private var currentEmail: String = authController.getEmailUser()!!
    private var usuarioController: UsuarioController = UsuarioController()
    private var contaFixaController: ContaFixaController = ContaFixaController(currentEmail)
    private var contaController: ContaController = ContaController(currentEmail)

    fun initUsuario(myCallBack: () -> Unit) {
        usuarioController.findByEmail(currentEmail).addOnSuccessListener { doc: DocumentSnapshot? ->
            //Usuario Carregado
            UsuarioSingleton.usuario = doc?.toObject(UsuarioModel::class.java)!!
            contaFixaController.findAll().addOnCompleteListener { taskFixas ->
                //Contas Fixas Carregadas
                val contasFixas: ArrayList<ContaFixaModel> = taskFixas.result?.toObjects(ContaFixaModel::class.java) as ArrayList<ContaFixaModel>
                //Ordenando
                contasFixas.sortBy { it.dataCriacao }
                //Setando no singleton
                ContaSingleton.contasFixas = contasFixas
                //Contas Normais Solicitação de Carregamento
                loadContasNormais(myCallBack)
            }
        }

    }

    fun setStatusContasFixas(){
        val keyDate: String = UsuarioSingleton.keyDate()
        ContaSingleton.contasFixas = ContaSingleton.contasFixas
            .map{ GeralUtil.updateStatusContaFixa(it, keyDate) }.toCollection(ArrayList())
    }

    fun loadContasNormais(myCallBack: () -> Unit){

        val keyDate: String = UsuarioSingleton.keyDate()

        contaController.findAllByKeyDate(keyDate).addOnCompleteListener { taskNormais ->
            //Carregando documento com um array de contas
            val contasDto: ContasDto? = taskNormais.result?.toObject(ContasDto::class.java)
            //Extraindo uma lista por meio do array "all"
            val contasNormais: ArrayList<ContaModel> = contasDto?.all ?: arrayListOf()
            //Ordenando
            contasNormais.sortBy { it.dataCriacao }
            Log.d("IFGOIANO", " contasNormais1 Keys: ${ContaSingleton.contasNormais.keys}")

            //Setando a lista na key para o mês/ano correspondente
            ContaSingleton.contasNormais[keyDate] = contasNormais

            Log.d("IFGOIANO", " contasNormais2 Keys: ${ContaSingleton.contasNormais.keys}")

            //Avisando que o processo foi concluído
            myCallBack.invoke()
        }
    }

    fun logoutUser(){
        authController.logout()
    }

}