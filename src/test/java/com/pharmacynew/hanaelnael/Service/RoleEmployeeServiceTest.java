package com.pharmacynew.hanaelnael.Service;

import com.pharmacynew.hanaelnael.DAO.RoleDAO;
import com.pharmacynew.hanaelnael.Entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RoleEmployeeServiceTest {

    @Mock
    private RoleDAO roleDAO;

    @InjectMocks
    private RoleEmployeeService roleEmployeeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllRoles() {
        Role role1 = new Role("ROLE_USER");
        Role role2 = new Role("ROLE_ADMIN");
        List<Role> roles = Arrays.asList(role1, role2);

        when(roleDAO.findAll()).thenReturn(roles);

        List<Role> result = roleEmployeeService.getAllRoles();

        assertEquals(2, result.size());
        assertTrue(result.contains(role1));
        assertTrue(result.contains(role2));
        verify(roleDAO, times(1)).findAll();
    }

    @Test
    public void testGetRoleById() {
        Role role = new Role("ROLE_USER");
        role.setId(1);

        when(roleDAO.findById(1)).thenReturn(Optional.of(role));

        Optional<Role> result = roleEmployeeService.getRoleById(1);

        assertTrue(result.isPresent());
        assertEquals("ROLE_USER", result.get().getRoleName());
        verify(roleDAO, times(1)).findById(1);
    }

    @Test
    void testCreateOrUpdateRole_NewRole() {
        String roleName = "ROLE_USER";
        Role role = new Role(roleName);

        // Mock dependencies
        RoleDAO roleDAOMock = mock(RoleDAO.class);
        RoleEmployeeService roleEmployeeService = new RoleEmployeeService(roleDAOMock);

        // Capture the argument passed to roleDAO.save()
        ArgumentCaptor<Role> roleCaptor = ArgumentCaptor.forClass(Role.class);

        // Set up the mock behavior
        when(roleDAOMock.save(roleCaptor.capture())).thenReturn(role);

        // Call the method under test
        Role result = roleEmployeeService.createOrUpdateRole(roleName);

        // Verify the result
        assertNotNull(result);
        assertEquals(roleName, result.getRoleName());

        // Verify that roleDAO.save() was called with the expected argument
        verify(roleDAOMock, times(1)).save(roleCaptor.capture());
        assertEquals(roleName, roleCaptor.getValue().getRoleName());
    }
    @Test
    public void testCreateOrUpdateRole_NullRoleName() {
        Role defaultRole = new Role("Employee");

        Role result = roleEmployeeService.createOrUpdateRole(null);

        assertNotNull(result);
        assertEquals("Employee", result.getRoleName());
        verify(roleDAO, never()).save(any(Role.class));
    }

    @Test
    public void testGetRoleByRoleName() {
        String roleName = "ROLE_USER";
        Role role = new Role(roleName);

        when(roleDAO.findByRoleName(roleName)).thenReturn(role);

        Role result = roleEmployeeService.getRoleByRoleName(roleName);

        assertNotNull(result);
        assertEquals(roleName, result.getRoleName());
        verify(roleDAO, times(1)).findByRoleName(roleName);
    }

    @Test
    public void testDeleteRoleById() {
        int roleId = 1;

        doNothing().when(roleDAO).deleteById(roleId);

        roleEmployeeService.deleteRoleById(roleId);

        verify(roleDAO, times(1)).deleteById(roleId);
    }
}
