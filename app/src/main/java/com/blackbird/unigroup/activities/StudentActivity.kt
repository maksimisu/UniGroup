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
import com.blackbird.unigroup.databinding.ActivityStudentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class StudentActivity : AppCompatActivity() {

    private lateinit var dbReference: DatabaseReference
    lateinit var auth: FirebaseAuth
    lateinit var email: String
    lateinit var student: Student
    lateinit var binding: ActivityStudentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        dbReference = FirebaseDatabase.getInstance().reference

        var extraListId = intent.getIntExtra("EXTRA_ID", 0)


        val profileListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val studentData = dataSnapshot.child("users/${auth.uid}/group/id_student_${auth.uid}_$extraListId").getValue(
                    Student::class.java)
                binding.tvStudentLastname.text = studentData?.lastname
                binding.tvStudentName.text = studentData?.name
                binding.tvStudentSurname.text = studentData?.surname
                binding.tvStudentListId.text = studentData?.listId.toString()
                binding.tvStudentEmail.text = studentData?.email
                binding.tvStudentPhone.text = studentData?.phoneNumber
                binding.tvStudentBirthday.text = studentData?.birthday
                email = studentData?.email.toString()
                student = Student(
                        studentData?.lastname.toString(),
                        studentData?.name.toString(),
                        studentData?.surname.toString(),
                        studentData?.listId.toString().toInt(),
                        studentData?.email.toString(),
                        studentData?.phoneNumber.toString(),
                        studentData?.birthday.toString()
                )
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "Failed to read value.", databaseError.toException())
            }
        }
        dbReference.addValueEventListener(profileListener)

        binding.btnDeleteStudent.setOnClickListener {
            dbReference.child("users/${auth.uid}/group/id_student_${auth.uid}_$extraListId").removeValue()
            finish()
        }

        binding.btnSendEmail.setOnClickListener {
            composeEmail(email)
        }

        binding.btnSendSMS.setOnClickListener {
            Intent(Intent.ACTION_VIEW).also {
                it.data = Uri.parse("smsto:")
                it.putExtra("address", student.phoneNumber)
                it.type = "vnd.android-dir/mms-sms"
                startActivity(it)
            }
        }

        binding.btnEditStudent.setOnClickListener {
            Intent(this, EditStudentActivity::class.java).also {
                it.putExtra("EXTRA_ID", extraListId)
                startActivity(it)
            }
        }

        binding.btnMigrateStudent.setOnClickListener {
            val newId = binding.etNewStudentId.text.toString().toInt()
            student.listId = newId
            dbReference.child("users/${auth.uid}/group/id_student_${auth.uid}_${newId}").setValue(student)
            dbReference.child("users/${auth.uid}/group/id_student_${auth.uid}_$extraListId").removeValue()
            extraListId = newId
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