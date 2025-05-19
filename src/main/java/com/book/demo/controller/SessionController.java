package com.book.demo.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/api")
@CrossOrigin("http://localhost:3000/")
public class SessionController
{
    @GetMapping("/username")
    public ResponseEntity<String> getLoggedInUsername(Principal principal, HttpSession session) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
        }
        System.out.println("Session ID: " + session.getId());
        return ResponseEntity.ok(principal.getName());
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Invalidate the current session
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // Clear Spring Security context
        SecurityContextHolder.clearContext();

        // Remove session cookie
        Cookie logoutCookie = new Cookie("JSESSIONID", "");
        logoutCookie.setMaxAge(0);
        logoutCookie.setPath("/");
        response.addCookie(logoutCookie);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.getWriter().write("{\"message\": \"Logged out successfully\"}");

        return ResponseEntity.ok().build();
    }



}
