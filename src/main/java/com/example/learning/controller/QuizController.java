package com.example.learning.controller;

import com.example.learning.dto.QuizDTOs.*;
import com.example.learning.entity.*;
import com.example.learning.repository.QuestionRepository;
import com.example.learning.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class QuizController {

    @Autowired private QuizService quizService;
    @Autowired private QuestionRepository questionRepository;

    @GetMapping("/courses/{courseId}/quizzes")
    public List<QuizView> list(@PathVariable Long courseId){
        return quizService.listByCourse(courseId).stream().map(this::toView).collect(Collectors.toList());
    }

    @PostMapping("/courses/{courseId}/quizzes")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public QuizView create(@PathVariable Long courseId, @RequestBody QuizCreate req, Authentication auth){
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_"+Role.ADMIN.name()));
        List<Question> qs = req.questions.stream().map(q -> {
            Question qu = new Question();
            qu.setText(q.text);
            qu.setOptions(q.options);
            qu.setCorrectOptionIndex(q.correctOptionIndex);
            qu.setPoints(q.points);
            return qu;
        }).collect(Collectors.toList());
        Quiz q = quizService.createQuiz(courseId, req.title, qs, auth.getName(), isAdmin);
        return toView(q);
    }

    @PutMapping("/quizzes/{quizId}")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public QuizView update(@PathVariable Long quizId, @RequestBody QuizCreate req, Authentication auth){
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_"+Role.ADMIN.name()));
        List<Question> qs = req.questions.stream().map(q -> {
            Question qu = new Question();
            qu.setText(q.text);
            qu.setOptions(q.options);
            qu.setCorrectOptionIndex(q.correctOptionIndex);
            qu.setPoints(q.points);
            return qu;
        }).collect(Collectors.toList());
        Quiz q = quizService.updateQuiz(quizId, req.title, qs, auth.getName(), isAdmin);
        return toView(q);
    }

    @PostMapping("/quizzes/{quizId}/submit")
    @PreAuthorize("hasRole('STUDENT')")
    public SubmissionResponse submit(@PathVariable Long quizId, @RequestBody SubmissionRequest req, Authentication auth){
        Map<Long,Integer> ans = new HashMap<>();
        for (AnswerDTO a : req.answers){
            ans.put(a.questionId, a.selectedOptionIndex);
        }
        QuizSubmission sub = quizService.submit(quizId, auth.getName(), ans);
        int max = quizService.maxScore(sub.getQuiz());
        SubmissionResponse resp = new SubmissionResponse();
        resp.submissionId = sub.getId();
        resp.score = sub.getScore();
        resp.maxScore = max;
        return resp;
    }

    private QuizView toView(Quiz q){
        QuizView v = new QuizView();
        v.id = q.getId();
        v.title = q.getTitle();
        v.questions = q.getQuestions().stream().map(qu -> {
            QuestionView qv = new QuestionView();
            qv.id = qu.getId();
            qv.text = qu.getText();
            qv.options = qu.getOptions();
            qv.points = qu.getPoints();
            return qv;
        }).collect(Collectors.toList());
        return v;
    }
}
