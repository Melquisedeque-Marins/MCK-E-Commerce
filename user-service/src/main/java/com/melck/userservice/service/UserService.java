package com.melck.userservice.service;

import com.melck.userservice.entity.User;
import com.melck.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User registerUser(User user) {
        return userRepository.save(user);
    }
}
