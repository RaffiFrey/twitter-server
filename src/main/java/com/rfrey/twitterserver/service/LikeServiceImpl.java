package com.rfrey.twitterserver.service;

import com.rfrey.twitterserver.exception.LikeException;
import com.rfrey.twitterserver.exception.TweetException;
import com.rfrey.twitterserver.exception.UserException;
import com.rfrey.twitterserver.model.Like;
import com.rfrey.twitterserver.model.Tweet;
import com.rfrey.twitterserver.model.User;
import com.rfrey.twitterserver.repository.LikeRepository;
import com.rfrey.twitterserver.repository.TweetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;
    private final TweetRepository tweetRepository;
    private final TweetService tweetService;

    public LikeServiceImpl(LikeRepository likeRepository, TweetRepository tweetRepository, TweetService tweetService) {
        this.likeRepository = likeRepository;
        this.tweetRepository = tweetRepository;
        this.tweetService = tweetService;
    }

    @Override
    public Like likeTweet(Long tweetId, User user) throws UserException, TweetException {
        Like isLikeExisting = likeRepository.isLikeExist(user.getId(), tweetId);
        if (isLikeExisting != null) {
            likeRepository.deleteById(isLikeExisting.getId());
            return isLikeExisting;
        }
        Tweet tweet = tweetService.findById(tweetId);
        Like like = new Like();
        like.setTweet(tweet);
        like.setUser(user);
        Like savedLike = likeRepository.save(like);
        tweet.getLikes().add(savedLike);
        tweetRepository.save(tweet);
        return savedLike;
    }

    @Override
    public Like unlikeTweet(Long tweetId, User user) throws UserException, TweetException, LikeException {
        Like like = likeRepository.findById(tweetId).orElseThrow(() -> new LikeException("Like not found"));
        if (like.getUser().getId().equals(user.getId())) {
            throw new UserException("Something went wrong");
        }
        likeRepository.deleteById(like.getId());
        return like;
    }

    @Override
    public List<Like> getAllLikes(Long tweetId) throws TweetException {
        Tweet tweet = tweetService.findById(tweetId);
        return likeRepository.findByTweetId(tweet.getId());
    }
}
