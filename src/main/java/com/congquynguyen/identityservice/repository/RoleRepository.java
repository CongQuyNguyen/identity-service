package com.congquynguyen.identityservice.repository;

import com.congquynguyen.identityservice.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, String> {

}
