package com.example.timetable.service;

import com.example.timetable.dto.TimetableRequest;
import com.example.timetable.entity.Subject;
import com.example.timetable.entity.TimetableEntry;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.*;

@Component
public class TimetableGenerator {

    public List<TimetableEntry> generate(TimetableRequest request,
                                         List<Subject> subjects) {

        List<TimetableEntry> timetable = new ArrayList<>();
        Map<Long, Integer> remainingHours = new HashMap<>();
        Random random = new Random();

        // Track subject hours
        for (Subject subject : subjects) {
            remainingHours.put(subject.getId(), subject.getHoursPerWeek());
        }

        for (int day = 1; day <= request.getNumberOfDays(); day++) {

            LocalTime currentTime = request.getStartTime();

            for (int period = 1; period <= request.getPeriodsPerDay(); period++) {

                // BREAK
                if (period == request.getBreakAfterPeriod()) {
                    timetable.add(createEntry("Day " + day, period,
                            currentTime,
                            currentTime.plusMinutes(request.getBreakDuration()),
                            null,
                            "BREAK"));

                    currentTime = currentTime.plusMinutes(request.getBreakDuration());
                    continue;
                }

                // LUNCH
                if (period == request.getLunchAfterPeriod()) {
                    timetable.add(createEntry("Day " + day, period,
                            currentTime,
                            currentTime.plusMinutes(request.getLunchDuration()),
                            null,
                            "LUNCH"));

                    currentTime = currentTime.plusMinutes(request.getLunchDuration());
                    continue;
                }

                // Available subjects
                List<Subject> availableSubjects = subjects.stream()
                        .filter(s -> remainingHours.get(s.getId()) > 0)
                        .toList();

                if (availableSubjects.isEmpty()) break;

                Subject randomSubject =
                        availableSubjects.get(random.nextInt(availableSubjects.size()));

                timetable.add(createEntry("Day " + day, period,
                        currentTime,
                        currentTime.plusMinutes(request.getPeriodDuration()),
                        randomSubject,
                        "CLASS"));

                currentTime = currentTime.plusMinutes(request.getPeriodDuration());

                remainingHours.put(randomSubject.getId(),
                        remainingHours.get(randomSubject.getId()) - 1);
            }
        }

        return timetable;
    }

    private TimetableEntry createEntry(String day,
                                       int period,
                                       LocalTime start,
                                       LocalTime end,
                                       Subject subject,
                                       String type) {

        TimetableEntry entry = new TimetableEntry();
        entry.setDay(day);
        entry.setPeriodNumber(period);
        entry.setStartTime(start);
        entry.setEndTime(end);
        entry.setSubject(subject);
        entry.setType(type);

        return entry;
    }
}
