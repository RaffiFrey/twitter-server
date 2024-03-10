package com.rfrey.twitterserver.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TweetReplyRequest {

    private String content;
    private Long tweetId;
    private LocalDateTime createdAt;
    private String image;
}
