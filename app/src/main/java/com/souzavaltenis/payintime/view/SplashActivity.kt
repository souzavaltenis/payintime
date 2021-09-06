package com.souzavaltenis.payintime.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.souzavaltenis.payintime.R
import com.souzavaltenis.payintime.controller.AuthController

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({

            val authController = AuthController()
            val intent = Intent(this,
                if(authController.getUser() == null) LoginActivity::class.java
                else HomeActivity::class.java
            )

            startActivity(intent)
            finish()
            
        }, 3000)
    }
}