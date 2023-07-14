package com.francis.springreact.student;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class StudentCourse {

    private final UUID studentId;
    private final UUID courseId;
    private final String name;
    private final String description;
    private final String department;
    private final String teacherName;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Integer grade;

}
