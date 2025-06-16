package com.example.repos;

import com.example.model.users.User;
import com.example.model.users.admin.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnouncementRepo extends JpaRepository<Announcement, Long> {
    List<Announcement> findByAuthor(User author);
    Announcement findByTitle(String title);
}
