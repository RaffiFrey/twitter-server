package com.rfrey.twitterserver.service;

import com.rfrey.twitterserver.exception.LikeException;
import com.rfrey.twitterserver.exception.TweetException;
import com.rfrey.twitterserver.exception.UserException;
import com.rfrey.twitterserver.model.Like;
import com.rfrey.twitterserver.model.User;

import java.util.List;

public interface LikeService {

    public Like likeTweet(Long tweetId, User user) throws UserException, TweetException;
    public Like unlikeTweet(Long tweetId, User user) throws UserException, TweetException, LikeException;
    public List<Like> getAllLikes(Long tweetId) throws TweetException;
}
