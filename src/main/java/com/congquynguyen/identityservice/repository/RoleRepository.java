package com.congquynguyen.identityservice.repository;

import com.congquynguyen.identityservice.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, String> {

}
