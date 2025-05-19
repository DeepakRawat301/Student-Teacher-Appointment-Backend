package com.book.demo.service;

import com.book.demo.entity.AdminEntity;
import com.book.demo.entity.StudentEntity;
import com.book.demo.entity.TeacherEntity;
import com.book.demo.repository.AdminRepository;
import com.book.demo.repository.StudentRepository;
import com.book.demo.repository.TeacherRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDetailServiceImpl implements UserDetailsService
{
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private TeacherRespository teacherRespository;
    @Autowired
    private StudentRepository studentRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        AdminEntity admin=adminRepository.findByUsername(username);
        if(admin!=null && admin.isEmailVerified())
        {
            return new User(admin.getUsername(), admin.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_Admin")));
        }

        TeacherEntity teacher=teacherRespository.findByUsername(username);
        if(teacher!=null && teacher.isEmailVerified())
        {
            return new User(teacher.getUsername(), teacher.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_Teacher")));
        }

        StudentEntity student=studentRepository.findByUsername(username);
        if(student!=null && student.isEmailVerified())
        {
            return new User(student.getUsername(), student.getPassword(),List.of(new SimpleGrantedAuthority("ROLE_Student")));
        }
        throw new UsernameNotFoundException("User not found");
    }
}
