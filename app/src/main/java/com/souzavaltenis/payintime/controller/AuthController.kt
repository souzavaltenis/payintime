package com.souzavaltenis.payintime.controller

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AuthController {

    private var firebaseAuth: FirebaseAuth = Firebase.auth

    fun signup(email: String, password: String): Task<AuthResult> {
        return firebaseAuth.createUserWithEmailAndPassword(email, password)
    }

    fun signin(email: String, password: String): Task<AuthResult>{
        return firebaseAuth.signInWithEmailAndPassword(email, password)
    }

    fun getUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    fun getEmailUser(): String? {
        val user: FirebaseUser? = getUser()
        if (user != null) {
            return user.email
        }
        return null
    }
}