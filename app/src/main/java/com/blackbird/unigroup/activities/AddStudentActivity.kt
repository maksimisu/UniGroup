package com.blackbird.unigroup.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.blackbird.unigroup.R
import com.blackbird.unigroup.data.Student
import com.blackbird.unigroup.databinding.ActivityAddStudentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddStudentActivity : AppCompatActivity() {

    private lateinit var dbReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityAddStudentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAddStudentData.setOnClickListener {
            addStudent()
            finish()
        }
    }

    private fun addStudent() {
        dbReference = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        val student = Student(
            binding.etStudentLastname.text.toString(),
            binding.etStudentName.text.toString(),
            binding.etStudentSurname.text.toString(),
            binding.etStudentListId.text.toString().toInt(),
            binding.etStudentEmail.text.toString(),
            binding.etStudentPhone.text.toString(),
            binding.etStudentBirthday.text.toString()
        )
        dbReference.child("users")
            .child(auth.uid!!)
            .child("group")
            .child("id_student_${auth.uid}_${binding.etStudentListId.text}")
            .setValue(student)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_student, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.actionBack -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}