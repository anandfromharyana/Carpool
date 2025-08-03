package com.anand.carpool

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var loginbtn: Button
    private lateinit var useremail: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var auth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
         setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        loginbtn = findViewById(R.id.buttonLogin)
        useremail = findViewById(R.id.editTextEmail)
        passwordEditText = findViewById(R.id.editTextPassword)



        val loginRedirect = findViewById<TextView>(R.id.textViewSignUp)

        loginRedirect.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)

            startActivity(intent)
             overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)

              finish() // Optional: if you don't want to come back to signup on back press
        }


        loginbtn.setOnClickListener {
            val mail = useremail.text.toString().trim()
            val pass = passwordEditText.text.toString().trim()

            if (mail.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            auth.signInWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(this) { task ->


                    if (task.isSuccessful) {
                        // Sign in success!
                        Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()

                         val intent = Intent(this, app_navigation::class.java)
                        startActivity(intent)
                       finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        val errorMessage = task.exception?.message ?: "Authentication failed."
                        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                    }
                }

        }
    }
}

