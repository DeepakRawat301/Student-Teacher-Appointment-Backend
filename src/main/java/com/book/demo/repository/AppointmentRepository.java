package com.book.demo.repository;

import com.book.demo.entity.AppointmentEntity;
import com.book.demo.entity.StudentEntity;
import com.book.demo.entity.TeacherEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends MongoRepository<AppointmentEntity, String>
{

    List<AppointmentEntity>findByusername(TeacherEntity teacher);

    List<AppointmentEntity> findByUsername_Username(String username);


    List<AppointmentEntity> findByStudentusernameAndStartTimeLessThanAndEndTimeGreaterThan(
            StudentEntity student, LocalDateTime endTime, LocalDateTime startTime
    );

    List<AppointmentEntity>findAll();
    List<AppointmentEntity> findByStudentusername(StudentEntity student);

}
