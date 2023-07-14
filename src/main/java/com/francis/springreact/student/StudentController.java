package com.francis.springreact.student;

import com.francis.springreact.exception.ApiRequestException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/students")
public class StudentController {


    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService){
        this.studentService = studentService;
    }
    @GetMapping()
    public List<Student> getAllStudents() throws IllegalStateException{
        //throw new ApiRequestException("Oops cannot get all students with custom exception");
        return studentService.getAllStudents();
    }

    @GetMapping(path = "{studentId}/courses")
    public List<StudentCourse> getAllCoursesForStudent( @PathVariable("studentId") UUID studentId){
        System.out.println(studentId);
        return studentService.getStudentCourses(studentId);
    }

    @PostMapping
    public void addNewStudent(@RequestBody @Valid Student student){
        studentService.addNewStudent(student);
    }
}
