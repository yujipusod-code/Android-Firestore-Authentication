package com.example.techno

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        // Check if user is already logged in
        auth.currentUser?.let {
            // User is logged in, go straight to HomeActivity
            startActivity(Intent(this, HomeActivity::class.java))
            finish() // prevent going back to login
        }

        val email = findViewById<EditText>(R.id.edtEmail)
        val pass = findViewById<EditText>(R.id.edtPassword)
        val loginBtn = findViewById<Button>(R.id.btnLogin)
        val createAcc = findViewById<TextView>(R.id.txtCreateAcc)

        // Navigate to RegisterActivity if user wants to create account
        createAcc.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        loginBtn.setOnClickListener {
            val userEmail = email.text.toString().trim()
            val userPass = pass.text.toString().trim()

            if (userEmail.isEmpty() || userPass.isEmpty()) {
                Toast.makeText(this, "Please enter email & password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            loginBtn.isEnabled = false
            auth.signInWithEmailAndPassword(userEmail, userPass)
                .addOnCompleteListener { task ->
                    loginBtn.isEnabled = true

                    if (task.isSuccessful) {
                        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Login Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}
