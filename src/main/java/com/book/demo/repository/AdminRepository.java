package com.book.demo.repository;

import com.book.demo.entity.AdminEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends MongoRepository<AdminEntity, ObjectId>
{
    Optional<AdminEntity>findByMail(String email);
    AdminEntity findByUsername(String username);
    Optional<AdminEntity>findByPhone(Long phone);
}
