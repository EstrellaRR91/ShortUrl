package com.shorturl;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.shorturl.dto.LoginRequest;
import com.shorturl.dto.RegisterRequest;
import com.shorturl.service.AuthenticationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController("AuthenticationControllerV2")
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<com.shorturl.dto.AuthenticatorResponse> register(@RequestBody RegisterRequest request) {
        
        return ResponseEntity.ok(authenticationService.register(request));
        
    }
    
    @PostMapping("/login")
    public ResponseEntity<com.shorturl.dto.AuthenticatorResponse> login(@RequestBody LoginRequest request) {
        
        return ResponseEntity.ok(authenticationService.login(request));
    }
    
}
