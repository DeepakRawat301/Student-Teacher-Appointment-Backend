package com.book.demo.service;

import com.book.demo.dto.AdminRegisterDto;
import com.book.demo.dto.AdminVerifyDto;
import com.book.demo.entity.AdminEntity;
import com.book.demo.repository.AdminRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class AdminService
{
    @Autowired
    private EmailService emailService;
    @Autowired
    private AdminRepository adminRepository;

    private final static PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();


    public AdminEntity signup(AdminRegisterDto input)
    {
        String encodedPassword=passwordEncoder.encode(input.getPassword());
        AdminEntity adminEntity=new AdminEntity(input.getUsername(),input.getName(),input.getMail(), input.getPhone(), encodedPassword);
        adminEntity.setOtp(generateOtp());
        adminEntity.setOtpExpiredAt(LocalDateTime.now().plusMinutes(15));
        adminEntity.setEmailVerified(false);

        adminRepository.save(adminEntity);
        sendVerificationEmail(adminEntity);

        return adminEntity;
    }
    public void verifyAdmin(AdminVerifyDto input)
    {
        Optional<AdminEntity> optionalAdmin=adminRepository.findByPhone(input.getPhone());
        if(optionalAdmin.isPresent())
        {
            AdminEntity adminEntity=optionalAdmin.get();
            if(adminEntity.getOtpExpiredAt().isBefore(LocalDateTime.now()))
            {
                throw new RuntimeException("OTP has expired");
            }
            if(adminEntity.getOtp().equals(input.getOtp()))
            {
                adminEntity.setEmailVerified(true);
                adminEntity.setOtp(null);
                adminEntity.setOtpExpiredAt(null);
                adminRepository.save(adminEntity);
            }
            else
            {
                throw new RuntimeException("Invalid OTP");
            }
        }
        else
        {
            throw new RuntimeException("Admin not found");
        }
    }

    public void resendOtp(String email)
    {
        Optional<AdminEntity>optionalAdmin=adminRepository.findByMail(email);
        if(optionalAdmin.isPresent())
        {
            AdminEntity adminEntity=optionalAdmin.get();
            if(adminEntity.isEmailVerified())
            {
                throw new RuntimeException("Admin is already verified");
            }
            adminEntity.setOtp(generateOtp());
            adminEntity.setOtpExpiredAt(LocalDateTime.now().plusMinutes(5));
            sendVerificationEmail(adminEntity);
            adminRepository.save(adminEntity);
        }
        else
        {
            throw new RuntimeException("Admin not found");
        }
    }

    public void sendVerificationEmail(AdminEntity adminEntity)
    {
        String subject = "Account Verification";
        String verificationCode = "VERIFICATION CODE " + adminEntity.getOtp();
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
            emailService.sendVerificationEmail(adminEntity.getMail(), subject, htmlMessage);
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

    public boolean updateAdminDetails(String username, AdminEntity updatedAdmin)
    {
        AdminEntity existingAdmin=adminRepository.findByUsername(username);
        if(existingAdmin!=null&&existingAdmin.isEmailVerified())
        {
            existingAdmin.setName(updatedAdmin.getName());
            existingAdmin.setMail(updatedAdmin.getMail());
            existingAdmin.setPhone(updatedAdmin.getPhone());
            existingAdmin.setPassword(updatedAdmin.getPassword());

            adminRepository.save(existingAdmin);
            return true;
        }
        return false;
    }

    public boolean deleteAdminByUsername(String username)
    {
        AdminEntity admin=adminRepository.findByUsername(username);
        if(admin!=null)
        {
            adminRepository.delete(admin);
            return true;
        }
        return false;
    }

    public AdminEntity findByUsername(String username)
    {
        return adminRepository.findByUsername(username);
    }


}
