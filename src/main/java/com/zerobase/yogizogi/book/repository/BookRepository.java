package com.zerobase.yogizogi.book.repository;

import com.zerobase.yogizogi.book.domain.entity.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAllByUser_Id(Long userId);
}
