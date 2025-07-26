package com.ssukssugi.ssukssugilji.plant.entity;

import com.ssukssugi.ssukssugilji.common.BaseEntity;
import com.ssukssugi.ssukssugilji.plant.dto.Place;
import com.ssukssugi.ssukssugilji.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
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
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column
    private Short shine;

    @Column
    @Enumerated(EnumType.STRING)
    private Place place;

    @OneToMany(mappedBy = "plant", cascade = CascadeType.ALL)
    private List<Diary> diaries = new ArrayList<>();

    public void addDiary(Diary diary) {
        diaries.add(diary);
        diary.setPlant(this);
    }

    public void deleteDiary(Diary diary) {
        diaries.remove(diary);
        diary.setPlant(null);
    }
}
