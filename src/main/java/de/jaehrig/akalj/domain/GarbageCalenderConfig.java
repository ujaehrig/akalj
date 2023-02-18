package de.jaehrig.akalj.domain;

import de.jaehrig.akalj.infrastructure.SwkaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GarbageCalenderConfig {

    private final SwkaClient client;
    private final HtmlExtractor extractor;
    private final ApplicationConfiguration config;

    GarbageCalenderConfig(final SwkaClient client, final HtmlExtractor extractor, final ApplicationConfiguration config) {
        this.client = client;
        this.extractor = extractor;
        this.config = config;
    }

    @Bean
    GarbageCalendar garbageCalendar() {
        KaWebsite garbageCalendar = new KaWebsite(client, extractor, config);
        return new CachingGarbageCalendarDecorator(garbageCalendar);
    }

}
