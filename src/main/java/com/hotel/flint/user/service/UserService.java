package com.hotel.flint.user.service;

import com.hotel.flint.user.domain.User;
import com.hotel.flint.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByUserId(Long id) {
        User user = userRepository.findById(id).orElseThrow(()->new EntityNotFoundException("해당 id가 존재하지 않습니다."));
        return user;
    }
}
