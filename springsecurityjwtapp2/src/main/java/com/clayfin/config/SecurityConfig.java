package com.clayfin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.clayfin.security.JwtAuthFilter;
import com.clayfin.security.JwtEntryPoint;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	// autowire jwtentrypoint

	@Autowired
	private JwtEntryPoint jwtEntryPoint;

	// create a bean for authfilter

	@Bean
	JwtAuthFilter jwtAuthFilter() {
		return new JwtAuthFilter();
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		// disable csrf
		http.csrf(t -> t.disable());

		// disable cors

		http.cors(t -> t.disable());

		// manage session
		http.sessionManagement(t -> t.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		
		
		//exception handling
		
		http.exceptionHandling(e->e.authenticationEntryPoint(jwtEntryPoint));
		
		

		http.authorizeHttpRequests(t -> t.requestMatchers("/public").permitAll().requestMatchers("/user")
				.hasAnyRole("USER")
				.requestMatchers("/").permitAll()
				.requestMatchers("/auth/**").permitAll()
				.requestMatchers("/admin").hasRole("ADMIN"));

		
		// add filter
		
		http.addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);
		
		
		return http.build();
	}

	@Bean
	@SuppressWarnings("deprecation")
	PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

}
