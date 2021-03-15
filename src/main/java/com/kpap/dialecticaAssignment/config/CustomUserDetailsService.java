package com.kpap.dialecticaAssignment.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kpap.dialecticaAssignment.model.User;
import com.kpap.dialecticaAssignment.repository.UserRepository;

/**
 * @author k.papageorgiou
 * Custom implementation of @UserDetailsService 
 * Retrieve from db user detail based on email.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new CustomUserPrincipal(user);
    }
    
    
}