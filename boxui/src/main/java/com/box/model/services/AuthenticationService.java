package com.box.model.services;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.box.model.data.repository.UserRepository;
import com.box.model.domain.User;

@Named
public class AuthenticationService {

	private final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

	@Inject
	private UserRepository userRepository;

	public boolean isAuthenticated(User user) {
		boolean isAuthenticated = false;

		User retrievedUser = userRepository.findByUserNamePassword(user.getUserName(), user.getPassword());
		log.debug(retrievedUser + "<<<<<----");

		if ((retrievedUser != null) && (retrievedUser.getUserName().equals(user.getUserName()))
				&& (retrievedUser.getPassword().equals(user.getPassword()))) {
			isAuthenticated = true;

			// populate state of authenticated user
			// TODO : Replace it with cloning
			user.setId(retrievedUser.getId());
			user.setFirstName(retrievedUser.getFirstName());
			user.setLastName(retrievedUser.getLastName());
		}
		return isAuthenticated;
	}

	public User findByUserName(String userName) {
		return userRepository.findByUserName(userName);
	}
}
