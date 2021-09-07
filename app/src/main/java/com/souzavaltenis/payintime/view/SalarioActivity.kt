package com.souzavaltenis.payintime.view

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.souzavaltenis.payintime.R
import com.souzavaltenis.payintime.controller.SalarioController
import com.souzavaltenis.payintime.singleton.UsuarioSingleton
import com.souzavaltenis.payintime.util.EditTextMask
import com.souzavaltenis.payintime.util.GeralUtil

class SalarioActivity : AppCompatActivity() {

    private lateinit var etSalarioUpdate: EditText
    private lateinit var salarioController: SalarioController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_salario)

        val btBack: Button = findViewById(R.id.btBack)
        btBack.setOnClickListener{onBackPressed()}

        salarioController = SalarioController()

        etSalarioUpdate = findViewById(R.id.etSalarioUpdate)
        etSalarioUpdate.addTextChangedListener(EditTextMask.insertCurrency(etSalarioUpdate))
        etSalarioUpdate.setText(GeralUtil.formatarValor(UsuarioSingleton.usuario.salario))
    }

    fun atualizarSalario(view: View){
        val novoSalario: Double = EditTextMask.unMaskBRL(etSalarioUpdate.text.toString()).toDouble()
        salarioController.atualizarSalario(novoSalario).addOnCompleteListener {
            Toast.makeText(this, "Salário atualizado com sucesso!", Toast.LENGTH_SHORT).show()
            onBackPressed()
        }
    }

}