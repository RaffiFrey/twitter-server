package com.rfrey.twitterserver.util;

import com.rfrey.twitterserver.model.User;

import java.time.LocalDateTime;

public class UserUtil {

    public static boolean isReqUser(User reqUser, User user) {
        return reqUser.getId().equals(user.getId());
    }

    public static boolean isFollowedByReqUser(User reqUser, User user) {
        return reqUser.getFollowings().contains(user);
    }

    public static boolean isVerified(LocalDateTime endsDate) {
        if (endsDate != null) {
            return endsDate.isAfter(LocalDateTime.now());
        }
        return false;
    }
}
