package com.synacy.student

class StudentController {

    static responseFormats = ['json']

    StudentService studentService

    def fetchAllStudents() {
        List<Student> students = studentService.fetchStudents()
        respond(students)
    }

    def createNewStudent() {
        String name = request.JSON.name
        Integer age = request.JSON.age
        String gender = request.JSON.gender
        String yearLevel = request.JSON.yearLevel

        Student student = studentService.createNewStudent(name, age, gender, yearLevel)
        respond(student)
    }
}
