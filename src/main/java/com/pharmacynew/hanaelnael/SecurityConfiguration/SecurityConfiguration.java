package com.pharmacynew.hanaelnael.SecurityConfiguration;

import com.pharmacynew.hanaelnael.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@ComponentScan
@EnableWebSecurity
public class SecurityConfiguration extends SecurityFilterAutoConfiguration {

    PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private UserService userService;

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(authenticationProvider())
                .build();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

            http.csrf().disable()
                    .authorizeRequests((authorize)->
                            authorize
                                    .requestMatchers("/favicon.ico", "/error", "/Pharma/createOrUpdate", "/Pharma/createOrUpdateRole"
                                            , "/Pharma/createUser").permitAll()
                                    . requestMatchers("/Pharma/userNameUpdateForm","/Pharma/updateUserName","/Pharma/changePasswordForm","/Pharma/changePassword").permitAll()
                                    . requestMatchers("/home").authenticated()
                                    .requestMatchers("/Pharma/lowStock/{threshold}").hasRole("student")
                                  .requestMatchers("/Pharma/totalSales/{Id}").hasRole("student")
                                 .requestMatchers("/Pharma/dailySale/{saleDate}").hasRole("student")
                                 .requestMatchers("/Pharma/nearExpiry/{thresholdDate}").hasRole("student")
                                 .requestMatchers("/Pharma/showCreateInventoryForm").hasRole("student")
                                .requestMatchers("/Pharma/createInventory").hasRole("student")
                                    .requestMatchers("/Pharma/forgetPasswordForm").permitAll()
                                    .requestMatchers("/Pharma/resetPassword").permitAll()
                                .requestMatchers("/Pharma/createSales").hasRole("student")
                                    .requestMatchers("/Pharma/showRegistrationForm").permitAll()
                          .anyRequest( ).authenticated()
                    )
                    .formLogin(form ->
                            form
                                    .loginPage("/showMyLoginPage")
                                    .loginProcessingUrl("/authenticateTheUser")
                                    .defaultSuccessUrl("/home")
                                    .permitAll()
                    )
                    .logout(logout ->
                            logout.permitAll());

            return http.build();

    }
}
