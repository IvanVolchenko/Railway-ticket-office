package com.example.epam.finalProject.Railwayticketoffice.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class MyUser extends User {

    private long id;
    private String firstName;
    private String lastName;
    private String documentNumber;
    private String emailAddress;

    public MyUser(String username, String password, boolean enabled, boolean accountNonExpired,
                  boolean credentialsNonExpired, boolean accountNonLocked,
                  Collection<? extends GrantedAuthority> authorities,long id, String firstName, String lastName,
                  String documentNumber, String emailAddress) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id=id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.documentNumber = documentNumber;
        this.emailAddress = emailAddress;
    }

    public long getId() {
        return id;
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

    @Override
    public String toString() {
        return "MyUser{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", documentNumber='" + documentNumber + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                '}';
    }
}
