package com.pricematrix.pricematrix.auth.controller;

import com.pricematrix.pricematrix.auth.entity.CommonUser;
import com.pricematrix.pricematrix.auth.repository.CommonUserRepository;
import com.pricematrix.pricematrix.auth.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
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

        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .path("/")
                .maxAge(60 * 60 * 8)
                .sameSite("None")
                .secure(true)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(Map.of(
                "username", user.getUsername(),
                "role", user.getRole()
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .sameSite("None")
                .secure(true)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok(Map.of("message", "登出成功"));
    }
}