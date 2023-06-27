package com.zerobase.yogizogi.user.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.zerobase.yogizogi.book.domain.entity.Book;
import com.zerobase.yogizogi.global.entity.BaseEntity;
import com.zerobase.yogizogi.review.domain.entity.Review;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
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
@Table(name = "appUser")
public class AppUser extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String email;
    private boolean sns;
    @Column(nullable = false)
    private String password;
    @Column(unique = true)
    private String nickName;

    private String bookName;

    @Column(unique = true)
    @Pattern(regexp = "^(01[016-9])-(\\d{3,4})-(\\d{4})$", message = "휴대폰 번호 형식이 유효하지 않습니다.")
    private String phoneNumber;

    private boolean active;

    private String emailAuthKey;

    private LocalDateTime emailAuthDateTime;

    //외래키
    @JsonManagedReference
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)//mappedBy는 자체 속성 변수명으로 해야함
    private Set<Book> books;
    @JsonManagedReference
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Review> reviews;

    //@OneToMany
    //private List<ChattingRoom> chattingRooms;
    //@OneToMany
    //private List<Message> messages;
}
