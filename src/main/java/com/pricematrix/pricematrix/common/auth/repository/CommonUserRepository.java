package com.pricematrix.pricematrix.common.auth.repository;

import com.pricematrix.pricematrix.common.auth.entity.CommonUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CommonUserRepository extends JpaRepository<CommonUser, Long> {
    Optional<CommonUser> findByUsername(String username);
}