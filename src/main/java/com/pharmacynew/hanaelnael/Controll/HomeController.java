package com.pharmacynew.hanaelnael.Controll;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String homePage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext( ).getAuthentication( );
        model.addAttribute("username", authentication.getName( ));
        model.addAttribute("authorities", authentication.getAuthorities( ));
        if (authentication.getAuthorities( ).stream( ).anyMatch(r -> r.getAuthority( ).equals("ROLE_Manager") || r.getAuthority().equals("ROLE_Admin"))) {
            return "home";
        } else return "HomeForOthers";
    }
}
