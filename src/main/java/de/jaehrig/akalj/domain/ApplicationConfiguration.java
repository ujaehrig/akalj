package de.jaehrig.akalj.domain;

import java.util.List;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("akalj")
public record ApplicationConfiguration(
        Map<String, List<String>> garbageTypes
) {}
