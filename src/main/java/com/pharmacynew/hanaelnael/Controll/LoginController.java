package com.pharmacynew.hanaelnael.Controll;

import com.pharmacynew.hanaelnael.DTO.Credentials;
import com.pharmacynew.hanaelnael.Entity.User;
import com.pharmacynew.hanaelnael.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class LoginController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserService userService;

    @PostMapping("/authenticateTheUser")
    public ResponseEntity<String> authenticateUser(@RequestBody Credentials credentials) {

        String username = credentials.getUsername();
        String password = credentials.getPassword();

        try {

            User user = (User) userService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());

            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            return ResponseEntity.ok("Authentication successful");
        } catch (UsernameNotFoundException ex) {

            return ResponseEntity.status(401).body("Invalid username or password");
        } catch (Exception ex) {

            return ResponseEntity.status(401).body("Authentication failed");
        }
    }

    @GetMapping("/showMyLoginPage")
    public String showMyLoginPage(Model model) {
        model.addAttribute("credentials", new Credentials());

        return "plain_login";
    }

    @PostMapping("/")
    public RedirectView processLogin() {
        return new RedirectView("/home");

    }
}
