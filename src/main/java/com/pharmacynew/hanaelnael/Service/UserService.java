package com.pharmacynew.hanaelnael.Service;


import com.pharmacynew.hanaelnael.DAO.RoleDAO;
import com.pharmacynew.hanaelnael.DAO.UserDAO;
import com.pharmacynew.hanaelnael.DTO.RegistrationDTO;
import com.pharmacynew.hanaelnael.Entity.Role;
import com.pharmacynew.hanaelnael.Entity.User;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UserService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private AuthenticationManager authenticationManager;

   // @Autowired
  //  public void setAuthenticationManager(AuthenticationManager authenticationManager) {
   //     this.authenticationManager = authenticationManager;
  //  }

    UserDAO userDAO;
    RoleEmployeeService roleEmployeeService;
     PasswordEncoder passwordEncoder;
     RoleDAO roleDAO;
    @Autowired
    public UserService(RoleDAO roleDAO,UserDAO userDAO,PasswordEncoder passwordEncoder,RoleEmployeeService roleEmployeeService) {
        this.roleEmployeeService = roleEmployeeService;
        this.roleDAO=roleDAO;
        this.userDAO= userDAO;
        this.passwordEncoder=passwordEncoder;
    }

    public UserService() {

    }


    public List<User> getAllUsers() {
        return (List<User>) userDAO.findAll( );
    }

    public Optional<User> getUserById(int id) {
        return userDAO.findById(id);
    }
@Transactional
    public User createUser(RegistrationDTO registrationDTO, String roleName) {
        Role roleUser = roleDAO.findByRoleName(roleName);
        if (roleUser == null) {
            Role newRole=new Role(  );
            logger.error("Role with roleName " + roleName + " not found");
            newRole.setRoleName(roleName);;
            roleUser = roleDAO.save(newRole);
        }
        User user = userDAO.findByEmail(registrationDTO.getEmail());
        if (user == null) {
            User newUser=new User();

            newUser.setUsername(registrationDTO.getUsername());
            newUser.setEmail(registrationDTO.getEmail());
            String encodedPassword = passwordEncoder.encode(registrationDTO.getPassword( ));
            newUser.setPassword(encodedPassword);
            newUser.setRole(roleUser);

            logger.info("Creating user: " + newUser);
            userDAO.save(newUser);

            return newUser;
        } else {
            throw new UsernameNotFoundException("User with email " + registrationDTO.getEmail() + " already exists");
        }
    }
        public void deleteUserById ( int id){

            userDAO.deleteById(id);
        }

        public User updateUserName(String email,String newUserName){
        User user=userDAO.findByEmail(email);
       user.setUsername(newUserName);
       userDAO.save(user);
           return user;
        }
        public User changePassword(String email,String currentPassword,String newPassword){
        User user=userDAO.findByEmail(email);
        logger.info("user is found "+ user.getUsername());
        if (passwordEncoder.matches(currentPassword,user.getPassword())){
                 user.setPassword(passwordEncoder.encode(newPassword));
                 userDAO.save(user);
                 return user;
        }else throw new UsernameNotFoundException("User not found or current password is not correct!! ");
        }

    public User resetPassword(String email,String userName,String newPassword){
        User user=userDAO.findByEmail(email);
        logger.info("user is found "+ user.getUsername());
        if (user.getUsername()==userName){
            user.setPassword(passwordEncoder.encode(newPassword));
            userDAO.save(user);
            logger.info("user is "+user.toString());
            return user;
        }else throw new UsernameNotFoundException("User is  not found  !! ");
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDAO.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), getAuthorities(user.getRole()));
    }
    private Set<GrantedAuthority> getAuthorities(Role role) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
        return authorities;
    }
    @Transactional
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword( )));
        userDAO.save(user);
    }
    public boolean authenticateUser(String username, String password) {
        User user = userDAO.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return true;
        }
        return false;
    }
    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {

        User user = userDAO.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return user;
    }

}

