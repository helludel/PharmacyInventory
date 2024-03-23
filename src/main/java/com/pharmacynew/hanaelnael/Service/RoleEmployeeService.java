package com.pharmacynew.hanaelnael.Service;

import com.pharmacynew.hanaelnael.DAO.RoleDAO;
import com.pharmacynew.hanaelnael.Entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
    @Service
    public class RoleEmployeeService {

        private final RoleDAO roleDAO;

        @Autowired
        public RoleEmployeeService(RoleDAO roleDAO) {
            this.roleDAO = roleDAO;
        }


        public List<Role> getAllRoles() {
            List<Role>role= (List<Role>) roleDAO.findAll();
            return role;
        }

        public Optional<Role> getRoleById(int id) {
            return roleDAO.findById(id);
        }

        public Role createOrUpdateRole(String roleName) {
            if (roleName == null) {
                // Return a default role or create a default role object
                return new Role("Employee");
            }

            Role role =new Role(roleName );

            return roleDAO.save(role);
        }

        public Role getRoleByRoleName(String roleName){
            return roleDAO.findByRoleName(roleName);
        }
        public void deleteRoleById(int id) {
            roleDAO.deleteById(id);
        }
    }

