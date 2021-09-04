package com.souzavaltenis.payintime.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.souzavaltenis.payintime.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun login(view: View) {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    fun cadastro(view: View){
        startActivity(Intent(this, CadastroActivity::class.java))
    }
}