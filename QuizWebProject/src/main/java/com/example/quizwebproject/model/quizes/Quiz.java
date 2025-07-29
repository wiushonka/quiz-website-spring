package com.example.quizwebproject.model.quizes;

import com.example.quizwebproject.model.questions.Question;
import com.example.quizwebproject.model.users.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private boolean randomize;

    private boolean quickResults;

    private boolean mulPages;

    private boolean practiceMode;

    private String description;

    private String type;

    @ManyToOne
    private User author;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<QuizResult> history = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Question> questions = new ArrayList<>();

    private LocalDateTime creationDate;

    public Quiz() {}

    public Quiz(boolean randomize, boolean quickResults, boolean mulPages, boolean practiceMode, String description,
                List<Question> questions, User author, String type) {
        this.randomize = randomize;
        this.quickResults = quickResults;
        this.mulPages = mulPages;
        this.practiceMode = practiceMode;
        this.description = description;
        this.questions = questions;
        this.author = author;
        this.type = type;
    }

    @PrePersist
    protected void onCreate() {
        this.creationDate = LocalDateTime.now();
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isRandomize() {
        return randomize;
    }

    public void setRandomize(boolean randomize) {
        this.randomize = randomize;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public List<QuizResult> getHistory() {
        return history;
    }

    public void addQuestion(Question question) {
        questions.add(question);
    }

    public void removeQuestion(Question question) {
        questions.remove(question);
    }

    public void insertQuestion(Question question, int idx) {
        questions.add(idx, question);
    }

    public void removeIndexQuestion(int idx) {
        questions.remove(idx);
    }

    public void addResult(QuizResult res) {
        history.add(res);
        res.getUser().addResultToHistory(res);
    }

    public void clearHistory() {
        for(QuizResult qr : history) {
            List<QuizResult> userHist = qr.getUser().getUserHistory();
            userHist.remove(qr);
            qr.setQuiz(null);
            qr.setUser(null);
        }
        history.clear();
    }

    public Long getId() {
        return id;
    }

    public User getAuthor() {
        return author;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) { this.type = type; }

    public boolean isPracticeMode() { return practiceMode; }

    public boolean isMulPages() { return mulPages; }

    public boolean isQuickResults() {
        return quickResults;
    }
}
