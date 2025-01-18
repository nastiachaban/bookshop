package com.example.books.repos;

import com.example.books.models.Book;
import com.example.books.models.OrderBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderBookRepo extends JpaRepository<OrderBook, Long> {
}
