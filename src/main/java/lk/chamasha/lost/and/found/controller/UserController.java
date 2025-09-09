package lk.chamasha.lost.and.found.controller;

import jakarta.annotation.security.RolesAllowed;
import lk.chamasha.lost.and.found.controller.request.UserRequest;
import lk.chamasha.lost.and.found.controller.response.UserResponse;
import lk.chamasha.lost.and.found.exception.UserNotCreatedException;
import lk.chamasha.lost.and.found.exception.UserNotFoundException;
import lk.chamasha.lost.and.found.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

//    @RolesAllowed({"ADMIN","STUDENT"})
    @PostMapping("/signup")
    public ResponseEntity<UserResponse> register(@RequestBody UserRequest userRequest) throws UserNotCreatedException {
        UserResponse response = userService.register(userRequest);
        return ResponseEntity.ok(response);
    }

//    @RolesAllowed({"ADMIN","STUDENT"})
    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody UserRequest userRequest) throws UserNotFoundException {
        UserResponse response = userService.login(userRequest);
        return ResponseEntity.ok(response);
    }

    @RolesAllowed({"ADMIN","STUDENT"})
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) throws UserNotFoundException {
        UserResponse response = userService.getUserById(id);
        return ResponseEntity.ok(response);
    }

    @RolesAllowed({"ADMIN","STUDENT"})
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> responseList = userService.getAllUsers();
        return ResponseEntity.ok(responseList);
    }
}
