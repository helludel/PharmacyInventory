package com.pharmacynew.hanaelnael.Controll;

import com.pharmacynew.hanaelnael.DTO.RegistrationDTO;
import com.pharmacynew.hanaelnael.Entity.Role;
import com.pharmacynew.hanaelnael.Entity.User;
import com.pharmacynew.hanaelnael.Service.RoleEmployeeService;
import com.pharmacynew.hanaelnael.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/Pharma")
public class UserControl {

    private final UserService userService;


    private final RoleEmployeeService roleEmployeeService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserControl(UserService userService,RoleEmployeeService roleEmployeeService,PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleEmployeeService=roleEmployeeService;
        this.passwordEncoder=passwordEncoder;
    }

    @GetMapping("/listOfUsers")
    public String getAllUsers(Model model) {
        List<User> users = userService.getAllUsers( );
        model.addAttribute("users",users);
        return "employeeList";
    }
    @GetMapping("/userDeleteOrGetById")
    public String userDeleteOrGetByIdForm(){
        return "userRemoveOrFindById";
    }

    @GetMapping("/getUserById")
    public ResponseEntity<Optional<User>> getUserById(@RequestParam int id) {
        Optional<User> user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
    @DeleteMapping("/userDeleteById")
    public ResponseEntity<Void> deleteUserById(@RequestParam int id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/showRegistrationForm")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registrationDTO", new RegistrationDTO( ));
        return "registrationForm";
    }
    @PostMapping("/createUser")
    public String createUser(
            @ModelAttribute("registrationDTO") @Valid RegistrationDTO registrationDTO,
            @RequestParam(name = "roleName", required = false) String roleName,
            BindingResult bindingResult,
            Model model) {

        List<Role> roles = roleEmployeeService.getAllRoles();

        model.addAttribute("roles", roles);

        if (bindingResult.hasErrors()) {
            return "registrationForm";
        }

        // Proceed with user creation
        userService.createUser(registrationDTO, roleName);

        // If execution reaches here, user creation was successful
        return "success";
    }


    @GetMapping("/userNameUpdateForm")
    public String showUserNameUpdateForm(){
        return "userNameUpdateForm";
    }
    @PostMapping("/updateUserName")
    public String updateUserName(Model model,@RequestParam String email,@RequestParam String newUserName){
        userService.updateUserName(email, newUserName);
        return "success";
}
    @GetMapping("/changePasswordForm")
    public String showChangePasswordForm(){
        return "changePasswordForm";
    }
    @PostMapping("/changePassword")
    public String changePassword(Model model,@RequestParam String email,
                                 @RequestParam String currentPassword,
                                 @RequestParam String newPassword){
    userService.changePassword(email, currentPassword, newPassword);
            return "success";
    }
    @GetMapping("/forgetPasswordForm")
    public String showForgetPasswordForm(){
        return "forgetPasswordForm";
    }
    @PostMapping("/resetPassword")
    public String resetPassword(Model model,@RequestParam String email,
                                 @RequestParam String userName,
                                 @RequestParam String newPassword){
        userService.resetPassword(email, userName, newPassword);
        return "success";
    }


}