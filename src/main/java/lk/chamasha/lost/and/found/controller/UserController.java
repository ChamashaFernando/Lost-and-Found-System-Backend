//package lk.chamasha.lost.and.found.controller;
//
//import jakarta.annotation.security.RolesAllowed;
//import lk.chamasha.lost.and.found.controller.request.UserRequest;
//import lk.chamasha.lost.and.found.controller.response.UserResponse;
//import lk.chamasha.lost.and.found.exception.UserNotCreatedException;
//import lk.chamasha.lost.and.found.exception.UserNotFoundException;
//import lk.chamasha.lost.and.found.repository.UserRepository;
//import lk.chamasha.lost.and.found.service.UserService;
//import lk.chamasha.lost.and.found.service.impl.FcmService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Map; // ✅ Missing import added
//
//@RestController
//@RequestMapping("/api/users")
//@RequiredArgsConstructor
//public class UserController {
//
//    private final UserService userService;
//    private final UserRepository userRepository;
//    private final FcmService fcmService;
//
//
//    // ✅ Register new user
//    @PostMapping("/signup")
//    public ResponseEntity<UserResponse> register(@RequestBody UserRequest userRequest) throws UserNotCreatedException {
//        UserResponse response = userService.register(userRequest);
//        return ResponseEntity.ok(response);
//    }
//
//    @PostMapping("/{userId}/send-notification")
//    public ResponseEntity<String> sendNotificationToUser(
//            @PathVariable Long userId,
//            @RequestBody Map<String, String> request
//    ) {
//        String fcmToken = userService.getFcmTokenByUserId(userId);
//        String title = request.get("title");
//        String body = request.get("body");
//
//        if (fcmToken == null) {
//            return ResponseEntity.badRequest().body("User does not have FCM token saved.");
//        }
//
//        String messageId = fcmService.sendNotification(fcmToken, title, body);
//        return ResponseEntity.ok("Notification sent with ID: " + messageId);
//    }
//
//    // ✅ Login
//    @PostMapping("/login")
//    public ResponseEntity<UserResponse> login(@RequestBody UserRequest userRequest) throws UserNotFoundException {
//        UserResponse response = userService.login(userRequest);
//        return ResponseEntity.ok(response);
//    }
//
//    // ✅ Get user by ID
//    @RolesAllowed({"ADMIN","STUDENT"})
//    @GetMapping("/{id}")
//    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) throws UserNotFoundException {
//        UserResponse response = userService.getUserById(id);
//        return ResponseEntity.ok(response);
//    }
//
//    // ✅ Get all users
//    @RolesAllowed({"ADMIN","STUDENT"})
//    @GetMapping
//    public ResponseEntity<List<UserResponse>> getAllUsers() {
//        List<UserResponse> responseList = userService.getAllUsers();
//        return ResponseEntity.ok(responseList);
//    }
//}


package lk.chamasha.lost.and.found.controller;

import jakarta.annotation.security.RolesAllowed;
import lk.chamasha.lost.and.found.controller.request.UserRequest;
import lk.chamasha.lost.and.found.controller.response.UserResponse;
import lk.chamasha.lost.and.found.exception.UserNotCreatedException;
import lk.chamasha.lost.and.found.exception.UserNotFoundException;
import lk.chamasha.lost.and.found.repository.UserRepository;
import lk.chamasha.lost.and.found.service.UserService;
import lk.chamasha.lost.and.found.service.impl.FcmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final FcmService fcmService;

    // ===========================
    // ✅ User Registration
    // ===========================
    @PostMapping("/signup")
    public ResponseEntity<UserResponse> register(@RequestBody UserRequest userRequest) throws UserNotCreatedException {
        UserResponse response = userService.register(userRequest);
        return ResponseEntity.ok(response);
    }

    // ===========================
    // ✅ User Login
    // ===========================
    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody UserRequest userRequest) throws UserNotFoundException {
        UserResponse response = userService.login(userRequest);
        return ResponseEntity.ok(response);
    }

    // ===========================
    // ✅ Send Push Notification to User
    // ===========================
    @PostMapping("/{userId}/send-notification")
    public ResponseEntity<String> sendNotificationToUser(
            @PathVariable Long userId,
            @RequestBody Map<String, String> request
    ) {
        String fcmToken = userService.getFcmTokenByUserId(userId);
        String title = request.get("title");
        String body = request.get("body");

        if (fcmToken == null) {
            return ResponseEntity.badRequest().body("User does not have FCM token saved.");
        }

        try {
            // FIX: catch exception
            fcmService.sendNotification(fcmToken, title, body);
            return ResponseEntity.ok("Notification sent successfully");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body("Failed to send notification: " + e.getMessage());
        }
    }

    // ===========================
    // ✅ Get User by ID
    // ===========================
    @RolesAllowed({"ADMIN","STUDENT"})
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) throws UserNotFoundException {
        UserResponse response = userService.getUserById(id);
        return ResponseEntity.ok(response);
    }

    // ===========================
    // ✅ Get All Users
    // ===========================
    @RolesAllowed({"ADMIN","STUDENT"})
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> responseList = userService.getAllUsers();
        return ResponseEntity.ok(responseList);
    }
    @PostMapping("/{userId}/token")
    public ResponseEntity<String> updateFcmToken(
            @PathVariable Long userId,
            @RequestBody Map<String, String> request
    ) {
        String fcmToken = request.get("fcmToken");

        if (fcmToken == null || fcmToken.isBlank()) {
            return ResponseEntity.badRequest().body("FCM token is required");
        }

        userService.updateFcmToken(userId, fcmToken);

        return ResponseEntity.ok("FCM token updated successfully");
    }
}
