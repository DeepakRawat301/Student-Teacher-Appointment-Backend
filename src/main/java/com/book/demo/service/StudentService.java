package com.book.demo.service;

import com.book.demo.dto.StudentRegisterDto;
import com.book.demo.dto.StudentVerifyDto;
import com.book.demo.entity.StudentEntity;
import com.book.demo.repository.StudentRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class StudentService
{
    @Autowired
    private EmailService emailService;
    @Autowired
    private StudentRepository studentRepository;

    private final static PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();

    public StudentEntity signup(StudentRegisterDto input)
    {
        String encodedPassword=passwordEncoder.encode(input.getPassword());
        StudentEntity studentEntity=new StudentEntity(input.getUsername(),input.getName(),input.getMail(), input.getPhone(), encodedPassword, input.getDepartment(), input.getCourse(),input.getSubjects());
        studentEntity.setOtp(generateOtp());
        studentEntity.setOtpExpiredAt(LocalDateTime.now().plusMinutes(15));
        studentEntity.setEmailVerified(false);

        studentRepository.save(studentEntity);
        sendVerificationEmail(studentEntity);

        return studentEntity;
    }

    public void verifyStudent(StudentVerifyDto input)
    {
        Optional<StudentEntity> optionalStudent=studentRepository.findByPhone(input.getPhone());
        if(optionalStudent.isPresent())
        {
            StudentEntity studentEntity=optionalStudent.get();
            if(studentEntity.getOtpExpiredAt().isBefore(LocalDateTime.now()))
            {
                throw new RuntimeException("OTP has expired");
            }
            if(studentEntity.getOtp().equals(input.getOtp()))
            {
                studentEntity.setEmailVerified(true);
                studentEntity.setOtp(null);
                studentEntity.setOtpExpiredAt(null);
                studentRepository.save(studentEntity);
            }
            else
            {
                throw new RuntimeException("Invalid OTP");
            }
        }
        else
        {
            throw new RuntimeException("Student not found");
        }
    }

    public void resendOtp(String email)
    {
        Optional<StudentEntity>optionalStudent=studentRepository.findByMail(email);
        if(optionalStudent.isPresent())
        {
            StudentEntity studentEntity=optionalStudent.get();
            if(studentEntity.isEmailVerified())
            {
                throw new RuntimeException("Student is already verified");
            }
            studentEntity.setOtp(generateOtp());
            studentEntity.setOtpExpiredAt(LocalDateTime.now().plusMinutes(5));
            sendVerificationEmail(studentEntity);
            studentRepository.save(studentEntity);
        }
        else
        {
            throw new RuntimeException("Student not found");
        }
    }

    public void sendVerificationEmail(StudentEntity studentEntity)
    {
        String subject = "Account Verification";
        String verificationCode = "VERIFICATION CODE " + studentEntity.getOtp();
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to our app!</h2>"
                + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        try {
            emailService.sendVerificationEmail(studentEntity.getMail(), subject, htmlMessage);
        } catch (MessagingException e) {
            // Handle email sending exception
            e.printStackTrace();
        }
    }

    private String generateOtp()
    {
        Random random=new Random();
        int code= random.nextInt(900000)+100000;
        return String.valueOf(code);
    }


    public List<StudentEntity> searchByName(String name)
    {
        return studentRepository.findByNameContainingIgnoreCase(name);
    }

    public List<StudentEntity>searchByDepartment(String department)
    {
        return studentRepository.findByDepartmentContainingIgnoreCase(department);
    }

    public List<StudentEntity>searchByCourse(String course)
    {
        return studentRepository.findByCourseContainingIgnoreCase(course);
    }

    public List<StudentEntity>searchBySubject(String subject)
    {
        return studentRepository.findBySubjectContainingIgnoreCase(subject);
    }

    public List<StudentEntity>getAllStudent()
    {
        return studentRepository.findAll();
    }

    public boolean updateStudentDetails(String username,StudentEntity updatedStudent)
    {
        StudentEntity existingStudent=studentRepository.findByUsername(username);
        if(existingStudent!=null&&existingStudent.isEmailVerified())
        {
            existingStudent.setName(updatedStudent.getName());
            existingStudent.setMail(updatedStudent.getMail());
            existingStudent.setPhone(updatedStudent.getPhone());
            existingStudent.setPassword(updatedStudent.getPassword());
            existingStudent.setDepartment(updatedStudent.getDepartment());
            existingStudent.setSubjects(updatedStudent.getSubjects());

            studentRepository.save(existingStudent);
            return true;
        }
        return false;
    }

    public boolean deleteStudentByUsername(String username)
    {
        StudentEntity student=studentRepository.findByUsername(username);
        if(student!=null)
        {
            studentRepository.delete(student);
            return true;
        }
        return false;
    }

    public StudentEntity findByUsername(String username)
    {
        return studentRepository.findByUsername(username);
    }



}
