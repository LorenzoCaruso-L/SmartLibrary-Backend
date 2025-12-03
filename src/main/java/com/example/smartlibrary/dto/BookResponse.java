package com.example.smartlibrary.dto;

public class BookResponse {
    private Long id;
    private String title;
    private String author;
    private Integer publicationYear;
    private String genre;
    private Integer copiesAvailable;
    private String description;
    private String coverImageUrl;
    private Double averageRating;
    private Long reviewsCount;

    public BookResponse(Long id, String title, String author, Integer publicationYear, String genre,
                        Integer copiesAvailable, String description, String coverImageUrl, Double averageRating, Long reviewsCount) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.genre = genre;
        this.copiesAvailable = copiesAvailable;
        this.description = description;
        this.coverImageUrl = coverImageUrl;
        this.averageRating = averageRating;
        this.reviewsCount = reviewsCount;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public Integer getPublicationYear() { return publicationYear; }
    public String getGenre() { return genre; }
    public Integer getCopiesAvailable() { return copiesAvailable; }
    public String getDescription() { return description; }
    public String getCoverImageUrl() { return coverImageUrl; }
    public Double getAverageRating() { return averageRating; }
    public Long getReviewsCount() { return reviewsCount; }

    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setPublicationYear(Integer publicationYear) { this.publicationYear = publicationYear; }
    public void setGenre(String genre) { this.genre = genre; }
    public void setCopiesAvailable(Integer copiesAvailable) { this.copiesAvailable = copiesAvailable; }
    public void setDescription(String description) { this.description = description; }
    public void setCoverImageUrl(String coverImageUrl) { this.coverImageUrl = coverImageUrl; }
    public void setAverageRating(Double averageRating) { this.averageRating = averageRating; }
    public void setReviewsCount(Long reviewsCount) { this.reviewsCount = reviewsCount; }
}

