package com.example.learning.controller;

import com.example.learning.dto.PerformanceDTO;
import com.example.learning.entity.Enrollment;
import com.example.learning.entity.QuizSubmission;
import com.example.learning.entity.User;
import com.example.learning.repository.EnrollmentRepository;
import com.example.learning.repository.UserRepository;
import com.example.learning.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/performance")
public class PerformanceController {

    @Autowired private EnrollmentRepository enrollmentRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private QuizService quizService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('STUDENT')")
    public PerformanceDTO myPerformance(Authentication auth){
        User student = userRepository.findByUsername(auth.getName()).orElseThrow();
        List<Enrollment> enrollments = enrollmentRepository.findByStudent(student);
        double completion = 0.0;
        if (!enrollments.isEmpty()){
            completion = enrollments.stream().mapToDouble(Enrollment::getProgressPct).average().orElse(0.0);
        }

        List<QuizSubmission> subs = quizService.submissionsOf(auth.getName());
        double quizPct = 0.0;
        if (!subs.isEmpty()){
            int totalScore = 0, totalMax = 0;
            for (QuizSubmission s : subs){
                totalScore += s.getScore();
                totalMax += quizService.maxScore(s.getQuiz());
            }
            if (totalMax > 0) quizPct = (100.0 * totalScore) / totalMax;
        }

        PerformanceDTO dto = new PerformanceDTO();
        dto.completionPct = completion;
        dto.quizPct = quizPct;
        dto.overallPct = (completion * 0.5) + (quizPct * 0.5);
        dto.feedback = feedback(dto.overallPct);
        return dto;
    }

    private String feedback(double pct){
        if (pct >= 85) return "Excellent progress – keep pushing!";
        if (pct >= 70) return "Great work – a bit more practice to reach the top.";
        if (pct >= 50) return "You’re on track – review weak areas and continue.";
        if (pct > 0) return "Early days – focus on completing modules and quizzes.";
        return "No activity yet – start with your first course!";
    }
}
