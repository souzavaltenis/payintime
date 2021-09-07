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

        val etNomeSignup: EditText = findViewById(R.id.etNomeSignup)
        val etEmailSingup: EditText = findViewById(R.id.etEmailSingup)
        val etSenhaSignup: EditText = findViewById(R.id.etSenhaSignup)
        val etSalarioSignup: EditText = findViewById(R.id.etSalarioSignup)

        val name: String = etNomeSignup.text.toString()
        val email: String = etEmailSingup.text.toString()
        val password: String = etSenhaSignup.text.toString()
        val salario: Double = etSalarioSignup.text.toString().toDouble()

        authController.signup(email, password).addOnCompleteListener { task: Task<AuthResult> ->

            if(task.isSuccessful){

                val usuario = UsuarioModel(name, email, salario)

                usuarioController.save(usuario).addOnCompleteListener {
                    Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
                    onBackPressed()
                }

            }else{
                Toast.makeText(this, "Não foi possível realizar o cadastro.", Toast.LENGTH_SHORT).show()
            }
        }

    }
}