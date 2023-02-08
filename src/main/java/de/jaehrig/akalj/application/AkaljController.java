package de.jaehrig.akalj.application;

import de.jaehrig.akalj.infrastructure.SwkaClient;
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

    private final SwkaClient client;
    private final de.jaehrig.akalj.domain.HtmlExtractor extractor;

    @Autowired
    public AkaljController(final SwkaClient client, de.jaehrig.akalj.domain.HtmlExtractor extractor) {
        this.client = client;
        this.extractor = extractor;
    }

    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, List<LocalDate>> getJson(@RequestParam String street, @RequestParam String number) {
        return getMap(street, number);
    }

    @GetMapping(path = "", produces = "text/calendar")
    public String getIcal(@RequestParam String street, @RequestParam String number) {
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
        var page = client.getPage(street, number);
        return extractor.extract(page);
    }
}
