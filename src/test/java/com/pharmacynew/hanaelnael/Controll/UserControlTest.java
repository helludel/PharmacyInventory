package com.pharmacynew.hanaelnael.Controll;

import com.pharmacynew.hanaelnael.DTO.RegistrationDTO;
import com.pharmacynew.hanaelnael.Entity.Role;
import com.pharmacynew.hanaelnael.Entity.User;
import com.pharmacynew.hanaelnael.Service.RoleEmployeeService;
import com.pharmacynew.hanaelnael.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class UserControlTest {

    @Mock
    private UserService userService;

    @Mock
    private Model model;
    @Mock
    private RoleEmployeeService roleEmployeeService;

    @InjectMocks
    private UserControl userControl;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userControl).build();
    }

    @Test
    public void testGetAllUsers() throws Exception {
        // Arrange
        Role role = new Role(); // Assuming a default constructor exists
        List<User> mockUsers = new ArrayList<>();
        mockUsers.add(new User(1, "user1", "user1@example.com", "password1", "manager"));
        mockUsers.add(new User(2, "user2", "user2@example.com", "password2","manager"));

        when(userService.getAllUsers()).thenReturn(mockUsers);
        Model mockModel = mock(Model.class);

        // Act
        String viewName = userControl.getAllUsers(mockModel);

        // Assert
        assertEquals("employeeList", viewName);
        verify(userService, times(1)).getAllUsers();
        verify(mockModel, times(1)).addAttribute("users", mockUsers);

        // MockMvc test
        mockMvc.perform(get("/Pharma/listOfUsers"))
                .andExpect(status().isOk())
                .andExpect(view().name("employeeList"));
    }
    @Test
    public void testUserDeleteOrGetByIdForm() throws Exception {
        mockMvc.perform(get("/Pharma/userDeleteOrGetById"))
                .andExpect(status().isOk())
                .andExpect(view().name("userRemoveOrFindById"));
    }
    @Test
    public void testGetUserById() {
        // Mock data
        int userId = 1;
        User user = new User(); // Mock user object
        user.setId(userId);
        Optional<User> optionalUser = Optional.of(user);

        // Mock service behavior
        when(userService.getUserById(userId)).thenReturn(optionalUser);

        // Call the controller method
        ResponseEntity<Optional<User>> responseEntity = userControl.getUserById(userId);

        // Verify the response entity
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(optionalUser, responseEntity.getBody());
    }

    @Test
    public void testDeleteUserById() {
        // Mock data
        int userId = 1;

        // Call the controller method
        ResponseEntity<Void> responseEntity = userControl.deleteUserById(userId);

        // Verify the response entity
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody()); // Ensure that the body is null

        // Verify that the service method was called with the correct ID
        verify(userService, times(1)).deleteUserById(userId);
    }

    @Test
    public void testShowRegistrationForm() {
        String viewName = userControl.showRegistrationForm(model);

        // Verify that addAttribute is called with the correct values
        verify(model).addAttribute(eq("registrationDTO"), any(RegistrationDTO.class));

        // Ensure that the view name returned by the controller method is correct
        assertEquals("registrationForm", viewName);
    }
    @Test
    public void testCreateUser_Success() {
        RegistrationDTO registrationDTO = new RegistrationDTO();
        String roleName = "ROLE_USER"; // Example role name

        // Mock service behavior
        List<Role> roles = Arrays.asList(new Role(), new Role()); // Example roles
        when(roleEmployeeService.getAllRoles()).thenReturn(roles);

        // Call the controller method
        String viewName = userControl.createUser(registrationDTO, roleName, mock(BindingResult.class), model);

        // Verify that the getAllRoles method is called
        verify(roleEmployeeService).getAllRoles();

        // Verify that the roles are added to the model
        verify(model).addAttribute("roles", roles);

        // Verify that the userService.createUser method is called with the correct parameters
        verify(userService).createUser(registrationDTO, roleName);

        // Ensure that the view name returned by the controller method is correct
        assertEquals("success", viewName);
    }

    @Test
    public void testCreateUser_ValidationErrors() {
        // Simulate validation errors
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        RegistrationDTO registrationDTO = new RegistrationDTO();
        String roleName = "ROLE_USER"; // Example role name

        // Call the controller method
        String viewName = userControl.createUser(registrationDTO, roleName, bindingResult, model);

        // Ensure that the view name returned by the controller method is correct
        assertEquals("registrationForm", viewName);
    }
    @Test
    public void testShowUserNameUpdateForm() {
        String viewName = userControl.showUserNameUpdateForm();

        // Ensure that the view name returned by the controller method is correct
        assertEquals("userNameUpdateForm", viewName);
    }

    @Test
    public void testUpdateUserName() {
        // Mock data
        String email = "test@example.com";
        String newUserName = "newUserName";

        // Call the controller method
        String viewName = userControl.updateUserName(mock(Model.class), email, newUserName);

        // Verify that the userService.updateUserName method is called with the correct parameters
        verify(userService).updateUserName(email, newUserName);

        // Ensure that the view name returned by the controller method is correct
        assertEquals("success", viewName);
    }
    @Test
    public void testShowChangePasswordForm() {
        String viewName = userControl.showChangePasswordForm();

        // Ensure that the view name returned by the controller method is correct
        assertEquals("changePasswordForm", viewName);
    }
    @Test
    public void testChangePassword() {
        // Mock data
        String email = "test@example.com";
        String currentPassword = "currentPassword";
        String newPassword = "newPassword";

        // Call the controller method
        String viewName = userControl.changePassword(mock(Model.class), email, currentPassword, newPassword);

        // Verify that the userService.changePassword method is called with the correct parameters
        verify(userService).changePassword(email, currentPassword, newPassword);

        // Ensure that the view name returned by the controller method is correct
        assertEquals("success", viewName);
    }
    @Test
    public void testShowForgetPasswordForm() {
        String viewName = userControl.showForgetPasswordForm();

        // Ensure that the view name returned by the controller method is correct
        assertEquals("forgetPasswordForm", viewName);
    }
    @Test
    public void testResetPassword() {
        // Mock data
        String email = "test@example.com";
        String userName = "username";
        String newPassword = "newPassword";

        // Call the controller method
        String viewName = userControl.resetPassword(mock(Model.class), email, userName, newPassword);

        // Verify that the userService.resetPassword method is called with the correct parameters
        verify(userService).resetPassword(email, userName, newPassword);

        // Ensure that the view name returned by the controller method is correct
        assertEquals("success", viewName);
    }
}


