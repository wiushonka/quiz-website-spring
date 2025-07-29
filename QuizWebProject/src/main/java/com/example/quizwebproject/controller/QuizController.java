package com.example.quizwebproject.controller;

import com.example.quizwebproject.DTOs.ChallengeAcceptanceDTO;
import com.example.quizwebproject.model.questions.Question;
import com.example.quizwebproject.model.quizes.Quiz;
import com.example.quizwebproject.model.quizes.QuizResult;
import com.example.quizwebproject.model.users.User;
import com.example.quizwebproject.service.QuizService;
import com.example.quizwebproject.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
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
    public String quiz(@PathVariable Long id, Model model, HttpSession session,
                       @RequestParam(value = "practiceMode", required = false) Boolean practiceMode) {
        if(practiceMode==null || !practiceMode){
            model.addAttribute("quiz", quizService.getQuizById(id));
            Quiz quiz = quizService.getQuizById(id);
            session.setAttribute("startTime", System.currentTimeMillis());
            session.setAttribute("quiz", quiz);

            if (quiz != null && quiz.isMulPages()) {
                session.setAttribute("quiz", quiz);
                Double x = 0D;
                session.setAttribute("resultSoFar", x);
                return "redirect:/quiz/" + id.toString() + "/question/" + "0";
            }else if(quiz != null && !quiz.isMulPages()){
                return "displayQuizSinglePage";
            }
            return "errorPages/quizNotFound";
        }else{
            Quiz quiz = quizService.getQuizById(id);
            ArrayList<Integer> correctCnt = new ArrayList<>(quiz.getQuestions().size());
            for (int i = 0; i < quiz.getQuestions().size(); ++i) correctCnt.add(0);

            session.setAttribute("correctCnt", correctCnt);
            session.setAttribute("quiz", quiz);
            Double x = 0D;
            if(quiz.isQuickResults()) session.setAttribute("resultSoFar", x);

            User user = (User) session.getAttribute("user");
            userService.givePracticeMakesPerfectToUser(user.getId());

            return "redirect:/quiz/" + id + "/practiceMode/question/0";
        }
    }

    @PostMapping("/quiz/{id}/question/{index}/mulPages")
    public String mulPageQuestionAnswered(@PathVariable Long id, @PathVariable int index,
                                          HttpSession session,
                                          @RequestParam MultiValueMap<String, String> formData,
                                          @RequestParam("wentForward") boolean forward,
                                          @RequestParam(value = "submittedQuiz", required = false, defaultValue = "false") Boolean submitted) {
        List<String> userAnswers = new ArrayList<>();
        for (String key : formData.keySet()) {
            if (key.startsWith("answer_")) {
                userAnswers.addAll(formData.get(key));
            }
        }
        if (userAnswers.isEmpty()) userAnswers = List.of("");
        StringBuilder answers = new StringBuilder();
        for (String userAnswer : userAnswers) {
            answers.append(userAnswer).append(",");
        }

        Quiz quiz = (Quiz) session.getAttribute("quiz");
        Question question = quiz.getQuestions().get(index);

        if (!answers.toString().equals(",")) {
            question.setUserAnswer(answers.toString());

            Double currentScore = (Double) session.getAttribute("resultSoFar");
            if (currentScore == null) currentScore = 0D;
            if (question.getUserAnswer() != null && !question.getUserAnswer().isEmpty()) {
                double questionScore = question.getResult();
                session.setAttribute("resultSoFar", currentScore + questionScore);
            } else {
                double updatedScore = quizService.calculateScore(quiz);
                session.setAttribute("resultSoFar", updatedScore);
            }
        }

        if(submitted != null && submitted) {
            return "redirect:/quiz/mulPages/finalResult";
        }

        if (forward) ++index;
        else --index;

        return "redirect:/quiz/" + id + "/question/" + index;
    }



    @RequestMapping("/quiz/mulPages/finalResult")
    public String getMultiplePagesFinalResult(HttpSession session, Model model){
        long curTime = System.currentTimeMillis();
        Long startTime = (Long) session.getAttribute("startTime");
        long elapsedTime = ((curTime - startTime) / 1000L);

        Quiz quiz = (Quiz) session.getAttribute("quiz");
        User user = (User) session.getAttribute("user");

        Double score = (Double) session.getAttribute("resultSoFar");
        if (score == null) score = 0D;

        QuizResult result = new QuizResult(elapsedTime, score, quiz, user);
        quizService.addNewResult(elapsedTime, score, quiz.getId(), user.getId());

        model.addAttribute("quizResult", result);
        return "resultpage";
    }


    @RequestMapping("/quiz/{id}/question/{index}")
    public String question(@PathVariable Long id, @PathVariable Integer index, Model model) {
        Quiz quiz = quizService.getQuizById(id);

        if (quiz == null || quiz.getQuestions() == null || index < 0 || index >= quiz.getQuestions().size()) {
            return "errorPages/quizNotFound";
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

    @RequestMapping("/quiz/{id}/question/{index}/immidiateResult")
    public String showImmediateResult(@PathVariable Long id, @PathVariable Integer index, Model model,
                                      @RequestParam MultiValueMap<String, String> formData,
                                      HttpSession session) {

        List<String> userAnswers = new ArrayList<>();
        for (String key : formData.keySet()) {
            if (key.startsWith("answer_")) {
                userAnswers.addAll(formData.get(key));
            }
        }
        if (userAnswers.isEmpty()) userAnswers = List.of("");

        Quiz quiz = (Quiz) session.getAttribute("quiz");
        List<Question> questions = quiz.getQuestions();

        if(index<0 || index>=questions.size()) throw new RuntimeException("CAN NOT CALCULATE ANSWER FOR QUESTION INDEX : -1");

        Question question=questions.get(index);
        StringBuilder answer = new StringBuilder();
        for(String answ : userAnswers){
            answer.append(answ).append(",");
        }

        question.setUserAnswer(answer.toString());

        Double x = (Double) session.getAttribute("resultSoFar");
        if (x==null) x=0D;
        session.setAttribute("resultSoFar", x);
        double y = question.getResult();
        session.setAttribute("resultSoFar",x+y);

        model.addAttribute("questionText",question.getQuestion());
        model.addAttribute("correctAnswer",question.getCorrectAnswers());
        model.addAttribute("wasUserCorrect",y>0);
        model.addAttribute("quiz",quiz);
        model.addAttribute("index",index);
        model.addAttribute("hasNext", index < questions.size() - 1);
        model.addAttribute("resultSoFar",x+y);

        return "displayImmidateResult";
    }

    @RequestMapping("/quiz/startQuiz/{id}")
    public String startQuiz(@PathVariable Long id,
                            Model model,
                            HttpSession session,
                            @ModelAttribute("dto") ChallengeAcceptanceDTO dto,
                            @RequestParam(value = "orderBy", required = false) String orderBy) {

        model.addAttribute("quiz", quizService.getQuizById(id));
        if(session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        User user = (User) session.getAttribute("user");
        model.addAttribute("friends", userService.getUserFriends(user.getId()));
        model.addAttribute("user",user);

        if(dto != null && dto.quizResult() != null && dto.getQuiz() != null) {
            model.addAttribute("dto", dto);
        }else {
            model.addAttribute("dto", null);
        }
        List<QuizResult> pastPerformance=quizService.getUserPastPerformance(user.getId(),id);
        if(orderBy!=null){
            switch(orderBy){
                case "date": pastPerformance.sort(Comparator.comparing(QuizResult::getResultDate).reversed());
                    break;
                case "score": pastPerformance.sort(Comparator.comparingDouble(QuizResult::getPoints).reversed());
                    break;
                case "time": pastPerformance.sort(Comparator.comparingLong(QuizResult::getTime));
                    break;
            }
        }

        model.addAttribute("userPastPerformance",pastPerformance);
        model.addAttribute("allTimeLeaderboard",quizService.getAllTimeLeaderboard(id));
        model.addAttribute("lastDayLeaderboard",quizService.getLastDayLb(id));
        model.addAttribute("recentHistory",quizService.getRecentHistory(id));
        model.addAttribute("stats",quizService.getStats(id));

        return "quizStartPage";
    }

    @PostMapping("/quiz/{id}/immidiateResults/lastResult")
    public String lastImmidiateResult(@PathVariable Long id,HttpSession session, Model model) {
        Double score = (Double) session.getAttribute("resultSoFar");
        long curTime = System.currentTimeMillis();
        Long startTime = (Long) session.getAttribute("startTime");
        long elapsedTime = ((curTime - startTime) / 1000L);

        Quiz quiz = (Quiz) session.getAttribute("quiz");
        User user = (User) session.getAttribute("user");

        QuizResult result = new QuizResult(elapsedTime, score, quiz, user);

        quizService.addNewResult(elapsedTime,score,quiz.getId(),user.getId());

        model.addAttribute("quizResult", result);
        return "resultpage";
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