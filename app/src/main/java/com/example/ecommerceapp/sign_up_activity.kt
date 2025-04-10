package com.example.ecommerceapp

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
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class sign_up_activity : AppCompatActivity() {

    private lateinit var full_name_input: EditText
    private lateinit var email_sign_up: EditText
    private lateinit var password_sign_up: EditText
    private lateinit var sign_up_button_action: Button
    private lateinit var login_from_signup: TextView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)

        auth = Firebase.auth

        full_name_input = findViewById(R.id.full_name_input)
        email_sign_up = findViewById(R.id.email_sign_up)
        password_sign_up = findViewById(R.id.password_sign_up)
        sign_up_button_action = findViewById(R.id.sign_up_button)
        login_from_signup = findViewById(R.id.login_from_signup)

        sign_up_button_action.setOnClickListener {
            val fullName = full_name_input.text.toString().trim()
            val email = email_sign_up.text.toString().trim()
            val password = password_sign_up.text.toString().trim()

            if (fullName.isEmpty()) {
                full_name_input.error = "Full name is required"
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                email_sign_up.error = "Email is required"
                return@setOnClickListener
            }

            if (password.isEmpty() || password.length < 6) {
                password_sign_up.error = "Password must be at least 6 characters"
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser

                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName(fullName)
                            .build()

                        user?.updateProfile(profileUpdates)
                            ?.addOnCompleteListener { profileTask ->
                                if (profileTask.isSuccessful) {
                                    Toast.makeText(this, "Sign up successful", Toast.LENGTH_SHORT).show()

                                    val intent = Intent(this, Login_page::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                    } else {
                        Toast.makeText(this, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        login_from_signup.setOnClickListener {
            val intent = Intent(this, Login_page::class.java)
            startActivity(intent)
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}