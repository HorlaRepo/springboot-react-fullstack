package com.francis.springreact.student;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.lang.NonNullFields;

import java.util.UUID;

@AllArgsConstructor
@Data
public class Student {

    private final UUID id;
    @NotBlank
    private final String firstName;
    @NotBlank
    private final String lastName;
    @Email
    private final String email;
    @NotNull
    private Gender gender;

    enum Gender{
        MALE,FEMALE
    }

}
