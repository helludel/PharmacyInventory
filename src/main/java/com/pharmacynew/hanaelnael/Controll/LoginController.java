package com.pharmacynew.hanaelnael.Controll;

import com.pharmacynew.hanaelnael.DTO.Credentials;
import com.pharmacynew.hanaelnael.Entity.User;
import com.pharmacynew.hanaelnael.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.view.RedirectView;

import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.FailedLoginException;
import java.util.Map;


@Controller
public class LoginController {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserService userService;
    @Autowired
     AuthenticationManager authenticationManager;


    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/authenticateTheUser")
    public ResponseEntity<?> authenticateUser(@RequestBody Credentials credentials) throws AccountNotFoundException, FailedLoginException {
        String username = credentials.getUsername();
        String password = credentials.getPassword();

        try {
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            User user = (User) authentication.getPrincipal();

            // Return success response with user data
            return ResponseEntity.ok().body(Map.of(
                    "success", true,
                    "username", user.getUsername(),
                    "authorities", user.getAuthorities()
            ));
        } catch (Exception ex) {
            // Handle other authentication failures
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("success", false, "message", "Authentication failed")
            );
        }
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/showMyLoginPage")
    public String showMyLoginPage(Model model) {
        model.addAttribute("credentials", new Credentials());

        return "plain_login";
    }

    @GetMapping("/")
    public RedirectView processLogin() {
        return new RedirectView("/home");

    }
}
