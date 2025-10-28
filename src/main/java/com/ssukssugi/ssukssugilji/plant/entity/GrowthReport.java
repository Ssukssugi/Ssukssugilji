package com.ssukssugi.ssukssugilji.plant.entity;


import com.ssukssugi.ssukssugilji.common.BaseEntity;
import com.ssukssugi.ssukssugilji.user.entity.User;
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

@Table(name = "growth_reports")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class GrowthReport extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long growthReportId;

    @JoinColumn(name = "growthId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Growth growth;

    @JoinColumn(name = "reporterId")
    @ManyToOne(fetch = FetchType.LAZY)
    private User reporter;
}
