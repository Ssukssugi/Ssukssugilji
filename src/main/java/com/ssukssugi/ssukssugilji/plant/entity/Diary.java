package com.ssukssugi.ssukssugilji.plant.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "diaries")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Diary {

    @Id
    @GeneratedValue
    private Long diaryId;

    @Column
    private Long plantId;

    @Column
    private LocalDate date;

    @Column
    @Enumerated(EnumType.STRING)
    private CareType careType;

    @Column
    private String diary;

    @Column
    private String imageUrl;
}
