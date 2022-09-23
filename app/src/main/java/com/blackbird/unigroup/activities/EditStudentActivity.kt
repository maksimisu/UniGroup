package com.blackbird.unigroup.activities

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.blackbird.unigroup.R
import com.blackbird.unigroup.data.Student
import com.blackbird.unigroup.databinding.ActivityEditStudentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_edit_student.*

class EditStudentActivity : AppCompatActivity() {

    private lateinit var dbReference: DatabaseReference
    lateinit var auth: FirebaseAuth
    private var listId: Int = 0
    private lateinit var path: String
    private lateinit var binding: ActivityEditStudentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        dbReference = FirebaseDatabase.getInstance().reference

        listId = intent.getIntExtra("EXTRA_ID", 0)
        path = "users/${auth.uid}/group/id_student_${auth.uid}_$listId"

        val profileListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val student = dataSnapshot.child("users/${auth.uid}/group/id_student_${auth.uid}_$listId").getValue(
                    Student::class.java)
                binding.etNewLastname.setText(student?.lastname)
                binding.etNewName.setText(student?.name)
                binding.etNewSurname.setText(student?.surname)
                binding.etNewEmail.setText(student?.email)
                binding.etNewPhone.setText(student?.phoneNumber)
                binding.etNewBirthday.setText(student?.birthday)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "Failed to read value.", databaseError.toException())
            }
        }
        dbReference.addValueEventListener(profileListener)

        binding.btnApplyStudentChanges.setOnClickListener {
            applyChanges()
            finish()
        }
    }

    private fun applyChanges() {
        dbReference.child("$path/lastname").setValue(binding.etNewLastname.text.toString())
        dbReference.child("$path/name").setValue(binding.etNewName.text.toString())
        dbReference.child("$path/surname").setValue(binding.etNewSurname.text.toString())
        dbReference.child("$path/email").setValue(binding.etNewEmail.text.toString())
        dbReference.child("$path/phoneNumber").setValue(binding.etNewPhone.text.toString())
        dbReference.child("$path/birthday").setValue(binding.etNewBirthday.text.toString())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_student, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.actionBack -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}