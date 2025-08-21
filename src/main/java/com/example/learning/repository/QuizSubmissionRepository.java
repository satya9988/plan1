package com.example.learning.repository;

import com.example.learning.entity.QuizSubmission;
import com.example.learning.entity.Quiz;
import com.example.learning.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface QuizSubmissionRepository extends JpaRepository<QuizSubmission, Long> {
    List<QuizSubmission> findByStudent(User student);
    List<QuizSubmission> findByQuizAndStudent(Quiz quiz, User student);
    Optional<QuizSubmission> findFirstByQuizAndStudentOrderBySubmittedAtDesc(Quiz quiz, User student);
}
