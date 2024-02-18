package com.proj.user.services;

import com.proj.user.entities.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    User saveUser(User user);
    List<User> getAllUser();

    User getUser(String userId);
}
