package com.example.taller

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Podometro : AppCompatActivity(), SensorEventListener {

    private var sensorManager: SensorManager? = null
    private var running = false
    private var totalSteps = 0f
    private var previousTotalSteps = 0f
    private var startBool = false

    private lateinit var tvStepsTaken: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_podometro)

        // Inicializar vistas
        tvStepsTaken = findViewById(R.id.btn_5_1)

        loadData()
        resetSteps()
        navegar()
        botones()

        // Configuración del SensorManager
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // Solicitar permiso si es necesario (Android 10 o superior)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACTIVITY_RECOGNITION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.ACTIVITY_RECOGNITION),
                    101
                )
            }
        }

        // Manejo de insets para diseño
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onResume() {
        super.onResume()
        running = true

        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (stepSensor == null) {
            Toast.makeText(this, "No sensor detected on this device", Toast.LENGTH_SHORT).show()
        } else {
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        // Desregistrar el listener para evitar el uso innecesario del sensor
        sensorManager?.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.values != null && event.values.isNotEmpty() && startBool) {
            Log.d("Podometro", "onSensorChanged: Sensor value received = ${event.values[0]}")
            if (running) {
                totalSteps = event.values[0]
                val currentSteps = totalSteps.toInt() - previousTotalSteps.toInt()

                // Actualizar la interfaz de usuario
                runOnUiThread {
                    tvStepsTaken.text = currentSteps.toString()
                }

                Log.d("Podometro", "Current steps: $currentSteps")
            }
        }
    }

    fun resetSteps() {
        tvStepsTaken.setOnClickListener {
            Toast.makeText(this, "Long tap to reset steps", Toast.LENGTH_SHORT).show()
        }

        tvStepsTaken.setOnLongClickListener {
            previousTotalSteps = totalSteps
            tvStepsTaken.text = "0"
            saveData()
            true
        }
    }

    private fun saveData() {
        val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putFloat("key1", previousTotalSteps)
        editor.apply()
        Log.d("Podometro", "Data saved: $previousTotalSteps")
    }

    private fun loadData() {
        val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val savedNumber = sharedPreferences.getFloat("key1", 0f)
        previousTotalSteps = savedNumber
        Log.d("Podometro", "Data loaded: $previousTotalSteps")
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Este método no es necesario para el podómetro
    }

    fun botones(){
        val btnStart = findViewById<Button>(R.id.btn_5_2)
        val btnStop = findViewById<Button>(R.id.btn_5_3)
        btnStart.setOnClickListener(){
            startBool = true
            Toast.makeText(this, "Está funcionando", Toast.LENGTH_SHORT).show()
        }
        btnStop.setOnClickListener(){
            startBool = false
            Toast.makeText(this, "Está detenido", Toast.LENGTH_SHORT).show()
        }
    }

    fun navegar(){
        val btn1 = findViewById<Button>(R.id.btn_5_4)
        btn1.setOnClickListener(){
            val saltarVentana = Intent(this,MenuPrincipal::class.java)
            startActivity(saltarVentana)
        }
    }
}