package com.proj.user.services;

import com.proj.user.entities.Hotel;
import com.proj.user.entities.Rating;
import com.proj.user.entities.User;
import com.proj.user.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

        Rating[] ratingsOfUser = restTemplate.getForObject("http://RATING-SERVICE/ratings/users/"+user.getUserId(), Rating[].class);  //ArrayList.class refers which kind of object we need
        //the above procedure is not a good one, as we are giving hard coded uri, maybe in future port can be changed

        List<Rating> ratings = Arrays.stream(ratingsOfUser).toList();

        List<Rating>ratingList = ratings.stream().map(rating ->{
            //api call to hotelService to get the hotel
            //http://localhost:8082/hotels/5dafad18-0b56-4783-9b09-4da5ce70fe96

            ResponseEntity<Hotel> forEntity = restTemplate.getForEntity("http://HOTEL-SERVICE/hotels/"+rating.getHotelId(), Hotel.class);
            Hotel hotel = forEntity.getBody(); // if we had used getForObject we could directly get the data
            //by using getForEntity we can get the details also, like status code
            logger.info("response status code: {}",forEntity.getStatusCode());
            //set the hotel to rating
            rating.setHotel(hotel);
            //review the rating
            return rating;
        }).collect(Collectors.toList());

        logger.info("{}",ratingsOfUser);
        user.setRatings(ratingList);

        return user;
    }
}
