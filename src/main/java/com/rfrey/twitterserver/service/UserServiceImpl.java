package com.rfrey.twitterserver.service;

import com.rfrey.twitterserver.config.JwtProvider;
import com.rfrey.twitterserver.exception.UserException;
import com.rfrey.twitterserver.model.User;
import com.rfrey.twitterserver.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    public UserServiceImpl(UserRepository userRepository, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public User findUserById(Long userId) throws UserException {
        return userRepository.findById(userId).orElseThrow(() -> new UserException("No user found with id: " + userId));
    }

    @Override
    public User findUserProfileByJwt(String jwt) throws UserException {
        String email = jwtProvider.getEmailFromToken(jwt);
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserException("There is no user with email: " + email);
        }
        return user;
    }

    @Override
    public User updateUser(Long userId, User user) throws UserException {
        User currentUser = findUserById(userId);
        if(user.getFullName()!= null) {
            currentUser.setFullName(user.getFullName());
        }
        if(user.getImage()!=null) {
            currentUser.setImage(user.getImage());
        }
        if(user.getBackgroundImage()!=null) {
            currentUser.setBackgroundImage(user.getBackgroundImage());
        }
        if(user.getBirthDate()!=null) {
            currentUser.setBirthDate(user.getBirthDate());
        }
        if(user.getLocation()!=null) {
            currentUser.setLocation(user.getLocation());
        }
        if(user.getBio()!=null) {
            currentUser.setBio(user.getBio());
        }
        if(user.getWebsite()!=null) {
            currentUser.setWebsite(user.getWebsite());
        }
        return userRepository.save(user);
    }

    @Override
    public User followUser(Long userId, User user) throws UserException {
        User userToFollow = findUserById(userId);
        if (user.getFollowings().contains(userToFollow) && userToFollow.getFollowers().contains(user)) {
            user.getFollowings().remove(userToFollow);
            userToFollow.getFollowers().remove(user);
        }
        else {
            userToFollow.getFollowers().add(user);
            user.getFollowings().add(userToFollow);
        }
        userRepository.save(user);
        userRepository.save(userToFollow);
        return userToFollow;
    }

    @Override
    public List<User> searchUser(String query) {
        return userRepository.searchUser(query);
    }
}
