package de.jaehrig.akalj.application;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

import de.jaehrig.akalj.domain.GarbageCalendar;
import java.time.LocalDate;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Map;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.util.RandomUidGenerator;
import net.fortuna.ical4j.util.UidGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class AkaljController {

    private final GarbageCalendar garbageCalendar;

    @Autowired
    public AkaljController(final GarbageCalendar garbageCalendar) {
        this.garbageCalendar = garbageCalendar;
    }

    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, List<LocalDate>> getJson(@RequestParam final String street,
                                                @RequestParam final String number) {
        return getMap(street, number);
    }

    @GetMapping(path = "", produces = "text/calendar")
    public String getIcal(@RequestParam final String street,
                          @RequestParam final String number) {
        Calendar calendar = new Calendar().withDefaults().getFluentTarget();
        UidGenerator ug = new RandomUidGenerator();

        getMap(street, number).entrySet().stream()
                .flatMap(e -> e.getValue().stream().map(v -> new SimpleEntry<>(e.getKey(), v)))
                .forEach(e -> calendar
                        .withComponent(new VEvent(new Date(), e.getKey())
                                .withProperty(ug.generateUid()).getFluentTarget())
                );

        return calendar.toString();
    }

    private Map<String, List<LocalDate>> getMap(final String street, final String number) {
         return garbageCalendar.calendarEntries(street, number)
                .stream()
                .map(e -> new SimpleEntry<>(e.type().toString(), e.date()))
                .collect(groupingBy(SimpleEntry::getKey, mapping(SimpleEntry::getValue, toList())));
    }
}
