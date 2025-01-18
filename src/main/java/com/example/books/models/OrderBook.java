package com.example.books.models;

import jakarta.persistence.*;

@Entity
public class OrderBook {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Cart cart;

    @ManyToOne
    private Book book;
    private int amount;

    public OrderBook(Cart cart, Book book, int amount) {
        this.cart = cart;
        this.book = book;
        this.amount = amount;
    }

    public OrderBook() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
