package com.example.books.repos;

import com.example.books.models.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorRepo extends JpaRepository<Author,Long> {

    List<Author> findAllByFirstName(String firstName);
}
