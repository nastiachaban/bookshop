package com.example.books;

import com.example.books.controllers.BookController;
import com.example.books.models.Author;
import com.example.books.models.Book;
import com.example.books.models.Genre;
import com.example.books.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class BookControllerTest {

    @Mock
    BookService service;

    @InjectMocks
    BookController controller;

    @Test
    public void getAllBooksTest(){
        Book book1= new Book("book1",new Author("fn","ln",2000,"qwertz","static/image/test.png","usa"),"ukrainian", Genre.ADVENTURES,2000,150);
        Book book2= new Book("book2",new Author("fn","ln",2000,"qwertz","static/image/test.png","usa"),"ukrainian", Genre.ADVENTURES,2000,150);

        given(service.getAllBooks())
                .willReturn(List.of(book1,book2));

        ResponseEntity<List<Book>> result=controller.getAllBooks();
        assertThat(result.getBody(),notNullValue());
        assertThat(result.getBody().size(), equalTo(new Integer(2)));
    }
}
