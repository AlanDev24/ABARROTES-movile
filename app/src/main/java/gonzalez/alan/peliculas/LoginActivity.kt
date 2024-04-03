package gonzalez.alan.peliculas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import gonzalez.alan.peliculas.R

class LoginActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth

    lateinit var btn_ingresar: Button
    lateinit var btn_registro_login: Button
    lateinit var tv_recuperar: TextView
    lateinit var et_correoLogin:EditText
    lateinit var et_contraseñaLogin:EditText



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        btn_ingresar = findViewById(R.id.btn_ingresar)
        btn_registro_login = findViewById(R.id.btn_registro_login)
        tv_recuperar = findViewById(R.id.tv_recuperar)
        et_correoLogin = findViewById(R.id.et_correoLogin)
        et_contraseñaLogin = findViewById(R.id.et_contraseñaLogin)

        //inicializar auth firebase
        auth = Firebase.auth

        btn_registro_login.setOnClickListener{
            var intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }

        btn_ingresar.setOnClickListener{

            var correo:String = et_correoLogin.text.toString()
            var contraseña:String = et_contraseñaLogin.text.toString()

            if(!correo.isNullOrEmpty() || !contraseña.isNullOrEmpty()){
                auth.signInWithEmailAndPassword(correo, contraseña)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information

                            val user = auth.currentUser
                            var intetn = Intent(this, MainActivity::class.java)
                            startActivity(intetn)
                        } else {
                            // If sign in fails, display a message to the user
                            Toast.makeText(
                                this,
                                "Datos incorrectos.",
                                Toast.LENGTH_SHORT,
                            ).show()

                        }
                    }
            } else{
                Toast.makeText(this, "Ingresar correo y contraseña", Toast.LENGTH_SHORT).show()
            }





        }

        tv_recuperar.setOnClickListener{
            var intent = Intent(this, RecuperacionActivity::class.java)
            startActivity(intent)
        }
    }
}