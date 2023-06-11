package com.clayfin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BasicController {

	@GetMapping("/public")
	public String publicApi() {
		return "Iam a Public Api ";
	}

	@GetMapping("/user")
	public String userApi() {
		return "Iam a User Api ";
	}

	@GetMapping("/admin")
	public String adminApi() {
		return "Iam a Admin Api ";
	}

}
