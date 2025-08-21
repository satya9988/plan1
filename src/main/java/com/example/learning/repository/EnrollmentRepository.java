package com.example.learning.repository;

import com.example.learning.entity.Enrollment;
import com.example.learning.entity.Course;
import com.example.learning.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    Optional<Enrollment> findByStudentAndCourse(User student, Course course);
    List<Enrollment> findByStudent(User student);
    long countByStudent(User student);
    long countByStudentAndCompleted(User student, boolean completed);
}
