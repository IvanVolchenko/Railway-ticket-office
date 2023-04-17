package com.example.epam.finalProject.Railwayticketoffice.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "username", unique = true)
//    @NotEmpty(message = "Username should not be empty")
    private String username;
    @Column(name = "first_name")
    @NotBlank(message = "Name should not be empty")
    @Size(min = 2, max = 20, message = "Name should not be between 2 and 20 characters" )
    private String firstName;
    @Column(name = "last_name")
    @NotBlank(message = "Last name should not be empty")
    @Size(min = 2, max = 20, message = "Last name should not be between 2 and 20 characters" )
    private String lastName;
    @Column(name = "document_number", unique = true)
    @NotBlank(message = "Document number should not be empty")
    @Size(min = 5, max = 20, message = "Document number should not be between 2 and 20 characters" )
    private String documentNumber;
    @Column(name = "email_address", unique = true)
    @NotBlank(message = "Email should not be empty")
    @Email(message = "Email should be valid")
    private String emailAddress;
    @Column(name = "password")
    @NotBlank(message = "Password should not be empty")
    @Size(min = 8, max = 60, message = "Password should not be between 2 and 60 characters" )
    private String password;
    @Column(name = "authorities", nullable = false)
//    @NotEmpty
    private String authorities;

    public User() {
    }

    public User(String firstName, String lastName, String documentNumber, String emailAddress, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.documentNumber = documentNumber;
        this.emailAddress = emailAddress;
        this.password = password;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthorities() {
        return authorities;
    }

    public void setAuthorities(String authorities) {
        this.authorities = authorities;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", documentNumber='" + documentNumber + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", password='" + password + '\'' +
                ", authorities='" + authorities + '\'' +
                '}';
    }
}
