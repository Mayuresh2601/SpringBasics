package com.bridgelabz.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bridgelabz.model.Student;

public interface StudentRepository extends MongoRepository<Student, Integer>{

}
