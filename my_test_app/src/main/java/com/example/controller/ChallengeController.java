package com.example.controller;

import com.example.DTOs.ChallengeAcceptanceDTO;
import com.example.model.quizes.QuizResult;
import com.example.model.users.Challenge;
import com.example.model.users.User;
import com.example.service.ChallengeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ChallengeController {

    private final ChallengeService challengeService;

    public ChallengeController(ChallengeService challengeService) {
        this.challengeService = challengeService;
    }

    @PostMapping("/sendChallenge")
    public String sendChallenge(@RequestParam("recieverId") Long recieverId,
                                @RequestParam("quizId") Long quizId,
                                HttpSession session) {
        User sender = (User) session.getAttribute("user");
        if (sender == null) return "redirect:/login";

        challengeService.sendChallenge(sender.getId(),recieverId,quizId);

        return "redirect:/quiz/startQuiz/" + quizId;
    }

    @PostMapping("/challenge/accept")
    public String acceptChallenge(@RequestParam("chalId") Long chalId,
                                  RedirectAttributes redirectAttributes) {
        ChallengeAcceptanceDTO dto = challengeService.acceptChallenge(chalId);
        redirectAttributes.addFlashAttribute("dto", dto);
        return "redirect:/quiz/startQuiz/" + dto.getQuiz().getId();
    }

    @PostMapping("/challenge/reject")
    public String rejectChallenge(@RequestParam("chalId") Long chalId) {
        challengeService.rejectChallenge(chalId);
        return "redirect:/homepage";
    }

    @GetMapping("/allChallenges/{userId}")
    public String displayAllChallenges(@PathVariable Long userId, Model model) {
        model.addAttribute("allChallenges",challengeService.getAllChallenges(userId));
        return "allChallenges";
    }
}
