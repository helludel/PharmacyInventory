package com.pharmacynew.hanaelnael.DTO;

public class RegistrationDTO {


    private String username;

    private String password;
    private String email;

  // private String roleName;

    public  RegistrationDTO(){

   }

 //   public RegistrationDTO(String roleName) {
  ////      this.roleName = roleName;
   // }

    public RegistrationDTO(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

   // public String getRoleName() {
  //      return roleName;
 //   }

  //  public void setRole(String roleName) {
  //      this.roleName= roleName;
 //   }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    private String selectedRole;

    // Getters and setters for other fields...

    public String getSelectedRole() {
        return selectedRole;
    }

    public void setSelectedRole(String selectedRole) {
        this.selectedRole = selectedRole;
    }

    @Override
    public String toString() {
        return "RegistrationDTO{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
             //   ", roleName=" + roleName +
                '}';
    }
}
