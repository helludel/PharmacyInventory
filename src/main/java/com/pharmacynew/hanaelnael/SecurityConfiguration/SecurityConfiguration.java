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
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

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
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setPrefix("classpath:/templates/"); // Ensure the prefix points to the correct directory
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        return templateResolver;
    }
    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.setEnableSpringELCompiler(true);
        return templateEngine;
    }

    public void configureViewResolvers(ViewResolverRegistry registry) {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        registry.viewResolver(resolver);
    }

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