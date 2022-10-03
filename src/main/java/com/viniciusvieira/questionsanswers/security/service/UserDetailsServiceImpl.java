package com.viniciusvieira.questionsanswers.security.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.viniciusvieira.questionsanswers.repositories.ApplicationUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final ApplicationUserRepository userRepository;

    // BUG
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return Optional.ofNullable(userRepository.findByUsername(username))
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
    }
}
