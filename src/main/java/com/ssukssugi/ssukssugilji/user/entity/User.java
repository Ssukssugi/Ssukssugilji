package com.ssukssugi.ssukssugilji.user.entity;

import com.ssukssugi.ssukssugilji.common.BaseEntity;
import com.ssukssugi.ssukssugilji.user.dto.LoginType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column
    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    @Column
    private String emailAddress;

    @Column
    private String socialId;

    @Column
    private Boolean agreeToReceiveMarketing;

    @Column
    private String nickname;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserDetail userDetail;
}
