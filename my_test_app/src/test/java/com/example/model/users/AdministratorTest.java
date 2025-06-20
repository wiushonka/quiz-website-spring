package com.example.model.users;

import com.example.model.quizes.Quiz;
import com.example.model.quizes.QuizResult;
import com.example.model.users.admin.Announcement;
import com.example.repos.AnnouncementRepo;
import com.example.repos.QuizRepo;
import com.example.repos.UserRepo;
import com.example.service.AdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@SpringBootTest
@Transactional
class AdministratorTest {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private QuizRepo quizRepo;

    @Autowired
    private AnnouncementRepo announcementRepo;


    @Test
    public void test1() {
        User admin = new User("admin", "admin");
        User john = new User("John", "doe");
        User alice = new User("Alice", "doe");
        User bob = new User("Bob", "doe");
        userRepo.save(admin);
        userRepo.save(john);
        userRepo.save(alice);
        userRepo.save(bob);

        Quiz q1 = new Quiz();
        quizRepo.save(q1);

        QuizResult jr1 = new QuizResult(1L,1,q1,john);
        q1.addResult(jr1);
        QuizResult jr2 = new QuizResult(1L,1,q1,john);
        q1.addResult(jr2);
        QuizResult ar1 = new QuizResult(1L,1,q1,alice);
        q1.addResult(ar1);
        QuizResult br1 = new QuizResult(1L,1,q1,bob);
        q1.addResult(br1);
        QuizResult ar2 = new QuizResult(1L,1,q1,alice);
        q1.addResult(ar2);
        QuizResult br2 = new QuizResult(1L,1,q1,bob);
        q1.addResult(br2);

        AdminService service = new AdminService(userRepo,quizRepo,announcementRepo);


        System.out.println("PRINTING STATISTICS RN ----------------------------------------------");
        HashMap<String,Long> stats = service.seeStats();
        for(String title : stats.keySet()) {
            System.out.println(title + ": " + stats.get(title));
        }
        System.out.println("======================================================================= \n");

        System.out.println("--------- Quiz History before administrator went chimp mode --------");
        List<QuizResult> q1hist1 = q1.getHistory();
        for(QuizResult qr : q1hist1) {
            System.out.println("username: " + qr.getUser().getUsername());
        }
        System.out.println("======================================================================= \n");

        System.out.println("--------- Quiz History after administrator went chimp mode --------");
        service.clearQuizHistory(q1.getId());
        List<QuizResult> q1hist2 = q1.getHistory();
        for(QuizResult qr : q1hist2) {
            System.out.println("username: " + qr.getUser().getUsername() + "\n");
        }
        System.out.println("======================================================================= \n");

        System.out.println("PRINTING STATISTICS RN ----------------------------------------------");
        stats = service.seeStats();
        for(String title : stats.keySet()) {
            System.out.println(title + ": " + stats.get(title));
        }
        System.out.println("======================================================================= \n");

        QuizResult jr11 = new QuizResult(1L,1,q1,john);
        QuizResult jr12 = new QuizResult(1L,1,q1,john);
        QuizResult ar11 = new QuizResult(1L,1,q1,alice);

        q1.addResult(jr11);
        q1.addResult(jr12);
        q1.addResult(ar11);
        System.out.println("PRINTING STATISTICS RN ----------------------------------------------");
        stats = service.seeStats();
        for(String title : stats.keySet()) {
            System.out.println(title + ": " + stats.get(title));
        }
        System.out.println("======================================================================= \n");

        service.deleteUser(bob.getId());

        System.out.println("ADMIN WENT BANANAS AGAIN --------------------------------------------");
        System.out.println("PRINTING STATISTICS RN ----------------------------------------------");
        stats = service.seeStats();
        for(String title : stats.keySet()) {
            System.out.println(title + ": " + stats.get(title));
        }
        System.out.println("======================================================================= \n");

        QuizResult jr21 = new QuizResult(1L,1,q1,john);
        QuizResult jr22 = new QuizResult(1L,1,q1,john);
        QuizResult ar21 = new QuizResult(1L,1,q1,alice);

        q1.addResult(jr21);
        q1.addResult(jr22);
        q1.addResult(ar21);

        System.out.println("PRINTING STATISTICS LAST ----------------------------------------------");
        stats = service.seeStats();
        for(String title : stats.keySet()) {
            System.out.println(title + ": " + stats.get(title));
        }
        System.out.println("======================================================================= \n");

        service.promoteUser(john);
        service.removeQuiz(q1.getId());

        System.out.println("PRINTING STATISTICS LASTLAST ----------------------------------------------");
        stats = service.seeStats();
        for(String title : stats.keySet()) {
            System.out.println(title + ": " + stats.get(title));
        }
        System.out.println("======================================================================= \n");
    }

    @Test
    public void test2() {
        User bondo = new User("bondo","gela");
        userRepo.save(bondo);
        bondo.promote();
        AdminService service = new AdminService(userRepo,quizRepo,announcementRepo);

        System.out.println("PRINTING STATISTICS RN ----------------------------------------------");
        HashMap<String,Long> stats = service.seeStats();
        for(String title : stats.keySet()) {
            System.out.println(title + ": " + stats.get(title));
        }
        System.out.println("======================================================================= \n");

        Announcement a1 = new Announcement(bondo,"1","linganguliguliguli");
        Announcement a2 = new Announcement(bondo,"2","wazaalinggangooo");
        service.addAnnouncement(a1);
        service.addAnnouncement(a2);

        System.out.println("======================================================================= \n");
        List<Announcement> lst = announcementRepo.findByAuthor(bondo);
        for(Announcement a : lst) {
            System.out.println(a.getTitle());
            System.out.println(a.getContent());
        }
        System.out.println("======================================================================= \n");

        service.deleteAnnouncement(a1.getId());


        System.out.println("======================================================================= \n");
        lst = announcementRepo.findByAuthor(bondo);
        for(Announcement a : lst) {
            System.out.println(a.getTitle());
            System.out.println(a.getContent());
        }
        System.out.println("======================================================================= \n");
    }

}
