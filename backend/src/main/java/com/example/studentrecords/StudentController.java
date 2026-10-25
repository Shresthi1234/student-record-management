package com.example.studentrecords;
import org.springframework.web.bind.annotation.*;import org.springframework.http.*;import org.springframework.web.server.ResponseStatusException;import jakarta.validation.Valid;import java.util.*;
@RestController @RequestMapping("/api/students") public class StudentController{
private final StudentRepository repo;public StudentController(StudentRepository repo){this.repo=repo;}
@GetMapping public List<Student> all(){return repo.findAll();}
@GetMapping("/{id}") public Student one(@PathVariable Long id){return repo.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Student not found"));}
@PostMapping @ResponseStatus(HttpStatus.CREATED) public Student create(@Valid @RequestBody Student s){if(repo.existsByRollNumber(s.rollNumber))throw new ResponseStatusException(HttpStatus.CONFLICT,"Roll number already exists");s.id=null;return repo.save(s);}
@PutMapping("/{id}") public Student update(@PathVariable Long id,@Valid @RequestBody Student s){Student old=one(id);if(repo.existsByRollNumberAndIdNot(s.rollNumber,id))throw new ResponseStatusException(HttpStatus.CONFLICT,"Roll number already exists");old.rollNumber=s.rollNumber;old.name=s.name;old.email=s.email;old.department=s.department;old.year=s.year;old.phone=s.phone;return repo.save(old);}
@DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id){if(!repo.existsById(id))throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Student not found");repo.deleteById(id);}
}
