package com.souzavaltenis.payintime.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.souzavaltenis.payintime.R
import com.souzavaltenis.payintime.controller.AuthController
import com.souzavaltenis.payintime.controller.UsuarioController
import com.souzavaltenis.payintime.model.UsuarioModel
import com.souzavaltenis.payintime.util.EditTextMask
import com.souzavaltenis.payintime.util.GeralUtil
import java.util.*

class CadastroActivity : AppCompatActivity() {

    private lateinit var authController: AuthController
    private lateinit var usuarioController: UsuarioController

    private lateinit var etNomeSignup: EditText
    private lateinit var etEmailSingup: EditText
    private lateinit var etSenhaSignup: EditText
    private lateinit var etSalarioSignup: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        authController = AuthController()
        usuarioController = UsuarioController()

        etNomeSignup = findViewById(R.id.etNomeSignup)
        etEmailSingup = findViewById(R.id.etEmailSingup)
        etSenhaSignup = findViewById(R.id.etSenhaSignup)
        etSalarioSignup = findViewById(R.id.etSalarioSignup)
        etSalarioSignup.addTextChangedListener(EditTextMask.insertCurrency(etSalarioSignup))
    }

    fun cadastrar(view: View){

        val name: String = etNomeSignup.text.toString()
        if(name.isEmpty()){
            return GeralUtil.setErrorEditText(etNomeSignup, "Informe um nome.")
        }

        val email: String = etEmailSingup.text.toString()
        if(!GeralUtil.isValidEmail(email)){
            return GeralUtil.setErrorEditText(etEmailSingup, "Informe um email válido.")
        }

        val password: String = etSenhaSignup.text.toString()
        if(password.isEmpty() || password.length < 6){
            return GeralUtil.setErrorEditText(etSenhaSignup, "Informe uma senha com 6 ou mais dígitos.")
        }

        val textSalario: String = etSalarioSignup.text.toString()
        if(textSalario.isEmpty()){
            return GeralUtil.setErrorEditText(etSalarioSignup, "Informe um salário.")
        }

        val salario: Double = EditTextMask.unMaskBRL(textSalario).toDouble()

        authController.signup(email, password).addOnCompleteListener { task: Task<AuthResult> ->

            if(task.isSuccessful){

                val usuario = UsuarioModel(name, email, salario, Date())

                usuarioController.save(usuario).addOnCompleteListener {
                    Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
                    authController.logout()
                    finish()
                }

            }else{
                Toast.makeText(this, "Não foi possível realizar o cadastro.", Toast.LENGTH_SHORT).show()
            }
        }

    }
}