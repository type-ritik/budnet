package com.network.buddy.model;

import java.sql.Date;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.network.buddy.dto.AuthenticateUserRequest;
import com.network.buddy.dto.RegisterUserRequest;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private UUID id;

    @Column(nullable = false, length = 25)
    private String name;

    @Column(unique = true, nullable = false, length = 20)
    private String username;

    @Column(unique = true, nullable = false, length = 40)
    private String email;

    @Column(nullable = false, length = 80)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    public UserEntity(RegisterUserRequest signupRequest) {
        this.name = signupRequest.name();
        this.username = signupRequest.username();
        this.email = signupRequest.email();
        this.password = signupRequest.password();
    }

    public UserEntity(AuthenticateUserRequest loginRequest) {
        this.email = loginRequest.email();
        this.password = loginRequest.password();
    }

    public UserEntity() {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setPassword(String _password) {
        this.password = _password;
    }

    public void setName(String _name) {
        this.name = _name;
    }

    public void setEmail(String _email) {
        this.email = _email;
    }

    public void setUsername(String _username) {
        this.username = _username;
    }

    public void setRole(Role _role) {
        this.role = _role;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return this.role;
    }

}
