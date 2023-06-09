package com.zerobase.yogizogi.room.domain.entity;

import com.zerobase.yogizogi.accommodation.domain.entity.Accommodation;
import com.zerobase.yogizogi.book.domain.entity.Book;
import com.zerobase.yogizogi.global.entity.BaseEntity;
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
public class Room extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int roomDefaultPeople;
    private String roomName;
    private Integer peopleNum;
    private Integer maxRoomNum;
    private String roomInTime;
    private String roomOutTime;
    private Integer personAddFee;
    //외래키
    @ManyToOne
    private Accommodation accommodation;
    @ManyToOne
    private AppUser user;
    @OneToMany
    private List<Book> books;
    @OneToMany
    private List<Price> prices;
}
