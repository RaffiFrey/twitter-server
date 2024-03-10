package com.rfrey.twitterserver.service;

import com.rfrey.twitterserver.exception.TweetException;
import com.rfrey.twitterserver.exception.UserException;
import com.rfrey.twitterserver.model.Tweet;
import com.rfrey.twitterserver.model.User;
import com.rfrey.twitterserver.repository.TweetRepository;
import com.rfrey.twitterserver.request.TweetReplyRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TweetServiceImpl implements TweetService {

    private final TweetRepository tweetRepository;

    public TweetServiceImpl(TweetRepository tweetRepository) {
        this.tweetRepository = tweetRepository;
    }

    @Override
    public Tweet createTweet(Tweet req, User user) throws UserException {
        Tweet tweet = new Tweet();
        tweet.setContent(req.getContent());
        tweet.setCreatedAt(LocalDateTime.now());
        tweet.setImage(req.getImage());
        tweet.setUser(user);
        tweet.setReply(false);
        tweet.setTweet(true);
        tweet.setVideo(req.getVideo());
        return tweetRepository.save(tweet);
    }

    @Override
    public List<Tweet> findAllTweets() {
        return tweetRepository.findAllByisTweetTrueOrderByCreatedAtDesc();
    }

    @Override
    public Tweet retweet(Long tweetId, User user) throws TweetException {
        Tweet tweet = findById(tweetId);
        if (tweet.getRetweetUsers().contains(user)) {
            tweet.getRetweetUsers().remove(user);
        }
        else {
            tweet.getRetweetUsers().add(user);
        }
        return tweetRepository.save(tweet);
    }

    @Override
    public Tweet findById(Long tweetId) throws TweetException {
        return tweetRepository.findById(tweetId).orElseThrow(() -> new TweetException("Tweet not found with Id: " + tweetId));
    }

    @Override
    public void deleteTweetById(Long tweetId, Long userId) throws TweetException, UserException {
        Tweet tweet = findById(tweetId);

        if (!userId.equals(tweet.getUser().getId())) {
            throw new UserException("You can't delete another users tweet!");
        }
        tweetRepository.deleteById(tweet.getId());
    }

    @Override
    public Tweet removeFromRetweet(Long tweetId, User user) throws TweetException, UserException {
        Tweet tweet = findById(tweetId);
        tweet.getRetweetUsers().remove(user);
        return tweetRepository.save(tweet);
    }

    @Override
    public Tweet createReply(TweetReplyRequest req, User user) throws TweetException {
        Tweet tweet = findById(req.getTweetId());
        Tweet reply = new Tweet();
        reply.setContent(req.getContent());
        reply.setCreatedAt(LocalDateTime.now());
        reply.setImage(req.getImage());
        reply.setUser(user);
        reply.setReplyFor(tweet);
        reply.setReply(true);
        reply.setTweet(false);
        Tweet savedReply = tweetRepository.save(reply);
        tweet.getReplyTweets().add(savedReply);
        tweetRepository.save(tweet);
        return tweet;
    }

    @Override
    public List<Tweet> getUsersTweets(User user) {
        return tweetRepository.findByRetweetUsersContainsOrUser_IdAndIsTweetTrueOrderByCreatedAtDesc(user, user.getId());
    }

    @Override
    public List<Tweet> findByLikesContainsUser(User user) {
        return tweetRepository.findByLikesUser_Id(user.getId());
    }
}
