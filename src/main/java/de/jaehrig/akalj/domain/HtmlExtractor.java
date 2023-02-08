package de.jaehrig.akalj.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

@Component
public class HtmlExtractor {

    private static final Pattern DATE_PATTERN = Pattern.compile("[0-3]\\d\\.[01]\\d\\.20\\d\\d");
    private static final Pattern FIRST_WORD_PATTERN = Pattern.compile("[^,]*");


    public Map<String, List<LocalDate>> extract(String htmlDocument) {
        Document document = Jsoup.parse(htmlDocument);
        return Optional.ofNullable(document.getElementById("nfoo"))
                .map(content -> content.getElementsByClass("row"))
                .map(rows -> rows.stream()
                        .filter(row -> !row.getElementsByClass("col_3-3").isEmpty())
                        .map(this::rowToEntry)
                        .collect(Collectors.toMap(Entry::getKey, Entry::getValue)))
                .orElse(Map.of());
    }

    private Map.Entry<String, List<LocalDate>> rowToEntry(Element row) {
        String title = row.getElementsByClass("col_3-2").text();
        var matcher = FIRST_WORD_PATTERN.matcher(title);
        String key = matcher.find() ? matcher.group() : title;

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        List<LocalDate> value = row.getElementsByClass("col_3-3")
                .stream()
                .map(Element::text)
                .flatMap(this::getDates)
                .map(s -> LocalDate.parse(s, dateTimeFormatter))
                .toList();

        return new SimpleEntry<>(key, value);
    }

    private Stream<String> getDates(final String s) {
        return DATE_PATTERN.matcher(s)
                .results()
                .map(MatchResult::group);
    }

}
