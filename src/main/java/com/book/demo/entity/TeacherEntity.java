package com.book.demo.entity;

import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection="teacher")
public class TeacherEntity
{
    @Id
    private ObjectId tid;
    @Indexed(unique = true)
    @NonNull
    private String username;
    @NonNull

    private String name;
    @NonNull
    private String mail;
    @NonNull
    private Long phone;
    @NonNull
    private String password;
    @NonNull
    private String department;
    @NonNull
    private String[]subjects;
    @NonNull
    private boolean available;

    private String otp;
    private boolean emailVerified=false;
    private LocalDateTime otpExpiredAt;

    public ObjectId getTid() {
        return tid;
    }

    public void setTid(ObjectId tid) {
        this.tid = tid;
    }

    public @NonNull String getUsername() {
        return username;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String[] getSubjects() {
        return subjects;
    }

    public void setSubjects(String[] subjects) {
        this.subjects = subjects;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public LocalDateTime getOtpExpiredAt() {
        return otpExpiredAt;
    }

    public void setOtpExpiredAt(LocalDateTime otpExpiredAt) {
        this.otpExpiredAt = otpExpiredAt;
    }

    public TeacherEntity(@NonNull String username, @NonNull String name, @NonNull String mail, @NonNull Long phone, @NonNull String password, @NonNull String department, @NonNull String[] subjects, @NonNull boolean available) {
        this.username = username;
        this.name = name;
        this.mail = mail;
        this.phone = phone;
        this.password = password;
        this.department = department;
        this.subjects = subjects;
        this.available = available;
    }
}
