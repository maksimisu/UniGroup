package com.blackbird.unigroup.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.blackbird.unigroup.R
import com.blackbird.unigroup.data.RVClick
import com.blackbird.unigroup.data.Student
import com.blackbird.unigroup.data.StudentsAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_students_list.*

class StudentsListActivity : AppCompatActivity() {

    private lateinit var dbReference: DatabaseReference
    lateinit var auth: FirebaseAuth
    private var studentsList = mutableListOf<Student>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_students_list)

        auth = FirebaseAuth.getInstance()
        dbReference = FirebaseDatabase.getInstance().reference.child("users/${auth.uid}/group")

        rvStudents.layoutManager = LinearLayoutManager(this)
        val adapter = StudentsAdapter(studentsList)
        rvStudents.adapter = adapter

        val profileListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                studentsList.clear()
                for(i: DataSnapshot in dataSnapshot.children) {
                    val data = i.getValue(Student::class.java)
                    studentsList.add(data!!)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "Failed to read value.", databaseError.toException())
            }
        }
        dbReference.addValueEventListener(profileListener)

        val recyclerView = rvStudents
        recyclerView.addOnItemTouchListener(RVClick(this, recyclerView, object : RVClick.OnItemClickListener {
            override fun onItemClick(view: View?, position: Int) {
                for(i in 0 until studentsList.size) {
                    if(position == i) {
                        val intent = Intent(this@StudentsListActivity, StudentActivity::class.java).also {
                            it.putExtra("EXTRA_ID", studentsList[i].listId)
                            startActivity(it)
                        }
                    }
                }
            }

            override fun onLongItemClick(view: View?, position: Int) {
                for(i in 0 until studentsList.size) {
                    if(position == i) {
                        Toast.makeText(this@StudentsListActivity, "Student ${studentsList[i].name} chosen", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }))

        btnAddStudent.setOnClickListener {
            val intent = Intent(this, AddStudentActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_students_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.actionBack -> {
                onBackPressed()
                true
            }
            R.id.actionDeleteAll -> {
                dbReference.removeValue()
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

}