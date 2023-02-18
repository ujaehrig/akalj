package de.jaehrig.akalj.domain;

import java.util.List;

public interface GarbageCalendar {

    /**
     * return a list of {@link CalendarEntry} for the given street/number
     * the list is empty, if no values could be found
     *
     * @param street
     * @param number
     */
    List<CalendarEntry> calendarEntries(String street, String number);

}
