package com.blackbird.unigroup.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.blackbird.unigroup.R
import com.blackbird.unigroup.data.Student
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_edit_student.*

class EditStudentActivity : AppCompatActivity() {

    private lateinit var dbReference: DatabaseReference
    lateinit var auth: FirebaseAuth
    private var listId: Int = 0
    private lateinit var path: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_student)

        auth = FirebaseAuth.getInstance()
        dbReference = FirebaseDatabase.getInstance().reference

        listId = intent.getIntExtra("EXTRA_ID", 0)
        path = "users/${auth.uid}/group/id_student_${auth.uid}_$listId"

        val profileListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val student = dataSnapshot.child("users/${auth.uid}/group/id_student_${auth.uid}_$listId").getValue(
                    Student::class.java)
                etNewLastname.setText(student?.lastname)
                etNewName.setText(student?.name)
                etNewSurname.setText(student?.surname)
                etNewEmail.setText(student?.email)
                etNewPhone.setText(student?.phoneNumber)
                etNewBirthday.setText(student?.birthday)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "Failed to read value.", databaseError.toException())
            }
        }
        dbReference.addValueEventListener(profileListener)

        btnApplyStudentChanges.setOnClickListener {
            dbReference.child("$path/lastname").setValue(etNewLastname.text.toString())
            dbReference.child("$path/name").setValue(etNewName.text.toString())
            dbReference.child("$path/surname").setValue(etNewSurname.text.toString())
            dbReference.child("$path/email").setValue(etNewEmail.text.toString())
            dbReference.child("$path/phoneNumber").setValue(etNewPhone.text.toString())
            dbReference.child("$path/birthday").setValue(etNewBirthday.text.toString())
            finish()
        }
    }
}