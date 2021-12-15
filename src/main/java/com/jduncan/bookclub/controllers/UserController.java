package com.jduncan.bookclub.controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.jduncan.bookclub.models.LoginUser;
import com.jduncan.bookclub.models.User;
import com.jduncan.bookclub.services.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	// **** DISPLAY ROUTES ****
	
	// Login & Registration page - routes to index.jsp if not logged in, redirects to dashboard if logged in
	@GetMapping("/")
	public String index(Model model, HttpSession session) {
		if(session.getAttribute("uuid") != null) {
			return "redirect:/dashboard";
		}
		model.addAttribute("newUser", new User());
		model.addAttribute("newLogin", new LoginUser());
		return "index.jsp";
	}
	
	// Dashboard page - only reached if user is logged in or registers
	@GetMapping("/dashboard")
	public String dashboard(Model model, HttpSession session) {
		if(session.getAttribute("uuid") == null) {
			return "redirect:/";
		}
		model.addAttribute("loggedUser", userService.retrieveOneUser((Long) session.getAttribute("uuid")));
		return "dashboard.jsp";
	}
	
	// Logs user out and clears session, returning to index.jsp
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
	
	
	// **** ACTION ROUTES ****
	
	// processes registration of new user
	@PostMapping("/register")
	public String register(@Valid @ModelAttribute("newUser") User newUser, BindingResult binding, Model model, HttpSession session) {
		userService.register(newUser, binding);
		if(binding.hasErrors()) {
			model.addAttribute("newLogin", new LoginUser());
			return "index.jsp";
		}
		session.setAttribute("uuid", newUser.getId());
		return "redirect:/dashboard";
	}
	
	// processes registered user logging in
	@PostMapping("/login")
	public String login(@Valid @ModelAttribute("newLogin") LoginUser newLogin, BindingResult binding, Model model, HttpSession session) {
		User user = userService.login(newLogin, binding);
		if(binding.hasErrors()) {
			model.addAttribute("newUser", new User());
			return "index.jsp";
		}
		session.setAttribute("uuid", user.getId());
		return "redirect:/dashboard";
	}


}
