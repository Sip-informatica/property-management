package es.sipinformatica.propertymanagement.security.configurations;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import es.sipinformatica.propertymanagement.security.domain.services.JwtService;
import es.sipinformatica.propertymanagement.security.domain.services.UserDetailsServiceImpl;
import io.jsonwebtoken.JwtException;



public class AuthTokenFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthTokenFilter.class);
    
    @Autowired    
    private JwtService jwtService;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
                String msg = "";
                try {
                    String jwt = jwtService.parseJwt(request);
                    if (jwt !=null && jwtService.validateJwtToken(jwt)){
                        String username = jwtService.getUserNameFromJwtToken(jwt);
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                } catch (Exception e) {
                    LOGGER.error("Cannot set user authentication: {}", e.getMessage());
                    ObjectMapper objectMapper = new ObjectMapper();
                    if (e instanceof JwtException) {
                        msg = e.getMessage();
                    }
                    response.setCharacterEncoding("UTF-8");
                    response.setContentType(MediaType.APPLICATION_JSON.getType());
                    response.getWriter().write(objectMapper.writeValueAsString(msg));
                    response.getWriter().flush();
                    response.getWriter().close();
                    return;
                }
            filterChain.doFilter(request, response);                        

    }

   
}
