package com.example.controller;

import com.example.DTOs.ChallengeAcceptanceDTO;
import com.example.model.questions.Question;
import com.example.model.quizes.Quiz;
import com.example.model.quizes.QuizResult;
import com.example.model.users.User;
import com.example.service.QuizService;
import com.example.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
public class QuizController {
    private final QuizService quizService;

    private final UserService userService;

    public QuizController(QuizService quizService, UserService userService) {
        this.quizService = quizService;
        this.userService = userService;
    }

    @RequestMapping("/quiz/{id}")
    public String quiz(@PathVariable Long id, Model model, HttpSession session) {
        Quiz quiz = quizService.getQuizById(id);
        model.addAttribute("quiz", quizService.getQuizById(id));

        session.setAttribute("startTime", System.currentTimeMillis());
        session.setAttribute("quiz", quiz);

        if (quiz != null && quiz.isMulPages()) {
            return "redirect:/quiz/" + id.toString() + "/question/" + "0";
        }else if(quiz != null && !quiz.isMulPages()){
            return "displayQuizSinglePage";
        }
        return "quizNotFound";
    }

    @RequestMapping("/quiz/{id}/question/{index}")
    public String question(@PathVariable Long id, @PathVariable Integer index, Model model) {
        Quiz quiz = quizService.getQuizById(id);

        if (quiz == null || quiz.getQuestions() == null || index < 0 || index >= quiz.getQuestions().size()) {
            return "quizNotFound";
        }

        List<Question> questions = quiz.getQuestions();
        Question question = questions.get(index);

        model.addAttribute("quiz", quiz);
        model.addAttribute("question", question);
        model.addAttribute("index", index);
        model.addAttribute("hasNext", index < questions.size() - 1);
        model.addAttribute("hasPrevious", index > 0);

        return "displayQuestion";
    }

    @RequestMapping("/quiz/startQuiz/{id}")
    public String startQuiz(@PathVariable Long id,
                            Model model,
                            HttpSession session,
                            @RequestParam(value = "newFriendRequestSent", required = false) Boolean sent,
                            @ModelAttribute("dto") ChallengeAcceptanceDTO dto) {
        model.addAttribute("quiz", quizService.getQuizById(id));
        if((User)session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        User user = (User) session.getAttribute("user");
        model.addAttribute("friends", userService.getUserFriends(user.getId()));
        model.addAttribute("user",user);
        model.addAttribute("newFriendRequestSent",sent);

        if(dto != null && dto.quizResult() != null && dto.getQuiz() != null) {
            model.addAttribute("dto", dto);
        }else {
            model.addAttribute("dto", null);
        }

        return "quizStartPage";
    }

    @PostMapping("/quiz/{id}/calculateResult")
    public String calculateScore(
            @PathVariable Long id,
            @RequestParam Map<String, String> formData,
            HttpSession session,
            Model model) {

        long curTime = System.currentTimeMillis();
        Long startTime = (Long) session.getAttribute("startTime");
        long elapsedTime = ((curTime - startTime) / 1000L);

        Quiz quiz = (Quiz) session.getAttribute("quiz");
        User user = (User) session.getAttribute("user");

        for (Question question : quiz.getQuestions()) {
            String paramKey = "answer_" + question.getId();

            if (formData.containsKey(paramKey)) {
                String answer = formData.get(paramKey);

                question.setUserAnswer(answer);
            } else {
                question.setUserAnswer("");
            }
        }

        double score = quizService.calculateScore(quiz);
        QuizResult result = new QuizResult(elapsedTime, score, quiz, user);

        quizService.addNewResult(elapsedTime,score,quiz.getId(),user.getId());

        model.addAttribute("quizResult", result);
        return "resultpage";
    }

    @GetMapping("/allQuizzes")
    public String displayAllQuizzes(Model model) {
        model.addAttribute("quizzs", quizService.getAllQuizzs());
        return "allQuizzes";
    }
}
