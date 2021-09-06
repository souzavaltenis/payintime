package com.souzavaltenis.payintime.view

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.gms.tasks.Task
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.DocumentSnapshot
import com.souzavaltenis.payintime.R
import com.souzavaltenis.payintime.controller.AuthController
import com.souzavaltenis.payintime.controller.UsuarioController
import com.souzavaltenis.payintime.model.UsuarioModel
import com.souzavaltenis.payintime.util.DateUtil
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
class HomeActivity : AppCompatActivity() {

    private lateinit var usuarioController: UsuarioController
    private lateinit var usuarioModel: UsuarioModel
    private lateinit var date: LocalDate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        usuarioController = UsuarioController()
        date = LocalDate.now()

        showDate()
        loadData()
    }

    fun plusDate(view: View){

        val newDatePlusOneMonth: LocalDate = date.plusMonths(1)

        if(!DateUtil.dateIsGreaterOrLessThanOneYear(newDatePlusOneMonth)){
            date = newDatePlusOneMonth
            showDate()
        }else{
            Toast.makeText(this, "O limite máximo é de um ano.", Toast.LENGTH_SHORT).show()
        }
    }

    fun minusDate(view: View){

        val newDateMinusOneMonth: LocalDate = date.minusMonths(1)

        if(!DateUtil.dateIsGreaterOrLessThanOneYear(newDateMinusOneMonth)){
            date = newDateMinusOneMonth
            showDate()
        }else{
            Toast.makeText(this, "O limite máximo é de um ano.", Toast.LENGTH_SHORT).show()
        }

    }

    fun showDate() {

        val tvMes: TextView = findViewById(R.id.tvMes)
        val tvAno: TextView = findViewById(R.id.tvAno)

        tvMes.text = DateUtil.getNameMonthUpper(date)
        tvAno.text = date.year.toString()

        Log.d("getKeyFromDate", "Key: ${DateUtil.getKeyFromDate(date)}")
    }

    fun loadData() {

        val authController = AuthController()
        val currentEmail: String? = authController.getEmailUser()

        if(!currentEmail.isNullOrEmpty()){
            usuarioController.findByEmail(currentEmail).addOnCompleteListener { task: Task<DocumentSnapshot> ->
                if(task.isSuccessful){
                    usuarioModel = task.result?.toObject(UsuarioModel::class.java)!!
                    loadMenu()
                }else{
                    Toast.makeText(this, "Não foi possível carregar os dados do usuário.", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    @SuppressLint("SetTextI18n")
    fun loadMenu(){

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val actionBarToggle = ActionBarDrawerToggle(this, drawerLayout, 0, 0)

        drawerLayout.addDrawerListener(actionBarToggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        actionBarToggle.syncState()

        val navView: NavigationView = findViewById(R.id.nav_view)

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.teste1 -> {
                    Toast.makeText(this, "Teste1", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.teste2 -> {
                    Toast.makeText(this, "Teste2", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.teste3 -> {
                    Toast.makeText(this, "Teste3", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> {
                    false
                }
            }
        }

        val btFirstCharUser: Button = findViewById(R.id.btFirstCharUser)
        val tvNomeHeader: TextView = findViewById(R.id.tvNomeHeader)

        val nameSplited: List<String> = usuarioModel.nome.split(" ")
        val firstName: String = if(nameSplited.isEmpty()) usuarioModel.nome else nameSplited[0]

        btFirstCharUser.text = firstName[0] + ""
        tvNomeHeader.text = firstName

        val ivMenu: ImageView = findViewById(R.id.ivMenu)

        ivMenu.setOnClickListener {
            drawerLayout.openDrawer(navView)
        }
    }


}