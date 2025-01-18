package com.example.books.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy="cart")
    private List<OrderBook> listOfOrders;

    public double getTotalPrice(){
        double total=0;
        for(OrderBook o: listOfOrders){
            total+=o.getAmount()*o.getBook().getPrice();
        }
        return total;
    }

    public Cart() {
        listOfOrders= new ArrayList<>();
    }

    public Cart(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void addOrderBook(OrderBook book){
        listOfOrders.add(book);
    }

    public List<OrderBook> getListOfOrders() {
        return listOfOrders;
    }

    public void setListOfOrders(List<OrderBook> listOfOrders) {
        this.listOfOrders = listOfOrders;
    }
}
