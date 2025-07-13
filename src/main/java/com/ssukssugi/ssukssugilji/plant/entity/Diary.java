package com.ssukssugi.ssukssugilji.plant.entity;

import com.ssukssugi.ssukssugilji.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;
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
public class Diary extends BaseEntity {

    @Id
    @GeneratedValue
    private Long diaryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plantId")
    private Plant plant;

    @Column
    private LocalDate date;

    @Column
    @Convert(converter = CareTypeConverter.class)
    private List<CareType> careTypes;

    @Column
    private String diary;

    @Column
    private String imageUrl;
}
