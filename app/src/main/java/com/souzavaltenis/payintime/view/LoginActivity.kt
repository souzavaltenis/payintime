package com.souzavaltenis.payintime.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.souzavaltenis.payintime.R
import com.souzavaltenis.payintime.controller.AuthController
import com.souzavaltenis.payintime.util.GeralUtil

class LoginActivity : AppCompatActivity() {

    private lateinit var authController: AuthController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        authController = AuthController()
    }

    fun login(view: View) {

        val etEmail: EditText = findViewById(R.id.etEmail)
        val etSenha: EditText = findViewById(R.id.etSenha)

        val email: String = etEmail.text.toString()
        if(!GeralUtil.isValidEmail(email)){
            return GeralUtil.setErrorEditText(etEmail, "Informe um email válido.")
        }

        val password: String = etSenha.text.toString()
        if(password.isEmpty() || password.length < 6){
            return GeralUtil.setErrorEditText(etSenha, "Informe uma senha com 6 ou mais dígitos.")
        }

        authController.signin(email, password).addOnCompleteListener { task: Task<AuthResult> ->

                if (task.isSuccessful) {
                    Toast.makeText(this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Não foi possível realizar o login.", Toast.LENGTH_SHORT).show()
                }

        }

    }

    fun cadastro(view: View){
        startActivity(Intent(this, CadastroActivity::class.java))
    }
}