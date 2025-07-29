package com.example.quizwebproject.controller;

import com.example.quizwebproject.model.questions.Question;
import com.example.quizwebproject.model.quizes.Quiz;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class QuizPracticeModeController {

    @PostMapping("/quiz/{id}/practiceMode/question/{index}/quickCheck")
    public String quickCheck(@PathVariable Long id,
                             @PathVariable Integer index,
                             @RequestParam(value = "userAnswer", required = false) List<String> answerList,
                             HttpSession session,
                             Model model) {
        if(answerList==null) answerList=List.of("");

        Quiz quiz = (Quiz) session.getAttribute("quiz");
        List<Question> questions = quiz.getQuestions();

        if(index<0 || index>=questions.size()) throw new RuntimeException("CAN NOT CALCULATE ANSWER FOR QUESTION INDEX : -1");

        Question question=questions.get(index);
        StringBuilder answer = new StringBuilder();
        for(String answ : answerList){
            answer.append(answ).append(",");
        }
        question.setUserAnswer(answer.toString());

        ArrayList<Integer> correctCnt=(ArrayList<Integer>) session.getAttribute("correctCnt");
        if(question.getResult()>0){
            correctCnt.set(index,correctCnt.get(index)+1);
        }
        model.addAttribute("question", question);
        model.addAttribute("correctAnswer", question.getCorrectAnswers());
        model.addAttribute("quizId", id);
        model.addAttribute("index", index);
        model.addAttribute("hasNext", index<questions.size());
        return "practiceMode/showCorrectAnswer";
    }

    @GetMapping("/quiz/{id}/practiceMode/question/{index}")
    public String practiceMode(@PathVariable Long id, @PathVariable Integer index, Model model, HttpSession session) {
        Quiz quiz = (Quiz) session.getAttribute("quiz");

        if (quiz == null || quiz.getQuestions() == null || index < 0 || index > quiz.getQuestions().size()) {
            return "errorPages/quizNotFound";
        }

        if(index>=quiz.getQuestions().size()) index=0;

        ArrayList<Integer> correctCnt=(ArrayList<Integer>) session.getAttribute("correctCnt");
        boolean flag=false;
        for(Integer i:correctCnt){
            if(i<3){
                flag=true;
                break;
            }
        }

        if(!flag) return "practiceMode/practiceFinished";

        if(correctCnt.get(index)==3){
            int ol=index+1;
            while(true){
                if(ol==index) break;
                if(ol>=quiz.getQuestions().size()) ol=0;
                if(correctCnt.get(ol)<3){
                    index=ol;
                    break;
                }
                ++ol;
            }
        }

        List<Question> questions = quiz.getQuestions();
        Question question = questions.get(index);
        model.addAttribute("quiz", quiz);
        model.addAttribute("question", question);
        model.addAttribute("index", index);

        return "practiceMode/showPracticeModeQuestion";
    }
}
