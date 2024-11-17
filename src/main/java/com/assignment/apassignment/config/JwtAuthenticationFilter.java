package com.assignment.apassignment.config;

import com.assignment.apassignment.service.TokenService;
import com.assignment.apassignment.utility.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.logging.Logger;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = Logger.getLogger(JwtAuthenticationFilter.class.getName());

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final TokenService tokenService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService, TokenService tokenService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = extractTokenFromRequest(request);

        if (token != null) {
            try {

                // Check if the token is revoked
                if (tokenService.isTokenRevoked(token)) {
                    sendErrorResponse(response, "Token is revoked");
                    return;
                }

                // Extract the username from the token
                String username = jwtUtil.extractUsername(token);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    // Check if the token is valid for the user
                    if (jwtUtil.isTokenValid(token, userDetails)) {
                        // Create an Authentication object
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());

                        // Set the details (client-side information) in the authentication object
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        // Set the authentication in the SecurityContext
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            } catch (ExpiredJwtException e) {
                logger.severe("JWT Token has expired: " + e.getMessage());
                sendErrorResponse(response, "Token expired");
                return;
            } catch (SignatureException e) {
                logger.severe("JWT Token has an invalid signature: " + e.getMessage());
                sendErrorResponse(response, "Invalid token signature");
                return;
            } catch (MalformedJwtException e) {
                logger.severe("Malformed JWT Token: " + e.getMessage());
                sendErrorResponse(response, "Malformed token");
                return;
            } catch (JwtException e) {
                logger.severe("JWT Token processing error: " + e.getMessage());
                sendErrorResponse(response, "Error processing JWT");
                return;
            } catch (Exception e) {
                logger.severe("Unexpected error occurred: " + e.getMessage());
                sendErrorResponse(response, "An unexpected error occurred");
                return;
            }
        }

        // Continue the filter chain if no errors
        filterChain.doFilter(request, response);
    }

    // Extract JWT token from Authorization header
    private String extractTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7); // Extract token after "Bearer "
        }
        return null;
    }

    // Send custom error response
    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}
