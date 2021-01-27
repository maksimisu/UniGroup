package com.blackbird.unigroup.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.blackbird.unigroup.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser
    private lateinit var dbReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser!!
        dbReference = FirebaseDatabase.getInstance().reference

        btnGroupList.setOnClickListener {
            val intent = Intent(this, StudentsListActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.actionExitAccount -> {
                auth.signOut()
                finish()
                true
            }
            R.id.actionBack -> {
                moveTaskToBack(true)
                android.os.Process.killProcess(android.os.Process.myPid())
                exitProcess(1)
                true
            }
            R.id.actionDeleteAccount -> {
                deleteUser()
                true
            }
            R.id.actionProfileInfo -> {
                val intent = Intent(this, ProfileActivity::class.java).also {
                    startActivity(it)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun deleteUser() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to delete account? It will delete all your data!")
                .setPositiveButton("Yes") { _, _ ->
                    dbReference.child("users/${currentUser.uid}").removeValue()
                    currentUser.delete().addOnCompleteListener { task ->
                        if(task.isSuccessful) {
                            Toast.makeText(this, "Account deleted successfully", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this, "Account delete failed. Try to re-login", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                .setNegativeButton("No") { _, _ ->
                }
        builder.create().show()
    }

}