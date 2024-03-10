package com.rfrey.twitterserver.controller;

import com.rfrey.twitterserver.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<ApiResponse> homeController() {
        ApiResponse res = new ApiResponse("Welcome to Twitter Api", true);
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }
}
