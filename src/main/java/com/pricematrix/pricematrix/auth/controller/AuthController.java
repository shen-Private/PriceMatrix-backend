package com.pricematrix.pricematrix.auth.controller;

import com.pricematrix.pricematrix.auth.entity.CommonUser;
import com.pricematrix.pricematrix.auth.repository.CommonUserRepository;
import com.pricematrix.pricematrix.auth.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final CommonUserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body,
                                   HttpServletResponse response) {

        String username = body.get("username");
        String password = body.get("password");

        CommonUser user = userRepository.findByUsername(username)
                .orElse(null);
        if (user != null) {
            System.out.println("DB hash: " + user.getPasswordHash());
            System.out.println("Input password: " + password);
        }
        if (user == null || !passwordEncoder.matches(password, user.getPasswordHash())) {
            return ResponseEntity.status(401).body(Map.of("error", "帳號或密碼錯誤"));
        }

        if (!user.getIsActive()) {
            return ResponseEntity.status(403).body(Map.of("error", "帳號已停用"));
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());

        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 8); // 8小時
        response.addCookie(cookie);

        return ResponseEntity.ok(Map.of(
                "username", user.getUsername(),
                "role", user.getRole()
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok(Map.of("message", "登出成功"));
    }
}