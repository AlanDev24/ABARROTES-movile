package gonzalez.alan.peliculas

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.database.FirebaseDatabase

class RegistroActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var etNombre: EditText
    private lateinit var etGenero: EditText
    private lateinit var etEdad: EditText
    private lateinit var etTelefono: EditText
    private lateinit var etCorreo: EditText
    private lateinit var etContraseña: EditText
    private lateinit var etContraseña2: EditText
    private lateinit var btnRegistrar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        auth = FirebaseAuth.getInstance()

        etNombre = findViewById(R.id.etNombre)
        etGenero = findViewById(R.id.etGenero)
        etEdad = findViewById(R.id.etEdad)
        etTelefono = findViewById(R.id.etTelefono)
        etCorreo = findViewById(R.id.etCorreo)
        etContraseña = findViewById(R.id.etContraseña)
        etContraseña2 = findViewById(R.id.etContraseña2)
        btnRegistrar = findViewById(R.id.btnRegistrar)

        btnRegistrar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val genero = etGenero.text.toString().trim()
            val edad = etEdad.text.toString().trim()
            val telefono = etTelefono.text.toString().trim()
            val correo = etCorreo.text.toString().trim()
            val contraseña1 = etContraseña.text.toString().trim()
            val contraseña2 = etContraseña2.text.toString().trim()

            if (nombre.isEmpty() || genero.isEmpty() || edad.isEmpty() || telefono.isEmpty() ||
                correo.isEmpty() || contraseña1.isEmpty() || contraseña2.isEmpty()) {
                Toast.makeText(this, "Todos los campos son requeridos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (contraseña1 != contraseña2) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(correo, contraseña1)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        val userId = user?.uid ?: ""
                        guardarUsuarioEnBaseDeDatos(userId, nombre, genero, edad, telefono, correo)
                        Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        val errorCode = if (task.exception is FirebaseAuthException) {
                            (task.exception as FirebaseAuthException).errorCode
                        } else {
                            ""
                        }
                        handleAuthError(errorCode)
                    }
                }
        }
    }

    private fun guardarUsuarioEnBaseDeDatos(
        userId: String,
        nombre: String,
        genero: String,
        edad: String,
        telefono: String,
        correo: String
    ) {
        val database = FirebaseDatabase.getInstance().reference
        val userData = hashMapOf(
            "nombre" to nombre,
            "genero" to genero,
            "edad" to edad,
            "telefono" to telefono,
            "correo" to correo
        )
        database.child("usuarios").child(userId).setValue(userData)
    }

    private fun handleAuthError(errorCode: String) {
        when (errorCode) {
            "ERROR_INVALID_EMAIL" -> Toast.makeText(this, "El formato del correo electrónico es inválido", Toast.LENGTH_SHORT).show()
            "ERROR_WEAK_PASSWORD" -> Toast.makeText(this, "La contraseña es demasiado débil", Toast.LENGTH_SHORT).show()
            else -> Toast.makeText(this, "Error de autenticación: $errorCode", Toast.LENGTH_SHORT).show()
        }
    }
}