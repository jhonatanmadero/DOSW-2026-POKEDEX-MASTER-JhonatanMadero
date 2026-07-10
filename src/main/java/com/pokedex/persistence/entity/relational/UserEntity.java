package com.pokedex.persistence.entity.relational;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "app_user", indexes = @Index(name = "idx_user_email", columnList = "email"))
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "password_hash", length = 200)
    private String passwordHash;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "favorite_region", length = 50)
    private String favoriteRegion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RoleEntity role;

    @Column(nullable = false)
    private Boolean active;

    @Column(name = "from_google", nullable = false)
    private Boolean fromGoogle;

    @Column(name = "avatar_url", length = 300)
    private String avatarUrl;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
