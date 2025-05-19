package com.book.demo.controller;

import com.book.demo.dto.StudentRegisterDto;
import com.book.demo.dto.StudentVerifyDto;
import com.book.demo.dto.TeacherRegisterDto;
import com.book.demo.dto.TeacherVerifyDto;
import com.book.demo.entity.AdminEntity;
import com.book.demo.entity.AppointmentEntity;
import com.book.demo.entity.StudentEntity;
import com.book.demo.entity.TeacherEntity;
import com.book.demo.service.AdminService;
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

@RestController
@RequestMapping("/user")
@CrossOrigin("http://localhost:3000/")
public class AdminController
{
    @Autowired
    private AdminService adminService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private AppointmentService appointmentService;


    // ALl request related to Admin

    @GetMapping("/admin/searchByUsername")
    public ResponseEntity<?> searchByUserName(@RequestParam String username) {
        AdminEntity admin = adminService.findByUsername(username);
        if (admin != null) {
            return ResponseEntity.ok(admin);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found.");
    }


    @PutMapping("/admin/update")
    public ResponseEntity<?> updateAdmin(@RequestBody AdminEntity updatedAdmin) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        AdminEntity userDB=adminService.findByUsername(username);
        if(userDB!=null)
        {
            boolean success = adminService.updateAdminDetails(username, updatedAdmin);
            if (success) {
                return ResponseEntity.ok("Admin details updated successfully.");
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Admin not found or email not verified.");
    }

    @DeleteMapping("/admin/delete")
    public ResponseEntity<String> deleteAdmin() {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        boolean deleted = adminService.deleteAdminByUsername(authentication.getName());
        if (deleted) {
            return ResponseEntity.ok("Admin deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found.");
        }
    }

    //All request related to Teacher

    @PostMapping("/teacher/signup")
    public ResponseEntity<TeacherEntity> register(@RequestBody TeacherRegisterDto teacherRegisterDto)
    {
        TeacherEntity teacherEntity = teacherService.signup(teacherRegisterDto);
        return ResponseEntity.ok(teacherEntity);
    }

    @PostMapping("/teacher/verify")
    public ResponseEntity<?>verifyTeacher(@RequestBody TeacherVerifyDto teacherVerifyDto)
    {
        try
        {
            teacherService.verifyTeacher(teacherVerifyDto);
            return ResponseEntity.ok("Teacher verified successfully");
        }
        catch(RuntimeException e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/teacher/resend")
    public ResponseEntity<?>resendOtTeacher(@RequestParam String email)
    {
        try{
            teacherService.resendOtp(email);
            return ResponseEntity.ok("Verification code sent");
        }
        catch(RuntimeException e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

        @GetMapping("/teacher/all")
        public ResponseEntity<?> getAllTeacher() {
            List<TeacherEntity> teachers = teacherService.getAllTeacher();
            return ResponseEntity.ok(teachers);
        }

//    @DeleteMapping("/teacher/delete")
//    public ResponseEntity<?> deleteTeacher() {
//        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
//        boolean deleted=teacherService.deleteTeacherByUsername(authentication.getName());
//        if(deleted)
//        {
//            return ResponseEntity.ok("Teacher deleted successfully.");
//        }
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Teacher not found.");
//    }

    @DeleteMapping("/teacher/delete/{username}")
    public ResponseEntity<?> deleteTeacher(@PathVariable String username) {
        TeacherEntity userDB=teacherService.findByUsername(username);
        if(userDB!=null)
        {
            boolean deleted=teacherService.deleteTeacherByUsername(username);
            if(deleted)
            {
                return ResponseEntity.ok("Teacher deleted successfully.");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Teacher not found.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Teacher not found or email not verified.");

    }

//    @PutMapping("/teacher/update")
//    public ResponseEntity<?> updateTeacher(@RequestBody TeacherEntity updatedTeacher) {
//        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
//        String username=authentication.getName();
//        TeacherEntity userDB=teacherService.findByUsername(username);
//        if(userDB!=null)
//        {
//            boolean success = teacherService.updateTeacherDetails(username, updatedTeacher);
//            if (success) {
//                return ResponseEntity.ok("Teacher details updated successfully.");
//            }
//        }
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Teacher not found or email not verified.");
//    }

    @PutMapping("/teacher/update/{username}")
    public ResponseEntity<?> updateTeacher(@PathVariable String username, @RequestBody TeacherEntity updatedTeacher) {
        TeacherEntity userDB=teacherService.findByUsername(username);
        if(userDB!=null)
        {
            boolean success = teacherService.updateTeacherDetails(username, updatedTeacher);
            if (success) {
                return ResponseEntity.ok("Teacher details updated successfully.");
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Teacher not found or email not verified.");
    }

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

    @GetMapping("/teacher/searchByUsername")
    public ResponseEntity<?> searchByTUsername(@RequestParam String username) {
        TeacherEntity teacher = teacherService.findByUsername(username);
        if (teacher != null) {
            return ResponseEntity.ok(teacher);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Teacher not found.");
    }

    @GetMapping("/teacher/searchBySubject")
    public List<TeacherEntity>searchBuTSubject(@RequestParam String subject)
    {
        return teacherService.searchBySubject(subject);
    }


    //All request related to Student

    @PostMapping("/student/signup")
    public ResponseEntity<StudentEntity> register(@RequestBody StudentRegisterDto studentRegisterDto)
    {
        StudentEntity studentEntity = studentService.signup(studentRegisterDto);
        return ResponseEntity.ok(studentEntity);
    }

    @PostMapping("/student/verify")
    public ResponseEntity<?>verifyStudent(@RequestBody StudentVerifyDto studentVerifyDto)
    {
        try
        {
            studentService.verifyStudent(studentVerifyDto);
            return ResponseEntity.ok("Student verified successfully");
        }
        catch(RuntimeException e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/student/resend")
    public ResponseEntity<?>resendOtpStudent(@RequestParam String email)
    {
        try{
            studentService.resendOtp(email);
            return ResponseEntity.ok("Verification code sent");
        }
        catch(RuntimeException e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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

//    @PutMapping("/student/update")
//    public ResponseEntity<?> updateStudent(@RequestBody StudentEntity updatedStudent) {
//        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
//        String username=authentication.getName();
//        StudentEntity userDB=studentService.findByUsername(username);
//        if(userDB!=null)
//        {
//            boolean success = studentService.updateStudentDetails(username, updatedStudent);
//            if (success) {
//                return ResponseEntity.ok("Student details updated successfully.");
//            }
//        }
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Student not found or email not verified.");
//    }

    @PutMapping("/student/update/{username}")
    public ResponseEntity<?> updateStudent(@PathVariable String username, @RequestBody StudentEntity updatedStudent) {
        StudentEntity userDB=studentService.findByUsername(username);
        if(userDB!=null)
        {
            boolean success = studentService.updateStudentDetails(username, updatedStudent);
            if (success) {
                return ResponseEntity.ok("Student details updated successfully.");
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Student not found or email not verified.");
    }

//    @DeleteMapping("/student/delete")
//    public ResponseEntity<String> deleteStudent() {
//        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
//        boolean deleted = studentService.deleteStudentByUsername(authentication.getName());
//        if (deleted) {
//            return ResponseEntity.ok("Student deleted successfully.");
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found.");
//        }
//    }

    @DeleteMapping("/student/delete/{username}")
    public ResponseEntity<?> deleteStudent(@PathVariable String username) {
        StudentEntity userDB=studentService.findByUsername(username);
        if(userDB!=null)
        {
            boolean deleted=studentService.deleteStudentByUsername(username);
            if(deleted)
            {
                return ResponseEntity.ok("Student deleted successfully.");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Student not found or email not verified.");

    }

    @GetMapping("/student/all")
    public ResponseEntity<?> getAllStudent() {
        List<StudentEntity> student = studentService.getAllStudent();
        return ResponseEntity.ok(student);
    }

    @GetMapping("/student/searchByUsername")
    public ResponseEntity<?> searchBySUsername(@RequestParam String username) {
        StudentEntity student = studentService.findByUsername(username);
        if (student != null) {
            return ResponseEntity.ok(student);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("student not found.");
    }


    //Request related to Appointments
    @GetMapping("/appointment/all")
    public ResponseEntity<?> getAllAppointment() {
        List<AppointmentEntity> appointment = appointmentService.getAllAppointment();
        return ResponseEntity.ok(appointment);
    }



}
