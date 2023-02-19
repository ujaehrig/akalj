package de.jaehrig.akalj.domain;

import de.jaehrig.akalj.infrastructure.AddressRepository;
import de.jaehrig.akalj.infrastructure.SwkaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GarbageCalenderConfig {

    private final AddressRepository repository;

    GarbageCalenderConfig(final AddressRepository repository) {
        this.repository = repository;
    }

    @Bean
    GarbageCalendar garbageCalendar(final SwkaClient client, final HtmlExtractor extractor, final ApplicationConfiguration config) {
        KaWebsite garbageCalendar = new KaWebsite(client, extractor, config);
        return new CachingGarbageCalendarDecorator(repository, garbageCalendar);
    }

}
