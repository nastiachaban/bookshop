package com.example.books.models;
import jakarta.persistence.*;

@Entity
public class Book {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @ManyToOne
    private Author author;
    private String language;
    @Enumerated(EnumType.STRING)
    private Genre genre;
    private int publication;
    private int pages;
    private double price;
    private int available;

    public Book(){}

    public Book(String name, Author author, String language, Genre genre, int publication, int pages) {
        this.name = name;
        this.author = author;
        this.language = language;
        this.genre = genre;
        this.publication = publication;
        this.pages = pages;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public int getPublication() {
        return publication;
    }

    public void setPublication(int publication) {
        this.publication = publication;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "Book{" +
                "firstName='" + name + '\'' +
                ", author=" + author +
                ", language='" + language + '\'' +
                ", genre=" + genre +
                ", publication=" + publication +
                ", pages=" + pages +
                '}';
    }
}
