package com.box.model.services;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.box.model.data.repository.UserRepository;
import com.box.model.domain.User;

@Named
public class RegistrationService {

	private final Logger log = LoggerFactory.getLogger(RegistrationService.class);

	@Inject
	private UserRepository repository;

	public void register(User user) {
		repository.save(user);
	}
}
