package com.pricematrix.pricematrix.auth.dto;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String newPassword;
}
