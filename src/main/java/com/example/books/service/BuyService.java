package com.example.books.service;

import com.example.books.controllers.UserController;
import com.example.books.models.Cart;
import com.example.books.repos.BookRepo;
import com.example.books.repos.CartRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class BuyService {

    @Autowired
    private CartRepo repo;

    public void addCart(){
        if(!repo.existsByUser(UserController.user)){
         Cart cart=new Cart(UserController.user);
         repo.save(cart);
        }
    }

    public Cart getCart(){
        Optional<Cart> cart = repo.findCartByUser(UserController.user);
        if(cart.isPresent()){
            return cart.get();
        }
        else{
            Cart cart2= new Cart(UserController.user);
            cart2.setListOfOrders(new ArrayList<>());
            repo.save(cart2);
            return cart2;
        }
    }

    public Cart saveCart(Cart cart){
        repo.save(cart);
        return cart;
    }
}
