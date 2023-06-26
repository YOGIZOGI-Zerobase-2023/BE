package com.zerobase.yogizogi.accommodation.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.zerobase.yogizogi.global.entity.BaseEntity;
import com.zerobase.yogizogi.review.domain.entity.Review;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
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
    @Column(name = "rate") //공유 필요
    private Double rate;
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
    @OneToMany(mappedBy = "accommodation")
    @Column(name = "picUrls")
    private List<Picture> picUrls;
    @Column(name = "detail")
    private String detail;

    @OneToMany(mappedBy = "accommodation")
    private List<Room> rooms;

    @OneToMany(mappedBy = "accommodation")
    @JsonManagedReference
    private List<Review> reviews = new ArrayList<>();

    //score값 변경(Double로 객체로 null 허용)
    public void updateScore(Double rate) {
        if (reviews == null || reviews.isEmpty() && rate == null) {
            this.rate = 0.0;
            return;
        }

        double totalRate = 0.0;
        int reviewCount = 0;

        for (Review review : reviews) {
            if (review.getRate() != null) {
                totalRate += review.getRate();
                reviewCount++;
            }
        }
        //현재와 같이 값이 잡혀 있는 이유는, 현재 데이터에는 기본 평점이 있기 때문.
        if (rate != null) {
            totalRate += rate;
            reviewCount++;
        }

        if (reviewCount > 0) {
            this.rate = totalRate / reviewCount;
        } else {
            this.rate = 0.0;
        }
    }
}
