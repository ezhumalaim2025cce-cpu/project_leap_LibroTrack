package com.example.libro_track.Repository;


import com.example.libro_track.Entity.IssueRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IssueRecordRepository
        extends JpaRepository<IssueRecord, Long> {

    List<IssueRecord> findByStudentIdAndReturnDateIsNull(Long studentId);

    List<IssueRecord> findByBookIdAndReturnDateIsNull(Long bookId);
}
