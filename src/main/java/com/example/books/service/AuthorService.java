package com.example.books.service;

import com.example.books.models.Author;
import com.example.books.repos.AuthorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepo repo;

    public List<Author> getAll(){
        return repo.findAll();
    }

    public Optional<Author> getById(long id) {
        return repo.findById(id);
    }

    public boolean existsById(long id) {
        return repo.existsById(id);
    }

    public Author createAuthor(Author author) {
        return repo.save(author);
    }

    public void deleteById(long id) {
        repo.deleteById(id);
    }

    public Author updateAuthor(Author author) {
        return repo.save(author);
    }

    public List<Author> findByName(String name) {
        return repo.findAllByFirstName(name);
    }

    public List<Author> sortByLastName() {
        List<Author> authors = repo.findAll();
        authors.sort((B1, B2) -> B1.getLastName().compareTo(B2.getLastName()));
        return authors;
    }
}
