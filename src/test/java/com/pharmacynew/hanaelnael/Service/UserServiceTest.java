package com.pharmacynew.hanaelnael.Service;

import com.pharmacynew.hanaelnael.DAO.RoleDAO;
import com.pharmacynew.hanaelnael.DAO.UserDAO;
import com.pharmacynew.hanaelnael.DTO.RegistrationDTO;
import com.pharmacynew.hanaelnael.Entity.Role;
import com.pharmacynew.hanaelnael.Entity.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private RoleDAO roleDAO;

    @Mock
    private UserDAO userDao;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;
    private Role role;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(0);
        user.setUsername("testUser");
        user.setPassword("password");
        user.setEmail("test@example.com");
        user.setRole(new Role("ROLE_USER"));

        role = new Role();
        role.setId(0);
        role.setRoleName("ROLE_USER");

//        when(userDao.findByEmail(anyString())).thenReturn(null); // Simulate that the user does not already exist
        lenient().when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        lenient().when(roleDAO.findByRoleName("ROLE_USER")).thenReturn(role);
    }

    @Test
    void testCreateUser_NewUser() {
        // Prepare test data
        RegistrationDTO userDto = new RegistrationDTO();
        userDto.setUsername("testUser");
        userDto.setPassword("password");
        userDto.setEmail("test@example.com");
        String roleName = "ROLE_USER";

        // Set up mock behavior for password encoding
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        // Capture the user argument passed to userRepository.save()
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        // Call the method under test
        userService.createUser(userDto, roleName);

        // Verify that userRepository.save() was called with the correct user object
        verify(userDao, times(1)).save(userCaptor.capture());

        // Assert properties of the captured user object
        User capturedUser = userCaptor.getValue();
        assertNotNull(capturedUser);
        assertEquals("testUser", capturedUser.getUsername());
        assertEquals("encodedPassword", capturedUser.getPassword()); // Ensure password is encoded
    }

    @Test
    public void testGetUserById() {
        // Creating sample users
        User user1 = new User("baba", "Alice", "hhh@gmail.com");
        User user2 = new User("Bob", "mmmm", "ttt@gmail.com");

        // Stubbing the behavior of userDao.findById() for existing and non-existing users
        when(userDao.findById(1)).thenReturn(Optional.of(user1)); // User with ID 1 exists
        when(userDao.findById(2)).thenReturn(Optional.of(user2)); // User with ID 2 exists
        when(userDao.findById(3)).thenReturn(Optional.empty()); // User with ID 3 does not exist

        // Testing for existing users
        assertEquals(user1, userService.getUserById(1).orElse(null)); // User with ID 1
        assertEquals(user2, userService.getUserById(2).orElse(null)); // User with ID 2

        // Testing for non-existing user
        assertNull(userService.getUserById(3).orElse(null)); // User with ID 3 does not exist
    }

    @Test
    public void testDeleteUserById() {
        int userId = 1;

        // Call the method under test
        userService.deleteUserById(userId);

        // Verify that the deleteById method was called on userDao with the correct id
        verify(userDao, times(1)).deleteById(userId);
    }

    @Test
    void testUpdateUserName_Success() {
        // Test data
        String email = "test@example.com";
        String newUserName = "newUserName";

        // Create a User object for mocking
        User user = new User();
        user.setEmail(email);

        // Set up mock behavior
        when(userDao.findByEmail(email)).thenReturn(user);

        // Capture the argument passed to userDao.save()
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        // Call the method under test
        userService.updateUserName(email, newUserName);

        // Verify that userDao.save() was called with the correct user object
        verify(userDao, times(1)).save(userCaptor.capture());

        // Retrieve the captured user object
        User capturedUser = userCaptor.getValue();

        // Additional assertions
        assertNotNull(capturedUser);
        assertEquals(newUserName, capturedUser.getUsername());
    }
    @Test
    void testUpdateUserName_UserNotFound() {
        String email = "test@example.com";
        String newUserName = "newUserName";

        when(userDao.findByEmail(email)).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.updateUserName(email, newUserName);
        });

        verify(userDao, times(1)).findByEmail(email);
        verify(userDao, times(0)).save(any(User.class));
    }

    @Test
    public void testChangePassword_Success() {
        // Arrange
        String email = "test@example.com";
        String currentPassword = "password"; // Correct current password
        String newPassword = "newPassword";

        when(userDao.findByEmail(email)).thenReturn(user);
        when(passwordEncoder.matches(currentPassword, user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedPassword");
        when(userDao.save(user)).thenReturn(user);

        // Act
        User updatedUser = userService.changePassword(email, currentPassword, newPassword);

        // Assert
        assertNotNull(updatedUser);
        assertEquals("encodedPassword", updatedUser.getPassword()); // Password should be encoded
        verify(userDao, times(1)).save(user); // Check if save method is called
    }

    @Test
    public void testChangePassword_WrongCurrentPassword() {
        // Arrange
        String email = "test@example.com";
        String currentPassword = "wrongPassword";
        String newPassword = "newPassword";

        when(userDao.findByEmail(email)).thenReturn(user);
        when(passwordEncoder.matches(currentPassword, user.getPassword())).thenReturn(false);

        // Act and Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            userService.changePassword(email, currentPassword, newPassword);
        });

        // Verify that save method is not called
        verify(userDao, never()).save(any(User.class));
    }
    @Test
    void testResetPassword_Success() {
        // Arrange
        String email = "test@example.com";
        String userName = "testUser";
        String newPassword = "newPassword";

        when(userDao.findByEmail(email)).thenReturn(user);
        when(userDao.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPassword");

        // Act
        User updatedUser = userService.resetPassword(email, userName, newPassword);

        // Assert
        assertNotNull(updatedUser);
        assertEquals("encodedNewPassword", updatedUser.getPassword()); // Ensure password is encoded
        verify(userDao, times(1)).findByEmail(email);
        verify(userDao, times(1)).save(user);
    }

    @Test
    void testResetPassword_UserNotFound() {
        // Arrange
        String email = "test@example.com";
        String userName = "wrongUser";
        String newPassword = "newPassword";

        when(userDao.findByEmail(email)).thenReturn(user);

        // Act and Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            userService.resetPassword(email, userName, newPassword);
        });
    }


        @Test
        public void testLoadUserByUsername_Success() {
            when(userDao.findByUsername("testUser")).thenReturn(user);

            UserDetails userDetails = userService.loadUserByUsername("testUser");

            assertNotNull(userDetails);
            assertEquals("testUser", userDetails.getUsername());
            assertEquals("password", userDetails.getPassword());
            //assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
        }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        when(userDao.findByUsername("nonExistentUser")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername("nonExistentUser");
        });
    }

    @Test
    @Transactional
    public void testSaveUser() {
        User newUser = new User();
        newUser.setUsername("newUser");
        newUser.setPassword("newPassword");
        newUser.setEmail("newUser@example.com");
        newUser.setRole(role);

        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");

        userService.saveUser(newUser);

        assertEquals("encodedNewPassword", newUser.getPassword());
        verify(userDao, times(1)).save(newUser);
    }

    @Test
    public void testAuthenticateUser_Success() {
        when(userDao.findByUsername("testUser")).thenReturn(user);
       // when(passwordEncoder.matches("password", "password")).thenReturn(true);

        boolean isAuthenticated = userService.authenticateUser("testUser", "password");

        assertTrue(isAuthenticated);
    }

    @Test
    public void testAuthenticateUser_Failure() {
        when(userDao.findByUsername("testUser")).thenReturn(user);
      //  when(passwordEncoder.matches("wrongPassword", "password")).thenReturn(false);

        boolean isAuthenticated = userService.authenticateUser("testUser", "wrongPassword");

        assertFalse(isAuthenticated);
    }

    @Test
    public void testLoadUserByEmail_Success() {
        when(userDao.findByEmail("test@example.com")).thenReturn(user);

        UserDetails userDetails = userService.loadUserByEmail("test@example.com");

        assertNotNull(userDetails);
        assertEquals("testUser", userDetails.getUsername());
    }

    @Test
    public void testLoadUserByEmail_UserNotFound() {
        when(userDao.findByEmail("nonexistent@example.com")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByEmail("nonexistent@example.com");
        });
    }
}


