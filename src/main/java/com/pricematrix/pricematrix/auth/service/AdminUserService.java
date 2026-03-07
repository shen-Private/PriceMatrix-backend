package com.pricematrix.pricematrix.auth.service;
import com.pricematrix.pricematrix.auth.dto.CreateUserRequest;
import com.pricematrix.pricematrix.auth.entity.CommonUser;
import com.pricematrix.pricematrix.auth.repository.CommonUserRepository;
import com.pricematrix.pricematrix.auth.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final CommonUserRepository commonUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public CommonUser findById(Long id) {
        return commonUserRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    // 查所有帳號
    public List<CommonUser> getAllUsers() {
        return commonUserRepository.findAll();
    }

    // 新增帳號
    public CommonUser createUser(CreateUserRequest request) {
        CommonUser user = new CommonUser();
        user.setUsername(request.getUsername());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setIsActive(true);
        return commonUserRepository.save(user);
    }

    // 停用 / 啟用
    public CommonUser updateStatus(Long id, boolean isActive) {
        CommonUser user = commonUserRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setIsActive(isActive);
        return commonUserRepository.save(user);
    }

    // 重設密碼
    public void resetPassword(Long id, String newPassword) {
        CommonUser user = commonUserRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        commonUserRepository.save(user);
    }
}