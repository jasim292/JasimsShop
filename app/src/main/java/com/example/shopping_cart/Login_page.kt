package com.example.shopping_cart

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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Login_page : AppCompatActivity() {
    private lateinit var emailLogin: EditText
    private lateinit var password_login: EditText
    private lateinit var login_button_action: Button
    private lateinit var sign_up_text: TextView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.login_page_layout)

        // Initialize Firebase Auth
        auth = Firebase.auth

        emailLogin = findViewById(R.id.email_login)
        password_login = findViewById(R.id.password_login)
        login_button_action = findViewById(R.id.login_button_action)
        sign_up_text = findViewById(R.id.sign_up_text)

        login_button_action.setOnClickListener {
            val email = emailLogin.text.toString().trim()
            val password = password_login.text.toString().trim()

            if (email.isEmpty()) {
                emailLogin.error = "Email is required"
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                password_login.error = "Password is required"
                return@setOnClickListener
            }

            // Authenticate with Firebase
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Login success
                        Toast.makeText(this, "Login successful",Toast.LENGTH_SHORT).show()

                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // If login fails
                        Toast.makeText(this, "Authentication failed: ${task.exception?.message ?: "Unknown error"}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        sign_up_text.setOnClickListener {
            val intent = Intent(this, sign_up_activity::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is already logged in
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // User is already logged in, redirect to main screen
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}