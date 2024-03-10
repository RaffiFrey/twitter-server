package com.rfrey.twitterserver.controller;

import com.rfrey.twitterserver.dto.TweetDto;
import com.rfrey.twitterserver.dto.mapper.TweetDtoMapper;
import com.rfrey.twitterserver.exception.TweetException;
import com.rfrey.twitterserver.exception.UserException;
import com.rfrey.twitterserver.model.Tweet;
import com.rfrey.twitterserver.model.User;
import com.rfrey.twitterserver.request.TweetReplyRequest;
import com.rfrey.twitterserver.response.ApiResponse;
import com.rfrey.twitterserver.service.TweetService;
import com.rfrey.twitterserver.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tweets")
@Tag(name="Tweet Management", description = "Endpoints for managing tweets")
public class TweetController {

    private final TweetService tweetService;
    private final UserService userService;

    public TweetController(TweetService tweetService, UserService userService) {
        this.tweetService = tweetService;
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<TweetDto> createTweet(@RequestBody Tweet req, @RequestHeader("Authorization")String jwt) throws UserException, TweetException {
        User user = userService.findUserProfileByJwt(jwt);
        Tweet tweet = tweetService.createTweet(req, user);
        TweetDto tweetDto = TweetDtoMapper.toTweetDto(tweet, user);
        return new ResponseEntity<>(tweetDto, HttpStatus.CREATED);
    }
    @PostMapping("/reply")
    public ResponseEntity<TweetDto> replyTweet(@RequestBody TweetReplyRequest req, @RequestHeader("Authorization")String jwt) throws UserException, TweetException {
        User user = userService.findUserProfileByJwt(jwt);
        Tweet tweet = tweetService.createReply(req, user);
        TweetDto tweetDto = TweetDtoMapper.toTweetDto(tweet, user);
        return new ResponseEntity<>(tweetDto, HttpStatus.CREATED);
    }
    @PutMapping("/{tweetId}/retweet")
    public ResponseEntity<TweetDto> retweet(@PathVariable Long tweetId, @RequestHeader("Authorization") String jwt) throws UserException, TweetException {
        User user = userService.findUserProfileByJwt(jwt);
        Tweet tweet = tweetService.retweet(tweetId, user);
        TweetDto tweetDto = TweetDtoMapper.toTweetDto(tweet, user);
        return new ResponseEntity<>(tweetDto, HttpStatus.OK);
    }
    @GetMapping("/{tweetId}")
    public ResponseEntity<TweetDto> findTweetById(@PathVariable Long tweetId, @RequestHeader("Authorization") String jwt) throws UserException, TweetException {
        User user = userService.findUserProfileByJwt(jwt);
        Tweet tweet = tweetService.findById(tweetId);
        TweetDto tweetDto = TweetDtoMapper.toTweetDto(tweet, user);
        return new ResponseEntity<>(tweetDto, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{tweetId}")
    public ResponseEntity<ApiResponse> deleteTweetById(@PathVariable Long tweetId, @RequestHeader("Authorization") String jwt) throws UserException, TweetException {
        User user=userService.findUserProfileByJwt(jwt);

        tweetService.deleteTweetById(tweetId, user.getId());

        ApiResponse res=new ApiResponse();
        res.setMessage("twit deleted successfully");
        res.setStatus(true);

        return new ResponseEntity<>(res,HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<TweetDto>> findAllTwits(@RequestHeader("Authorization") String jwt) throws UserException{
        User user=userService.findUserProfileByJwt(jwt);
        List<Tweet> twits=tweetService.findAllTweets();
        List<TweetDto> twitDtos=TweetDtoMapper.toTweetDtos(twits,user);
        return new ResponseEntity<List<TweetDto>>(twitDtos,HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TweetDto>> getUsersTwits(@PathVariable Long userId,
                                                       @RequestHeader("Authorization") String jwt)
            throws UserException{
        User reqUser=userService.findUserProfileByJwt(jwt);
        User user=userService.findUserById(userId);
        List<Tweet> twits=tweetService.getUsersTweets(user);
        List<TweetDto> twitDtos=TweetDtoMapper.toTweetDtos(twits,reqUser);
        return new ResponseEntity<List<TweetDto>>(twitDtos,HttpStatus.OK);
    }

    @GetMapping("/user/{userId}/likes")
    public ResponseEntity<List<TweetDto>> findTwitByLikesContainsUser(@PathVariable Long userId,
                                                                     @RequestHeader("Authorization") String jwt)
            throws UserException{
        User reqUser=userService.findUserProfileByJwt(jwt);
        User user=userService.findUserById(userId);
        List<Tweet> twits=tweetService.findByLikesContainsUser(user);
        List<TweetDto> twitDtos=TweetDtoMapper.toTweetDtos(twits,reqUser);
        return new ResponseEntity<List<TweetDto>>(twitDtos,HttpStatus.OK);
    }
}
