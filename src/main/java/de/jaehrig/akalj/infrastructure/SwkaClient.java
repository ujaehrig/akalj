package de.jaehrig.akalj.infrastructure;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.resolver.DefaultAddressResolverGroup;
import java.util.Objects;
import javax.net.ssl.SSLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Component
public class SwkaClient {

    private final WebClient webClient;

    @Autowired
    public SwkaClient(final WebClient.Builder webClientBuilder) throws SSLException {

        SslContext sslContext = SslContextBuilder.forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();

        this.webClient = webClientBuilder
                .baseUrl("https://web4.karlsruhe.de/service/abfall/akal/akal_2024.php")
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create()
                        .secure(t -> t.sslContext(sslContext))
                        .resolver(DefaultAddressResolverGroup.INSTANCE)))
                .build();
    }

    // 'https://web6.karlsruhe.de/service/abfall/akal/akal.php?strasse=Neureuter%20Hauptstrasse&hausnr=1'
    public String getPage(String street, String number) {
        return Objects.requireNonNullElse(
                webClient
                        .get()
                        .uri(uriBuilder -> uriBuilder
                                .path("")
                                .queryParam("strasse", "{street}")
                                .queryParam("hausnr", "{number}")
                                .build(street, number))
                        .retrieve()
                        .bodyToMono(String.class)
                        .block(),
                "");
    }
}
