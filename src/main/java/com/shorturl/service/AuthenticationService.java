package com.shorturl.service;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.shorturl.dto.AuthenticatorResponse;
import com.shorturl.dto.LoginRequest;
import com.shorturl.dto.RegisterRequest;
import com.shorturl.model.User;
import com.shorturl.repository.UserRepository;
import com.shorturl.security.JwtUtil;


@Service
public class AuthenticationService{
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtils;
    private final PasswordEncoder passwordEncoder;




public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtil jwtUtils){
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtils =jwtUtils; 
        this.passwordEncoder =passwordEncoder; 

    }
        public boolean login(String username, String rawPassword){
            User user = userRepository.findByUsername(username).orElse(null);
            return user !=null && passwordEncoder.matches(rawPassword, user.getPassword());
        }

    public AuthenticatorResponse register (RegisterRequest request){

    if (userRepository.findByUsername(request.getUsername()).isPresent()) {
        throw new RuntimeException( "El usuario ya existe.");
    }

    User user = new User();
    user.setUsername(request.getUsername());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    userRepository.save(user);

    String token =jwtUtils.generateJwtToken(user.getUsername(), null);
    return new AuthenticatorResponse(token);
}  

public AuthenticatorResponse login (LoginRequest request){
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

    String token= jwtUtils.generateJwtToken(request.getUsername(), null);
    return new AuthenticatorResponse(token)
    ;
}

}


