package com.rfrey.twitterserver.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.rfrey.twitterserver.exception.UserException;
import com.rfrey.twitterserver.config.JwtProvider;
import com.rfrey.twitterserver.model.User;
import com.rfrey.twitterserver.model.Verification;
import com.rfrey.twitterserver.repository.UserRepository;
import com.rfrey.twitterserver.request.LoginWithGoogleRequest;
import com.rfrey.twitterserver.response.AuthResponse;
import com.rfrey.twitterserver.service.CustomUserDetailsServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.CredentialException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final CustomUserDetailsServiceImpl customUserDetailsService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider, CustomUserDetailsServiceImpl customUserDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.customUserDetailsService = customUserDetailsService;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws UserException {

        System.out.println("user " + user);
        String email = user.getEmail();
        String password = user.getPassword();
        String fullName = user.getFullName();
        String birthDate = user.getBirthDate();

        User isEmailExist = userRepository.findByEmail(email);
        if (isEmailExist != null) {
            throw new UserException("Email is already in use!");
        }

        User createdUser = new User();
        createdUser.setEmail(email);
        createdUser.setFullName(fullName);
        createdUser.setBirthDate(birthDate);
        createdUser.setPassword(passwordEncoder.encode(password));
        createdUser.setVerification(new Verification());

        User savedUser = userRepository.save(createdUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);

        AuthResponse res = new AuthResponse(token, true);

        return new ResponseEntity<AuthResponse>(res, HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signin(@RequestBody User user) {
        String username = user.getEmail();
        String password = user.getPassword();

        Authentication authentication = authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);
        AuthResponse res = new AuthResponse(token, true);

        return new ResponseEntity<AuthResponse>(res, HttpStatus.ACCEPTED);
    }

    @PostMapping("/signin/google")
    public ResponseEntity<AuthResponse> googleLogin(@RequestBody LoginWithGoogleRequest req) throws GeneralSecurityException, IOException {
        User user = validateGoogleIdToken(req);

        String email = user.getEmail();
        User existingUser = userRepository.findByEmail(email);

        if (existingUser == null) {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setImage(user.getImage());
            newUser.setFullName(user.getFullName());
            newUser.setLoginWithGoogle(true);
            newUser.setPassword(user.getPassword());
            newUser.setVerification(new Verification());
            userRepository.save(newUser);
        }
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setStatus(true);
        authResponse.setJwt(token);
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    private User validateGoogleIdToken(LoginWithGoogleRequest req) throws GeneralSecurityException, IOException {
        HttpTransport transport = new NetHttpTransport();
        JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(req.getClientId()))
                .build();
        GoogleIdToken token = verifier.verify(req.getCredential());
        if (req.getCredential() != null) {
            GoogleIdToken.Payload payload = token.getPayload();
            String email = payload.getEmail();
            String userId = payload.getEmail();
            String name = (String)payload.get("name");
            String pictureUrl = (String)payload.get("picture");

            User user = new User();
            user.setImage(pictureUrl);
            user.setEmail(email);
            user.setFullName(name);
            user.setPassword(userId);

            return user;
        }
        else {
            throw new CredentialException("Invalid token send from google");
        }
    }

    private Authentication authenticate(String username, String password) throws BadCredentialsException {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        if (userDetails == null) {
            throw new BadCredentialsException("Invalid username");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword()))
        {
            throw new BadCredentialsException("Invalid password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
