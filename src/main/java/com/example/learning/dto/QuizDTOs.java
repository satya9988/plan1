package com.example.learning.dto;

import java.util.*;

public class QuizDTOs {
    public static class QuestionCreate {
        public String text;
        public List<String> options;
        public int correctOptionIndex;
        public int points;
    }
    public static class QuizCreate {
        public String title;
        public List<QuestionCreate> questions;
    }
    public static class QuestionView {
        public Long id;
        public String text;
        public List<String> options;
        public int points;
    }
    public static class QuizView {
        public Long id;
        public String title;
        public List<QuestionView> questions;
    }
    public static class AnswerDTO {
        public Long questionId;
        public int selectedOptionIndex;
    }
    public static class SubmissionRequest {
        public List<AnswerDTO> answers;
    }
    public static class SubmissionResponse {
        public Long submissionId;
        public int score;
        public int maxScore;
    }
}
