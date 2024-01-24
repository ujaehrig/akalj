package de.jaehrig.akalj.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import de.jaehrig.akalj.domain.ApplicationProperties;
import java.util.Map;
import javax.net.ssl.SSLException;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

class SwkaClientTest {

    final ApplicationProperties properties = new ApplicationProperties(
            Map.of(),
            "https://web4.karlsruhe.de/service/abfall/akal/akal_2024.php",
            false);

    @Test
    void getKnownAddress() throws SSLException {
        // given
        WebClient.Builder builder = WebClient.builder();
        SwkaClient classUnderTest = new SwkaClient(builder, properties);

        // when
        String page = classUnderTest.getPage("Neureuter Hauptstrasse", "1");

        // then
        assertThat(page).isNotEmpty();
    }
}
