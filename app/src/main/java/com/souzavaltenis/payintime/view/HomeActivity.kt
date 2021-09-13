package com.souzavaltenis.payintime.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.souzavaltenis.payintime.R
import com.souzavaltenis.payintime.controller.HomeController
import com.souzavaltenis.payintime.model.ContaModel
import com.souzavaltenis.payintime.model.enums.StatusConta
import com.souzavaltenis.payintime.singleton.ContaSingleton
import com.souzavaltenis.payintime.singleton.UsuarioSingleton
import com.souzavaltenis.payintime.util.interfaces.CallbackFragment
import com.souzavaltenis.payintime.util.DateUtil
import com.souzavaltenis.payintime.util.adapaters.TabPageAdapter
import com.souzavaltenis.payintime.util.interfaces.CallbackMenuConta
import com.souzavaltenis.payintime.view.fragments.ContaOptionsFragment
import java.time.LocalDate

class HomeActivity : AppCompatActivity() {

    private lateinit var homeController: HomeController
    private lateinit var tvMes: TextView
    private lateinit var tvAno: TextView

    private lateinit var btMenos: Button
    private lateinit var btMais: Button

    private lateinit var tvInfoContasPendentes: TextView
    private lateinit var tvInfoContasPagas: TextView
    private lateinit var tvInfoContasVencidas: TextView

    private lateinit var ivAddConta: ImageView
    private lateinit var tabLayout: TabLayout

    //Remover daqui
    private var callbacksFragmentsMap: HashMap<Int, CallbackFragment> = hashMapOf()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

//        val ivCalendar: ImageView = findViewById(R.id.ivCalendar)
//        ivCalendar.setOnClickListener {
//
//        }

        homeController = HomeController()

        tvMes = findViewById(R.id.tvMes)
        tvAno = findViewById(R.id.tvAno)

        btMenos = findViewById(R.id.btMenos)
        btMais = findViewById(R.id.btMais)

        tvInfoContasPendentes = findViewById(R.id.tvInfoContasPendentes)
        tvInfoContasPagas = findViewById(R.id.tvInfoContasPagas)
        tvInfoContasVencidas = findViewById(R.id.tvInfoContasVencidas)

        ivAddConta = findViewById(R.id.ivAddConta)
        tabLayout = findViewById(R.id.tabLayout)

