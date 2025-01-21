package com.congquynguyen.identityservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "address")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class AddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "country")
    String country;

    @Column(name = "city")
    String city;

    @Column(name = "district")
    String district;

    @Column(name = "ward")
    String ward;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    UserEntity user;
}
