package com.example.controller;

import com.example.model.questions.FillBlankQuestion;
import com.example.model.questions.Question;
import com.example.model.questions.ResponseQuestion;
import com.example.model.questions.TestQuestion;
import com.example.model.quizes.Quiz;
import com.example.model.users.User;
import com.example.service.QuizCreationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class QuizCreationController {

    private final QuizCreationService quizCreationService;

    public QuizCreationController(QuizCreationService quizCreationService) {
        this.quizCreationService = quizCreationService;
    }

    @PostMapping("/quizCreation/addNewQuiz")
    @Transactional
    public String addNewQuiz(
            HttpSession session,
            @RequestParam("description")  String description,
            @RequestParam("type")         String type,
            @RequestParam(value = "randomize",    defaultValue = "false") boolean randomize,
            @RequestParam(value = "quickResults", defaultValue = "false") boolean quickResults,
            @RequestParam(value = "mulPages",      defaultValue = "false") boolean mulPages,
            @RequestParam(value = "practiceMode",  defaultValue = "false") boolean practiceMode) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        List<Question> questions = new ArrayList<>();

        Quiz quiz = new Quiz(randomize, quickResults, mulPages, practiceMode, description, questions, user, type);

        Long quizId = quizCreationService.createQuiz(quiz);

        return "redirect:/editQuiz/" + quizId;
    }

    @GetMapping("/editQuiz/{id}")
    public String showEditQuiz(@PathVariable Long id, Model model) {
        model.addAttribute("id",id);
        return "editQuiz";
    }

    @PostMapping("/editQuiz/{id}")
    public String editQuiz(@PathVariable Long id,
                           @RequestParam("questionText") String question,
                           @RequestParam("questionType") String questionType,
                           @RequestParam("correctAnswer") String answer,
                           @RequestParam(value = "possibleAnswers", required = false, defaultValue = "") String posAnswers,
                           @RequestParam("category") String category,
                           @RequestParam("maxPoints") int maxPoints,
                           @RequestParam(value = "ordered", required = false, defaultValue = "false") boolean order) {
        Question q;
        switch (questionType) {
            case "TestQuestion":
                q = new TestQuestion(question,List.of(answer.split(",")),category,maxPoints,posAnswers);
                quizCreationService.addNewQuestionToQuiz(id, q);
            case "ResponseQuestion":
                q = new ResponseQuestion(question,answer,order,category,maxPoints);
                quizCreationService.addNewQuestionToQuiz(id, q);
            case "FillBlankQuestion":
                q = new FillBlankQuestion(question,category,answer);
                quizCreationService.addNewQuestionToQuiz(id, q);
        }
        return "editQuiz";
    }
}
