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
import com.souzavaltenis.payintime.model.enums.StatusConta
import com.souzavaltenis.payintime.singleton.ContaSingleton
import com.souzavaltenis.payintime.singleton.UsuarioSingleton
import com.souzavaltenis.payintime.util.DateUtil
import java.time.LocalDate
import java.util.function.Consumer

@RequiresApi(Build.VERSION_CODES.O)
class HomeActivity : AppCompatActivity() {

    private lateinit var homeController: HomeController
    private lateinit var tvMes: TextView
    private lateinit var tvAno: TextView

    private lateinit var tvInfoConta: TextView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        homeController = HomeController()
        homeController.initUsuario{initSetup()}

        tvMes = findViewById(R.id.tvMes)
        tvAno = findViewById(R.id.tvAno)

        tvInfoConta = findViewById(R.id.tvInfoConta)

        val tvTipoConta: TextView = findViewById(R.id.tvTipoConta)
        tvTipoConta.text = "Contas"
    }

    fun initSetup(){
        loadMenu()
        showAndUpdateDateContent()
        setOnCliksPlusAndMinus()
        addClickCreateConta()
    }

    fun addClickCreateConta(){
        val ivAddConta: ImageView = findViewById(R.id.ivAddConta)
        ivAddConta.setOnClickListener{
            startActivityForResult(
                Intent(this, CriarContaActivity::class.java),
                CriarContaActivity.RESULT_OK_CONTA_FIXA
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == CriarContaActivity.RESULT_OK_CONTA_NORMAL){
            Toast.makeText(this,"Conta Adicionada com Sucesso!", Toast.LENGTH_SHORT).show()

        }
    }


    //função chamada quando atualiza
    fun updateContent() {

        val keyDate: String = DateUtil.getKeyFromDate()

        Log.d("teste", "|||| Info For $keyDate -> ${ContaSingleton.contasNormais[keyDate]}")
    }

    fun showDate() {
        tvMes.text = DateUtil.getNameMonthUpper(UsuarioSingleton.dataSelecionada)
        tvAno.text = UsuarioSingleton.dataSelecionada.year.toString()
    }

    fun showAndUpdateDateContent() {
        showDate()
        updateContent()
    }

    fun setOnCliksPlusAndMinus() {

        val btMais: Button = findViewById(R.id.btMais)
        val btMenos: Button = findViewById(R.id.btMenos)

        btMais.setOnClickListener {

            val newDatePlusOneMonth: LocalDate = UsuarioSingleton.dataSelecionada.plusMonths(1)

            if(!DateUtil.dateIsGreaterOrLessThanOneYear(newDatePlusOneMonth)){
                UsuarioSingleton.dataSelecionada = newDatePlusOneMonth
                showAndUpdateDateContent()
            }else{
                Toast.makeText(this, "O limite máximo é de um ano.", Toast.LENGTH_SHORT).show()
            }

        }

        btMenos.setOnClickListener {

            val newDateMinusOneMonth: LocalDate = UsuarioSingleton.dataSelecionada.minusMonths(1)

            if(!DateUtil.dateIsGreaterOrLessThanOneYear(newDateMinusOneMonth)){
                UsuarioSingleton.dataSelecionada = newDateMinusOneMonth
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
                startActivity(Intent(this, ContasFixasActivity::class.java))
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


//    fun atualizarInformacoesContas() {
//
//        val quantidadeContasPagas: Int = ContaSingleton.contasNormais
//            .stream()
//            .filter{ conta -> conta.status == StatusConta.PAGA }
//            .count()
//            .toInt()
//
//        //tvInfoConta
//
//        Log.d("teste", "quantidadeContasPagas $quantidadeContasPagas")
//    }
}