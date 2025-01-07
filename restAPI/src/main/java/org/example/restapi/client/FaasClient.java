package org.example.restapi.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class FaasClient {
    private final RestTemplate restTemplate;
    private final String faasUrl;

    public FaasClient(RestTemplate restTemplate,
                      @Value("${faas-url}") String faasUrl) {
        this.restTemplate = restTemplate;
        this.faasUrl = faasUrl;
    }

    public String convertMarkdownToHtml(String markdown) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        HttpEntity<String> entity = new HttpEntity<>(markdown, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(faasUrl, HttpMethod.GET, entity, String.class);
        return responseEntity.getBody();
    }
}
