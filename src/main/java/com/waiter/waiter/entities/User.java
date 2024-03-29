package com.waiter.waiter.entities;

import com.waiter.waiter.enums.Gender;
import com.waiter.waiter.enums.Role;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "INT(11) UNSIGNED")
    private Integer id;

    @NotNull
    @Size(min=5, max=30)
    @Column(length = 30, nullable = false)
    private String username;//register input done

    private String password;//register input done
    @Size(min=2, max=30)
    private String firstName;//register input done
    @Size(min=2, max=30)
    private String lastName;//register input done
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date dateOfBirth;//register input done
    @Size(min=4,max =100)
    private String address;
    @Size(min=3, max=13)
    private String phoneNumber;//register input done
    @Email
    private String email;//register input done
    @Enumerated(EnumType.STRING)
    private Gender gender;//register input done
    @Enumerated(EnumType.STRING)
    private Role role;//register input done
    private boolean enabled;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public User(){

    }
    public User(String username, String password){
        this.username = username;
        this.password = password;
        this.role = Role.WAITER;
    }
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", role=" + role +
                '}';
    }
}
