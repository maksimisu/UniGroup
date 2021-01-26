package com.blackbird.unigroup.data

class Student() {

    var lastname: String? = null
    var name: String? = null
    var surname: String? = null
    var listId: Int? = null
    var email: String? = null
    var phoneNumber: String? = null
    var birthday: String? = null

    constructor(lastname: String, name: String, surname: String, listId: Int, email: String, phoneNumber: String, birthday: String) : this() {
        this.lastname = lastname
        this.name = name
        this.surname = surname
        this.listId = listId
        this.email = email
        this.phoneNumber = phoneNumber
        this.birthday = birthday
    }
}
