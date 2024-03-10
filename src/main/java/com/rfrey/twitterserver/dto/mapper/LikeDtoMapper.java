package com.rfrey.twitterserver.dto.mapper;

import com.rfrey.twitterserver.dto.LikeDto;
import com.rfrey.twitterserver.dto.TweetDto;
import com.rfrey.twitterserver.dto.UserDto;
import com.rfrey.twitterserver.model.Like;
import com.rfrey.twitterserver.model.User;

import java.util.ArrayList;
import java.util.List;

public class LikeDtoMapper {

    public static LikeDto toLikeDto(Like like, User user) {

        UserDto userDto = UserDtoMapper.toUserDto(like.getUser());
        TweetDto tweetDto = TweetDtoMapper.toTweetDto(like.getTweet(), user);

        LikeDto likeDto = new LikeDto();
        likeDto.setId(like.getId());
        likeDto.setTweet(tweetDto);
        likeDto.setUser(userDto);

        return likeDto;
    }

    public static List<LikeDto> toLikeDtos(List<Like> likes, User user) {
        List<LikeDto> likeDtos = new ArrayList<>();

        for (Like like : likes) {
            likeDtos.add(toLikeDto(like, user));
        }
        return likeDtos;
    }
}
