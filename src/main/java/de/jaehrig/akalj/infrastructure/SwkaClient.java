package de.jaehrig.akalj.infrastructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class SwkaClient {

    private final WebClient webClient;

    @Autowired
    public SwkaClient(final WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://web6.karlsruhe.de/service/abfall/akal/akal.php")
                .build();
    }

    // 'https://web6.karlsruhe.de/service/abfall/akal/akal.php?strasse=Neureuter%20Hauptstrasse&hausnr=1'
    public String getPage(String street, String number) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("")
                        .queryParam("strasse", "{street}")
                        .queryParam("hausnr", "{number}")
                        .build(street, number))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
