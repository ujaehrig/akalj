package de.jaehrig.akalj.domain;

import static java.util.function.Predicate.not;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisGarbageCalenderDecorator implements GarbageCalendar {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisGarbageCalenderDecorator.class);
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final GarbageCalendar decorated;

    RedisGarbageCalenderDecorator(final RedisTemplate<String, String> redisTemplate,
                                  final ObjectMapper objectMapper,
                                  final GarbageCalendar decorated) {

        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.decorated = decorated;
    }

    @Override
    public Collection<CalendarEntry> calendarEntries(final String street, final String number) {
        return getFromRedis(street, number)
                .filter(this::isUpToDate)
                .orElseGet(() -> update(street, number));
    }

    private Optional<Collection<CalendarEntry>> getFromRedis(final String street, final String number) {
        try {
            String key = key(street, number);
            if (redisTemplate.hasKey(key) == Boolean.TRUE) {
                TypeReference<Collection<CalendarEntry>> typeRef = new TypeReference<>() {
                };
                String value = redisTemplate.opsForValue().get(key);
                return Optional.of(objectMapper.readValue(value, typeRef));
            }
        } catch (JsonProcessingException e) {
            LOGGER.info("Read failed", e);
        }
        return Optional.empty();
    }

    private void writeToRedis(final String street, final String number, final Collection<CalendarEntry> calendarEntries) {
        try {
            String key = key(street, number);
            String value = objectMapper.writeValueAsString(calendarEntries);
            redisTemplate.opsForValue().set(key, value);

            long expiration = calcExpiration(calendarEntries).getSeconds();
            LOGGER.debug("Set expiration to {} seconds", expiration);
            redisTemplate.expire(key, expiration, TimeUnit.SECONDS);
        } catch (JsonProcessingException exc) {
            LOGGER.warn("Saving failed", exc);
        }
    }

    private Duration calcExpiration(final Collection<CalendarEntry> calendarEntries) {
        return calendarEntries.stream()
                .map(CalendarEntry::date)
                .reduce(this::minDate)
                .map(d -> Duration.ofDays(ChronoUnit.DAYS.between(LocalDate.now(), d)))
                .filter(not(Duration::isNegative))
                .orElse(Duration.ofDays(1));
    }

    private LocalDate minDate(final LocalDate d1, final LocalDate d2) {
        return d1.isBefore(d2) ? d1 : d2;
    }

    private String key(final String street, final String number) {
        return street + ":" + number;
    }

    private Collection<CalendarEntry> update(final String street, final String number) {
        Collection<CalendarEntry> calendarEntries = decorated.calendarEntries(street, number);
        writeToRedis(street, number, calendarEntries);
        return calendarEntries;
    }

    private boolean isUpToDate(final Collection<CalendarEntry> calendarEntries) {
        LocalDate today = LocalDate.now();
        return calendarEntries
                .stream()
                .noneMatch(e -> e.date().isBefore(today));
    }

}
