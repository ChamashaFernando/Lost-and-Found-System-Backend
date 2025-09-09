package lk.chamasha.lost.and.found.service;


import lk.chamasha.lost.and.found.controller.request.UserRequest;
import lk.chamasha.lost.and.found.controller.response.UserResponse;
import lk.chamasha.lost.and.found.exception.UserNotCreatedException;
import lk.chamasha.lost.and.found.exception.UserNotFoundException;

import java.util.List;

public interface UserService {
    public UserResponse register(UserRequest userRequest)throws UserNotCreatedException;
    public UserResponse login(UserRequest userRequest) throws UserNotFoundException;
    public UserResponse getUserById(Long id)throws UserNotFoundException;
    public List<UserResponse> getAllUsers ();

}
