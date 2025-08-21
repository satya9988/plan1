package com.example.learning.repository;

import com.example.learning.entity.Course;
import com.example.learning.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByTeacher(User teacher);
}
