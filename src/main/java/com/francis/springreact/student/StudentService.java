package com.francis.springreact.student;
import com.francis.springreact.EmailValidator;
import com.francis.springreact.exception.ApiRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class StudentService {

    private final StudentDataAccessService dataAccessService;
    private final EmailValidator emailValidator;

    @Autowired
    public StudentService(StudentDataAccessService dataAccessService, EmailValidator emailValidator) {
        this.dataAccessService = dataAccessService;
        this.emailValidator = emailValidator;
    }

    List<Student> getAllStudents(){
        return dataAccessService.selectAllStudents();
    }

    List<StudentCourse> getStudentCourses(UUID studentId){
        return dataAccessService.selectStudentCourses(studentId);
    }

    void addNewStudent(Student student){
        addNewStudent(null, student);
    }



    void addNewStudent(UUID id, Student student) {
        UUID newId = Optional.ofNullable(id)
                .orElse(UUID.randomUUID());

        if (!emailValidator.test(student.getEmail())){
            throw new ApiRequestException(student.getEmail() + " is not valid");
        }
        //TODO: verify the email is not taken
        if(dataAccessService.isEmailTaken(student.getEmail())){
            throw new ApiRequestException(student.getEmail() + " is taken");
        }
        dataAccessService.insertStudent(newId, student);
    }
}
