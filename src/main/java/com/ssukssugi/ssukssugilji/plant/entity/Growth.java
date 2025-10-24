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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "growths")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Growth extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long growthId;

    @JoinColumn(name = "userId")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "diaryId")
    @OneToOne(fetch = FetchType.EAGER)
    private Diary beforeDiary;

    @JoinColumn(name = "diaryId")
    @OneToOne(fetch = FetchType.EAGER)
    private Diary afterDiary;
}
