package com.congquynguyen.identityservice.repository;

import com.congquynguyen.identityservice.entity.TokenValidationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenValidationRepository extends JpaRepository<TokenValidationEntity, String> {
}
