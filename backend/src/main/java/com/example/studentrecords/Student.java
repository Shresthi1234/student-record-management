package com.example.studentrecords;
import jakarta.persistence.*;import jakarta.validation.constraints.*;
@Entity @Table(name="students") public class Student {
@Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
@NotBlank @Column(nullable=false,unique=true) public String rollNumber;
@NotBlank public String name;
@NotBlank @Email public String email;
@NotBlank public String department;
@NotNull @Min(1) @Max(4) public Integer year;
public String phone;
public Student(){}
}
