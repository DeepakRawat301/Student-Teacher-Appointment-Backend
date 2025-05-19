package com.book.demo.controller;

import com.book.demo.dto.AppointmentDto;
import com.book.demo.entity.AppointmentEntity;
import com.book.demo.entity.StudentEntity;
import com.book.demo.entity.TeacherEntity;
import com.book.demo.repository.AppointmentRepository;
import com.book.demo.service.AppointmentService;
import com.book.demo.service.StudentService;
import com.book.demo.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/teacher")
@CrossOrigin("http://localhost:3000/")
public class TeacherController
{
    @Autowired
    private TeacherService teacherService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private AppointmentService appointmentService;

    @PutMapping("/updateAvailability/{username}")
    public ResponseEntity<?> updateAvailability(@PathVariable String username,@RequestBody TeacherEntity teacherEntity, @RequestParam boolean available) {
        TeacherEntity userDB=teacherService.findByUsername(username);
        if(userDB!=null)
        {
            boolean updated = teacherService.updateAvailability(username, available);
            if (updated) {
                return ResponseEntity.ok("Availability updated successfully.");
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Teacher not found or not verified.");
    }


    @GetMapping("/student/searchByName")
    public List<StudentEntity> searchBySName(@RequestParam String name)
    {
        return studentService.searchByName(name);
    }

    @GetMapping("/student/searchByDepartment")
    public List<StudentEntity>searchBySDepartment(@RequestParam String department)
    {
        return studentService.searchByDepartment(department);
    }

    @GetMapping("/student/searchByCourse")
    public List<StudentEntity>searchBySCourse(@RequestParam String course)
    {
        return studentService.searchByCourse(course);
    }

    @GetMapping("/student/searchBySubject")
    public List<StudentEntity>searchBuSSubject(@RequestParam String subject)
    {
        return studentService.searchBySubject(subject);
    }

    @GetMapping("/student/all")
    public ResponseEntity<?> getAllStudent() {
        List<StudentEntity> student = studentService.getAllStudent();
        return ResponseEntity.ok(student);
    }

    @PutMapping("/actionOnAppointment")
    public ResponseEntity<?> updateStatus(@RequestBody Map<String, String> request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        TeacherEntity userDB = teacherService.findByUsername(username);

        if (userDB != null) {
            try {
                String appointmentId = request.get("appointmentId");
                String status = request.get("status");

                if (appointmentId == null || status == null) {
                    return ResponseEntity.badRequest().body("Missing appointmentId or status.");
                }

                if (!status.equalsIgnoreCase("approved") && !status.equalsIgnoreCase("declined")) {
                    return ResponseEntity.badRequest().body("Invalid status. Must be 'approved' or 'declined'.");
                }

                AppointmentEntity update = appointmentService.updateAppointmentStatus(appointmentId, status);
                return ResponseEntity.ok("Appointment status updated successfully");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Appointment not found");
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Teacher not found.");
    }

    @GetMapping("/teacher/appointments")
    public ResponseEntity<?>getAppointmentsForTeacher()
    {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        try {
            List<AppointmentEntity> appointments = appointmentService.getAppointmentsForTeacher(username);
            return ResponseEntity.ok(appointments);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Autowired
    private AppointmentRepository appointmentRepository;

    @GetMapping("/appointments")
    public List<AppointmentDto> getAppointments(@RequestParam(required = false) String teacherUsername) {
        List<AppointmentEntity> appointments;

        // If teacherUsername is provided, filter appointments by teacher
        if (teacherUsername != null && !teacherUsername.isEmpty()) {
            appointments = appointmentRepository.findByUsername_Username(teacherUsername);
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
