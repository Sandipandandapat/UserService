package com.proj.user.controller;

import com.proj.user.entities.User;
import com.proj.user.services.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    //create new user
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user){
        //at the time of user creation we will generate an userid, that we are doing in UserService.
        User user1= userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user1);
    }

    int retryCount=1;
    //get single user
    //we will apply circuit breaker here because this is calling rating service
    @GetMapping("/{userId}")
//    @CircuitBreaker(name = "ratingHotelBreaker",fallbackMethod = "ratingHotelFallback") //if rating or hotel service will be down then this fallBack method will be executed
//    @Retry(name = "ratingHotelService", fallbackMethod = "ratingHotelFallback")  //this method will call the dependent method given number of times , if it gets no response then it will execute the fallback
    @RateLimiter(name="userRateLimiter", fallbackMethod = "ratingHotelFallback")  //if we want to give a limit for accessing a service we can use rateLimiter
    public ResponseEntity<User> getSingleUser(@PathVariable String userId){  //pathvariable is used to get the value from url
        logger.info("Get single user Handler: UserController");
        logger.info("retry count: {}",retryCount);
        retryCount++;
        User user = userService.getUser(userId);
        return ResponseEntity.ok(user);
    }

    public ResponseEntity<User> ratingHotelFallback(String userId,Exception ex){
        logger.info("Fallback is executed because service is down: ",ex.getMessage());
        User user = User.builder()
                .email("dummy@gmail.com")
                .name("Dummy")
                .about("This user is created because some service is down")
                .userId("12345678")
                .build();
        return new ResponseEntity<>(user,HttpStatus.OK);

    }

    //get all user
    @GetMapping
    public ResponseEntity<List<User>> getAllUser(){
        List<User> allUser = userService.getAllUser();
        return ResponseEntity.ok(allUser);
    }
}
