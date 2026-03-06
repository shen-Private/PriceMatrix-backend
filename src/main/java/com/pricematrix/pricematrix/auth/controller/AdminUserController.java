package com.pricematrix.pricematrix.auth.controller;

import com.pricematrix.pricematrix.auth.dto.CreateUserRequest;
import com.pricematrix.pricematrix.auth.dto.ResetPasswordRequest;
import com.pricematrix.pricematrix.auth.dto.UpdateStatusRequest;
import com.pricematrix.pricematrix.auth.entity.CommonUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.pricematrix.pricematrix.auth.service.AdminUserService;
import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    // 查所有帳號
    @GetMapping
    public ResponseEntity<List<CommonUser>> getAllUsers() {
        return ResponseEntity.ok(adminUserService.getAllUsers());
    }

    // 新增帳號
    @PostMapping
    public ResponseEntity<CommonUser> createUser(@RequestBody CreateUserRequest request) {
        return ResponseEntity.ok(adminUserService.createUser(request));
    }

    // 停用 / 啟用
    @PatchMapping("/{id}/status")
    public ResponseEntity<CommonUser> updateStatus(@PathVariable Long id, @RequestBody UpdateStatusRequest request) {
        return ResponseEntity.ok(adminUserService.updateStatus(id, request.isActive()));
    }

    // 重設密碼
    @PatchMapping("/{id}/password")
    public ResponseEntity<Void> resetPassword(@PathVariable Long id, @RequestBody ResetPasswordRequest request) {
        adminUserService.resetPassword(id, request.getNewPassword());
        return ResponseEntity.ok().build();
    }
}