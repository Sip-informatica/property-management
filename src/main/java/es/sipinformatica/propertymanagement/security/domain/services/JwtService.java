package es.sipinformatica.propertymanagement.security.domain.services;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import es.sipinformatica.propertymanagement.security.api.dtos.UserDetailsImpl;
import io.jsonwebtoken.*;

@Service
public class JwtService {
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    @Value("${jwt.key}")
    private String jwtKey;
    @Value("${jwt.expiration}")
    private int jwtExpiration;

    public boolean validateJwtToken(String jwt) {
        try {
            Jwts.parser().setSigningKey(jwtKey).parseClaimsJws(jwt);
		    return true;
        } catch (SignatureException e) {
			logger.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
		}
        return false;        
    }
    
    public String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")){
            return headerAuth.substring(7, headerAuth.length());
        }
        return null;
    }

	public String getUserNameFromJwtToken(String jwt) {
		return Jwts.parser().setSigningKey(jwtKey).parseClaimsJws(jwt).getBody().getSubject();
	}

	public String createJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        String jwtToken = Jwts.builder()
        .setSubject(userPrincipal.getUsername())
        .setIssuedAt(new Date())
        .setExpiration(new Date((new Date().getTime() + jwtExpiration)))
        .signWith(SignatureAlgorithm.HS512, jwtKey)
        .compact();
        
		return jwtToken;
	}
}