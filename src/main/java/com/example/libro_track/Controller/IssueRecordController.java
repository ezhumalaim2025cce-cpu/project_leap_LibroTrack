package com.example.libro_track.Controller;


import com.example.libro_track.Entity.IssueRecord;
import com.example.libro_track.Service.IssueRecordService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/issues")
public class IssueRecordController {

    private final IssueRecordService issueService;

    public IssueRecordController(
            IssueRecordService issueService) {

        this.issueService = issueService;
    }

    // Issue book
    @PostMapping("/issue")
    public IssueRecord issueBook(
            @RequestParam Long bookId,
            @RequestParam Long studentId) {

        return issueService.issueBook(
                bookId,
                studentId
        );
    }

    // Return book
    @PutMapping("/return/{issueId}")
    public String returnBook(
            @PathVariable Long issueId) {

        double fine =
                issueService.returnBook(issueId);

        return "Book returned successfully. Fine = ₹"
                + fine;
    }

    // List currently issued books
    // for a student
    @GetMapping("/student/{studentId}")
    public List<IssueRecord> getIssuedBooks(
            @PathVariable Long studentId) {

        return issueService
                .getBooksIssuedToStudent(studentId);
    }
}
