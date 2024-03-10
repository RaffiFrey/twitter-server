package com.rfrey.twitterserver.repository;

import com.rfrey.twitterserver.model.Tweet;
import com.rfrey.twitterserver.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TweetRepository extends JpaRepository<Tweet, Long> {

    List<Tweet> findAllByisTweetTrueOrderByCreatedAtDesc();

    List<Tweet> findByRetweetUsersContainsOrUser_IdAndIsTweetTrueOrderByCreatedAtDesc(User user, Long userId);

    List<Tweet> findByLikesContainingOrderByCreatedAtDesc(User user);
    @Query("SELECT t FROM Tweet t JOIN t.likes l WHERE l.user.id=:userId")
    List<Tweet> findByLikesUser_Id(@Param("userId") Long userId);
}
