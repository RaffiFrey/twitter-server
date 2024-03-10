package com.rfrey.twitterserver.request;

import lombok.Data;

@Data
public class LoginWithGoogleRequest {

    private String credential;
    private String clientId;
}
