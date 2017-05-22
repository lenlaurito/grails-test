package com.synacy.student

import grails.plugins.rest.client.RestBuilder
import grails.plugins.rest.client.RestResponse
import grails.transaction.Transactional
import groovy.json.JsonBuilder
import groovy.util.logging.Log4j
import org.springframework.http.HttpStatus
import org.springframework.web.client.ResourceAccessException

@Transactional
@Log4j
class StudentService {

	RestBuilder restBuilder

	String SPRING_BOOT_STUDENT_API_URL = "http://localhost:8070/api/v1/student"

	public List<Student> fetchStudents() {
		List<Student> students = []
		try {
			RestResponse response = restBuilder.get(SPRING_BOOT_STUDENT_API_URL)
			if (response.statusCode == HttpStatus.OK) {
				List jsonArray = response.json
				students = convertSpringBootResponseToStudentList(jsonArray)
			} else {
				log.error("Spring boot api responded with ${response.statusCode}: ${response.body}")
			}
		} catch (ResourceAccessException e) {
			log.error "Cannot connect to ${SPRING_BOOT_STUDENT_API_URL}"
		}
		return students
	}

	public Student createNewStudent(String name, int age, String gender, String yearLevel) {
		String jsonRequest = new JsonBuilder([name: name, age: age, gender: gender, yearLevel: yearLevel]).toString()
		RestResponse response = restBuilder.post(SPRING_BOOT_STUDENT_API_URL) {
			json jsonRequest
		}
		if (response.statusCode == HttpStatus.CREATED) {
			return convertSprintBootReponseToStudent(response.json)
		} else {
			log.error("Spring boot api responded with ${response.statusCode}: ${response.body}")
			throw new InvalidRequestException()
		}
	}


	private List<Student> convertSpringBootResponseToStudentList(List<Map> jsonArray) {
		List<Student> students = []
		jsonArray.each { record ->
			Student student = new Student()
			student.name = record.name
			student.age = record.age
			student.yearLevel = record.yearLevel
			student.gender = record.gender
			students << student
		}
		return students
	}

	private Student convertSprintBootReponseToStudent(Map json) {
		Student student = new Student()
		student.name = json.name
		student.name = json.name
		student.age = json.age
		student.yearLevel = json.yearLevel
		student.gender = json.gender
		return student
	}
}
