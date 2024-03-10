package com.rfrey.twitterserver.controller;

import com.rfrey.twitterserver.dto.UserDto;
import com.rfrey.twitterserver.dto.mapper.UserDtoMapper;
import com.rfrey.twitterserver.exception.UserException;
import com.rfrey.twitterserver.model.User;
import com.rfrey.twitterserver.service.UserService;
import com.rfrey.twitterserver.util.UserUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name="User Management", description = "Endpoints for managing user profiles and information")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDto> getUserProfileHandler(@RequestHeader("Authorization") String jwt)
            throws UserException {

        User user=userService.findUserProfileByJwt(jwt);
        user.setPassword(null);
        user.setReqUser(true);
        UserDto userDto= UserDtoMapper.toUserDto(user);
        userDto.setReqUser(true);
        return new ResponseEntity<>(userDto, HttpStatus.ACCEPTED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserByIdHandler(@PathVariable Long userId,
                                                      @RequestHeader("Authorization") String jwt)
            throws UserException{

        User reqUser=userService.findUserProfileByJwt(jwt);

        User user=userService.findUserById(userId);

        UserDto userDto=UserDtoMapper.toUserDto(user);
        userDto.setReqUser(UserUtil.isReqUser(reqUser, user));
        userDto.setFollowed(UserUtil.isFollowedByReqUser(reqUser, user));
        return new ResponseEntity<>(userDto,HttpStatus.ACCEPTED);
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserDto>> searchUserHandler(@RequestParam String query,
                                                           @RequestHeader("Authorization") String jwt)
            throws UserException{

        User reqUser=userService.findUserProfileByJwt(jwt);

        List<User> users=userService.searchUser(query);

        List<UserDto> userDtos=UserDtoMapper.toUserDtos(users);

        return new ResponseEntity<>(userDtos,HttpStatus.ACCEPTED);
    }

    @PutMapping("/update")
    public ResponseEntity<UserDto> updateUserHandler(@RequestBody User req,
                                                     @RequestHeader("Authorization") String jwt)
            throws UserException{

        System.out.println("update user  "+req);
        User user=userService.findUserProfileByJwt(jwt);

        User updatedUser=userService.updateUser(user.getId(), req);
        updatedUser.setPassword(null);
        UserDto userDto=UserDtoMapper.toUserDto(user);
        userDto.setReqUser(true);
        return new ResponseEntity<>(userDto,HttpStatus.ACCEPTED);
    }

    @PutMapping("/{userId}/follow")
    public ResponseEntity<UserDto> followUserHandler(@PathVariable Long userId, @RequestHeader("Authorization") String jwt)
            throws UserException{

        User user=userService.findUserProfileByJwt(jwt);

        User updatedUser=userService.followUser(userId, user);
        UserDto userDto=UserDtoMapper.toUserDto(updatedUser);
        userDto.setFollowed(UserUtil.isFollowedByReqUser(user, updatedUser));
        return new ResponseEntity<>(userDto,HttpStatus.ACCEPTED);
    }
}
