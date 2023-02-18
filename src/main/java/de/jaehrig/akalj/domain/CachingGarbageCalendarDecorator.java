package de.jaehrig.akalj.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class CachingGarbageCalendarDecorator implements GarbageCalendar{

    private final GarbageCalendar decorated;

    public CachingGarbageCalendarDecorator(final GarbageCalendar decorated) {
        this.decorated = decorated;
    }

    @Override
    public List<CalendarEntry> calendarEntries(final String street, final String number) {
        return getFromDatabase(street, number)
                .filter(this::isUpToDate)
                .orElseGet(() -> update(street, number));


    }

    private List<CalendarEntry> update(final String street, final String number) {
         List<CalendarEntry> calendarEntries = decorated.calendarEntries(street, number);
        writeToDatabase(calendarEntries);
        return calendarEntries;
    }

    private void writeToDatabase(final List<CalendarEntry> calendarEntries) {
        // TODO
    }

    private boolean isUpToDate(final List<CalendarEntry> calendarEntries) {
        LocalDate today = LocalDate.now();
        return calendarEntries
                .stream()
                .anyMatch(e -> e.date().isBefore(today));
    }

    private Optional<List<CalendarEntry>> getFromDatabase(String street, String number) {
        return Optional.empty();
    }

}
