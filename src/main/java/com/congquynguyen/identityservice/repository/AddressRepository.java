package com.congquynguyen.identityservice.repository;

import com.congquynguyen.identityservice.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, String> {

}
