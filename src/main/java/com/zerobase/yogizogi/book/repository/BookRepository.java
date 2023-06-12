package com.zerobase.yogizogi.book.repository;

import com.zerobase.yogizogi.book.domain.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Page<Book> findAllByUserId(Long userId, Pageable pageable);
}
