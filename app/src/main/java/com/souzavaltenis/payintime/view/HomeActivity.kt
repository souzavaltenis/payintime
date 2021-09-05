package com.souzavaltenis.payintime.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.souzavaltenis.payintime.R
import com.souzavaltenis.payintime.controller.AuthController
import com.souzavaltenis.payintime.controller.UsuarioController
import com.souzavaltenis.payintime.model.UsuarioModel

class HomeActivity : AppCompatActivity() {

    private lateinit var usuarioController: UsuarioController
    private lateinit var usuarioModel: UsuarioModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        usuarioController = UsuarioController()
        loadData()
    }

    fun loadData() {

        val authController = AuthController()
        val currentEmail: String? = authController.getEmailUser()

        if(!currentEmail.isNullOrEmpty()){
            usuarioController.findByEmail(currentEmail).addOnCompleteListener { task: Task<DocumentSnapshot> ->
                if(task.isSuccessful){
                    usuarioModel = task.result?.toObject(UsuarioModel::class.java)!!
                    showData()
                }else{
                    Toast.makeText(this, "Não foi possível carregar os dados do usuário.", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    fun showData(){
        val textView2: TextView = findViewById(R.id.textView2)
        textView2.text = usuarioModel.toString()
    }
}