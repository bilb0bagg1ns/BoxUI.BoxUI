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
public class RegistrationService {
	
	@Inject
	private UserRepository repository;


	public void register (User user){
		repository.save(user);
	}
}
