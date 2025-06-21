package com.example.books.controllers;

import com.example.books.models.Book;
import com.example.books.models.Cart;
import com.example.books.models.OrderBook;
import com.example.books.repos.BookRepo;
import com.example.books.repos.OrderBookRepo;
import com.example.books.repos.UserRepo;
import com.example.books.service.BookService;
import com.example.books.service.BuyService;
import com.example.books.service.OrderBookService;
import com.example.books.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/buy")
public class BuyController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private BuyService buyService;

    @Autowired
    private OrderBookService orderBookService;

    static long idGenerator=1;

    @GetMapping("/cart")
    public String getCart(Model model){
       Cart cart = buyService.getCart();
        model.addAttribute("cart",cart);
        return "cart";
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteBook(@PathVariable long id) {
        Cart cart = buyService.getCart();
        for (OrderBook o : cart.getListOfOrders()) {
            if (o.getId() == id) {
                orderBookService.deleteOrderBook(o);
                break;
            }
        }
        return ResponseEntity.ok().build(); // Return 200 OK
    }


    @PostMapping("/{id}/addToCart")
    public String addToCart(@PathVariable long id){
        Optional<Book> b =  bookService.getBookById(id);
        Cart cart=buyService.getCart();
        if(b.isPresent()) {
            boolean exists= false;
            for(OrderBook o: cart.getListOfOrders()) {
                if (o.getBook().getId() == b.get().getId()) {
                    o.setAmount(o.getAmount()+1);
                    exists=true;
                }
            }
            if(!exists){
                OrderBook order = new OrderBook(buyService.getCart(), b.get(), 1);
                orderBookService.saveOrder(order);
            }
        }
        return "redirect:/books/allBooks";
    }

    @PutMapping("/update/{id}")
    @ResponseBody
    public ResponseEntity<Void> updateQuantity(@PathVariable long id, @RequestParam int quantity){
        Cart cart=buyService.getCart();
        for(OrderBook o: cart.getListOfOrders()) {
            if (o.getId() == id) {
                o.setAmount(quantity);
                orderBookService.saveOrder(o);
                break;
            }
        }
        return ResponseEntity.ok().build();
    }
}
