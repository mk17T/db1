package com.duck.db1

class Student {

    lateinit var name:String
    lateinit var course :String
    lateinit var duration:String

    constructor(){}

    constructor(name: String, course: String, Duration: String) {
        this.name = name
        this.course = course
        this.duration = Duration
    }

}