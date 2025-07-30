package com.shorturl.model;

import jakarta.persistence.*;
import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users")
public class User implements UserDetails{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 

    @Column( unique = true, nullable = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    public User(){}

    public User(String username, String password, Role role){
        this.username = username;
        this.password = password;
        this.role = role;

    }

    public Long getId() {
        return id;
    }
    public void setId(Long id){
        this.id =id;
    }
    public String getUsername(){
        return username;
    }

    public void setUsername (String username) {
        this.username = username; 
    }

    public void setPassword(String password){
        this.password= password;
    }

    public Role getRole(){
        return role;
    }

    public void setRole (Role role){
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return Collections.singleton(new SimpleGrantedAuthority(role.name()));
    }
    @Override
    public String getPassword(){
        return password;
    }

    @Override public boolean isAccountNonExpired(){ return true;}

    @Override public boolean isAccountNonLocked() { return true;}

    @Override public boolean isCredentialsNonExpired(){ return true;}

    @Override public boolean isEnabled() { return true;}
    }