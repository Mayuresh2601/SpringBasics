package com.setterconstructordependencies;

public class Student {
	
	private int studentId;
	private String studentName;
	
	public Student(int studentId, String studentName) {
	
		this.studentId = studentId;
		this.studentName = studentName;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}
	
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	
	public void displayStudentInfo() {
		
		System.out.println("Student ID is: "+ studentId + "\nStudent Name is: "+studentName);
	}

}
