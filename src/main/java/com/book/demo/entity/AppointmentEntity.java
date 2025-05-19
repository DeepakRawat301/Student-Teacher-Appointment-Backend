package com.book.demo.entity;

import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "appointment")
public class AppointmentEntity
{
    @Id
    private ObjectId apid;
    @NonNull
    private LocalDateTime date;
    @NonNull
    private LocalDateTime startTime;
    @NonNull
    private LocalDateTime endTime;
    @NonNull
    private String status;

    private String message;

    @DBRef(lazy = false)
    private TeacherEntity username;
    @DBRef(lazy = false)
    private StudentEntity studentusername;

    public ObjectId getApid() {
        return apid;
    }

    public void setApid(ObjectId apid) {
        this.apid = apid;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TeacherEntity getUsername() {
        return username;
    }

    public void setUsername(TeacherEntity username) {
        this.username = username;
    }

    public StudentEntity getStudentusername() {
        return studentusername;
    }

    public void setStudentusername(StudentEntity studentusername) {
        this.studentusername = studentusername;
    }
}
