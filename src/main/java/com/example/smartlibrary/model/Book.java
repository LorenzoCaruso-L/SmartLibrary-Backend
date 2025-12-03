package com.example.smartlibrary.model;

import jakarta.persistence.*;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private Integer publicationYear;
    private String genre;
    private Integer copiesAvailable;
    
    @Column(length = 2000)
    private String description;
    
    private String coverImageUrl;

    public Book() {}

    public Book(String title, String author, Integer year, String genre, Integer copiesAvailable) {
        this.title = title;
        this.author = author;
        this.publicationYear = year;
        this.genre = genre;
        this.copiesAvailable = copiesAvailable;
    }
    
    public Book(String title, String author, Integer year, String genre, Integer copiesAvailable, String description) {
        this.title = title;
        this.author = author;
        this.publicationYear = year;
        this.genre = genre;
        this.copiesAvailable = copiesAvailable;
        this.description = description;
    }
    
    public Book(String title, String author, Integer year, String genre, Integer copiesAvailable, String description, String coverImageUrl) {
        this.title = title;
        this.author = author;
        this.publicationYear = year;
        this.genre = genre;
        this.copiesAvailable = copiesAvailable;
        this.description = description;
        this.coverImageUrl = coverImageUrl;
    }

    // GETTERS E SETTERS

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public Integer getPublicationYear() { return publicationYear; }
    public void setPublicationYear(Integer publicationYear) { this.publicationYear = publicationYear; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public Integer getCopiesAvailable() { return copiesAvailable; }
    public void setCopiesAvailable(Integer copiesAvailable) { this.copiesAvailable = copiesAvailable; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCoverImageUrl() { return coverImageUrl; }
    public void setCoverImageUrl(String coverImageUrl) { this.coverImageUrl = coverImageUrl; }

}
