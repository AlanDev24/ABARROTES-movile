package gonzalez.alan.peliculas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class RecuperacionActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var btn_recuperar:Button
    lateinit var et_correoRecuperar:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperacion)

        btn_recuperar = findViewById(R.id.btn_recuperacion)
        et_correoRecuperar = findViewById(R.id.et_correoRecuperacion)
        //inicializar auth firebase
        auth = Firebase.auth

        btn_recuperar.setOnClickListener{
            var correo:String = et_correoRecuperar.text.toString()

            if(!correo.isNullOrEmpty()){

                Firebase.auth.sendPasswordResetEmail(correo)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Se ha enviado un correo electronico con instrucciones", Toast.LENGTH_SHORT).show()
                            finish()
                        }else {
                            Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
            Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show()

            }
        }
    }
}