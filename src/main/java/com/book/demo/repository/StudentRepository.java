package com.book.demo.repository;

import com.book.demo.entity.AdminEntity;
import com.book.demo.entity.StudentEntity;
import com.book.demo.entity.TeacherEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends MongoRepository<StudentEntity, ObjectId>
{
    StudentEntity findByUsername(String username);
    Optional<StudentEntity> findByMail(String email);

    List<StudentEntity>findAll();

    Optional<StudentEntity>findByPhone(Long phone);

    List<StudentEntity> findByNameContainingIgnoreCase(String name);
    List<StudentEntity>findByDepartmentContainingIgnoreCase(String department);
    List<StudentEntity>findByCourseContainingIgnoreCase(String course);

    @Query("{'subjects':{$in:[?0]}}")
    List<StudentEntity>findBySubjectContainingIgnoreCase(String subject);
}
