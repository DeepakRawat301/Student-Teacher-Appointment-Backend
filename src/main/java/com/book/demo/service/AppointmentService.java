package com.book.demo.service;

import com.book.demo.dto.AppointmentDto;
import com.book.demo.entity.AppointmentEntity;
import com.book.demo.entity.StudentEntity;
import com.book.demo.entity.TeacherEntity;
import com.book.demo.repository.AppointmentRepository;
import com.book.demo.repository.StudentRepository;
import com.book.demo.repository.TeacherRespository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService
{
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRespository teacherRespository;

    public void saveAppointment(AppointmentEntity appointmentEntity)
    {
        AppointmentEntity save=appointmentRepository.save(appointmentEntity);
    }

    public String validateAndCreateAppointment(String studentUsername,String teacherUsername,String status,String message)
    {
        //Extract teacher & student username
        StudentEntity student = studentRepository.findByUsername(studentUsername);
        TeacherEntity teacher = teacherRespository.findByUsername(teacherUsername);

        //Fetch teacher & student from database
        if (student == null || teacher == null) {
            return "Invalid Teacher or Student";
        }
        //Validation of department match
        if (!student.getDepartment().equalsIgnoreCase(teacher.getDepartment())) {
            return "Appointment Denied: Department and course do not match.";
        }
        //Validation of course match
        boolean subjectMatch = Arrays.stream(student.getSubjects())
                .anyMatch(sub -> Arrays.asList(teacher.getSubjects()).contains(sub));
        if (!subjectMatch) {
            return "Appointment Denied: No common subjects between teacher and student.";
        }
        //Time slot
        LocalDateTime startTime = LocalDateTime.now().plusMinutes(10);
        LocalDateTime endTime = LocalDateTime.now().plusMinutes(40);
        // Appointment clash check for student
        boolean hasClash = appointmentRepository
                .findByStudentusernameAndStartTimeLessThanAndEndTimeGreaterThan(student, endTime, startTime)
                .size() > 0;
        if (hasClash) {
            return "Appointment Denied: Student already has an appointment during this time.";
        }
        //If all condition passed then create appointment
        AppointmentEntity appointment = new AppointmentEntity();
        appointment.setStudentusername(student);
        appointment.setUsername(teacher);
        appointment.setStatus(status);
        appointment.setMessage(message);
        appointment.setDate(LocalDateTime.now());
        appointment.setStartTime(startTime);
        appointment.setEndTime(endTime);
        appointmentRepository.save(appointment);

        return "success";
    }

    public AppointmentEntity updateAppointmentStatus(String id, String status) {
        Optional<AppointmentEntity> optionalAppointment = appointmentRepository.findById(id);
        if (optionalAppointment.isEmpty()) {
            throw new RuntimeException("Appointment not found");
        }
        AppointmentEntity appointment = optionalAppointment.get();
        appointment.setStatus(status.toLowerCase());
        return appointmentRepository.save(appointment);
    }


    public List<AppointmentEntity>getAllAppointment()
    {
        return appointmentRepository.findAll();
    }

    public List<AppointmentEntity> getAppointmentsForTeacher(String teacherUsername) {
        TeacherEntity teacher = teacherRespository.findByUsername(teacherUsername);
        if (teacher == null) {
            throw new RuntimeException("Teacher not found");
        }
        return appointmentRepository.findByusername(teacher);
    }






}
