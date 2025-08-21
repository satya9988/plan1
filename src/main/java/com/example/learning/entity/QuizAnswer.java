package com.example.learning.entity;

import jakarta.persistence.*;

@Entity
public class QuizAnswer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private QuizSubmission submission;

    @ManyToOne
    private Question question;

    private int selectedOptionIndex;
    private int pointsAwarded;

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public QuizSubmission getSubmission() { return submission; }
    public void setSubmission(QuizSubmission submission) { this.submission = submission; }

    public Question getQuestion() { return question; }
    public void setQuestion(Question question) { this.question = question; }

    public int getSelectedOptionIndex() { return selectedOptionIndex; }
    public void setSelectedOptionIndex(int selectedOptionIndex) { this.selectedOptionIndex = selectedOptionIndex; }

    public int getPointsAwarded() { return pointsAwarded; }
    public void setPointsAwarded(int pointsAwarded) { this.pointsAwarded = pointsAwarded; }
}
