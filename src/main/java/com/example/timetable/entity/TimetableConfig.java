package com.example.timetable.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Getter
@Setter
public class TimetableConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int numberOfDays;

    private int periodsPerDay;

    private LocalTime startTime;

    private int periodDuration;      // in minutes

    private int breakAfterPeriod;

    private int breakDuration;       // in minutes

    private int lunchAfterPeriod;

    private int lunchDuration;       // in minutes
}
