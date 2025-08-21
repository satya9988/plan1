package com.example.learning.repository;

import com.example.learning.entity.Quiz;
import com.example.learning.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findByCourse(Course course);
}
