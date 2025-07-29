package com.example.quizwebproject.repos;

import com.example.quizwebproject.model.users.User;
import com.example.quizwebproject.model.users.admin.Announcement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AnnouncementRepo extends JpaRepository<Announcement, Long> {
    List<Announcement> findByAuthor(User author);

    @Query("SELECT a FROM Announcement a WHERE a.dateTime > :cutOff ORDER BY a.dateTime DESC")
    Page<Announcement> getRecentTenDayAnnouncements(@Param("cutOff") LocalDateTime cutOff, Pageable pageable);
}
