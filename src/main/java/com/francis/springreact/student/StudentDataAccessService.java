package com.francis.springreact.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class StudentDataAccessService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public StudentDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    List<Student> selectAllStudents(){
        String sql = ""+
                "SELECT id, " +
                "first_name," +
                "last_name," +
                "email," +
                "gender " +
                "FROM student";
        return jdbcTemplate.query(sql, mapStudentFromDb());
    }

    List<StudentCourse> selectStudentCourses(UUID studentId){
        String sql = ""+
                "SELECT * FROM student " +
                "JOIN student_course sc " +
                "on student.id = sc.student_id " +
                "JOIN course c " +
                "on c.course_id = sc.course_id " +
                "WHERE student_id = ?";

        return jdbcTemplate.query(sql, new Object[]{studentId}, mapStudentCourseFromDb());
    }

    void insertStudent(UUID id, Student student) {
        String sql = ""+
                "INSERT INTO student (" +
                "id, " +
                "first_name, " +
                "last_name, " +
                "email, " +
                "gender) " +
                "VALUES (?, ?, ?, ?, ?::gender)";
        jdbcTemplate.update(
                sql,
                id,
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                student.getGender().name().toUpperCase()
        );
    }

    private static RowMapper<Student> mapStudentFromDb() {
        return (resultSet, i) -> {
            String idStr = resultSet.getString("id");
            UUID id = UUID.fromString(idStr);
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String email = resultSet.getString("email");
            String genderStr = resultSet.getString("gender").toUpperCase();
            Student.Gender gender = Student.Gender.valueOf(genderStr);
            return new Student(id, firstName, lastName, email, gender);
        };
    }

    private static RowMapper<StudentCourse> mapStudentCourseFromDb() {
        return (resultSet, i) -> {
            UUID id = UUID.fromString(resultSet.getString("id"));
            UUID courseId = UUID.fromString(resultSet.getString("course_id"));
            String name = resultSet.getString("name");
            String description = resultSet.getString("description");
            System.out.println(description);
            String department = resultSet.getString("department");
            String teacherName = resultSet.getString("teacher_name");
            LocalDate startDate = resultSet.getDate("start_date").toLocalDate();
            LocalDate endDate = resultSet.getDate("end_date").toLocalDate();
            Integer grade = Optional.ofNullable(resultSet.getString("grade"))
                    .map(Integer::parseInt)
                    .orElse(null);

            return new StudentCourse(
                    id,
                    courseId,
                    name,
                    description,
                    department,
                    teacherName,
                    startDate,
                    endDate,
                    grade);
        };
    }


    @SuppressWarnings("ConstantConditions")
    boolean isEmailTaken(String email) {
        String sql = ""+
                "SELECT EXISTS ( " +
                "SELECT 1 " +
                "FROM student " +
                "WHERE email = ?" +
                ")";
        return jdbcTemplate.queryForObject(
                sql,
                new Object[] {email},
                (resultSet, i) -> resultSet.getBoolean(1)
        );
    }
}
