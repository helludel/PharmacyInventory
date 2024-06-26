package com.pharmacynew.hanaelnael.Controll;

import com.pharmacynew.hanaelnael.DTO.Credentials;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.FailedLoginException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginControllerTest {

    @Test
    void testAuthenticateUser_Success() throws FailedLoginException, AccountNotFoundException {
        // Setup
        LoginController loginController = new LoginController();
        Credentials credentials = new Credentials("testUser", "password");

        // Invoke
        ResponseEntity<?> responseEntity = loginController.authenticateUser(credentials);

        // Verify
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Map<String, Object> expectedResponseBody = new HashMap<>();
        expectedResponseBody.put("success", true);
        expectedResponseBody.put("username", "testUser");
        expectedResponseBody.put("authorities", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        assertEquals(expectedResponseBody, responseEntity.getBody());
    }

    // Add more test methods for other scenarios
}


