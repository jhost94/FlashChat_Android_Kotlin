package com.jhost.flashchat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity:AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailView: AutoCompleteTextView = findViewById(R.id.login_email)
        val passwordView: EditText = findViewById(R.id.login_password)

        passwordView.setOnEditorActionListener { textView, i, keyEvent ->
            attemptLogin(emailView.text.toString(), passwordView.text.toString())
        }
    }

    private fun attemptLogin(email: String, password: String): Boolean{
        if (email.isEmpty() || password.isEmpty()) return false

        Toast.makeText(this, "Login in progress...", Toast.LENGTH_SHORT)

        getAuth().signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isComplete) {
                val intent: Intent = Intent(this, MainChatActivity::class.java)
                finish()
                startActivity(intent)
            } else {
                Log.d("FlashChat", "error")
            }
        }
        return true
    }

    private fun getAuth(): FirebaseAuth{
        return FirebaseAuth.getInstance()
    }
}