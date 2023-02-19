package de.jaehrig.akalj.domain;

import java.util.Collection;

public interface GarbageCalendar {

    /**
     * return a list of {@link CalendarEntry} for the given street/number
     * the list is empty, if no values could be found
     *
     * @param street
     * @param number
     */
    Collection<CalendarEntry> calendarEntries(String street, String number);

}
