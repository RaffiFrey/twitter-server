package com.rfrey.twitterserver.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TweetRequest {
    private String content;
    private Long tweetId;
    private LocalDateTime createdAt;
    private String image;
    private boolean isReplay;
    private boolean isTweet;
    private Long replayFor;
}
