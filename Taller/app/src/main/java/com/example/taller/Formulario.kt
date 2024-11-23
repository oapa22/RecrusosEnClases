package com.example.taller

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Formulario : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_formulario)
        navegar()
        layoutForm()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun navegar(){
        val btn1 = findViewById<Button>(R.id.btn_2_3)
        btn1.setOnClickListener(){
            val saltarVentana = Intent(this,MenuPrincipal::class.java)
            startActivity(saltarVentana)
        }
    }

    fun layoutForm(){
        val btnguardar = findViewById<Button>(R.id.btn_2_2)
        val tvnombre = findViewById<TextView>(R.id.input1)
        val tvapellido = findViewById<TextView>(R.id.input2)
        btnguardar.setOnClickListener(){
            val message = "Nombre: ${tvnombre.text}, Es estudiante"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

}