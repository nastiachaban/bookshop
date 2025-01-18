package com.example.books.service;

import com.example.books.models.Book;
import com.example.books.models.Genre;
import com.example.books.repos.BookRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepo repo;

    public List<Book> getAllBooks() {
        return repo.findAll();
    }

    public Optional<Book> getBookById(long id) {
        return repo.findById(id);
    }

    public boolean existsById(long id) {
        return repo.existsById(id);
    }

    public Book createBook(Book book) {
        return repo.save(book);
    }

    public void deleteBookById(long id) {
        repo.deleteById(id);
    }

    public Book updateBook(Book book) {
        return repo.save(book);
    }

    public List<Book> getByPagesAndPublicationYear(int pages, int publication) {
        return repo.findAllByPagesAndPublication(pages, publication);
    }

    public List<Book> getByGenre(Genre genre) {
        return repo.findAllByGenre(genre);
    }
}
