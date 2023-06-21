package com.zerobase.yogizogi.room.domain.entity;

import com.zerobase.yogizogi.accommodation.domain.entity.Accommodation;
import com.zerobase.yogizogi.global.entity.BaseEntity;
import com.zerobase.yogizogi.accommodation.domain.entity.Price;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "room")
public class Room extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roomId")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "checkIn")
    private String checkInTime;
    @Column(name = "checkOut")
    private String cheekOutTime;
    @Column(name = "defaultPeople")
    private Integer defaultPeople;
    @Column(name = "maxPeople")
    private Integer maxPeople;
    //외래키
    @ManyToOne
    @JoinColumn(name = "accommodationId")
    private Accommodation accommodation;

// user 정보를 저장할 것인가? book과 연결
//    @ManyToOne
//    private AppUser user;

    @OneToMany(mappedBy = "room")
    private List<Price> prices;
}
