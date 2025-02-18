package com.thean.dreamshops.service.user;

import com.thean.dreamshops.dto.UserDTO;
import com.thean.dreamshops.model.User;

public interface IUserService {
    User getUserById(Long userId);
    User createUser(UserDTO request);
    User updateUser(UserDTO request, Long userId);
    void deleteUser(Long userId);

    UserDTO convertUserToDto(User user);

    User getAuthenticatedUser();
}
