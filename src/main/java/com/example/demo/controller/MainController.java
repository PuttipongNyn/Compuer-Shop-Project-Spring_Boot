package com.example.demo.controller;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;


@Controller
public class MainController {
	
	@GetMapping("/login")
	public String login() {
		return "auth/login";
	}
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	
	@GetMapping("/auth/index")
	public String home(Model model) {
		List<User> userList = userRepository.findAll();
		model.addAttribute("userList",userList);
		
		List<Role> roleList = roleRepository.findAll();
		model.addAttribute("roleList",roleList);
		
		return "auth/index";
	}

}