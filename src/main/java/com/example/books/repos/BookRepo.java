package com.example.books.repos;

import com.example.books.models.Book;
import com.example.books.models.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepo extends JpaRepository<Book, Long> {

    List<Book> findAllByPagesAndPublication(int pages, int publication);
    List<Book> findAllByGenre(Genre genre);

}
