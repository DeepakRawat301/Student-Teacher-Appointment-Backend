package com.book.demo.service;

import com.book.demo.dto.TeacherRegisterDto;
import com.book.demo.dto.TeacherVerifyDto;
import com.book.demo.entity.TeacherEntity;
import com.book.demo.repository.TeacherRespository;
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
public class TeacherService
{
    @Autowired
    private EmailService emailService;
    @Autowired
    private TeacherRespository teacherRespository;

    private final static PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();

    public TeacherEntity signup(TeacherRegisterDto input)
    {
        String encodedPassword=passwordEncoder.encode(input.getPassword());
        TeacherEntity teacherEntity=new TeacherEntity(input.getUsername(),input.getName(),input.getMail(), input.getPhone(), encodedPassword, input.getDepartment(),input.getSubjects(),input.isAvailable());
        teacherEntity.setOtp(generateOtp());
        teacherEntity.setOtpExpiredAt(LocalDateTime.now().plusMinutes(15));
        teacherEntity.setEmailVerified(false);

        teacherRespository.save(teacherEntity);
        sendVerificationEmail(teacherEntity);

        return teacherEntity;
    }

    public void verifyTeacher(TeacherVerifyDto input)
    {
        Optional<TeacherEntity> optionalTeacher=teacherRespository.findByPhone(input.getPhone());
        if(optionalTeacher.isPresent())
        {
            TeacherEntity teacherEntity=optionalTeacher.get();
            if(teacherEntity.getOtpExpiredAt().isBefore(LocalDateTime.now()))
            {
                throw new RuntimeException("OTP has expired");
            }
            if(teacherEntity.getOtp().equals(input.getOtp()))
            {
                teacherEntity.setEmailVerified(true);
                teacherEntity.setOtp(null);
                teacherEntity.setOtpExpiredAt(null);
                teacherRespository.save(teacherEntity);
            }
            else
            {
                throw new RuntimeException("Invalid OTP");
            }
        }
        else
        {
            throw new RuntimeException("Teacher not found");
        }
    }

    public void resendOtp(String email)
    {
        Optional<TeacherEntity>optionalTeacher=teacherRespository.findByMail(email);
        if(optionalTeacher.isPresent())
        {
            TeacherEntity teacherEntity=optionalTeacher.get();
            if(teacherEntity.isEmailVerified())
            {
                throw new RuntimeException("Teacher is already verified");
            }
            teacherEntity.setOtp(generateOtp());
            teacherEntity.setOtpExpiredAt(LocalDateTime.now().plusMinutes(5));
            sendVerificationEmail(teacherEntity);
            teacherRespository.save(teacherEntity);
        }
        else
        {
            throw new RuntimeException("Teacher not found");
        }
    }

    public void sendVerificationEmail(TeacherEntity teacherEntity)
    {
        String subject = "Account Verification";
        String verificationCode = "VERIFICATION CODE " + teacherEntity.getOtp();
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
            emailService.sendVerificationEmail(teacherEntity.getMail(), subject, htmlMessage);
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

    public List<TeacherEntity> searchByName(String name)
    {
        return teacherRespository.findByNameContainingIgnoreCaseAndAvailableTrue(name);
    }

    public List<TeacherEntity>searchByDepartment(String department)
    {
        return teacherRespository.findByDepartmentContainingIgnoreCaseAndAvailableTrue(department);
    }

    public List<TeacherEntity>searchBySubject(String subject)
    {
        return teacherRespository.findBySubjectContainingIgnoreCaseAndAvailableTrue(subject);
    }

    public boolean updateAvailability(String username,boolean available)
    {
        TeacherEntity teacher=teacherRespository.findByUsername(username);
        if(teacher!=null&&teacher.isEmailVerified())
        {
            teacher.setAvailable(available);
            teacherRespository.save(teacher);
            return true;
        }
        return false;
    }

    public List<TeacherEntity>getAllTeacher()
    {
        return teacherRespository.findAll();
    }

    public List<TeacherEntity>getAllAvailableTeacher()
    {
        return teacherRespository.findByAvailableTrue();
    }

    public boolean updateTeacherDetails(String username,TeacherEntity updatedTeacher)
    {
        TeacherEntity existingTeacher=teacherRespository.findByUsername(username);
        if(existingTeacher!=null&&existingTeacher.isEmailVerified())
        {
            existingTeacher.setName(updatedTeacher.getName());
            existingTeacher.setMail(updatedTeacher.getMail());
            existingTeacher.setPhone(updatedTeacher.getPhone());
            existingTeacher.setPassword(updatedTeacher.getPassword());
            existingTeacher.setDepartment(updatedTeacher.getDepartment());
            existingTeacher.setSubjects(updatedTeacher.getSubjects());

            existingTeacher.setAvailable(updatedTeacher.isAvailable());
            teacherRespository.save(existingTeacher);
            return true;
        }
        return false;
    }


    public boolean deleteTeacherByUsername(String username)
    {
        TeacherEntity teacher=teacherRespository.findByUsername(username);
        if(teacher!=null)
        {
            teacherRespository.delete(teacher);
            return true;
        }
        return false;
    }

    public TeacherEntity findByUsername(String username)
    {
        return teacherRespository.findByUsername(username);
    }


}
