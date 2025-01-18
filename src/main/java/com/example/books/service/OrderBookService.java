package com.example.books.service;

import com.example.books.models.OrderBook;
import com.example.books.repos.OrderBookRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderBookService {

    @Autowired
    private OrderBookRepo repo;

    public OrderBook saveOrder(OrderBook order){
        repo.save(order);
        return order;
    }

    public void deleteOrderBook(OrderBook order){
        repo.delete(order);
    }
}
