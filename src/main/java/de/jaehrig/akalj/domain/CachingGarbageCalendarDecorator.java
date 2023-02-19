package de.jaehrig.akalj.domain;

import de.jaehrig.akalj.domain.model.AddressEntity;
import de.jaehrig.akalj.domain.model.CalendarEntryEntity;
import de.jaehrig.akalj.infrastructure.AddressRepository;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

public class CachingGarbageCalendarDecorator implements GarbageCalendar {

    private final AddressRepository addressRepository;
    private final GarbageCalendar decorated;

    public CachingGarbageCalendarDecorator(final AddressRepository addressRepository,
                                           final GarbageCalendar decorated) {
        this.addressRepository = addressRepository;
        this.decorated = decorated;
    }

    @Override
    public Collection<CalendarEntry> calendarEntries(final String street, final String number) {
        return getFromDatabase(street, number)
                .filter(this::isUpToDate)
                .orElseGet(() -> update(street, number));

    }

    private Collection<CalendarEntry> update(final String street, final String number) {
        Collection<CalendarEntry> calendarEntries = decorated.calendarEntries(street, number);
        writeToDatabase(street, number, calendarEntries);
        return calendarEntries;
    }

    private void writeToDatabase(final String street, final String number, final Collection<CalendarEntry> calendarEntries) {
        AddressEntity address = addressRepository.findByStreetAndNumber(street, number)
                .orElse(new AddressEntity(street, number));

        address.setEntries(calendarEntries.stream()
                .map(entry -> new CalendarEntryEntity(entry.type(), entry.date()))
                .toList()
        );
        addressRepository.save(address);
    }

    private boolean isUpToDate(final Collection<CalendarEntry> calendarEntries) {
        LocalDate today = LocalDate.now();
        return calendarEntries
                .stream()
                .noneMatch(e -> e.date().isBefore(today));
    }

    private Optional<Collection<CalendarEntry>> getFromDatabase(String street, String number) {
        return addressRepository.findByStreetAndNumber(street, number)
                .map(AddressEntity::getEntries)
                .map(this::convert);
    }

    private Collection<CalendarEntry> convert(final Collection<CalendarEntryEntity> calendarEntryEntities) {
        return calendarEntryEntities.stream()
                .map(entity -> new CalendarEntry(entity.getType(), entity.getDate()))
                .toList();
    }
}
