package com.example.model.users;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.model.quizes.Quiz;
import com.example.model.quizes.QuizResult;
import com.example.repos.ChatRepo;
import com.example.repos.QuizRepo;
import com.example.repos.UserRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import java.util.List;

@SpringBootTest
@Transactional
public class UserTest {

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private ChatRepo chatRepo;

    @Autowired
    private QuizRepo quizRepo;


    @Test
    public void test() {

        User u1 = new User("John", "Doe");
        User u2 = new User("Alice", "123");
        User u3 = new User("Bob", "456");

        userRepository.save(u1);
        userRepository.save(u2);
        userRepository.save(u3);

        List<User> users = userRepository.findAll();
        System.out.println("----------------------------------------------------------------- LIN GAN GULI GULI GUULI ...");
        for (User u : users) {
            System.out.println(u.getUsername() + " " + u.getPassword() + " " + u.getId());
        }
        System.out.println("----------------------------------------------------------------- WAZAA LIN GAN GOO LIN GAN ...");

        FriendRequest fru1u2 = u1.sendFriendRequest(u2);
        u2.acceptFriendRequest(fru1u2);
        FriendRequest fru2u3 = u2.sendFriendRequest(u3);
        u3.acceptFriendRequest(fru2u3);
        FriendRequest fru3u1 = u3.sendFriendRequest(u1);
        u1.acceptFriendRequest(fru3u1);

        User juser = userRepository.findByUsername("John");
        User Alicer = userRepository.findByUsername("Alice");
        User bober = userRepository.findByUsername("Bob");

        System.out.println(" JOHN FRIENDS -----------------------------------------------------------------");
        for (User f : juser.getFriends()) {
            System.out.println(f.getUsername() + " " + f.getPassword() + " " + f.getId());
        }

        System.out.println(" ALICE FRIENDS -----------------------------------------------------------------");
        for (User f : Alicer.getFriends()) {
            System.out.println(f.getUsername() + " " + f.getPassword() + " " + f.getId());
        }

        System.out.println("BOB FRIENDS -----------------------------------------------------------------");
        for (User f : bober.getFriends()) {
            System.out.println(f.getUsername() + " " + f.getPassword() + " " + f.getId());
        }

        // userRepository.delete(juser);

        System.out.println("------------------O MAI GAD JON IS DED NOOOOOO---------------------------------");
        System.out.println(" JOHN FRIENDS -----------------------------------------------------------------");
        for (User f : juser.getFriends()) {
            System.out.println(f.getUsername() + " " + f.getPassword() + " " + f.getId());
        }

        System.out.println(" ALICE FRIENDS -----------------------------------------------------------------");
        for (User f : Alicer.getFriends()) {
            System.out.println(f.getUsername() + " " + f.getPassword() + " " + f.getId());
        }

        System.out.println("BOB FRIENDS -----------------------------------------------------------------");
        for (User f : bober.getFriends()) {
            System.out.println(f.getUsername() + " " + f.getPassword() + " " + f.getId());
        }


        System.out.print("ALICE - JON PRIVATE CHAT -------------------------------------------------------");
        Chat u1u2c = new Chat();
        u1u2c.addUser(u1);
        u1u2c.addUser(u2);

        u1.sendMessage("How old are you ?", u1u2c);
        chatRepo.save(u1u2c);
        u2.sendMessage(" 13", u1u2c);
        chatRepo.save(u1u2c);
        u1.sendMessage("Take of your T-shirt", u1u2c);
        u2.sendMessage("ok diddy", u1u2c);
        chatRepo.save(u1u2c);

        System.out.print("exposing: ALICE - JON PRIVATE CHAT -----------------------------------------------");

        List<String> massages = u1u2c.getMessages();
        for (String m : massages) {
            System.out.println(m);
        }

        Chat u2u3c = new Chat(List.of(u2,u3));
        u2.sendMessage("hey bob i think diddy is diddling",u2u3c) ;
        u3.sendMessage("yeah np im diddling sometimes myself too, how old ru ?", u2u3c);
        chatRepo.save(u2u3c);

        u1u2c.addUser(u3);
        u3.sendMessage("AYO WTF R U DOIN DIDDY ?????", u1u2c);
        chatRepo.save(u1u2c);
        System.out.println("Startingg schlabbbb ------------------------------------------------");
        User bob = userRepository.findByUsername("Bob");
        System.out.println(bob.getUsername() + " " + bob.getPassword() + " " + bob.getId());
        List<Chat> bobChats = bob.getChats();
        System.out.println(bobChats.size());
        for (Chat chat : bobChats) {
            Long chatId = chat.getId();
            System.out.println(chatId);
            Chat chaat = (Chat) chatRepo.getById(chatId);
            for(String message : chaat.getMessages()) {
                System.out.println(message);
            }
            System.out.println("FINISHED EXPOSING THIS CHAT, STARTING EXPOSING NEXT CHAT :> \n");
        }

        System.out.println("Bob was a diddler too :<------------------------------------------------");

        System.out.println("TESTING USER HISTORY----------------------------------------------------");

        Quiz q1 = new Quiz();
        quizRepo.save(q1);
        QuizResult res = new QuizResult(1,2,q1,bober);
        q1.addResult(res);
        QuizResult res1 = new QuizResult(1,2,q1,bober);
        q1.addResult(res1);
        QuizResult res2 = new QuizResult(2,2,q1,bober);
        q1.addResult(res1);
        Quiz q2 = new Quiz();
        quizRepo.save(q2);
        QuizResult r0 = new QuizResult(5,2,q2,bober);
        q2.addResult(r0);
        QuizResult r1 = new QuizResult(6,2,q2,bober);
        q2.addResult(r1);

        List<QuizResult> bobsQuizs = bober.getUserHistory();
        for (QuizResult r : bobsQuizs) {
            System.out.println("Time: " + r.getTime());
            System.out.println("Pts: " + r.getPoints());
            System.out.println("User: " + r.getUser().getUsername());
            System.out.println("QuizId: " + r.getQuiz().getId());
            System.out.println("CMAN BOB YOU CAN DO BETTER --------------------------------------------");

        }

        System.out.println("LEZ CHECK RESULTS FOR QUIZ -------------------------------------------------");
        List<QuizResult> q1Hist = q1.getHistory();
        for (QuizResult r : q1Hist) {
            System.out.println("Time: " + r.getTime());
            System.out.println("Pts: " + r.getPoints());
            System.out.println("User: " + r.getUser().getUsername());
            System.out.println("QuizId: " + r.getQuiz().getId());
            System.out.println("-----------------------------------------------------------------------");
        }

        List<QuizResult> q2Hist = q2.getHistory();
        for (QuizResult r : q2Hist) {
            System.out.println("Time: " + r.getTime());
            System.out.println("Pts: " + r.getPoints());
            System.out.println("User: " + r.getUser().getUsername());
            System.out.println("QuizId: " + r.getQuiz().getId());
            System.out.println("-----------------------------------------------------------------------");
        }
    }

