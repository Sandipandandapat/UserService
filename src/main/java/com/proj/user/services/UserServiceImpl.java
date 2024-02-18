package com.proj.user.services;

import com.proj.user.entities.Rating;
import com.proj.user.entities.User;
import com.proj.user.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;   //we will use resttemplate to call another service from the current service
    // we can use other options like webclient, feignclient to do this.

    private Logger logger = LoggerFactory.getLogger(UserService.class);
    @Override
    public User saveUser(User user) {
        String randomUserId = UUID.randomUUID().toString();
        user.setUserId(randomUserId);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User getUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->new ResourceAccessException(("User with this userId not exists!!")));

        ArrayList<Rating> ratingsOfUser = restTemplate.getForObject("http://localhost:8083/ratings/users/"+user.getUserId(), ArrayList.class);  //ArrayList.class refers which kind of object we need
        //the above procedure is not a good one, as we are giving hard coded uri, maybe in future port can be changed

        logger.info("{}",ratingsOfUser);
        user.setRatings(ratingsOfUser);

        return user;
    }
}
