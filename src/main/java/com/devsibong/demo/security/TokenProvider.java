package com.devsibong.demo.security;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.devsibong.demo.model.UserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class TokenProvider {
	private static final String SECRET_KEY = "test1QwefwafewfvsvEwfwdfqfawfvawevwedfqwdsvczsefwefvwvvzfewfzsef234";
	byte[] keyBytes = SECRET_KEY.getBytes();
	Key key = Keys.hmacShaKeyFor(keyBytes);

	public String create(UserEntity userEntity) {
		Date expireDate = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));
		return Jwts.builder().signWith(key, SignatureAlgorithm.HS512).setSubject(userEntity.getId())
				.setIssuer("demo app").setIssuedAt(new Date()).setExpiration(expireDate).compact();
	}

	public String validateAndGetUserId(String token) {
		Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

		return claims.getSubject();
	}
}
