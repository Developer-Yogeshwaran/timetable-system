timetableservice.java- package com.example.timetable.service;

import com.example.timetable.model.*;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.*;

@Service
public class TimetableService {

    private List<TimetableEntry> lastGeneratedTimetable = new ArrayList<>();

    public List<TimetableEntry> generateTimetable(TimetableRequest request) {

        List<TimetableEntry> timetable = new ArrayList<>();

        int days = request.getNumberOfDays();
        int periodsPerDay = request.getPeriodsPerDay();

        LocalTime currentTime = LocalTime.parse(request.getStartTime());

        List<Subject> subjects = new ArrayList<>(request.getSubjects());

        // Expand subjects based on hours per week
        List<Subject> subjectPool = new ArrayList<>();
        for (Subject subject : subjects) {
            for (int i = 0; i < subject.getHoursPerWeek(); i++) {
                subjectPool.add(subject);
            }
        }

        Collections.shuffle(subjectPool);

        int subjectIndex = 0;

        for (int day = 1; day <= days; day++) {

            currentTime = LocalTime.parse(request.getStartTime());

            for (int period = 1; period <= periodsPerDay; period++) {

                // BREAK
                if (period == request.getBreakAfterPeriod()) {

                    LocalTime breakEnd = currentTime.plusMinutes(request.getBreakDuration());

                    timetable.add(createEntry(day, period,
                            currentTime, breakEnd,
                            "Break", null));

                    currentTime = breakEnd;
                    continue;
                }

                // LUNCH
                if (period == request.getLunchAfterPeriod()) {

                    LocalTime lunchEnd = currentTime.plusMinutes(request.getLunchDuration());

                    timetable.add(createEntry(day, period,
                            currentTime, lunchEnd,
                            "Lunch", null));

                    currentTime = lunchEnd;
                    continue;
                }

                // SUBJECT
                Subject subject = null;

                if (subjectIndex < subjectPool.size()) {
                    subject = subjectPool.get(subjectIndex);
                    subjectIndex++;
                }

                LocalTime endTime = currentTime.plusMinutes(request.getPeriodDuration());

                timetable.add(createEntry(day, period,
                        currentTime, endTime,
                        "Lecture", subject));

                currentTime = endTime;
            }
        }

        this.lastGeneratedTimetable = timetable;

        return timetable;
    }

    public List<TimetableEntry> getLastGeneratedTimetable() {
        return lastGeneratedTimetable;
    }

    private TimetableEntry createEntry(int day,
                                       int period,
                                       LocalTime start,
                                       LocalTime end,
                                       String type,
                                       Subject subject) {

        TimetableEntry entry = new TimetableEntry();
        entry.setDay("Day " + day);
        entry.setPeriodNumber(period);
        entry.setStartTime(start);
        entry.setEndTime(end);
        entry.setType(type);
        entry.setSubject(subject);

        return entry;
    }
}
