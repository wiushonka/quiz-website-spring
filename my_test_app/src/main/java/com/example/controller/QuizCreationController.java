package com.example.controller;

import com.example.model.questions.FillBlankQuestion;
import com.example.model.questions.Question;
import com.example.model.questions.ResponseQuestion;
import com.example.model.questions.TestQuestion;
import com.example.model.quizes.Quiz;
import com.example.model.users.User;
import com.example.service.QuizCreationService;
import com.example.service.QuizService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class QuizCreationController {

    private final QuizCreationService quizCreationService;
    private final QuizService quizService;

    public QuizCreationController(QuizCreationService quizCreationService, QuizService quizService) {
        this.quizCreationService = quizCreationService;
        this.quizService = quizService;
    }

    @PostMapping("/quizCreation/addNewQuiz")
    public String addNewQuiz(
            HttpSession session,
            @RequestParam("description")  String description,
            @RequestParam("type")         String type,
            @RequestParam(value = "randomize",    defaultValue = "false") boolean randomize,
            @RequestParam(value = "quickResults", defaultValue = "false") boolean quickResults,
            @RequestParam(value = "mulPages",      defaultValue = "false") boolean mulPages) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        List<Question> questions = new ArrayList<>();
        Quiz quiz = new Quiz(randomize, quickResults, mulPages, false, description, questions, user, type);
        Long quizId = quizCreationService.createQuiz(quiz);
        session.setAttribute("editedQuiz", quiz);
        return "redirect:/editQuiz/" + quizId + "/question/0";
    }

    @GetMapping("/editQuiz/{id}/question/{index}")
    public String showEditQuiz(@PathVariable Long id,@PathVariable int index, Model model) {
        model.addAttribute("id",id);
        model.addAttribute("index",index);

        Quiz quiz=quizService.getQuizById(id);

        if(index != quiz.getQuestions().size()) model.addAttribute("question",quiz.getQuestions().get(index));
        model.addAttribute("currentMaxQuestions",quiz.getQuestions().size());

        model.addAttribute("hasNext",index<quiz.getQuestions().size());
        model.addAttribute("hasPrev",index>0);

        return "quizEditor/editQuiz";
    }

    private String trimBrackets(String input) {
        if (input != null && input.startsWith("[") && input.endsWith("]") && input.length() > 1) {
            return input.substring(1, input.length() - 1).trim();
        }
        return input;
    }

    @PostMapping("/editQuiz/{id}/question/{index}")
    public String editQuiz(@PathVariable Long id,
                           @PathVariable int index,
                           @RequestParam("questionText") String question,
                           @RequestParam("questionType") String questionType,
                           @RequestParam("correctAnswer") String answer,
                           @RequestParam(value = "possibleAnswers", required = false, defaultValue = "") String posAnswers,
                           @RequestParam("category") String category,
                           @RequestParam("maxPoints") int maxPoints,
                           @RequestParam(value = "ordered", required = false, defaultValue = "false") boolean order,
                           Model model) {

        question = trimBrackets(question);
        questionType = trimBrackets(questionType);
        answer = trimBrackets(answer);
        posAnswers = trimBrackets(posAnswers);
        category = trimBrackets(category);

        Quiz quiz = quizService.getQuizById(id);
        Question q;
        switch (questionType) {
            case "TestQuestion":
                q = new TestQuestion(question,List.of(answer.split(",")),category,maxPoints,posAnswers);
                if(index==quiz.getQuestions().size()){
                    quizCreationService.addNewQuestionToQuiz(id, q);
                }else{
                    quizCreationService.editQuestionInQuiz(id,index,q);
                }
                break;
            case "ResponseQuestion":
                q = new ResponseQuestion(question,answer,order,category,maxPoints);
                if(index==quiz.getQuestions().size()){
                    quizCreationService.addNewQuestionToQuiz(id, q);
                }else{
                    quizCreationService.editQuestionInQuiz(id,index,q);
                }
                break;
            case "FillBlankQuestion":
                q = new FillBlankQuestion(question,category,answer,maxPoints);
                if(index==quiz.getQuestions().size()){
                    quizCreationService.addNewQuestionToQuiz(id, q);
                }else{
                    quizCreationService.editQuestionInQuiz(id,index,q);
                }
                break;
        }

        model.addAttribute("id",id);
        model.addAttribute("index",index);
        model.addAttribute("hasNext",index<quiz.getQuestions().size());
        model.addAttribute("hasPrev",index>0);
        model.addAttribute("question",quiz.getQuestions().get(index));
        model.addAttribute("newQuestionAdded",index==quiz.getQuestions().size());

        return "quizEditor/editQuiz";
    }

    @PostMapping("/editQuiz/{id}/question/{index}/removeQuestion")
    public String removeQuestion(@PathVariable Long id, @PathVariable int index) {
        Quiz quiz = quizService.getQuizById(id);
        quizCreationService.removeQuestion(id,index);
        return "redirect:/editQuiz/" + quiz.getId() + "/question/0";
    }
}
