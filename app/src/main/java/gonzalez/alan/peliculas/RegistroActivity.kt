package gonzalez.alan.peliculas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegistroActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var et_nombre: EditText
    lateinit var et_genero: EditText
    lateinit var et_edad: EditText
    lateinit var et_telefono: EditText
    lateinit var et_correo: EditText
    lateinit var et_contraseña: EditText
    lateinit var et_contraseña2: EditText
    lateinit var btn_registrar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        auth = FirebaseAuth.getInstance()

        et_nombre = findViewById(R.id.editTextText)
        et_genero = findViewById(R.id.editTextText2)
        et_edad = findViewById(R.id.editTextNumber)
        et_telefono = findViewById(R.id.editTextPhone)
        et_correo = findViewById(R.id.et_correoRegistro)
        et_contraseña = findViewById(R.id.et_contraseña)
        et_contraseña2 = findViewById(R.id.et_contraseña2)
        btn_registrar = findViewById(R.id.btn_registro)

        btn_registrar.setOnClickListener {
            val nombre = et_nombre.text.toString().trim()
            val genero = et_genero.text.toString().trim()
            val edad = et_edad.text.toString().trim()
            val telefono = et_telefono.text.toString().trim()
            val correo = et_correo.text.toString().trim()
            val contraseña1 = et_contraseña.text.toString().trim()
            val contraseña2 = et_contraseña2.text.toString().trim()

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
                        // Guardar datos en la base de datos
                        guardarUsuarioEnBaseDeDatos(userId, nombre, genero, edad, telefono, correo)
                        // Redirigir al inicio de sesión
                        Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Log.w("RegistroActivity", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(this, "No se pudo registrar al usuario", Toast.LENGTH_SHORT).show()
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
}