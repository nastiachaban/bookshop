package com.example.books.controllers;

import com.example.books.models.Author;
import com.example.books.models.Book;
import com.example.books.models.Genre;
import com.example.books.service.AuthorService;
import com.example.books.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BookController {

    private final Logger LOGGER= LoggerFactory.getLogger(BookController.class);

    @Autowired
    private BookService service;

    @Autowired
    private AuthorService service2;

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return new ResponseEntity<>(service.getAllBooks(), HttpStatus.OK);
    }

    @GetMapping("/index")
    public String getIndex(){
        return "index";
    }

    @PostMapping("/createBook")
    public String createBook(Model model, Book book, BindingResult result){
        if(result.hasErrors()){
            LOGGER.error("binding result has errors");
            return "createBook";
        }
        service.createBook(book);
        return "redirect:/books/allBooks";
    }

    @GetMapping("/addBook")
    public String addBook(Model model){
        model.addAttribute("genres",Genre.values());
        model.addAttribute("book",new Book());
        model.addAttribute("authors",service2.getAll());
        return "createBook";
    }

    @GetMapping("/allBooks")
    public String allBooks(Model model){
        model.addAttribute("books",service.getAllBooks());
        System.out.println(Arrays.toString(service.getAllBooks().toArray()));
        return "allBooks";
    }

    @GetMapping("/{id}/delete")
    public String deleteBook(@PathVariable long id,Model model){
        service.deleteBookById(id);
        model.addAttribute("books",service.getAllBooks());
        return "allBooks";
    }

    @GetMapping("/{id}/edit")
    public String editBook(@PathVariable long id, Model model){
        Optional<Book> book=service.getBookById(id);
        if(book.isPresent()){
            model.addAttribute("genres",Genre.values());
            model.addAttribute("authors",service2.getAll());
            model.addAttribute("book",book.get());
            return "editBook";}
        else{
            return "redirect:/books/allBooks";
        }
    }

    @PostMapping("/{id}/edit")
    public String saveChanges(Model model,@PathVariable long id,Book book){
        LOGGER.info(id+"");
        LOGGER.info(book.getId()+" book id");
        book.setId(id);
        service.updateBook(book);
        model.addAttribute("books",service.getAllBooks());
        return "allBooks";
    }

    @GetMapping("/{id}/deleteBook")
    @PreAuthorize("hasRole('ADMIN')")
    public String areYouSure(Model model,@PathVariable long id){
        model.addAttribute("id",id);
        return "deleteBook";
    }

    @GetMapping("/{id}/details")
    public String bookDetails(Model model,@PathVariable long id){
        model.addAttribute("book",service.getBookById(id).get());
        return "BookDetails";
    }

    @PostMapping("/allBooks")
    public String searchBooks(Model model,@RequestParam String criteria,@RequestParam String choice){
        List<Book> books=new ArrayList<>();
        for(Book b: service.getAllBooks()){
            if(choice.equals("name")) {
                if (b.getName().toUpperCase().contains(criteria.toUpperCase())) {
                    books.add(b);
                }
            }
            else if(choice.equals("author")){
                String name=b.getAuthor().getFirstName()+" "+b.getAuthor().getLastName();
                if(name.toUpperCase().contains(criteria.toUpperCase())){
                    books.add(b);
                }
            }
            else if(choice.equals("language")){
                if(b.getLanguage().toUpperCase().contains(criteria.toUpperCase())){
                    books.add(b);
                }
            }
            else if(choice.equals("genre")){
                if(b.getGenre().toString().toUpperCase().contains(criteria.toUpperCase())){
                    books.add(b);
                }
            }
            else if(choice.equals("publication")){
                String publication=b.getPublication()+"";
                if(publication.contains(criteria)){
                    books.add(b);
                }
            }
            else if(choice.equals("pages")){
                String pages=b.getPages()+"";
                if(pages.contains(criteria)){
                    books.add(b);
                }
            }}
        model.addAttribute("books",books);
        return "allBooks";
    }

    @PostMapping("/allBooks/sort")
    public String sortBooks(Model model,@RequestParam String order,@RequestParam String choice2){
        List<Book> books=service.getAllBooks();

        if(choice2.equals("name")) {
            if(order.equals("asc")){
                books.sort((b1, b2) -> b1.getName().compareTo(b2.getName()));
            } else {
                books.sort((b1, b2) -> b2.getName().compareTo(b1.getName()));
            }
        } else if(choice2.equals("author")) {
            if(order.equals("asc")){
                books.sort((b1, b2) -> {
                    String name1 = b1.getAuthor().getFirstName() + " " + b1.getAuthor().getLastName();
                    String name2 = b2.getAuthor().getFirstName() + " " + b2.getAuthor().getLastName();
                    return name1.compareTo(name2);
                });
            } else {
                books.sort((b1, b2) -> {
                    String name1 = b1.getAuthor().getFirstName() + " " + b1.getAuthor().getLastName();
                    String name2 = b2.getAuthor().getFirstName() + " " + b2.getAuthor().getLastName();
                    return name2.compareTo(name1);
                });
            }
        } else if(choice2.equals("language")) {
            if(order.equals("asc")){
                books.sort((b1, b2) -> b1.getLanguage().compareTo(b2.getLanguage()));
            } else {
                books.sort((b1, b2) -> b2.getLanguage().compareTo(b1.getLanguage()));
            }
        } else if(choice2.equals("genre")) {
            if(order.equals("asc")){
                books.sort((b1, b2) -> b1.getGenre().toString().compareTo(b2.getGenre().toString()));
            } else {
                books.sort((b1, b2) -> b2.getGenre().toString().compareTo(b1.getGenre().toString()));
            }
        } else if(choice2.equals("publication")) {
            if(order.equals("asc")){
                books.sort((b1, b2) -> (b1.getPublication()+"").compareTo(b2.getPublication()+""));
            } else {
                books.sort((b1, b2) -> (b2.getPublication()+"").compareTo(b1.getPublication()+""));
            }
        } else if(choice2.equals("pages")) {
            if(order.equals("asc")){
                books.sort((b1, b2) -> Integer.compare(b1.getPages(), b2.getPages()));
            } else {
                books.sort((b1, b2) -> Integer.compare(b2.getPages(), b1.getPages()));
            }
        }

        model.addAttribute("books",books);
        return "allBooks";
    }

}

