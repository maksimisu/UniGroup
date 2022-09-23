package com.blackbird.unigroup.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blackbird.unigroup.R

class StudentsAdapter(
        var students: List<Student>
) : RecyclerView.Adapter<StudentsAdapter.StudentsViewHolder>() {

    inner class StudentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_group_item, parent, false)
        return StudentsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return students.size
    }

    override fun onBindViewHolder(holder: StudentsViewHolder, position: Int) {
        holder.itemView.apply {
            val tvStudentLastname: TextView = findViewById(R.id.tvStudentLastname)
            val tvStudentName: TextView = findViewById(R.id.tvStudentName)
            val tvStudentSurname: TextView = findViewById(R.id.tvStudentSurname)
            val tvStudentListId: TextView = findViewById(R.id.tvStudentListId)
            val tvStudentEmail: TextView = findViewById(R.id.tvStudentEmail)
            val tvStudentPhone: TextView = findViewById(R.id.tvStudentPhone)
            val tvStudentBirthday: TextView = findViewById(R.id.tvStudentBirthday)

            tvStudentLastname.text = students[position].lastname
            tvStudentName.text = students[position].name
            tvStudentSurname.text = students[position].surname
            tvStudentListId.text = students[position].listId.toString()
            tvStudentEmail.text = students[position].email
            tvStudentPhone.text = students[position].phoneNumber
            tvStudentBirthday.text = students[position].birthday
        }
    }

}