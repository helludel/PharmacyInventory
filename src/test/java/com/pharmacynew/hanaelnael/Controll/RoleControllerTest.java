package com.pharmacynew.hanaelnael.Controll;

import com.pharmacynew.hanaelnael.Entity.Role;
import com.pharmacynew.hanaelnael.Service.RoleEmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class RoleControllerTest {

    @InjectMocks
    private RoleController roleController;

    @Mock
    private RoleEmployeeService roleService;

    @Mock
    private Model model;

    @Test
    public void testGetAllRoles() {
        List<Role> roles = Arrays.asList(new Role(), new Role());
        when(roleService.getAllRoles()).thenReturn(roles);

        String viewName = roleController.getAllRoles(model);

        verify(model).addAttribute("roles", roles);
        assertEquals("roleList", viewName);
    }

    @Test
    public void testGetRoleById() {
        Role role = new Role();
        when(roleService.getRoleById(1)).thenReturn(Optional.of(role));

        ResponseEntity<Role> response = roleController.getRoleById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(role, response.getBody());
    }

    @Test
    public void testGetRoleById_NotFound() {
        when(roleService.getRoleById(1)).thenReturn(Optional.empty());

        ResponseEntity<Role> response = roleController.getRoleById(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }



    @Test
    public void testShowRoleForm() {
        String viewName = roleController.showRoleForm(model);

        // Verify that addAttribute is called with the correct values
        ArgumentCaptor<Role> roleCaptor = ArgumentCaptor.forClass(Role.class);
        verify(model).addAttribute(eq("role"), roleCaptor.capture());

        // Get the captured role object
        Role capturedRole = roleCaptor.getValue();

        // Compare individual properties or use assertAll() to ensure all properties are as expected
        assertEquals(0, capturedRole.getId());
        assertEquals(null, capturedRole.getRoleName());

        // Ensure that the view name returned by the controller method is correct
        assertEquals("role_form", viewName);
    }
    @Test
    public void testCreateOrUpdateRole() {
        Role role = new Role();
        when(roleService.createOrUpdateRole("Admin")).thenReturn(role);

        ResponseEntity<Role> response = roleController.createOrUpdateRole("Admin");

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(role, response.getBody());
    }

    @Test
    public void testShowGetOrDeleteRoleForm() {
        String viewName = roleController.showGetOrDeleteRoleForm();

        assertEquals("getAndDeleteRoleForm", viewName);
    }

    @Test
    public void testDeleteRoleById() {
        ResponseEntity<Void> response = roleController.deleteRoleById(1);

        verify(roleService).deleteRoleById(1);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}

