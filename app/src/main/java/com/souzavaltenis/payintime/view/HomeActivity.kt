package com.souzavaltenis.payintime.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.souzavaltenis.payintime.R
import com.souzavaltenis.payintime.controller.HomeController
import com.souzavaltenis.payintime.singleton.UsuarioSingleton
import com.souzavaltenis.payintime.util.DateUtil
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
class HomeActivity : AppCompatActivity() {

    private lateinit var homeController: HomeController
    private lateinit var date: LocalDate
    private lateinit var tvMes: TextView
    private lateinit var tvAno: TextView

//    private lateinit var contas: List<ContaModel>
//    private lateinit var contasFixas: List<ContaModel>

    private val firestore: FirebaseFirestore = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        homeController = HomeController()

        homeController.initUsuario().addOnCompleteListener {
            loadMenu()
            showAndUpdateDateContent()
            setOnCliksPlusAndMinus()
        }

        date = LocalDate.now()
        tvMes = findViewById(R.id.tvMes)
        tvAno = findViewById(R.id.tvAno)
    }

    //função chamada quando atualiza
    fun updateContent() {

        val keyDate: String = DateUtil.getKeyFromDate(date)
        Log.d("getKeyFromDate", "Key: $keyDate")

//        val items = HashMap<String, Any>()
//        items["all"] = listOf(ContaModel(),ContaModel(),ContaModel(),ContaModel(),ContaModel())
//
//        firestore.document("/users/${usuarioModel.email}/contas/$keyDate").set(items)
//
//        firestore.document("/users/${usuarioModel.email}/contas/$keyDate").get().addOnCompleteListener { task ->
//            val items: ContasModel = task.result?.toObject(ContasModel::class.java)!!
//
//            Log.d("getKeyFromDate", "items: $items")
//        }

        //Alguma logica...
    }

    fun showDate() {
        tvMes.text = DateUtil.getNameMonthUpper(date)
        tvAno.text = date.year.toString()
    }

    fun showAndUpdateDateContent() {
        showDate()
        updateContent()
    }

    fun setOnCliksPlusAndMinus() {

        val btMais: Button = findViewById(R.id.btMais)
        val btMenos: Button = findViewById(R.id.btMenos)

        btMais.setOnClickListener {

            val newDatePlusOneMonth: LocalDate = date.plusMonths(1)

            if(!DateUtil.dateIsGreaterOrLessThanOneYear(newDatePlusOneMonth)){
                date = newDatePlusOneMonth
                showAndUpdateDateContent()
            }else{
                Toast.makeText(this, "O limite máximo é de um ano.", Toast.LENGTH_SHORT).show()
            }

        }

        btMenos.setOnClickListener {

            val newDateMinusOneMonth: LocalDate = date.minusMonths(1)

            if(!DateUtil.dateIsGreaterOrLessThanOneYear(newDateMinusOneMonth)){
                date = newDateMinusOneMonth
                showAndUpdateDateContent()
            }else{
                Toast.makeText(this, "O limite máximo é de um ano.", Toast.LENGTH_SHORT).show()
            }

        }
    }

    fun optionsMenu(menuItem: MenuItem){

        when (menuItem.itemId) {
            R.id.opSalario -> {
                startActivity(Intent(this, SalarioActivity::class.java))
            }
            R.id.opContasFixas -> {
                Toast.makeText(this, "opContasFixas", Toast.LENGTH_SHORT).show()
            }
            R.id.opEconomia -> {
                Toast.makeText(this, "opEconomia", Toast.LENGTH_SHORT).show()
            }
        }

    }

    @SuppressLint("SetTextI18n")
    fun loadMenu() {

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val actionBarToggle = ActionBarDrawerToggle(this, drawerLayout, 0, 0)

        drawerLayout.addDrawerListener(actionBarToggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        actionBarToggle.syncState()

        val navView: NavigationView = findViewById(R.id.nav_view)

        navView.setNavigationItemSelectedListener { menuItem ->
            optionsMenu(menuItem)
            true
        }

        val btFirstCharUser: Button = findViewById(R.id.btFirstCharUser)
        val tvNomeHeader: TextView = findViewById(R.id.tvNomeHeader)

        val nameSplited: List<String> = UsuarioSingleton.usuario.nome.split(" ")
        val firstName: String = if(nameSplited.isEmpty()) UsuarioSingleton.usuario.nome else nameSplited[0]

        btFirstCharUser.text = firstName[0] + ""
        tvNomeHeader.text = firstName

        val btMenuHamburger: Button = findViewById(R.id.btMenuHamburger)

        btMenuHamburger.setOnClickListener {
            drawerLayout.openDrawer(navView)
        }
    }


}