    @Test
    public void test1() {
        System.out.println("GAGI GAGO CHALLENGE TESTS-------------------------------------------------");
        User gagi = new User("gagi", "123");
        User gago = new User("gago", "456");
        userRepository.save(gagi);
        userRepository.save(gago);

        Quiz q3 = new Quiz();
        quizRepo.save(q3);
        Challenge ch1 = gagi.challengeUser(gago,q3);

        Quiz q4 = new Quiz();
        quizRepo.save(q4);
        Challenge ch2 = gagi.challengeUser(gago,q3);
        Challenge ch3 = gagi.challengeUser(gago,q4);

        User gagoCpy = userRepository.findByUsername("gago");

        System.out.println(gagoCpy.getUsername());
        System.out.println("FOUND GAGO, PRINTING GAGOS CHALLENGES -----------------------------------------");

        List<Challenge> gagochals = gagoCpy.getChallenges();
        for (Challenge c : gagochals) {
            System.out.println("CHALLENGE id: " + c.getId());
            System.out.println("QUIZ ID: " + c.getQuiz().getId());
            System.out.println("Challenger: " + c.getSender().getUsername());
            System.out.println("Challenged: " + c.getReceiver().getUsername());
            System.out.println("Challenger HIGH SCORE (SHOULD BE NULL BEFORE ITS IMPLEMENTED)" + c.getSenderHS());
            System.out.println("---------------------------------------------------------------------------------");
        }
        System.out.println("---------------------------------------------------------------------------------");
        System.out.println("GAGO AINT BEACH GAGO ACCEPTING CHALS --------------------------------------------");
        gago.challengeAcceptedOrRejected(ch2);
        gago.challengeAcceptedOrRejected(ch3);

        List<Challenge> gagochalss = gagoCpy.getChallenges();
        for (Challenge c : gagochalss) {
            System.out.println("CHALLENGE id: " + c.getId());
            System.out.println("QUIZ ID: " + c.getQuiz().getId());
            System.out.println("Challenger: " + c.getSender().getUsername());
            System.out.println("Challenged: " + c.getReceiver().getUsername());
            System.out.println("Challenger HIGH SCORE (SHOULD BE NULL BEFORE ITS IMPLEMENTED)" + c.getSenderHS());
            System.out.println("---------------------------------------------------------------------------------");
        }
    }
}
