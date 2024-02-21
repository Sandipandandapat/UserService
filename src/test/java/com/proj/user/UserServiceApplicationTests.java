package com.proj.user;

import com.netflix.discovery.converters.Auto;
import com.proj.user.entities.Rating;
import com.proj.user.external.services.RatingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceApplicationTests {
	@Autowired
	private RatingService ratingService;

	@Test
	void contextLoads() {
	}
//	@Test
//	void createRating(){
//		Rating rating = Rating.builder().rating(10).hotelId("").feedback("This is created using feignClient")
//				.build();
//		Rating savedRating = ratingService.createRating(rating);
//		System.out.println("new rating created");
//	}

}
