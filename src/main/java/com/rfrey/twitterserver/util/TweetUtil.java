package com.rfrey.twitterserver.util;

import com.rfrey.twitterserver.model.Like;
import com.rfrey.twitterserver.model.Tweet;
import com.rfrey.twitterserver.model.User;

public class TweetUtil {

    public static boolean isLikedByReqUser(User reqUser, Tweet tweet) {
        for (Like like : tweet.getLikes()) {
            if (like.getUser().getId().equals(reqUser.getId())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isReteweetedByReqUser(User reqUser, Tweet tweet) {
        for (User user : tweet.getRetweetUsers()) {
            if (user.getId().equals(reqUser.getId())) {
                return true;
            }
        }
        return false;
    }
}
