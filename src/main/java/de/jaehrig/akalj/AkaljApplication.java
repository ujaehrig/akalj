package de.jaehrig.akalj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class AkaljApplication {

    public static void main(String[] args) {
        SpringApplication.run(AkaljApplication.class, args);
    }

}
