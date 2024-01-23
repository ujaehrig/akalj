package de.jaehrig.akalj.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import javax.net.ssl.SSLException;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

class SwkaClientTest {

    @Test
    void getKnownAddress() throws SSLException {
        // given
        WebClient.Builder builder = WebClient.builder();
        SwkaClient classUnderTest = new SwkaClient(builder);

        // when
        String page = classUnderTest.getPage("Neureuter Hauptstrasse", "1");

        // then
        assertThat(page).isNotEmpty();
    }
}
