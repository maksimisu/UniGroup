package com.blackbird.unigroup.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.blackbird.unigroup.R
import com.blackbird.unigroup.data.Student
import com.blackbird.unigroup.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser
    private lateinit var dbReference: DatabaseReference
    private var studentsList = mutableListOf<Student>()
    private var emailsList = ""
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser!!
        dbReference = FirebaseDatabase.getInstance().reference.child("users/${auth.uid}/group")

        val profileListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                studentsList.clear()
                for(i: DataSnapshot in dataSnapshot.children) {
                    val data = i.getValue(Student::class.java)
                    studentsList.add(data!!)
                }
                updateEmailsList()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "Failed to read value.", databaseError.toException())
            }
        }
        dbReference.addValueEventListener(profileListener)

        binding.btnGroupList.setOnClickListener {
            Intent(this, StudentsListActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnSendEmailAll.setOnClickListener {
            sendEmailAll()
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
                Intent(this, LoginActivity::class.java).also {
                    startActivity(it)
                }
                finish()
                true
            }
            R.id.actionBack -> {
                moveTaskToBack(true)
                android.os.Process.killProcess(android.os.Process.myPid())
                exitProcess(1)
            }
            R.id.actionDeleteAccount -> {
                deleteUser()
                true
            }
            R.id.actionProfileInfo -> {
                Intent(this, ProfileActivity::class.java).also {
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

    private fun sendEmailAll() {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(emailsList))
            putExtra(Intent.EXTRA_SUBJECT, "Headstudent")
        }
        if (intent.resolveActivity(packageManager) != null) {
            try {
                startActivity(Intent.createChooser(intent, "Choose Email Client..."))
            }
            catch (e: Exception){
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun updateEmailsList() {
        emailsList = ""
        for(i in 0 until studentsList.size) {
            emailsList += "${studentsList[i].email.toString()},"
        }
    }

}