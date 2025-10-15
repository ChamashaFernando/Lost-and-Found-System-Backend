package lk.chamasha.lost.and.found.service.impl;//package lk.chamasha.lost.and.found.service.impl;
//
//import lk.chamasha.lost.and.found.controller.request.UserRequest;
//import lk.chamasha.lost.and.found.controller.response.UserResponse;
//import lk.chamasha.lost.and.found.model.User;
//import lk.chamasha.lost.and.found.repository.UserRepository;
//import lk.chamasha.lost.and.found.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class UserServiceImpl implements UserService {
//    private final UserRepository userRepository;
//
////    @Override
////    public UserResponse createUser(UserRequest request) {
////        User user = new User(null, request.getName(), request.getEmail(), false);
////        return mapToResponse(userRepository.save(user));
////    }
////
////    @Override
////    public UserResponse getUserById(Long id) {
////        return userRepository.findById(id)
////                .map(this::mapToResponse)
////                .orElseThrow(() -> new RuntimeException("User not found"));
////    }
////
////    @Override
////    public List<UserResponse> getAllUsers() {
////        return userRepository.findAll().stream()
////                .map(this::mapToResponse)
////                .collect(Collectors.toList());
////    }
////
////    @Override
////    public void verifyUser(Long id) {
////        User user = userRepository.findById(id)
////                .orElseThrow(() -> new RuntimeException("User not found"));
////        user.setVerified(true);
////        userRepository.save(user);
////    }
////
////    private UserResponse mapToResponse(User user) {
////        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.isVerified());
////    }
//
//
//}



import lk.chamasha.lost.and.found.controller.request.UserRequest;
import lk.chamasha.lost.and.found.controller.response.UserResponse;
import lk.chamasha.lost.and.found.exception.UserNotCreatedException;
import lk.chamasha.lost.and.found.exception.UserNotFoundException;
import lk.chamasha.lost.and.found.model.LanguageType;
import lk.chamasha.lost.and.found.model.User;
import lk.chamasha.lost.and.found.repository.UserRepository;
import lk.chamasha.lost.and.found.security.ApplicationConfig;
import lk.chamasha.lost.and.found.security.JwtService;
import lk.chamasha.lost.and.found.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ApplicationConfig applicationConfig;
    private final JwtService jwtService;

    @Override
    public UserResponse register(UserRequest userRequest) throws UserNotCreatedException {
        if (userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
            throw new UserNotCreatedException("User already exists with email: " + userRequest.getEmail());
        }

        LanguageType language = null;
        if (userRequest.getLanguagePreference() != null) {
            try {
                language = LanguageType.valueOf(userRequest.getLanguagePreference().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid language type: " + userRequest.getLanguagePreference());
            }
        }

        User user = User.builder()
                .fullName(userRequest.getFullName())
                .email(userRequest.getEmail())
                .password(applicationConfig.passwordEncoder().encode(userRequest.getPassword()))
                .role(userRequest.getRole())
                .languagePreference(language)
                .reputationScore(0)
                .verified(false)
                .build();

        userRepository.save(user);

        Map<String, Object> claims = generateClaims(user);
        String token = jwtService.generateToken(user, claims);

        return buildUserResponse(user, token);
    }

    @Override
    public UserResponse login(UserRequest userRequest) throws UserNotFoundException {
        if(userRequest.getPassword() == null || userRequest.getPassword().isEmpty()){
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        User user = userRepository.findByEmail(userRequest.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + userRequest.getEmail()));

        if (!applicationConfig.passwordEncoder().matches(userRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // Update location if available
        if(userRequest.getLatitude() != null && userRequest.getLongitude() != null){
            user.setLatitude(userRequest.getLatitude());
            user.setLongitude(userRequest.getLongitude());
            userRepository.save(user);
        }

        Map<String, Object> claims = generateClaims(user);
        String token = jwtService.generateToken(user, claims);

        return buildUserResponse(user, token);
    }


    @Override
    public UserResponse getUserById(Long id) throws UserNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));

        return buildUserResponse(user, null); // Token not returned here
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> buildUserResponse(user, null))
                .toList();
    }

    // Helper: build JWT claims
    private Map<String, Object> generateClaims(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        claims.put("role", "ROLE_" + user.getRole().name());
        claims.put("fullName", user.getFullName());
        claims.put("verified", user.isVerified());
        claims.put("reputationScore", user.getReputationScore());
        if (user.getLanguagePreference() != null) {
            claims.put("language", user.getLanguagePreference().name());
        }
        return claims;
    }

    // Helper: build response object
    private UserResponse buildUserResponse(User user, String token) {
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .languagePreference(user.getLanguagePreference() != null ? user.getLanguagePreference().name() : null)
                .reputationScore(user.getReputationScore())
                .verified(user.isVerified())
                .token(token)
                .build();
    }
}


