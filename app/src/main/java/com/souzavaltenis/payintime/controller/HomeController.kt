package com.souzavaltenis.payintime.controller

import com.google.firebase.firestore.DocumentSnapshot
import com.souzavaltenis.payintime.model.ContaFixaModel
import com.souzavaltenis.payintime.model.UsuarioModel
import com.souzavaltenis.payintime.model.dtos.ContasDto
import com.souzavaltenis.payintime.singleton.ContaSingleton
import com.souzavaltenis.payintime.singleton.UsuarioSingleton
import com.souzavaltenis.payintime.util.DateUtil

class HomeController {

    private var usuarioController = UsuarioController()

    fun initUsuario(myCallBack: () -> Unit) {

        val authController = AuthController()
        val currentEmail: String? = authController.getEmailUser()
        val contaFixaController = ContaFixaController(currentEmail!!)
        val contaController = ContaController(currentEmail)

        usuarioController.findByEmail(currentEmail).addOnSuccessListener { doc: DocumentSnapshot? ->
            //Usuario Carregado
            UsuarioSingleton.usuario = doc?.toObject(UsuarioModel::class.java)!!
            contaFixaController.findAll().addOnCompleteListener { taskFixas ->
                //Contas Fixas Carregadas
                ContaSingleton.contasFixas = taskFixas.result?.toObjects(ContaFixaModel::class.java) as ArrayList<ContaFixaModel>
                val keyDate: String = DateUtil.getKeyFromDate()
                contaController.findAllByKeyDate(keyDate).addOnCompleteListener { taskNormais ->
                    //Contas Normais do mês atual Carregadas
                    val contasDto: ContasDto? = taskNormais.result?.toObject(ContasDto::class.java)
                    if (contasDto != null) {
                        ContaSingleton.contasNormais[keyDate] = contasDto.all
                    }
                    myCallBack.invoke()
                }
            }
        }
    }

}