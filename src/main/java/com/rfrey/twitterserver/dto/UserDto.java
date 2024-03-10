package com.rfrey.twitterserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String fullName;
    private String email;
    private String image;
    private String location;
    private String website;
    private String birthDate;
    private String mobile;
    private String backgroundImage;
    private String bio;
    private boolean reqUser;
    private boolean loginWithGoogle;
    private List<UserDto> followers = new ArrayList<>();
    private List<UserDto> followings = new ArrayList<>();
    private boolean followed;
    private boolean isVerified;
}
