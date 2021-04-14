package com.blackbird.unigroup.data

import android.util.Log

data class User(
    var username: String? = "",
    var email: String? = ""
) {

    fun checkUserName(name: String): Boolean {
        if(name.length < 2 || name.length > 15) {
            Log.e("USER_DATA_CLASS", "Wrong name!")
            return false
        }
        return true
    }

    fun checkUserEmail(email: String): Boolean {
        for(i in email.chars()) {
            val symbol = i.toChar()
            if(!symbol.isLetter() || !symbol.isDigit() || symbol != '@' || symbol != '.') {
                Log.e("USER_DATA_CLASS", "Wrong email!")
                return false
            }
        }
        return true
    }

    fun checkUserPassword(password: String): Boolean {
        if(password.length < 5) {
            Log.e("USER_DATA_CLASS", "Wrong password!")
            return false
        }
        return true
    }

}