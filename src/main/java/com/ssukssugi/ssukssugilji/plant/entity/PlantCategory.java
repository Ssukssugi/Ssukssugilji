package com.ssukssugi.ssukssugilji.plant.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "plant_categories")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class PlantCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long plantCategoryId;

    @Column
    private String name;
}
