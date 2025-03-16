package com.ssukssugi.ssukssugilji.plant.entity;

import com.ssukssugi.ssukssugilji.common.BaseEntity;
import com.ssukssugi.ssukssugilji.plant.dto.Place;
import com.ssukssugi.ssukssugilji.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "plants")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Plant extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long plantId;

    @Column
    private String plantCategory;

    @Column
    private String name;

    @JoinColumn(name = "userId")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User user;

    @Column
    private Short shine;

    @Column
    private Short wind;

    @Column
    private Place place;

    @Column
    private Boolean secret;
}
