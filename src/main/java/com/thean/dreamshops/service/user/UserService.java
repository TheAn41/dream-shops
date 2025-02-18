package com.thean.dreamshops.service.user;

import com.thean.dreamshops.dto.RoleDTO;
import com.thean.dreamshops.dto.UserDTO;
import com.thean.dreamshops.exception.AlredyExistingException;
import com.thean.dreamshops.exception.NotFoundException;
import com.thean.dreamshops.model.Role;
import com.thean.dreamshops.model.User;
import com.thean.dreamshops.repository.RoleRepository;
import com.thean.dreamshops.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(()-> new NotFoundException("User Not Found"));
    }

    @Override
    public User createUser(UserDTO request) {
        return Optional.of(request).filter(user->!userRepository.existsByEmail(request.getEmail()))
                .map(req->{
                    User user = new User();
                    user.setEmail(request.getEmail());
                    user.setPassword(passwordEncoder.encode(request.getPassword()));
                    user.setFirstName(request.getFirstName());
                    user.setLastName(request.getLastName());
                    Role userRole = roleRepository.findByName("role_user").get();
                    user.setRoles(Set.of(userRole));

                    return userRepository.save(user);
                }).orElseThrow(()-> new AlredyExistingException("User Already Exists"));
    }

    @Override
    public User updateUser(UserDTO request, Long userId) {
      return   userRepository.findById(userId).map(existingUser ->{
            existingUser.setFirstName(request.getFirstName());
            existingUser.setLastName(request.getLastName());

            return userRepository.save(existingUser);
        }).orElseThrow(()-> new NotFoundException(request.getEmail()+"Already Exists"));

    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId).ifPresentOrElse(userRepository::delete, ()->{
            throw new NotFoundException("User Not Found");
        });
    }

    @Override
    public UserDTO convertUserToDto(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email);
    }
}
