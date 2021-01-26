package com.blackbird.unigroup.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.blackbird.unigroup.R
import com.blackbird.unigroup.data.Student
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_student.*

class StudentActivity : AppCompatActivity() {

    private lateinit var dbReference: DatabaseReference
    lateinit var auth: FirebaseAuth
    lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student)

        auth = FirebaseAuth.getInstance()
        dbReference = FirebaseDatabase.getInstance().reference

        val listId = intent.getIntExtra("EXTRA_ID", 0)


        val profileListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val student = dataSnapshot.child("users/${auth.uid}/group/id_student_${auth.uid}_$listId").getValue(
                    Student::class.java)
                tvStudentLastname.text = student?.lastname
                tvStudentName.text = student?.name
                tvStudentSurname.text = student?.surname
                tvStudentListId.text = student?.listId.toString()
                tvStudentEmail.text = student?.email
                tvStudentPhone.text = student?.phoneNumber
                tvStudentBirthday.text = student?.birthday
                email = student?.email.toString()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "Failed to read value.", databaseError.toException())
            }
        }
        dbReference.addValueEventListener(profileListener)

        btnDeleteStudent.setOnClickListener {
            dbReference.child("users/${auth.uid}/group/id_student_${auth.uid}_$listId").removeValue()
            finish()
        }

        btnSendEmail.setOnClickListener {
            composeEmail(email)
        }

        btnEditStudent.setOnClickListener {
            val intent = Intent(this, EditStudentActivity::class.java).also {
                it.putExtra("EXTRA_ID", listId)
                startActivity(it)
            }
        }
    }

    private fun composeEmail(address: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$address")
            putExtra(Intent.EXTRA_EMAIL, address)
            putExtra(Intent.EXTRA_SUBJECT, "Headstudent")
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
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