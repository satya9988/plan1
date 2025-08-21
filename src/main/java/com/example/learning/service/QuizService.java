package com.example.learning.service;

import com.example.learning.entity.*;
import com.example.learning.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class QuizService {

    @Autowired private QuizRepository quizRepository;
    @Autowired private CourseRepository courseRepository;
    @Autowired private QuestionRepository questionRepository;
    @Autowired private QuizSubmissionRepository submissionRepository;
    @Autowired private UserRepository userRepository;

    public List<Quiz> listByCourse(Long courseId){
        Course c = courseRepository.findById(courseId).orElseThrow();
        return quizRepository.findByCourse(c);
    }

    @Transactional
    public Quiz createQuiz(Long courseId, String title, List<Question> questions, String username, boolean isAdmin){
        Course c = courseRepository.findById(courseId).orElseThrow();
        if (!isAdmin && !c.getTeacher().getUsername().equals(username)) throw new RuntimeException("Not allowed");
        Quiz q = new Quiz();
        q.setCourse(c);
        q.setTitle(title);
        q = quizRepository.save(q);
        for (Question qu : questions) {
            qu.setQuiz(q);
            questionRepository.save(qu);
        }
        return q;
    }

    @Transactional
    public Quiz updateQuiz(Long quizId, String title, List<Question> newQuestions, String username, boolean isAdmin){
        Quiz q = quizRepository.findById(quizId).orElseThrow();
        Course c = q.getCourse();
        if (!isAdmin && !c.getTeacher().getUsername().equals(username)) throw new RuntimeException("Not allowed");
        q.setTitle(title);
        // replace questions
        List<Question> old = questionRepository.findByQuiz(q);
        for (Question oq : old) questionRepository.delete(oq);
        for (Question nq : newQuestions) { nq.setQuiz(q); questionRepository.save(nq); }
        return quizRepository.save(q);
    }

    @Transactional
    public QuizSubmission submit(Long quizId, String username, Map<Long, Integer> answersMap){
        Quiz q = quizRepository.findById(quizId).orElseThrow();
        User student = userRepository.findByUsername(username).orElseThrow();

        QuizSubmission sub = new QuizSubmission();
        sub.setQuiz(q);
        sub.setStudent(student);
        sub.setSubmittedAt(new Date());
        sub.setScore(0);
        sub = submissionRepository.save(sub);

        int score = 0;
        List<Question> qlist = questionRepository.findByQuiz(q);
        for (Question qu : qlist) {
            int sel = answersMap.getOrDefault(qu.getId(), -1);
            int awarded = (sel == qu.getCorrectOptionIndex()) ? qu.getPoints() : 0;
            score += awarded;
            QuizAnswer ans = new QuizAnswer();
            ans.setSubmission(sub);
            ans.setQuestion(qu);
            ans.setSelectedOptionIndex(sel);
            ans.setPointsAwarded(awarded);
            sub.getAnswers().add(ans);
        }
        sub.setScore(score);
        return submissionRepository.save(sub);
    }

    public Optional<QuizSubmission> lastSubmission(Long quizId, String username){
        Quiz q = quizRepository.findById(quizId).orElseThrow();
        User student = userRepository.findByUsername(username).orElseThrow();
        return submissionRepository.findFirstByQuizAndStudentOrderBySubmittedAtDesc(q, student);
    }

    public int maxScore(Quiz quiz) {
        return questionRepository.findByQuiz(quiz).stream().mapToInt(Question::getPoints).sum();
    }

    public List<QuizSubmission> submissionsOf(String username){
        User student = userRepository.findByUsername(username).orElseThrow();
        return submissionRepository.findByStudent(student);
    }
}
