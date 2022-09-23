package com.blackbird.unigroup.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.blackbird.unigroup.R
import com.blackbird.unigroup.data.User
import com.blackbird.unigroup.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var dbReference: DatabaseReference
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbReference = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        binding.btnRegister.setOnClickListener {
            if(checkPasswordRepeat(binding.loginPassword.text.toString(), binding.loginPasswordConfirm.text.toString())) {
                val user = User(binding.loginName.text.toString(), binding.loginEmail.text.toString(), binding.loginPassword.text.toString())
                signUp(user)
            } else {
                showError()
            }
        }

        binding.btnLogIn.setOnClickListener {
            signIn(User(null, binding.loginEmail.text.toString(), binding.loginPassword.text.toString()))
        }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null) {
            Intent(this, MainActivity::class.java).also {
                startActivity(it)
            }
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_login, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.actionTip -> showTip()
            R.id.actionBack -> onBackPressed()
        }
        return true
    }

    private fun signUp(user: User) {
        val username = user.username
        val email = user.email
        val password = user.password
        auth.createUserWithEmailAndPassword(email!!, password!!).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Account created successfully.", Toast.LENGTH_SHORT).show()
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        Toast.makeText(this, "Authentication successful.", Toast.LENGTH_SHORT).show()
                        Intent(this, MainActivity::class.java).also {
                            startActivity(it)
                        }
                        writeNewUser(user!!.uid, username!!, email)
                        finish()
                    } else {
                        Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signIn(user: User) {
        val email = user.email
        val password = user.password
        auth.signInWithEmailAndPassword(email!!, password!!).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                auth.currentUser
                Toast.makeText(this, "Authentication successful.", Toast.LENGTH_SHORT).show()
                Intent(this, MainActivity::class.java).also {
                    startActivity(it)
                }
                finish()
            } else {
                Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun writeNewUser(userId: String, name: String, email: String?) {
        val user = User(name, email)
        dbReference.child("users").child(userId).setValue(user)
    }

    private fun showTip() {
        AlertDialog.Builder(this)
            .setMessage(R.string.tip)
            .setPositiveButton("Ok") { _, _ -> }
            .create()
            .show()
    }

    private fun checkPasswordRepeat(password: String, repeat: String) : Boolean =
        password == repeat

    private fun showError() {
        binding.ivLoginError.visibility = View.VISIBLE
        binding.tvLoginHelp.visibility = View.VISIBLE
        binding.tvLoginHelp.text = R.string.password_incorrect_repeat.toString()
    }

}
