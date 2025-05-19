package com.book.demo.repository;

import com.book.demo.entity.AdminEntity;
import com.book.demo.entity.TeacherEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRespository extends MongoRepository<TeacherEntity, ObjectId>
{
    TeacherEntity findByUsername(String username);

    Optional<TeacherEntity> findByMail(String email);
    List<TeacherEntity>findAll();
    Optional<TeacherEntity>findByPhone(Long phone);
    List<TeacherEntity>findByAvailableTrue();
    List<TeacherEntity> findByNameContainingIgnoreCaseAndAvailableTrue(String name);
    List<TeacherEntity>findByDepartmentContainingIgnoreCaseAndAvailableTrue(String department);

    @Query("{'subjects':{$in:[?0]},'available':true}")
    List<TeacherEntity>findBySubjectContainingIgnoreCaseAndAvailableTrue(String subject);
}
