package com.thean.dreamshops.security.user;

import com.thean.dreamshops.exception.NotFoundException;
import com.thean.dreamshops.model.User;
import com.thean.dreamshops.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ShopUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = Optional.ofNullable( userRepository.findByEmail(email))
                .orElseThrow(()-> new NotFoundException("User not found"));
        return ShopUserDetails.buildUserDetails(user);
    }
}
