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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var name: String
    private lateinit var dbReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        dbReference = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        btnRegister.setOnClickListener {
            if(loginPassword.text.toString() == loginPasswordConfirm.text.toString()) {
                email = loginEmail.text.toString()
                password = loginPassword.text.toString()
                name = loginName.text.toString()
                createAccount()
            } else {
                ivLoginError.visibility = View.VISIBLE
                tvLoginHelp.visibility = View.VISIBLE
                tvLoginHelp.text = "Your password repeat is not correct"
            }
        }

        btnLogIn.setOnClickListener {
            email = loginEmail.text.toString()
            password = loginPassword.text.toString()

            signIn()
        }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null) {
            Intent(this, MainActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    private fun createAccount() {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Account created successfully.", Toast.LENGTH_SHORT).show()
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        Toast.makeText(this, "Authentication successful.", Toast.LENGTH_SHORT).show()
                        Intent(this, MainActivity::class.java).also {
                            startActivity(it)
                        }
                        writeNewUser(user!!.uid, name, email)
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

    private fun signIn() {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_login, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.actionTip -> {
                val builder = AlertDialog.Builder(this)
                builder.setMessage(R.string.tip)
                        .setPositiveButton("Ok") { _, _ -> }
                builder.create().show()
                true
            }
            R.id.actionBack -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
