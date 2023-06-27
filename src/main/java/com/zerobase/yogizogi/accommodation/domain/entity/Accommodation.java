package com.zerobase.yogizogi.accommodation.domain.entity;

import com.zerobase.yogizogi.global.entity.BaseEntity;
import java.util.List;
import java.util.Set;
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
    private int category;
    @Column(name = "name")
    private String name;
    @Column(name = "rate")
    private double rate;
    @Column(name = "region")
    private String region;
    @Column(name = "ano")
    private int ano;
    @Column(name = "lat")
    private double lat;
    @Column(name = "lon")
    private double lon;
    @Column(name = "picUrl")
    private String picUrl;
    @Column(name = "address")
    private String address;
    @OneToMany(mappedBy = "accommodation")
    @Column(name = "picUrls")
    private Set<Picture> picUrls;
    @Column(name = "detail")
    private String detail;

    @OneToMany(mappedBy = "accommodation")
    private Set<Room> rooms;
    @OneToMany(mappedBy = "accommodation")
    @JsonManagedReference
    private List<Review> reviews = new ArrayList<>();

    //rate 변경(Double로 객체로 null 허용)
    public void updateScore(double rate) {
        if (reviews == null || reviews.isEmpty() && rate == null) {
            this.rate = 0.0;
            return;
        }

        double totalRate = 0.0;
        int reviewCount = 0;
        for (Review review : reviews) {
                totalRate += review.getRate();
                reviewCount++;
        }
        //현재와 같이 값이 잡혀 있는 이유는, 현재 데이터에는 기본 평점이 있기 때문.
            totalRate += rate;
            reviewCount++;


        if (reviewCount > 0) {
            this.rate = totalRate / reviewCount;
        } else {
            this.rate = 0.0;
        }
    }
}
