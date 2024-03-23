package com.pharmacynew.hanaelnael.Authentication;

import com.pharmacynew.hanaelnael.Entity.User;
import com.pharmacynew.hanaelnael.Service.UserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CustomAuthenticationProvider implements AuthenticationProvider {
UserService userService;
PasswordEncoder passwordEncoder;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        User user = (User) userService.loadUserByUsername(username);

        if (passwordEncoder.matches(password, user.getPassword())) {
            // Passwords match, user is authenticated
            return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
        } else {
            // Invalid credentials, authentication failed
            throw new AuthenticationException("Invalid credentials") {

                @Override
                public String getMessage() {
                    return super.getMessage( );
                }
            };
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // Specify the type of Authentication object your provider supports
        return true; // Replace with your supported Authentication type
    }
}
