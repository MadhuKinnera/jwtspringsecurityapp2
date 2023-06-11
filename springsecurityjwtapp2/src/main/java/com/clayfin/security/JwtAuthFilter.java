package com.clayfin.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.clayfin.model.AppUser;
import com.clayfin.repository.AppUserRepository;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	@Autowired
	private JwtAuthProvider jwtProvider;

	@Autowired
	private UserDetailsService userDetailsService;

	// autowiring repo to fetch user

	@Autowired
	private AppUserRepository appUserRepo;
	
	
	private String jwtSecretKey = "sIoVC8OFOgmxbk9XRYtY2zMKXuYXBGL2d3x1I";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// getToken form req

		String token = getTokenFromRequest(request);

		Integer userId = request.getIntHeader("userId");
		
		

		if (token != null && userId!=-1 && jwtProvider.validateToken(token, jwtSecretKey+userId)) {

			// validate token and get Username
			
			System.out.println("User Id got from headers is "+userId);

			String username = jwtProvider.getUsernameFromToken(token, jwtSecretKey+userId);

			// loaduser by username

			UserDetails userDetails = userDetailsService.loadUserByUsername(username);

			AppUser user = appUserRepo.findByUsername(username).get();

			UsernamePasswordAuthenticationToken authtoken = new UsernamePasswordAuthenticationToken(userDetails, null,
					userDetails.getAuthorities());

			authtoken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			// set to context path

			SecurityContextHolder.getContext().setAuthentication(authtoken);

			// Creating a httpSession and adding userId to it

			//response.addIntHeader("userId", user.getId());

		}

		filterChain.doFilter(request, response);
	}

	private String getTokenFromRequest(HttpServletRequest req) {

		String header = req.getHeader(HttpHeaders.AUTHORIZATION);

		if (!StringUtils.isEmpty(header) && header.startsWith("Bearer ")) {
			return header.substring(7);
		}

		return null;

	}
}
