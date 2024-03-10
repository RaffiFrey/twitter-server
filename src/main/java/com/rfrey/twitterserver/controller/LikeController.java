package com.rfrey.twitterserver.controller;

import com.rfrey.twitterserver.dto.LikeDto;
import com.rfrey.twitterserver.dto.mapper.LikeDtoMapper;
import com.rfrey.twitterserver.exception.LikeException;
import com.rfrey.twitterserver.exception.TweetException;
import com.rfrey.twitterserver.exception.UserException;
import com.rfrey.twitterserver.model.Like;
import com.rfrey.twitterserver.model.User;
import com.rfrey.twitterserver.service.LikeService;
import com.rfrey.twitterserver.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name="Like and Unlike Tweet")
public class LikeController {

    private final UserService userService;
    private final LikeService likeService;

    public LikeController(UserService userService, LikeService likeService) {
        this.userService = userService;
        this.likeService = likeService;
    }

    @PostMapping("/{tweetId}/like")
    public ResponseEntity<LikeDto> likeTweet(@PathVariable Long tweetId, @RequestHeader("Authorization") String jwt)
            throws UserException, TweetException {
        User user = userService.findUserProfileByJwt(jwt);
        Like like = likeService.likeTweet(tweetId, user);
        LikeDto likeDto = LikeDtoMapper.toLikeDto(like, user);
        return new ResponseEntity<>(likeDto, HttpStatus.CREATED);
    }
    @DeleteMapping("/tweet/{tweetId}")
    public ResponseEntity<LikeDto> unlikeTweet(@PathVariable Long tweetId, @RequestHeader("Authorization") String jwt)
            throws UserException, TweetException, LikeException {
        User user = userService.findUserProfileByJwt(jwt);
        Like like = likeService.unlikeTweet(tweetId, user);
        LikeDto likeDto = LikeDtoMapper.toLikeDto(like, user);
        return new ResponseEntity<>(likeDto, HttpStatus.CREATED);
    }
    @GetMapping("/tweet/{tweetId}")
    public ResponseEntity<List<LikeDto>> getAllLikes(@PathVariable Long tweetId, @RequestHeader("Authorization") String jwt)
            throws UserException, TweetException {
        User user = userService.findUserProfileByJwt(jwt);
        List<Like> likes = likeService.getAllLikes(tweetId);
        List<LikeDto> likeDtos = LikeDtoMapper.toLikeDtos(likes, user);
        return new ResponseEntity<>(likeDtos, HttpStatus.OK);
    }
}
