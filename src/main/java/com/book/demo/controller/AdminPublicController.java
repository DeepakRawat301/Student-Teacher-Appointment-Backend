package com.book.demo.controller;

import com.book.demo.dto.AdminRegisterDto;
import com.book.demo.dto.AdminVerifyDto;
import com.book.demo.entity.AdminEntity;
import com.book.demo.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/public")
@RestController
@CrossOrigin("http://localhost:3000/")
public class AdminPublicController
{
    @Autowired
    private AdminService adminService;


    @PostMapping("/signup")
    public ResponseEntity<AdminEntity> register(@RequestBody AdminRegisterDto adminRegisterDto)
    {
        AdminEntity adminEntity = adminService.signup(adminRegisterDto);
        return ResponseEntity.ok(adminEntity);
    }

    @PostMapping("/verify")
    public ResponseEntity<?>verifyAdmin(@RequestBody AdminVerifyDto adminVerifyDto)
    {
        try
        {
            adminService.verifyAdmin(adminVerifyDto);
            return ResponseEntity.ok("Admin verified successfully");
        }
        catch(RuntimeException e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/resend")
    public ResponseEntity<?>resendOtp(@RequestParam String email)
    {
        try{
            adminService.resendOtp(email);
            return ResponseEntity.ok("Verification code sent");
        }
        catch(RuntimeException e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
