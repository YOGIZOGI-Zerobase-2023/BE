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
    @Column(name = "score")
    private double score;
    @Column(name = "region")
    private String region;
    @Column(name = "ano")
    private int ano;
    @Column(name = "lat")
    private double lat;
    @Column(name = "lng")
    private double lng;
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
}
