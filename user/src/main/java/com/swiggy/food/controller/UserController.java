package com.swiggy.food.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.swiggy.food.feign.clients.PaymentMicroServiceFeignCleint;
import com.swiggy.food.request.PaymentDetails;
import com.swiggy.food.request.UserLogin;
import com.swiggy.food.request.UserRegistration;
import com.swiggy.food.service.UserService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
public class UserController {

	@Autowired
	UserService service;

	@Autowired
	PaymentMicroServiceFeignCleint paymentClinet;

	@Value("${swiggy.contact.email}")
	public String emailId;

	@PostMapping("/register")
	public String userRegisteration(@RequestBody UserRegistration requrest) {
		return service.userRegisteration(requrest);
	}

	@PostMapping("/login")
	public String userRegisteration(@RequestBody UserLogin requrest) {
		return service.userLogin(requrest);
	}

	@PostMapping("/make/payment")
	@CircuitBreaker( name="user-service" , fallbackMethod = "paymentStatus")
	public String paymentStatus(@RequestBody PaymentDetails details) {
		return paymentClinet.makePayment(details) + " : Please conatct If any issue " + emailId;
	}
	
	
	public String paymentStatus(Throwable  ex) {
			return "Backend Baking Systems are Not functioning. Please Contact Admin";
	}

}
