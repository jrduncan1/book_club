package com.jduncan.bookclub.services;

import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.jduncan.bookclub.models.LoginUser;
import com.jduncan.bookclub.models.User;
import com.jduncan.bookclub.repositories.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepo;
	
	public User register(User newUser, BindingResult binding) {
		if(userRepo.findByEmail(newUser.getEmail()).isPresent()) {
			binding.rejectValue("email", "Unique", "Email is already in use.");
		}
		if(!newUser.getPassword().equals(newUser.getConfirmPassword())) {
			binding.rejectValue("confirmPassword", "Matches", "Passwords do not match.");
		}
		if(binding.hasErrors()) {
			return null;
		} else {
			String who_hash = BCrypt.hashpw(newUser.getPassword(), BCrypt.gensalt());
			newUser.setPassword(who_hash);
			return userRepo.save(newUser);
		}
	}
	
	public User login(LoginUser newLogin, BindingResult binding) {
		if(binding.hasErrors()) {
			return null;
		}
		
		Optional<User> potentialUser = userRepo.findByEmail(newLogin.getEmail());
		if(!potentialUser.isPresent()) {
			binding.rejectValue("email", "Unique", "Email does not exist in our system.");
			return null;
		}
		
		User user = potentialUser.get();
		if(!BCrypt.checkpw(newLogin.getPassword(), user.getPassword())) {
			binding.rejectValue("password", "Matches", "Invalid password.");
		}
		
		if(binding.hasErrors()) {
			return null;
		} else {
			return user;
		}
		
	}
	
	public User retrieveOneUser(Long id) {
		Optional<User> user = userRepo.findById(id);
		
		if(user.isPresent()) {
			return user.get();
		} else {
			return null;
		}
	}

}
