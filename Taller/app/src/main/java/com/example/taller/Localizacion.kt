package com.example.taller

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class Localizacion : AppCompatActivity() {

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_localizacion)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        val btn_gpscalcular=findViewById<Button>(R.id.btn_4_1)
        val tv_mensaje=findViewById<TextView>(R.id.txt_4_2)
        navegar()

        btn_gpscalcular.setOnClickListener(){
            var msgLatLong="Ubicacion"
            tv_mensaje.setText(msgLatLong)
            findLocation()
        }
    }
    fun navegar(){
        val btn1 = findViewById<Button>(R.id.btn_4_2)
        btn1.setOnClickListener(){
            val saltarVentana = Intent(this,MenuPrincipal::class.java)
            startActivity(saltarVentana)
        }
    }

    private  fun findLocation(){
        val task = fusedLocationProviderClient.lastLocation
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 101)



            return
        }

        task.addOnSuccessListener {
            if (it!=null){
                val dire=getAddress(it.latitude, it.longitude)
                val tv_mensaje=findViewById<TextView>(R.id.txt_4_2)
                Toast.makeText(applicationContext, "${it.latitude} ${it.longitude}", Toast.LENGTH_SHORT).show()
                tv_mensaje.setText("${it.latitude} ${it.longitude} - ${it.altitude} - ${it.speed} - ${dire}")
            }
        }
    }

    private fun getAddress(lat: Double, lng: Double): String {
        val geocoder = Geocoder(this)
        val list = geocoder.getFromLocation(lat, lng, 1)
        return list!![0].getAddressLine(0)
    }
}