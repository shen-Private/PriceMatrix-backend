package com.pricematrix.pricematrix.common.auth.dto;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String newPassword;
}
