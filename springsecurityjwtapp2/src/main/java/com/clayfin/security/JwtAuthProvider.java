package com.clayfin.security;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.clayfin.util.Encryption;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtAuthProvider {

	// jwtSecretKey

	// private String jwtSecretKey = "sIoVC8OFOgmxbk9XRYtY2zMKXuYXBGL2d3x1IV37";
	private String jwtSecretKey = "sIoVC8OFOgmxbk9XRYtY2zMKXuYXBGL2d3x1I";

	// expirationTime
	private Long jwtExpiration = 500000L;

	// parsing the token

	public Claims parseToken(String token, String jwtSecretKey) {
		
		System.out.println("THe jwt secret key in parsing token is "+jwtSecretKey );

		JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(jwtSecretKey.getBytes()).build();

		return jwtParser.parseClaimsJws(token).getBody();

	}

	// validating the token

	public boolean validateToken(String token, String jwtSecretKey) {

		return parseToken(token,jwtSecretKey) != null;

	}

	// getUsername from the token
	public String getUsernameFromToken(String token, String jwtSecretKey) {

		// String token = Encryption.decrypt(eToken);

		Claims claims = parseToken(token,jwtSecretKey);

		if (claims != null)
			return claims.getSubject();
		else
			return null;

	}

	// generating the token

	public String generateToken(String username,String id) {

		
		String updatedjwtSecretKey = jwtSecretKey+id;
		
		System.out.println("The jwt Secret Key in Generating token is "+updatedjwtSecretKey);

		Key key = Keys.hmacShaKeyFor(updatedjwtSecretKey.getBytes());

		Date expire = new Date(new Date().getTime() + jwtExpiration);

		return Jwts.builder().setSubject(username).setIssuedAt(new Date()).setExpiration(expire).signWith(key)
				.compact();

	}

}