        enableViews(false)
        homeController.initUsuario{
            initSetup()
            enableViews(true)
        }
    }

    fun initSetup(){
        initTabBar()
        loadMenu()
        updateContent()
        setOnCliksPlusAndMinus()
        addClickCreateConta()
    }

    fun updateContaNormal(): CallbackMenuConta {
        return object : CallbackMenuConta {
            override fun notify(idConta: String) {
                val contaOptionsFragment = ContaOptionsFragment(idConta)
                contaOptionsFragment.show(supportFragmentManager, "conta_options_fragment")
            }
        }
    }

    fun initTabBar(){

        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        val tabLayout: TabLayout = findViewById(R.id.tabLayout)

        val adapter = TabPageAdapter(this, tabLayout.tabCount, callbacksFragmentsMap, updateContaNormal())

        viewPager.adapter = adapter

        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })

        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{

            override fun onTabSelected(tab: TabLayout.Tab){
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

    }

    fun addClickCreateConta(){
        ivAddConta.setOnClickListener{
            startActivityForResult(
                Intent(this, CriarContaActivity::class.java),
                CriarContaActivity.RESULT_OK_CONTA_FIXA
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(resultCode){
            CriarContaActivity.RESULT_OK_CONTA_NORMAL -> {
                Toast.makeText(this,"Conta Adicionada com Sucesso!", Toast.LENGTH_SHORT).show()
            }
            SalarioActivity.RESULT_OK_SALARIO -> {
                Toast.makeText(this, "Salário atualizado com sucesso!", Toast.LENGTH_SHORT).show()
            }
        }

        changeDate()
    }

    fun showPopup(view: View, position: Int) {
        val popup = PopupMenu(this, view)
        popup.inflate(R.menu.submenu_items)

        popup.setOnMenuItemClickListener { item: MenuItem? ->

            when (item!!.itemId) {
                R.id.header1 -> {
                    Toast.makeText(this, "${item.title} posi: $position", Toast.LENGTH_SHORT).show()
                }
                R.id.header2 -> {
                    Toast.makeText(this, "${item.title} posi: $position", Toast.LENGTH_SHORT).show()
                }
                R.id.header3 -> {
                    Toast.makeText(this, "${item.title} posi: $position", Toast.LENGTH_SHORT).show()
                }
            }

            true
        }

        popup.show()
    }

    fun updateContent() {

        val dataSelecionada: LocalDate = UsuarioSingleton.dataSelecionada

        tvMes.text = DateUtil.getNameMonthUpper(dataSelecionada)
        tvAno.text = dataSelecionada.year.toString()

        if(!ContaSingleton.contasNormais.containsKey(UsuarioSingleton.keyDate())){
            enableViews(false)
            val btMenos: Button = findViewById(R.id.btMenos)
            btMenos.isEnabled = false
            homeController.loadContasNormais{
                changeDate()
                enableViews(true)
            }
        }else{
            changeDate()
        }

    }

    fun enableViews(value: Boolean){
        tabLayout.isEnabled = value
        btMenos.isEnabled = value
        btMais.isEnabled = value
        ivAddConta.isEnabled = value
    }

    fun updateContentFragments(position: Int = -1){
        Log.d("fragment", "callbacksFragmentsMap KEYS: ${callbacksFragmentsMap.keys}")
        //Notifica um fragmento específico
        if(position != -1){
            callbacksFragmentsMap[position]?.notifyUpdate()
        }else{ //Notifica todos fragmentos
            callbacksFragmentsMap.forEach{ c -> c.value.notifyUpdate() }
        }
    }

    fun changeDate() {

        val keyDate: String = UsuarioSingleton.keyDate()

        homeController.setStatusContasFixas()
        updateContentFragments()

        val contasNormais: ArrayList<ContaModel> = ContaSingleton.contasNormais[keyDate]!!

        val (contasNormaisPagas, contasNormaisNaoPagas) = contasNormais
            .partition { it.status == StatusConta.PAGA }

        val (contasFixasPagas, contasFixasNaoPagas) = ContaSingleton.contasFixas
            .partition { it.pagamentos[keyDate] == StatusConta.PAGA }

        val (contasNormaisPendentes, contasNormaisVencidas) = contasNormaisNaoPagas
            .partition { it.status == StatusConta.PENDENTE }

        val (contasFixasPendentes, contasFixasVencidas) = contasFixasNaoPagas
            .partition { it.pagamentos[keyDate] == StatusConta.PENDENTE }

        val quantidadeContasPendentes: Int = contasNormaisPendentes.size + contasFixasPendentes.size
        val quantidadeContasPagas: Int = contasNormaisPagas.size + contasFixasPagas.size
        val quantidadeContasVencidas: Int = contasNormaisVencidas.size + contasFixasVencidas.size

        tvInfoContasPendentes.text = quantidadeContasPendentes.toString()
        tvInfoContasPagas.text = quantidadeContasPagas.toString()
        tvInfoContasVencidas.text = quantidadeContasVencidas.toString()
    }

    fun setOnCliksPlusAndMinus() {

        val btMais: Button = findViewById(R.id.btMais)
        val btMenos: Button = findViewById(R.id.btMenos)

        btMais.setOnClickListener {

            val newDatePlusOneMonth: LocalDate = UsuarioSingleton.dataSelecionada.plusMonths(1)

            if(!DateUtil.dateIsGreaterOrLessThanOneYear(newDatePlusOneMonth)){
                UsuarioSingleton.dataSelecionada = newDatePlusOneMonth
                updateContent()
            }else{
                Toast.makeText(this, "O limite máximo é de um ano.", Toast.LENGTH_SHORT).show()
            }

        }

        btMenos.setOnClickListener {

            val newDateMinusOneMonth: LocalDate = UsuarioSingleton.dataSelecionada.minusMonths(1)

            if(!DateUtil.dateIsGreaterOrLessThanOneYear(newDateMinusOneMonth)){
                UsuarioSingleton.dataSelecionada = newDateMinusOneMonth
                updateContent()
            }else{
                Toast.makeText(this, "O limite máximo é de um ano.", Toast.LENGTH_SHORT).show()
            }

        }
    }

    fun optionsMenu(menuItem: MenuItem){

        when (menuItem.itemId) {
            R.id.opSalario -> {
                startActivityForResult(Intent(this, SalarioActivity::class.java),
                    SalarioActivity.RESULT_OK_SALARIO)
            }
            R.id.opContasFixas -> {
                startActivityForResult(Intent(this, ContasFixasActivity::class.java),
                    ContasFixasActivity.RESULT_OK_CONTAS_FIXAS)
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
        val tvEmailHeader: TextView = findViewById(R.id.tvEmailHeader)

        val nameSplited: List<String> = UsuarioSingleton.usuario.nome.split(" ")
        val firstName: String = if(nameSplited.isEmpty()) UsuarioSingleton.usuario.nome else nameSplited[0]

        btFirstCharUser.text = firstName[0] + ""
        tvNomeHeader.text = firstName
        tvEmailHeader.text = UsuarioSingleton.usuario.email

        val btMenuHamburger: Button = findViewById(R.id.btMenuHamburger)

        btMenuHamburger.setOnClickListener {
            drawerLayout.openDrawer(navView)
        }
    }

    fun deslogar(view: View){
        homeController.logoutUser()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

}