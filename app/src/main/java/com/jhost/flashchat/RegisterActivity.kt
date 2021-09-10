package com.jhost.flashchat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest

class RegisterActivity:AppCompatActivity() {

    val CHAT_PREFS = "ChatPrefs"
    val DISPLAY_NAME_KEY = "username"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val emailView: EditText = findViewById(R.id.register_email)
        val passwordView: EditText = findViewById(R.id.register_password)
        val confirmPasswordView: EditText = findViewById(R.id.register_confirm_password)
        val usernameView: EditText = findViewById(R.id.register_username)

        confirmPasswordView.setOnEditorActionListener { textView, i, keyEvent ->
            if (i.equals(200) || i.equals(EditorInfo.IME_NULL)){
                attemptRegistration(emailView, usernameView.text.toString(), passwordView, confirmPasswordView.text.toString())
            }
            //WTF
            return@setOnEditorActionListener true
        }

    }

    private fun attemptRegistration(emailView: EditText, username: String, passwordView: EditText, confirmPassword: String): Boolean{
        emailView.error = null
        passwordView.error = null

        var isValid: Boolean = true
        var focusView: View? = null

        if (emailView.text.isEmpty()) {
            emailView.setError(R.string.error_field_required.toString())
            focusView = emailView
            isValid = false
        } else if (isValidEmail(emailView.text.toString())){
            emailView.setError(R.string.error_invalid_email.toString())
            focusView = emailView
            isValid = false
        }

        if (emailView.text.isEmpty() || !isValidPAssword(passwordView.text.toString(), confirmPassword)){
            passwordView.setError(R.string.error_invalid_password.toString())
            focusView = passwordView
            isValid = false
        }


        if (isValid) {
            createFirebaseUser(emailView.text.toString(), passwordView.text.toString(), username)
        } else {
            focusView?.requestFocus()
        }

        return isValid
    }

    private fun createFirebaseUser(email: String, password: String, username: String){
        getAuth().createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isComplete) {
                saveDisplayName(username)
                val intent: Intent = Intent(this, LoginActivity::class.java)
                finish()
                startActivity(intent)
            } else {
                //do smg
            }
        }
    }


    private fun isValidEmail(email: String): Boolean{
        return email.contains("@") && email.contains(".") && email.length > 5
    }

    private fun isValidPAssword(password: String, confirmPassword: String): Boolean {
        return password.length > 5 && password.equals(confirmPassword)
    }

    private fun saveDisplayName(username: String){
        val user: FirebaseUser? = getAuth().currentUser

        if (user != null) {
            val profileChangeRequest: UserProfileChangeRequest = UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .build()

            user.updateProfile(profileChangeRequest).addOnCompleteListener {  task ->
                Log.d("FlashChat", if (task.isComplete) { "Update complete"} else { "FK"}) }
        }
    }

    private fun getAuth(): FirebaseAuth{
        return FirebaseAuth.getInstance()
    }
}