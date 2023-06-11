package com.clayfin.controller;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clayfin.dto.AuthRequest;
import com.clayfin.dto.CreateUserReq;
import com.clayfin.model.AppUser;
import com.clayfin.repository.AppUserRepository;
import com.clayfin.security.JwtAuthProvider;
import com.clayfin.service.UserDetailsServiceImpl;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtAuthProvider jwtProvider;

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Autowired
	private AppUserRepository uRepo;

	@GetMapping("/hello")
	public String hello() {
		return "Hello Madhu";
	}

	@GetMapping("/token")
	public ResponseEntity<String> getToken(@RequestBody AuthRequest req) throws Exception {

		UserDetails user = userDetailsService.loadUserByUsername(req.getUsername());

		if (user == null)
			throw new Exception("User Not Found with username " + req.getUsername());

		if (passwordEncoder.matches(user.getPassword(), req.getPassword())) {

			AppUser appUser = uRepo.findByUsername(req.getUsername()).get();

			String jwt = jwtProvider.generateToken(req.getUsername(), appUser.getId()+"");

			HttpHeaders headers = new HttpHeaders();

			headers.add("userId", String.valueOf(appUser.getId()));

			return new ResponseEntity<>(jwt, headers, HttpStatus.CREATED);

		}

		throw new Exception("User Details invalid ");

	}

	@PostMapping("/createuser")
	public AppUser createUser(@RequestBody CreateUserReq req) {

		return userDetailsService.createUser(req);

	}

}
