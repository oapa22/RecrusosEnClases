package com.example.taller

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MenuPrincipal : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(R.style.Theme_Taller)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        navegar()
        navegar2()
        navegar3()
        navegar4()
        navegar5()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun navegar(){
        val btn1 = findViewById<Button>(R.id.btn1)
        btn1.setOnClickListener(){
            val saltarVentana = Intent(this,Formulario::class.java)
            startActivity(saltarVentana)
        }
    }
    fun navegar2(){
        val btn1 = findViewById<Button>(R.id.btn2)
        btn1.setOnClickListener(){
            val saltarVentana = Intent(this,Imagenes::class.java)
            startActivity(saltarVentana)
        }
    }
    fun navegar3(){
        val btn1 = findViewById<Button>(R.id.btn3)
        btn1.setOnClickListener(){
            val saltarVentana = Intent(this,Localizacion::class.java)
            startActivity(saltarVentana)
        }
    }

    fun navegar4(){
        val btn1 = findViewById<Button>(R.id.btn4)
        btn1.setOnClickListener(){
            val saltarVentana = Intent(this,Camara::class.java)
            startActivity(saltarVentana)
        }
    }

    fun navegar5(){
        val btn1 = findViewById<Button>(R.id.btn5)
        btn1.setOnClickListener(){
            val saltarVentana = Intent(this,Podometro::class.java)
            startActivity(saltarVentana)
        }
    }
}