package com.book.demo.controller;

import com.book.demo.dto.AppointmentDto;
import com.book.demo.entity.AppointmentEntity;
import com.book.demo.entity.TeacherEntity;
import com.book.demo.repository.AppointmentRepository;
import com.book.demo.service.AppointmentService;
import com.book.demo.service.StudentService;
import com.book.demo.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/student")
@CrossOrigin("http://localhost:3000/")
public class StudentController
{
    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("/teacher/searchByName")
    public List<TeacherEntity> searchByTName(@RequestParam String name)
    {
        return teacherService.searchByName(name);
    }

    @GetMapping("/teacher/searchByDepartment")
    public List<TeacherEntity>searchByTDepartment(@RequestParam String department)
    {
        return teacherService.searchByDepartment(department);
    }

    @GetMapping("/teacher/searchBySubject")
    public List<TeacherEntity>searchBuTSubject(@RequestParam String subject)
    {
        return teacherService.searchBySubject(subject);
    }

    @GetMapping("/teacher/all")
    public ResponseEntity<List<TeacherEntity>>getAllTeacherAvailable()
    {
        List<TeacherEntity> teacher=teacherService.getAllAvailableTeacher();
        return ResponseEntity.ok(teacher);
    }

    @PostMapping("/bookAppointment")
    public ResponseEntity<?> createAppointment(@RequestBody Map<String, Object> request) {
        try {
            // Fetch values directly as String
            String teacherUsername = (String) request.get("username");
            String studentUsername = (String) request.get("studentusername");
            String message = (String) request.get("message");

            if (teacherUsername == null || studentUsername == null || message == null) {
                return new ResponseEntity<>("Missing required fields", HttpStatus.BAD_REQUEST);
            }

            String result = appointmentService.validateAndCreateAppointment(studentUsername, teacherUsername, "pending", message);

            if (result.equals("success")) {
                return new ResponseEntity<>("Appointment successfully created", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Invalid Request Format", HttpStatus.BAD_REQUEST);
        }
    }


    @Autowired
    private AppointmentRepository appointmentRepository;

    @GetMapping("/appointments")
    public List<AppointmentDto> getAppointments(@RequestParam(required = false) String studentUsername) {
        List<AppointmentEntity> appointments;

        // If teacherUsername is provided, filter appointments by teacher
        if (studentUsername != null && !studentUsername.isEmpty()) {
            appointments = appointmentRepository.findByUsername_Username(studentUsername);
        } else {
            appointments = appointmentRepository.findAll();
        }

        return appointments.stream().map(app -> {
            AppointmentDto dto = new AppointmentDto();
            dto.setId(app.getApid().toHexString());
            dto.setDate(app.getDate());
            dto.setStartTime(app.getStartTime());
            dto.setEndTime(app.getEndTime());
            dto.setMessage(app.getMessage());
            dto.setStatus(app.getStatus());

            if (app.getUsername() != null) {
                dto.setTeacherUsername(app.getUsername().getUsername());
                dto.setTeacherName(app.getUsername().getName());
            }

            if (app.getStudentusername() != null) {
                dto.setStudentUsername(app.getStudentusername().getUsername());
                dto.setStudentName(app.getStudentusername().getName());
            }

            return dto;
        }).collect(Collectors.toList());
    }





}
