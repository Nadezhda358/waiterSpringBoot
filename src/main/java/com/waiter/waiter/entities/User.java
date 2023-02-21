package com.waiter.waiter.entities;

import com.waiter.waiter.enums.Gender;
import com.waiter.waiter.enums.Role;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Size(min=2, max=30)
    private String firstName;
    @Size(min=2, max=30)
    private String secondName;
    @Size(min=2, max=30)
    private String lastName;
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate dateOfBirth;
    @Size(min=3, max=13)
    private String phoneNumber;
    @Email
    @Column(unique = true)
    private String email;
    @Size(min=5, max=15)
    private String password;
    private Role role;
    private Gender gender;
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate hireDate;
    @Size(max=500)
    private String description;
}
