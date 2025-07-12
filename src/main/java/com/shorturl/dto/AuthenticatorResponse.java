package com.shorturl.dto;

public class AuthenticatorResponse {
    private String token;

        public AuthenticatorResponse (String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
}
