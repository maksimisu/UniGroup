package com.blackbird.unigroup.data

import android.util.Log

class Student() {

    var lastname: String? = null
    var name: String? = null
    var surname: String? = null
    var listId: Int? = null
    var email: String? = null
    var phoneNumber: String? = null
    var birthday: String? = null

    constructor(lastname: String, name: String, surname: String, listId: Int, email: String, phoneNumber: String, birthday: String) : this() {
        if(checkStudentLastname(lastname)
            && checkStudentName(name)
            && checkStudentSurname(surname)
            && checkStudentListId(listId)
            && checkStudentEmail(email)
            && checkStudentPhone(phoneNumber)
            && checkStudentBirthday(birthday)
        ) {
            this.lastname = lastname
            this.name = name
            this.surname = surname
            this.listId = listId
            this.email = email
            this.phoneNumber = phoneNumber
            this.birthday = birthday
        } else {
            Log.e("STUDENT_DATA_CLASS", "Can't add student!")
        }
    }

    fun checkStudentLastname(string: String): Boolean {
        if(string.isEmpty()) return true
        for(i in string.chars()) {
            val symbol = i.toChar()
            if(!symbol.isLetter()) {
                Log.e("STUDENT_DATA_CLASS", "Wrong lastname!")
                return false
            }
        }
        return true
    }

    fun checkStudentName(string: String): Boolean {
        if(string.isEmpty()) return true
        for(i in string.chars()) {
            val symbol = i.toChar()
            if(!symbol.isLetter()) {
                Log.e("STUDENT_DATA_CLASS", "Wrong name!")
                return false
            }
        }
        return true
    }

    fun checkStudentSurname(string: String): Boolean {
        if(string.isEmpty()) return true
        for(i in string.chars()) {
            val symbol = i.toChar()
            if(!symbol.isLetter()) {
                Log.e("STUDENT_DATA_CLASS", "Wrong surname!")
                return false
            }
        }
        return true
    }

    fun checkStudentListId(n: Int): Boolean {
        if(n.toString().isEmpty()) return true
        for(i in n.toString().chars()) {
            val symbol = i.toChar()
            if(!symbol.isDigit()) {
                Log.e("STUDENT_DATA_CLASS", "Wrong listId!")
                return false
            }
        }
        return true
    }

    fun checkStudentEmail(string: String): Boolean {
        if(string.isEmpty()) return true
        for(i in string.chars()) {
            val symbol = i.toChar()
            if(!symbol.isLetter() || !symbol.isDigit() || symbol != '@' || symbol != '.') {
                Log.e("STUDENT_DATA_CLASS", "Wrong email!")
                return false
            }
        }
        return true
    }

    fun checkStudentPhone(string: String): Boolean {
        if(string.isEmpty()) return true
        for(i in string.chars()) {
            val symbol = i.toChar()
            if(!symbol.isDigit() || symbol != '+') {
                Log.e("STUDENT_DATA_CLASS", "Wrong phone number!")
                return false
            }
        }
        return true
    }

    fun checkStudentBirthday(string: String): Boolean {
        if(string.isEmpty()) return true
        for(i in string.chars()) {
            val symbol = i.toChar()
            if(!symbol.isDigit() || symbol != '.') {
                Log.e("STUDENT_DATA_CLASS", "Wrong birthday!")
                return false
            }
        }
        return true
    }


}
