package com.blackbird.unigroup.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blackbird.unigroup.R
import kotlinx.android.synthetic.main.rv_group_item.view.*

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