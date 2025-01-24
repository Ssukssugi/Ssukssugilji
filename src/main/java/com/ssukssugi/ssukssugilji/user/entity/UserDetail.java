package com.ssukssugi.ssukssugilji.user.entity;

import com.ssukssugi.ssukssugilji.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "users")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userDetailId;

    @OneToOne
    @JoinColumn(name = "userId")
    private User user;

    @Column
    private Long ageGroup;

    @Column
    @Enumerated(EnumType.STRING)
    private PlantReason plantReason;

    @Column
    @Enumerated(EnumType.STRING)
    private SignUpPath signUpPath;
}
