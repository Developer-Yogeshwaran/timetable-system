package com.example.timetable.dto;

import com.example.timetable.entity.Subject;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class TimetableRequest {

    private int numberOfDays;

    private int periodsPerDay;

    private LocalTime startTime;

    private int periodDuration;      // in minutes

    private int breakAfterPeriod;

    private int breakDuration;

    private int lunchAfterPeriod;

    private int lunchDuration;

    private List<Subject> subjects;
}

