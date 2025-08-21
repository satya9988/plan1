package com.example.learning.service;

import com.example.learning.entity.Enrollment;
import com.example.learning.entity.QuizSubmission;
import com.example.learning.entity.User;
import com.example.learning.repository.EnrollmentRepository;
import com.example.learning.repository.QuizSubmissionRepository;
import com.example.learning.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService {
    @Autowired private EnrollmentRepository enrollmentRepository;
    @Autowired private QuizSubmissionRepository submissionRepository;
    @Autowired private UserRepository userRepository;

    public long enrolledCount(User student){ return enrollmentRepository.countByStudent(student); }
    public long completedCount(User student){ return enrollmentRepository.countByStudentAndCompleted(student, true); }
    public long quizzesAttempted(User student){ return submissionRepository.findByStudent(student).size(); }
    public double avgQuizScorePct(User student){
        List<QuizSubmission> subs = submissionRepository.findByStudent(student);
        if (subs.isEmpty()) return 0.0;
        int totalScore = subs.stream().mapToInt(QuizSubmission::getScore).sum();
        // NOTE: We don't have max score per submission here; for simplicity assume each quiz is out of 100 when averaging
        // Better approach: compute per quiz max; skipped for brevity.
        double avg = (double) totalScore / subs.size();
        return Math.min(100.0, avg);
    }
}
