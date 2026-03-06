package com.pricematrix.pricematrix.auth.dto;

import lombok.Data;

// CreateUserRequest.java
@Data
public class CreateUserRequest {
    private String username;
    private String password;
    private String role;
}
