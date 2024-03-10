package com.rfrey.twitterserver.service;

import com.rfrey.twitterserver.exception.TweetException;
import com.rfrey.twitterserver.exception.UserException;
import com.rfrey.twitterserver.model.Tweet;
import com.rfrey.twitterserver.model.User;
import com.rfrey.twitterserver.request.TweetReplyRequest;

import java.util.List;

public interface TweetService {

    public Tweet createTweet(Tweet req, User user) throws UserException;
    public List<Tweet> findAllTweets();
    public Tweet retweet(Long tweetId, User user) throws TweetException;
    public Tweet findById(Long tweetId) throws TweetException;
    public void deleteTweetById(Long tweetId, Long userId) throws TweetException, UserException;
    public Tweet removeFromRetweet(Long tweetId, User user) throws TweetException, UserException;
    public Tweet createReply(TweetReplyRequest req, User user) throws TweetException;
    public List<Tweet> getUsersTweets(User user);
    public List<Tweet> findByLikesContainsUser(User user);

}
