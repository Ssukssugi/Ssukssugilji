package com.ssukssugi.ssukssugilji.user.entity;

import com.ssukssugi.ssukssugilji.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "user_details")
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
    private String nickname;

    @Column
    private Long ageGroup;

    @Column
    @Convert(converter = PlantReasonListConverter.class)
    private List<PlantReason> plantReason;

    @Column
    @Convert(converter = SignupPathListConverter.class)
    private List<SignUpPath> signUpPath;
}
