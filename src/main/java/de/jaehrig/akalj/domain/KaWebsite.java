package de.jaehrig.akalj.domain;

import de.jaehrig.akalj.infrastructure.SwkaClient;
import java.time.LocalDate;
import java.util.AbstractMap.SimpleEntry;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

class KaWebsite implements GarbageCalendar {

    private final SwkaClient client;
    private final HtmlExtractor extractor;
    final Map<String, List<String>> garbageTypes;

    KaWebsite(final SwkaClient client, final HtmlExtractor extractor, final ApplicationProperties config) {
        this.client = client;
        this.extractor = extractor;
        garbageTypes = Map.copyOf(config.garbageTypes());
    }

    public Collection<CalendarEntry> calendarEntries(String street, String number) {
        return getMap(street, number).entrySet().stream()
                .flatMap(e -> e.getValue().stream().map(v -> new SimpleEntry<>(e.getKey(), v)))
                .filter(e -> fromString(e.getKey()).isPresent())
                .map(e -> new CalendarEntry(fromString(e.getKey()).orElseThrow(), e.getValue()))
                .toList();
    }

    private Optional<GarbageType> fromString(String s) {
        return garbageTypes.entrySet().stream()
                .filter(entry -> entry.getValue().contains(s))
                .map(Entry::getKey)
                .map(GarbageType::valueOf)
                .findAny();
    }

    private Map<String, List<LocalDate>> getMap(final String street, final String number) {
        var page = client.getPage(street, number);
        return extractor.extract(page);
    }
}
