package com.souzavaltenis.payintime.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.souzavaltenis.payintime.R
import com.souzavaltenis.payintime.controller.AuthController
import com.souzavaltenis.payintime.controller.UsuarioController
import com.souzavaltenis.payintime.model.UsuarioModel

class CadastroActivity : AppCompatActivity() {

    private lateinit var authController: AuthController
    private lateinit var usuarioController: UsuarioController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        authController = AuthController()
        usuarioController = UsuarioController()
    }

    fun cadastrar(view: View){

        val etNomeSignup: TextView = findViewById(R.id.etNomeSignup)
        val etEmailSingup: TextView = findViewById(R.id.etEmailSingup)
        val etSenhaSignup: TextView = findViewById(R.id.etSenhaSignup)

        val name: String = etNomeSignup.text.toString()
        val email: String = etEmailSingup.text.toString()
        val password: String = etSenhaSignup.text.toString()

        authController.signup(email, password).addOnCompleteListener { task: Task<AuthResult> ->
            if(task.isSuccessful){
                usuarioController.save(UsuarioModel(name, email)).addOnCompleteListener { task: Task<Void> ->
                    Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
                    onBackPressed()
                }
            }else{
                Toast.makeText(this, "Não foi possível realizar o cadastro.", Toast.LENGTH_SHORT).show()
            }
        }

        var usuario: UsuarioModel = UsuarioModel(name, email)
    }
}