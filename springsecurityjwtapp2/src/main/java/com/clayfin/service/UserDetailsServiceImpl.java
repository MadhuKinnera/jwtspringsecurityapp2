package com.clayfin.service;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.clayfin.dto.CreateUserReq;
import com.clayfin.model.AppUser;
import com.clayfin.repository.AppUserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private AppUserRepository uRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		AppUser appUser = uRepo.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username" + username));

		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(appUser.getRole());

		return new User(appUser.getUsername(), appUser.getPassword(), List.of(authority));

	}

	public AppUser createUser(CreateUserReq req) {

		String username = req.getUsername();
		String password = req.getPassword();
		String role = req.getRole();

		AppUser appUser = new AppUser();
		appUser.setUsername(username);
		appUser.setPassword(password);
		appUser.setRole(role);

		Integer id = new Random().nextInt(1, 200);
		System.out.println("generated id is "+id);
		appUser.setId(id);
		
		System.out.println("User is "+appUser);

		return uRepo.save(appUser);

	}

}
