package com.example.books.models;

public class SignUp {

    private String firstname;

    private String lastname;

    private String username;

    private String password;

    private String confirmPassword;

    private String email;

    private String info;

    public SignUp(String firstname, String lastname, String username, String password, String confirmPassword) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public SignUp(String firstname, String lastname, String username, String password, String confirmPassword, String email, String info) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.email = email;
        this.info = info;
    }

    public SignUp(String firstname, String lastname, String username, String password, String confirmPassword, String email) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.email = email;
    }

    public SignUp() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
