package com.example.learning.controller;

import com.example.learning.dto.DashboardDTO;
import com.example.learning.entity.User;
import com.example.learning.repository.UserRepository;
import com.example.learning.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired private DashboardService dashboardService;
    @Autowired private UserRepository userRepository;

    @GetMapping("/me")
    @PreAuthorize("hasRole('STUDENT')")
    public DashboardDTO me(Authentication auth){
        User student = userRepository.findByUsername(auth.getName()).orElseThrow();
        DashboardDTO dto = new DashboardDTO();
        dto.enrolledCourses = dashboardService.enrolledCount(student);
        dto.completedCourses = dashboardService.completedCount(student);
        dto.quizzesAttempted = dashboardService.quizzesAttempted(student);
        dto.averageQuizScorePct = dashboardService.avgQuizScorePct(student);
        return dto;
    }
}
