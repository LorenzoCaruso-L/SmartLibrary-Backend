package com.example.smartlibrary.service;

import com.example.smartlibrary.model.Book;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GoogleBooksService {
    
    private static final String GOOGLE_BOOKS_API = "https://www.googleapis.com/books/v1/volumes?q=";
    private final RestTemplate restTemplate;
    
    public GoogleBooksService() {
        this.restTemplate = new RestTemplate();
    }
    

    public List<Book> searchAndConvert(String query, int maxResults) {
        try {
            int searchMaxResults = Math.min(maxResults * 2, 40); // Cerca 2x libri per compensare quelli senza copertina
            String url = GOOGLE_BOOKS_API + query.replace(" ", "+") 
                      + "&maxResults=" + searchMaxResults;
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            
            if (response == null || !response.containsKey("items")) {
                return new ArrayList<>();
            }
            
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> items = (List<Map<String, Object>>) response.get("items");
            
            List<Book> books = new ArrayList<>();
            
            for (Map<String, Object> item : items) {
                if (books.size() >= maxResults) {
                    break;
                }
                
                @SuppressWarnings("unchecked")
                Map<String, Object> volumeInfo = (Map<String, Object>) item.get("volumeInfo");
                
                if (volumeInfo == null) continue;
                
                Book book = convertToBook(volumeInfo);
                if (book != null) {
                    books.add(book);
                }
            }
            
            return books;
            
        } catch (Exception e) {
            System.err.println("Errore nella ricerca Google Books: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    private Book convertToBook(Map<String, Object> volumeInfo) {
        try {
            String title = (String) volumeInfo.get("title");
            if (title == null || title.isEmpty()) return null;
            
            @SuppressWarnings("unchecked")
            List<String> authors = (List<String>) volumeInfo.get("authors");
            String author = (authors != null && !authors.isEmpty()) ? authors.get(0) : "Autore sconosciuto";
            
            Integer year = null;
            if (volumeInfo.containsKey("publishedDate")) {
                String publishedDate = (String) volumeInfo.get("publishedDate");
                if (publishedDate != null && publishedDate.length() >= 4) {
                    try {
                        year = Integer.parseInt(publishedDate.substring(0, 4));
                    } catch (NumberFormatException e) {
                    }
                }
            }
            
            String genre = "Generale";
            @SuppressWarnings("unchecked")
            List<String> categories = (List<String>) volumeInfo.get("categories");
            if (categories != null && !categories.isEmpty()) {
                genre = categories.get(0);
            }
            
            String description = (String) volumeInfo.get("description");
            if (description != null && description.length() > 2000) {
                description = description.substring(0, 1997) + "...";
            }

            String coverImageUrl = null;
            @SuppressWarnings("unchecked")
            Map<String, Object> imageLinks = (Map<String, Object>) volumeInfo.get("imageLinks");
            if (imageLinks != null) {
                coverImageUrl = (String) imageLinks.get("thumbnail");
                if (coverImageUrl != null) {
                    coverImageUrl = coverImageUrl.replace("http://", "https://")
                                                 .replace("&zoom=1", "&zoom=0")
                                                 .replace("zoom=1", "zoom=0");
                }
            }
            
            if (coverImageUrl == null || coverImageUrl.trim().isEmpty()) {
                return null;
            }
            
            Book book = new Book();
            book.setTitle(title);
            book.setAuthor(author);
            book.setPublicationYear(year);
            book.setGenre(genre);
            book.setCopiesAvailable(10);
            book.setDescription(description);
            book.setCoverImageUrl(coverImageUrl);
            
            return book;
            
        } catch (Exception e) {
            System.err.println("Errore nella conversione libro: " + e.getMessage());
            return null;
        }
    }
}

