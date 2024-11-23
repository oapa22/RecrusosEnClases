package com.example.taller

// Importaciones necesarias para funcionalidades como permisos, cámara, archivos, y más.
import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.taller.databinding.ActivityCamaraBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class Camara : AppCompatActivity() {
    private var imageCapture: ImageCapture? = null // Objeto para capturar imágenes.
    private lateinit var outputDirectory: File // Directorio donde se guardarán las fotos.
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var binding: ActivityCamaraBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configuración de View Binding para acceder a los elementos de la interfaz.
        binding = ActivityCamaraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        // Verificar si se tienen los permisos necesarios para usar la cámara.
        if (allPermissionsGranted()) {
            // Si los permisos están concedidos, inicia la cámara.
            startCamera()
        } else {
            // Si no, solicita los permisos necesarios al usuario.
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        // Configuración del botón de captura para tomar fotos al hacer clic.
        binding.cameraCaptureButton.setOnClickListener {
            takePhoto()
        }

        // Configuración del directorio de salida para guardar fotos.
        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    // Método para tomar una foto y guardarla en el dispositivo.
    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        // Crear un archivo con un nombre único basado en la fecha y hora actual.
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis()) + ".jpg"
        )

        // Configurar las opciones de salida para guardar la foto en el archivo.
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Capturar la imagen y manejar los resultados.
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    binding.ivCapture.visibility = View.VISIBLE
                    binding.ivCapture.setImageURI(savedUri)

                    // Mostrar un mensaje de éxito al usuario.
                    val msg = "Photo capture succeeded: $savedUri"
                    Toast.makeText(baseContext, msg, Toast.LENGTH_LONG).show()
                    Log.d(TAG, msg)
                }
            }
        )
    }

    // Método para iniciar la cámara.
    private fun startCamera() {
        // Obtener un proveedor de cámaras.
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        // Configurar la cámara una vez que esté lista.
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            // Configurar la captura de imágenes.
            imageCapture = ImageCapture.Builder().build()

            // Seleccionar la cámara trasera.
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )
            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    // Verificar si se han concedido todos los permisos necesarios.
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    // Obtener el directorio donde se guardarán las fotos.
    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else filesDir
    }

    // Manejar la respuesta del usuario al solicitar permisos.
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT)
                    .show()
                finish()
            }
        }
    }

    // Método para limpiar recursos cuando se destruye la actividad.
    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    // Constantes utilizadas en la clase.
    companion object {
        private const val TAG = "CameraXGFG"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 20
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}
