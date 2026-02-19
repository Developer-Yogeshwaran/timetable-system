package com.example.timetable.dto;

import com.example.timetable.entity.TimetableEntry;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class TimetableResponse {

    private String message;

    private List<TimetableEntry> timetable;
}
