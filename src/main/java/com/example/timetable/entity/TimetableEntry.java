package com.example.timetable.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Getter
@Setter
public class TimetableEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String day;

    private int periodNumber;

    private LocalTime startTime;

    private LocalTime endTime;

    private String type;   // CLASS / BREAK / LUNCH

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;
}
