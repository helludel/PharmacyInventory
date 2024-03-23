package com.pharmacynew.hanaelnael.Controll;

import com.pharmacynew.hanaelnael.Entity.Role;
import com.pharmacynew.hanaelnael.Service.RoleEmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

    @Controller
    @RequestMapping("/Pharma")
    public class RoleController {

        private final RoleEmployeeService roleService;

        public RoleController(RoleEmployeeService roleService) {
            this.roleService = roleService;
        }

        @GetMapping("/listRoles")
        public String getAllRoles(Model model) {
            List<Role>roles=roleService.getAllRoles();
            model.addAttribute("roles",roles);
            return "roleList";
        }

        @GetMapping("/roleGet")
        public ResponseEntity<Role> getRoleById(@RequestParam int id) {
            Optional<Role> role = roleService.getRoleById(id);
            return role.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

        }
        @GetMapping("/createRoleForm")
        public String showRoleForm(Model model) {
            model.addAttribute("role", new Role());
            return "role_form";
        }

        @PostMapping("/createRole")
        public ResponseEntity<Role> createOrUpdateRole(@RequestParam String roleName) {
            Role createdOrUpdatedRole = roleService.createOrUpdateRole(roleName);
            return new ResponseEntity<>(createdOrUpdatedRole, HttpStatus.CREATED);
        }
        @GetMapping("/getOrDeleteRole")
        public String showGetOrDeleteRoleForm(){
            return "getAndDeleteRoleForm";
        }
        @DeleteMapping("/roleDelete")
        public ResponseEntity<Void> deleteRoleById(@RequestParam int id) {
            roleService.deleteRoleById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

