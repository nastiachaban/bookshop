package com.example.books.service;

import com.example.books.controllers.UserController;
import com.example.books.models.Cart;
import com.example.books.repos.BookRepo;
import com.example.books.repos.CartRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            return null;
        }
    }

    public Cart saveCart(Cart cart){
        repo.save(cart);
        return cart;
    }
}
