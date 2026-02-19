package com.example.timetable.repository;

import com.example.timetable.entity.TimetableConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimetableConfigRepository extends JpaRepository<TimetableConfig, Long> {
}
