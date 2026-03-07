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
import io.jsonwebtoken.Claims;
import java.util.Map;
import com.pricematrix.pricematrix.auth.util.JwtUtil;
@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;
    private final JwtUtil jwtUtil;

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
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestBody UpdateStatusRequest request,
            @CookieValue(name = "jwt", required = false) String token) {

        if (token == null) return ResponseEntity.status(401).body(Map.of("error", "未登入"));

        Claims claims = jwtUtil.parseToken(token);
        String currentUsername = claims.getSubject();

        // 查出要操作的帳號
        CommonUser target = adminUserService.findById(id);
        if (target.getUsername().equals(currentUsername)) {
            return ResponseEntity.status(400).body(Map.of("error", "不能停用自己的帳號"));
        }

        return ResponseEntity.ok(adminUserService.updateStatus(id, request.isActive()));
    }

    // 重設密碼
    @PatchMapping("/{id}/password")
    public ResponseEntity<Void> resetPassword(@PathVariable Long id, @RequestBody ResetPasswordRequest request) {
        adminUserService.resetPassword(id, request.getNewPassword());
        return ResponseEntity.ok().build();
    }
}