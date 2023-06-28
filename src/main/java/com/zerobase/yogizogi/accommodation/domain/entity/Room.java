package com.zerobase.yogizogi.accommodation.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.Set;
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
public class Room {

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
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "accommodationId")
    private Accommodation accommodation;

    @JsonManagedReference
    @OneToMany(mappedBy = "room")
    private Set<RoomPicture> pictures;

    @JsonManagedReference
    @OneToMany(mappedBy = "room")
    private Set<Price> prices;

}
