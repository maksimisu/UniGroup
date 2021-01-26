package com.blackbird.unigroup.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.blackbird.unigroup.R
import com.blackbird.unigroup.data.Student
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_student.*

class AddStudentActivity : AppCompatActivity() {

    private lateinit var dbReference: DatabaseReference
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_student)

        btnAddStudentData.setOnClickListener {
            dbReference = FirebaseDatabase.getInstance().reference
            auth = FirebaseAuth.getInstance()
            val student = Student(etStudentLastname.text.toString(), etStudentName.text.toString(),
                etStudentSurname.text.toString(), etStudentListId.text.toString().toInt(),
                etStudentEmail.text.toString(), etStudentPhone.text.toString(), etStudentBirthday.text.toString())
            dbReference.child("users").child(auth.uid!!).child("group").child("id_student_${auth.uid}_${etStudentListId.text}").setValue(student)
            finish()
        }
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