package com.management.employee.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
	
	@GetMapping("/all")
	public String allAccess() {
		return "Public Content.";
	}
	
	@GetMapping("/user")
	@PreAuthorize("hasRole('USER') or hasRole('HR') or hasRole('ADMIN') or hasRole('MANAGER')")
	public String userAccess() {
		return "User Content.";
	}

	@GetMapping("/manager")
	@PreAuthorize("hasRole('MANAGER')")
	public String moderatorAccess() {
		return "Manager Board.";
	}

	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String adminAccess() {
		return "Admin Board.";
	}
	
	
//	@Autowired
//	private UserRepository userRepo;
//
//	@GetMapping("")
//	public String viewHomePage() {
//		System.out.println("Home");
//		return "index";
//	}
//	
//	@GetMapping("/register")
//	public String showSignupForm(Model model) {
//		model.addAttribute("user", new User());
//		System.out.println("Register");
//		return "signup_form";
//	}
//	
//	@PostMapping("/process_register")
//	public String processRegister(User user) {
//		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//		String encodedPassword = passwordEncoder.encode(user.getPassword());
//		user.setPassword(encodedPassword);
//		userRepo.save(user);
//		return "register_success";
//	}
//	
//	@GetMapping("/list_users")
//	public String showAllUsers(Model model) {
//		List<User> users = userRepo.findAll();
//		model.addAttribute("userList", users);
//		return "users";
//	}
}
