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

    fun sortByLastname(students: List<Student>) {
        students.sortedBy { it.lastname }
    }

    fun sortByName(students: List<Student>) {
        students.sortedBy { it.name }
    }

    fun sortBySurname(students: List<Student>) {
        students.sortedBy { it.surname }
    }

    fun sortByListId(students: List<Student>) {
        students.sortedBy { it.listId }
    }

    fun sortByEmail(students: List<Student>) {
        students.sortedBy { it.email }
    }

    fun sortByPhoneNumber(students: List<Student>) {
        students.sortedBy { it.phoneNumber }
    }

    fun sortByBirthday(students: List<Student>) {
        students.sortedBy { it.birthday }
    }

}
