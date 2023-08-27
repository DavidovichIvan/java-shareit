package ru.practicum.shareit.client;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Component
public class RestCreator {

    @Value("${shareit-server.url}")
    private String serverUrl;

    private String prefix;

    public RestTemplate createRestTemplate(String prefix) {
        return new RestTemplateBuilder()
                .uriTemplateHandler(new DefaultUriBuilderFactory((serverUrl + prefix)))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }
}
