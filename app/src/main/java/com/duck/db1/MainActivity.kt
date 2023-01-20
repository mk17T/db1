package com.duck.db1

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

private lateinit var edtAdd : EditText
private lateinit var btnAdd : Button
private lateinit var txtRead : TextView
private lateinit var btnShow : Button

private lateinit var mDb : FirebaseDatabase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edtAdd = findViewById(R.id.editText)
        btnAdd = findViewById(R.id.btn_Add)
        btnShow = findViewById(R.id.btn_Show)
        txtRead = findViewById(R.id.textView)

        btnAdd.setOnClickListener {
            var rollnum = edtAdd.text.toString()
            var student = Student("namerandom", "course","duration")
            addStudent(rollnum,student)

        }
        btnShow.setOnClickListener {
            getAllStudents() { students ->
                val studentsString = students.joinToString(separator = "\n") { student ->
                    if (student != null) "${student.name} - ${student.course}" else ""
                }
                txtRead.text = studentsString

            }
        }
    }



    private fun addDataToRoot( data:String){
        val mDb = FirebaseDatabase.getInstance()
        var root = mDb.getReference()
        if(data != null) {
            root.setValue(data)
            edtAdd.setText("")
            val toast = Toast.makeText(applicationContext,"Added data to root", Toast.LENGTH_SHORT)
            toast.show()
        }

    }

    private fun addRootNode(key: String, value:Any){
        val mDb = FirebaseDatabase.getInstance()
        var root = mDb.getReference()
        if(key != null) {
            val newNode: MutableMap<String, Any> = HashMap()
            newNode[key] = value
            //newNode["key2"] = "value2"
            // map can have multiple nodes which will be added

            root.updateChildren(newNode)

            val toast = Toast.makeText(applicationContext,"Added new root node", Toast.LENGTH_SHORT)
            toast.show()

        }

    }

    // Add new student to database students
    private fun addStudent( rollnum:String , student: Student){
        val mDb = FirebaseDatabase.getInstance()
        var sRef = mDb.getReference("students")
        /* Adding under students
            -students
                -rollnum
                    --name
                    --course
                    --duration
         */

        sRef.child(rollnum).setValue(student)

        Toast.makeText(applicationContext,"Added student", Toast.LENGTH_SHORT).show()

    }

    //get details of student with specified roll number
    /* getStudent("001") { student ->
                if (student != null) {
                    txtRead.append(student?.name)
                    txtRead.append(student?.course)
                } else {
                    // Handle error, student is null
                }
            }

     */
    private fun getStudent(rollNumber: String, callback: (student: Student?) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val studentRef = database.getReference("students").child(rollNumber)
        studentRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val student = dataSnapshot.getValue(Student::class.java)
                callback(student)
            }
            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun getAllStudents(callback: (students: ArrayList<Student>) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val studentsRef = database.getReference("students")
        val studentList = arrayListOf<Student>()
        studentsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (studentSnapshot in dataSnapshot.children) {
                    val student = studentSnapshot.getValue(Student::class.java)
                    if (student != null) {
                        studentList.add(student)
                    }
                }
                callback(studentList)
            }
            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }



/*
// Add value to root node
        addDataToRoot(data)


// Add new root node
             addRootNode("rootnode","")

//Add Student (class Student)
            var stud1 = Student("name1", "course1", "1yr")
            addStudent("001",stud1)


 */
}