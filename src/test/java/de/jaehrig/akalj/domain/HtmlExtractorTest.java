package de.jaehrig.akalj.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class HtmlExtractorTest {

    @Test
    void emptyDocument() {
        // given

        // when
        Map<String, List<LocalDate>> extract = new HtmlExtractor().extract("");

        // then
        assertThat(extract).isEmpty();
    }

    @Test
    void document() throws IOException {
        // given
        StringWriter out = new StringWriter();
        InputStream htmlDoc = getClass().getResourceAsStream("akal.html");
        InputStreamReader rdr = new InputStreamReader(htmlDoc);
        for (int c = rdr.read(); c != -1; c = rdr.read()) {
            out.write(c);
        }

        // when
        Map<String, List<LocalDate>> extract = new HtmlExtractor().extract(out.toString());

        // then
        assertThat(extract)
                .containsExactlyInAnyOrderEntriesOf(Map.of(
                        "Papier",
                                List.of(
                                        LocalDate.of(2023, 2, 14),
                                        LocalDate.of(2023, 3, 14),
                                        LocalDate.of(2023, 4, 12)),
                        "Restmüll",
                                List.of(
                                        LocalDate.of(2023, 2, 13),
                                        LocalDate.of(2023, 2, 27),
                                        LocalDate.of(2023, 3, 13)),
                        "Wertstoff",
                                List.of(LocalDate.of(2023, 2, 16), LocalDate.of(2023, 3, 2), LocalDate.of(2023, 3, 16)),
                        "Bioabfall",
                                List.of(
                                        LocalDate.of(2023, 2, 8),
                                        LocalDate.of(2023, 2, 15),
                                        LocalDate.of(2023, 2, 22))));
    }
}
