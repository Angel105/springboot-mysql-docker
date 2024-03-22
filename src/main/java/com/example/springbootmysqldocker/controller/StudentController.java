package com.example.springbootmysqldocker.controller;

import com.example.springbootmysqldocker.entity.Student;
import com.example.springbootmysqldocker.repo.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    private StudentRepo studentRepo;

    @Autowired
    public StudentController(StudentRepo theStudentRepo) {
        studentRepo = theStudentRepo;
    }

    @GetMapping("/all")
    public List<Student> getAllStudents() {
        return studentRepo.findAll();
    }

    @PostMapping("/")
    public Student insert(@RequestBody Student student) {
        return studentRepo.save(student);
    }


}
