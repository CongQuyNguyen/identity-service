package com.congquynguyen.identityservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Table(name = "role")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class RoleEntity {

    @Id
    String name;

    String description;

    @ManyToMany
    Set<PermissionEntity> permissions;
}
