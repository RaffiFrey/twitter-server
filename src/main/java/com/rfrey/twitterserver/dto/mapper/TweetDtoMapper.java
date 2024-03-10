package com.rfrey.twitterserver.dto.mapper;

import com.rfrey.twitterserver.dto.TweetDto;
import com.rfrey.twitterserver.dto.UserDto;
import com.rfrey.twitterserver.model.Tweet;
import com.rfrey.twitterserver.model.User;
import com.rfrey.twitterserver.util.TweetUtil;

import java.util.ArrayList;
import java.util.List;

public class TweetDtoMapper {

    public static TweetDto toTweetDto(Tweet tweet, User user) {
        UserDto userDto = UserDtoMapper.toUserDto(tweet.getUser());
        boolean isLiked = TweetUtil.isLikedByReqUser(user, tweet);
        boolean isRetweeted = TweetUtil.isLikedByReqUser(user, tweet);

        List<Long> retweetedUserId = new ArrayList<>();

        for (User u : tweet.getRetweetUsers()) {
            retweetedUserId.add(u.getId());
        }

        TweetDto tweetDto = new TweetDto();
        tweetDto.setId(tweet.getId());
        tweetDto.setContent(tweet.getContent());
        tweetDto.setCreatedAt(tweet.getCreatedAt());
        tweetDto.setImage(tweet.getContent());
        tweetDto.setTotalLikes(tweet.getLikes().size());
        tweetDto.setTotalReplies(tweet.getReplyTweets().size());
        tweetDto.setTotalRetweets(tweet.getRetweetUsers().size());
        tweetDto.setUser(userDto);
        tweetDto.setLiked(isLiked);
        tweetDto.setRetweeted(isRetweeted);
        tweetDto.setRetweetUsersId(retweetedUserId);
        tweetDto.setReplayTweets(toTweetDtos(tweet.getReplyTweets(), user));
        tweetDto.setVideo(tweetDto.getVideo());
        return tweetDto;
    }

    public static TweetDto toReplyTweetDto(Tweet tweet, User user) {
        return toTweetDto(tweet, user);
    }

    public static List<TweetDto> toTweetDtos(List<Tweet> tweets, User user) {
        List<TweetDto> tweetDtos = new ArrayList<>();
        for (Tweet tweet : tweets) {
            tweetDtos.add(toReplyTweetDto(tweet, user));
        }
        return tweetDtos;
    }
}
