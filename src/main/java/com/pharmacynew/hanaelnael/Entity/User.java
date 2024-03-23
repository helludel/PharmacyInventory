package com.pharmacynew.hanaelnael.Entity;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.Objects;
//uniqueConstraints=@UniqueConstraint(columnNames = "email")
@Table(name="user")
@Entity
public class User implements UserDetails {

    @Column(name="Id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
@Column(name="User_name")
    private String username;
@Column(name="Password")
    private String password;

@Column(name="email")
    private String email;


    @OneToOne
    @JoinColumn(name = "role_id", unique = true)

    private  Role role;



    public User(){

    }
    public User(Role role) {
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

  //  public void setPassword(String password) {
    //    this.password = password;}

    public User(String username, String password, String email) {
        this.username = username;
        this.email = email;
        this.password = password;

    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }



    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public String getPassword() {
        return password;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


   @Override
   public boolean equals(Object obj) {
       if (this == obj) {
           return true;
       }
       if (obj == null || getClass() != obj.getClass()) {
           return false;
       }
       User user = (User) obj;
       return Objects.equals(username, user.username);
   }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
    @Transient
    private PasswordEncoder passwordEncoder() {
        return
                PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    @Override
    public String toString() {
        return "User{" +
                "Id=" + Id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                '}';
    }

}