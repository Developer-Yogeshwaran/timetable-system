package com.example.timetable.repository;

import com.example.timetable.entity.TimetableEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimetableEntryRepository extends JpaRepository<TimetableEntry, Long> {
}
