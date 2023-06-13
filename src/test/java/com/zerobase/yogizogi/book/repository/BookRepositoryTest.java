package com.zerobase.yogizogi.book.repository;


import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.zerobase.yogizogi.book.domain.entity.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DataJpaTest
class BookRepositoryTest {

    @Autowired
    BookRepository bookRepository;

    @Test
    @DisplayName("bookRepository 테스트")
    void createBook() {
        //given
        Book book = Book.builder().bookName("홍길동").payAmount(1000).build();
        //when
        Book result = bookRepository.save(book);
        //then
        assertThat(result.getPayAmount()).isEqualTo(book.getPayAmount());
    }

    @Test
    @DisplayName("findAllByUserId 테스트")
    void findAllByUserId() {
        //given
        Long userId = 1L;
        Book book1 = Book.builder().bookName("홍길동").payAmount(1000).userId(userId).build();
        Book book2 = Book.builder().bookName("홍길순").payAmount(2000).userId(userId).build();
        Book book3 = Book.builder().bookName("홍길갑").payAmount(3000).userId(2L).build();

        bookRepository.save(book1);
        bookRepository.save(book2);
        bookRepository.save(book3);

        //when
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> result = bookRepository.findAllByUserId(userId, pageable);
        //then
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent()).containsExactlyInAnyOrder(book1, book2);
    }
}