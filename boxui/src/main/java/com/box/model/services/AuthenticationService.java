package com.box.model.services;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import com.box.model.data.repository.UserRepository;
import com.box.model.domain.User;

@Named
public class AuthenticationService {
	
	@Inject
	private UserRepository repository;


	public boolean isAuthenticated (User user){
		boolean isAuthenticated = false;
		
		User retrievedUser = repository.findByUserNamePassword(user.getUserName(), user.getPassword());
		System.out.println(retrievedUser + "<<<<<----");
		
		
		if ((retrievedUser != null) && (retrievedUser.getUserName().equals(user.getUserName())) && (retrievedUser.getPassword().equals(user.getPassword()))){
			isAuthenticated = true;

			// populate state of authenticated user
			//TODO : Replace it with cloning
			user.setId(retrievedUser.getId());
			user.setFirstName(retrievedUser.getFirstName());
			user.setLastName(retrievedUser.getLastName());
		}
		return isAuthenticated;
	}
}
