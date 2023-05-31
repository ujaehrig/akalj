package de.jaehrig.akalj.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.jaehrig.akalj.infrastructure.SwkaClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class GarbageCalenderConfig {

    private final SwkaClient client;
    private final HtmlExtractor extractor;
    private final ApplicationProperties properties;

    GarbageCalenderConfig(final SwkaClient client, final HtmlExtractor extractor, final ApplicationProperties properties) {
        this.client = client;
        this.extractor = extractor;
        this.properties = properties;
    }

    @Bean
    @ConditionalOnClass({RedisOperations.class})
    GarbageCalendar redisGarbageCalendar(final RedisTemplate<String, String> redisTemplate,
                                         final ObjectMapper objectMapper) {
        KaWebsite garbageCalendar = new KaWebsite(client, extractor, properties);
        return new RedisGarbageCalenderDecorator(redisTemplate, objectMapper, garbageCalendar);
    }

    @Bean
    @ConditionalOnMissingBean(GarbageCalendar.class)
    GarbageCalendar pureGarbageCalendar() {
        return new KaWebsite(client, extractor, properties);
    }

}
