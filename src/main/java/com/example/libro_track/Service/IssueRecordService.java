package com.example.libro_track.Service;


import com.example.libro_track.Entity.Book;
import com.example.libro_track.Entity.IssueRecord;
import com.example.libro_track.Entity.Student;
import com.example.libro_track.Repository.BookRepository;
import com.example.libro_track.Repository.IssueRecordRepository;
import com.example.libro_track.Repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class IssueRecordService {

    private final IssueRecordRepository issueRepository;
    private final BookRepository bookRepository;
    private final StudentRepository studentRepository;

    private static final double FINE_PER_DAY = 5.0;

    public IssueRecordService(
            IssueRecordRepository issueRepository,
            BookRepository bookRepository,
            StudentRepository studentRepository) {

        this.issueRepository = issueRepository;
        this.bookRepository = bookRepository;
        this.studentRepository = studentRepository;
    }

    public IssueRecord issueBook(Long bookId, Long studentId) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() ->
                        new RuntimeException("Book not found"));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new RuntimeException("Student not found"));

        // Business Rule:
        // Cannot issue if no copies are available

        if (book.getAvailableCopies() <= 0) {
            throw new RuntimeException(
                    "Book cannot be issued. All copies are already checked out."
            );
        }

        IssueRecord record = new IssueRecord();

        record.setBook(book);
        record.setStudent(student);

        LocalDate today = LocalDate.now();

        record.setIssueDate(today);

        // Due date = 14 days
        record.setDueDate(today.plusDays(14));

        record.setFine(0);

        book.setAvailableCopies(
                book.getAvailableCopies() - 1
        );

        bookRepository.save(book);

        return issueRepository.save(record);
    }

    public double returnBook(Long issueId) {

        IssueRecord record = issueRepository.findById(issueId)
                .orElseThrow(() ->
                        new RuntimeException("Issue record not found"));

        if (record.getReturnDate() != null) {
            throw new RuntimeException(
                    "Book has already been returned."
            );
        }

        LocalDate returnDate = LocalDate.now();

        record.setReturnDate(returnDate);

        long overdueDays = 0;

        if (returnDate.isAfter(record.getDueDate())) {

            overdueDays =
                    ChronoUnit.DAYS.between(
                            record.getDueDate(),
                            returnDate
                    );
        }

        double fine = overdueDays * FINE_PER_DAY;

        record.setFine(fine);

        Book book = record.getBook();

        book.setAvailableCopies(
                book.getAvailableCopies() + 1
        );

        bookRepository.save(book);

        issueRepository.save(record);

        return fine;
    }

    public List<IssueRecord> getBooksIssuedToStudent(
            Long studentId) {

        return issueRepository
                .findByStudentIdAndReturnDateIsNull(studentId);
    }
}