package com.souzavaltenis.payintime.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.souzavaltenis.payintime.R
import com.souzavaltenis.payintime.controller.AuthController

class LoginActivity : AppCompatActivity() {

    private lateinit var authController: AuthController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        authController = AuthController()
    }

    fun login(view: View) {

        val etEmail: TextView = findViewById(R.id.etEmail)
        val etSenha: TextView = findViewById(R.id.etSenha)

        val email: String = etEmail.text.toString()
        val password: String = etSenha.text.toString()

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