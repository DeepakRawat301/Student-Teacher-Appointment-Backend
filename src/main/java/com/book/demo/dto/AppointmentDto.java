package com.book.demo.dto;

import java.time.LocalDateTime;

public class AppointmentDto
{
    private String id;
    private String status;
    private String message;
    private LocalDateTime date;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private String studentUsername;
    private String studentName;

    private String teacherUsername;
    private String teacherName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getStudentUsername() {
        return studentUsername;
    }

    public void setStudentUsername(String studentUsername) {
        this.studentUsername = studentUsername;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getTeacherUsername() {
        return teacherUsername;
    }

    public void setTeacherUsername(String teacherUsername) {
        this.teacherUsername = teacherUsername;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public AppointmentDto() {
        // no-argument constructor
    }

    public AppointmentDto(String id, String teacherUsername, String studentUsername, String teacherName, String studentName,
                          LocalDateTime date, LocalDateTime startTime, LocalDateTime endTime,
                          String message, String status) {
        this.id = id;
        this.teacherUsername = teacherUsername;
        this.studentUsername = studentUsername;
        this.teacherName = teacherName;
        this.studentName = studentName;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.message = message;
        this.status = status;
    }


}
