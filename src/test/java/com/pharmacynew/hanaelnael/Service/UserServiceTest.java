package com.pharmacynew.hanaelnael.Service;

import com.pharmacynew.hanaelnael.DAO.RoleDAO;
import com.pharmacynew.hanaelnael.DAO.UserDAO;
import com.pharmacynew.hanaelnael.DTO.RegistrationDTO;
import com.pharmacynew.hanaelnael.Entity.Role;
import com.pharmacynew.hanaelnael.Entity.User;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class UserServiceTest {
    UserDAO userDAO;
    PasswordEncoder passwordEncoder;
    RoleEmployeeService roleEmployeeService;
    RoleDAO roleDAO;
    UserService userService;



    @Before
    public void setUp() {
         userDAO = mock(UserDAO.class);
         passwordEncoder=mock(PasswordEncoder.class);
         roleEmployeeService=mock(RoleEmployeeService.class);
         roleDAO=mock(RoleDAO.class);
        userService=mock(UserService.class);

    }

    @Test
    public void testGetAllUsers() {
        // Mocking UserDAO

        // Creating some sample users
        User user1 = new User("Baba","bbbb","bbb@gmail.com");
        User user2 = new User("Bob","oooo","eeee@gmail");
        User user3 = new User("Charlie","cccc","ccc@gmail.com");

        // Stubbing the behavior of userDAO.findAll() to return sample users
        List<User> sampleUsers = Arrays.asList(user1, user2, user3);
        when(userDAO.findAll()).thenReturn(sampleUsers);

        // Creating an instance of UserService with the mocked UserDAO
        UserService userService;
        userService = new UserService(  roleDAO,userDAO,passwordEncoder,roleEmployeeService);

        // Calling the method under test
        List<User> result = userService.getAllUsers();

        // Verifying that the returned list contains the sample users
        assertEquals(sampleUsers.size(), result.size());
        for (int i = 0; i < sampleUsers.size(); i++) {
            assertEquals(sampleUsers.get(i), result.get(i));
        }

        // Verifying that userDAO.findAll() was called exactly once
        verify(userDAO, times(1)).findAll();
    }
    @Test
    public void testGetUserById() {
        // Mocking UserDAO
        UserDAO userDAO = mock(UserDAO.class);
        PasswordEncoder passwordEncoder=mock(PasswordEncoder.class);
        RoleEmployeeService roleEmployeeService=mock(RoleEmployeeService.class);
        RoleDAO roleDAO=mock(RoleDAO.class);


        // Creating sample users
        User user1 = new User("baba", "Alice","hhh@gmail.com");
        User user2 = new User("Bob","mmmm","ttt@gmail.com");

        // Stubbing the behavior of userDAO.findById() for existing and non-existing users
        when(userDAO.findById(1)).thenReturn(Optional.of(user1)); // User with ID 1 exists
        when(userDAO.findById(2)).thenReturn(Optional.of(user2)); // User with ID 2 exists
        when(userDAO.findById(3)).thenReturn(Optional.empty()); // User with ID 3 does not exist

        // Creating an instance of UserService with the mocked UserDAO
        UserService userService = new UserService(roleDAO,userDAO,passwordEncoder,roleEmployeeService);

        // Testing for existing users
        assertEquals(user1, userService.getUserById(1).orElse(null)); // User with ID 1
        assertEquals(user2, userService.getUserById(2).orElse(null)); // User with ID 2

        // Testing for non-existing user
        assertEquals(null, userService.getUserById(3).orElse(null)); // User with ID 3 does not exist
    }

    @Test
    public void testCreateUser_Successful() {
       UserDAO userDAO = mock(UserDAO.class);
       RoleDAO roleDAO = mock(RoleDAO.class);
       PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
       RoleEmployeeService roleEmployeeService=mock(RoleEmployeeService.class);
        UserService userService = new UserService(roleDAO,userDAO,passwordEncoder,roleEmployeeService);

        // Mocking behavior for roleDAO.findByRoleName()
        Role role = new Role("ROLE_USER");
        when(roleDAO.findByRoleName("ROLE_USER")).thenReturn(role);

        // Mocking behavior for userDAO.findByEmail()
        when(userDAO.findByEmail("test@example.com")).thenReturn(null);

        // Mocking behavior for passwordEncoder.encode()
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        // Creating RegistrationDTO for test
        RegistrationDTO registrationDTO = new RegistrationDTO("username", "test@example.com", "password");

        // Call the method under test
        User createdUser = userService.createUser(registrationDTO, "ROLE_USER");

        // Verify that the user was created with the correct properties
        assertNotNull(createdUser);
        assertEquals("username", createdUser.getUsername());
        assertEquals("test@example.com", createdUser.getEmail());
        assertEquals("encodedPassword", createdUser.getPassword());
        assertEquals(role, createdUser.getRole());

        // Verify that save was called on userDAO once
        verify(userDAO, times(1)).save(any(User.class));
    }

    @Test
    public void testCreateUser_UserAlreadyExists() {
        // Mocking behavior for userDAO.findByEmail() to return an existing user
        when(userDAO.findByEmail("existing@example.com")).thenReturn(new User());

        // Creating RegistrationDTO for test
        RegistrationDTO registrationDTO = new RegistrationDTO("username", "existing@example.com", "password");

        // Call the method under test and expect an exception to be thrown
        assertThrows(UsernameNotFoundException.class, () -> userService.createUser(registrationDTO, "ROLE_USER"));
    }


    @Test
    void deleteUserById() {
    }

    @Test
    void updateUserName() {
    }

    @Test
    void changePassword() {
    }

    @Test
    void resetPassword() {
    }

    @Test
    void loadUserByUsername() {
    }

    @Test
    void saveUser() {
    }

    @Test
    void authenticateUser() {
    }

    @Test
    void loadUserByEmail() {
    }
}