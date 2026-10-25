package com.example.studentrecords;
import org.springframework.data.jpa.repository.JpaRepository;
public interface StudentRepository extends JpaRepository<Student,Long>{boolean existsByRollNumber(String rollNumber);boolean existsByRollNumberAndIdNot(String rollNumber,Long id);}
