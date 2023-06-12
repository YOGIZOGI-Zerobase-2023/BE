package com.zerobase.yogizogi.accommodation.domain.entity;

import com.zerobase.yogizogi.global.entity.BaseEntity;
import com.zerobase.yogizogi.room.domain.entity.Room;
import com.zerobase.yogizogi.user.domain.entity.AppUser;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
public class Accommodation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int category;
    private String address;
    private String name;
    private String detailContent;
    private double lat;
    private double lnt;
    private String image;
    private String location;
    @ManyToOne
    private AppUser user;
    @OneToMany
    private List<Room> rooms;
}
