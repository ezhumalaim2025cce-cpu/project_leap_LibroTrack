package com.example.libro_track.Controller;


import com.example.libro_track.Entity.Book;
import com.example.libro_track.Service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // Add book
    @PostMapping
    public Book addBook(
            @Valid @RequestBody Book book) {

        return bookService.addBook(book);
    }

    // Get all books
    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    // Get book
    @GetMapping("/{id}")
    public Book getBook(@PathVariable Long id) {
        return bookService.getBook(id);
    }

    // Update book
    @PutMapping("/{id}")
    public Book updateBook(
            @PathVariable Long id,
            @Valid @RequestBody Book book) {

        return bookService.updateBook(id, book);
    }

    // Delete book
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(
            @PathVariable Long id) {

        bookService.deleteBook(id);

        return ResponseEntity.ok(
                "Book deleted successfully"
        );
    }

    // Search by title
    @GetMapping("/search/title")
    public List<Book> searchTitle(
            @RequestParam String title) {

        return bookService.searchByTitle(title);
    }

    // Search by author
    @GetMapping("/search/author")
    public List<Book> searchAuthor(
            @RequestParam String author) {

        return bookService.searchByAuthor(author);
    }

    // Search by category
    @GetMapping("/search/category")
    public List<Book> searchCategory(
            @RequestParam String category) {

        return bookService.searchByCategory(category);
    }
}