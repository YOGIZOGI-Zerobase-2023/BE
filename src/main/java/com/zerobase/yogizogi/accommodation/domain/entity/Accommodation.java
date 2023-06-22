package com.zerobase.yogizogi.accommodation.domain.entity;

import com.zerobase.yogizogi.global.entity.BaseEntity;
import com.zerobase.yogizogi.review.domain.entity.Review;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "accommodation")
public class Accommodation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accommodationId")
    private Long id;
    @Column(name = "category")
    private Integer category;
    @Column(name = "name")
    private String name;
    @Column(name = "score") //공유 필요
    private Double score;
    @Column(name = "region")
    private String region;
    @Column(name = "ano")
    private Integer ano;
    @Column(name = "lat")
    private Double lat;
    @Column(name = "lng")
    private Double lng;
    @Column(name = "picUrl")
    private String picUrl;
    @Column(name = "address")
    private String address;
    @OneToMany(mappedBy = "accommodation", fetch = FetchType.LAZY)
    @Column(name = "picUrls")
    private List<Picture> picUrls;
    @Column(name = "detail")
    private String detail;

    @OneToMany(mappedBy = "accommodation", fetch = FetchType.LAZY)
    private List<Room> rooms;

    @OneToMany(mappedBy = "accommodation", fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();

    //score값 변경(Double로 객체로 null 허용)
    public void updateScore(Double score) {
        if (reviews == null || reviews.isEmpty() && score == null) {
            this.score = 0.0;
            return;
        }

        double totalRate = 0.0;
        int reviewCount = 0;

        for (Review review : reviews) {
            totalRate += review.getRate();
            reviewCount++;
        }
        score += totalRate / reviewCount;

        this.score = score;
    }
}
