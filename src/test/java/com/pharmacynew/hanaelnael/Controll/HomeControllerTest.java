package com.pharmacynew.hanaelnael.Controll;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.ui.Model;

import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class HomeControllerTest {

    @Test
    public void testHomePage_ForManagerOrAdmin() {
        // Mock Authentication
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testUser");
        Collection<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_Manager"));
        when(authentication.getAuthorities()).thenReturn((Collection) authorities);

        // Mock SecurityContextHolder
        SecurityContextHolder.setContext(new SecurityContextImpl(authentication));

        // Mock Model
        Model model = mock(Model.class);

        // Create instance of HomeController
        HomeController homeController = new HomeController();

        // Call the homePage method
        String viewName = homeController.homePage(model);

        // Verify that the username and authorities are added to the model
        verify(model).addAttribute("username", "testUser");
        verify(model).addAttribute("authorities", authorities);

        // Verify that the view name returned by the controller method is correct
        assertEquals("home", viewName);
    }

    @Test
    public void testHomePage_ForOthers() {
        // Mock Authentication
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testUser");
        Collection<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_User"));
        when(authentication.getAuthorities()).thenReturn((Collection) authorities);

        // Mock SecurityContextHolder
        SecurityContextHolder.setContext(new SecurityContextImpl(authentication));

        // Mock Model
        Model model = mock(Model.class);

        // Create instance of HomeController
        HomeController homeController = new HomeController();

        // Call the homePage method
        String viewName = homeController.homePage(model);

        // Verify that the username and authorities are added to the model
        verify(model).addAttribute("username", "testUser");
        verify(model).addAttribute("authorities", authorities);

        // Verify that the view name returned by the controller method is correct
        assertEquals("HomeForOthers", viewName);
    }
}

