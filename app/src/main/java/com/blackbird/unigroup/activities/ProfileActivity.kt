package com.blackbird.unigroup.activities

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.blackbird.unigroup.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser
    private lateinit var dbReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        dbReference = FirebaseDatabase.getInstance().reference

        val profileEmail = tvProfileEmail
        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser!!
        currentUser.let {
            val email = currentUser.email
            profileEmail.text = email
        }

        val profileName = tvProfileName
        val profileListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val name = dataSnapshot.child("users/${currentUser.uid}/username").value.toString()
                profileName.text = name
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "Failed to read value.", databaseError.toException())
            }
        }
        dbReference.addValueEventListener(profileListener)

        btnChangeProfileName.setOnClickListener {
            changeName()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_profile, menu)
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
                onBackPressed()
                true
            }
            R.id.actionDeleteAccount -> {
                deleteUser()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun deleteUser() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to delete account? It will delete all your data!")
            .setPositiveButton("Yes") { _, _ ->
                dbReference.child("users/${auth.uid}").removeValue()
                auth.currentUser!!.delete().addOnCompleteListener { task ->
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

    private fun changeName() {
        val newName = etProfileName.text.toString()
        dbReference.child("users/${auth.uid}/username").setValue(newName)
    }
}