package com.example.techno

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        // Match XML IDs
        val fullname = findViewById<EditText>(R.id.edtFullName)
        val email = findViewById<EditText>(R.id.edtEmail)
        val pass = findViewById<EditText>(R.id.edtPassword)
        val confirm = findViewById<EditText>(R.id.edtConfirm)
        val signupBtn = findViewById<Button>(R.id.btnSignUp)
        val backToLogin = findViewById<TextView>(R.id.txtBackLogin)

        signupBtn.setOnClickListener {
            val userFullname = fullname.text.toString().trim()
            val userEmail = email.text.toString().trim()
            val userPass = pass.text.toString().trim()
            val confirmPass = confirm.text.toString().trim()

            if (userFullname.isEmpty() || userEmail.isEmpty() || userPass.isEmpty() || confirmPass.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (userPass != confirmPass) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            signupBtn.isEnabled = false

            auth.createUserWithEmailAndPassword(userEmail, userPass)
                .addOnCompleteListener { task ->
                    signupBtn.isEnabled = true

                    if (task.isSuccessful) {
                        auth.currentUser?.updateProfile(
                            com.google.firebase.auth.UserProfileChangeRequest.Builder()
                                .setDisplayName(userFullname)
                                .build()
                        )

                        Toast.makeText(this, "Account Created!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }

        backToLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
