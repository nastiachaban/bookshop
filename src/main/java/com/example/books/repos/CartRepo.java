package com.example.books.repos;

import com.example.books.models.Book;
import com.example.books.models.Cart;
import com.example.books.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepo extends JpaRepository<Cart, Long> {

    boolean existsByUser(User user);

    Optional<Cart> findCartByUser(User user);
}
