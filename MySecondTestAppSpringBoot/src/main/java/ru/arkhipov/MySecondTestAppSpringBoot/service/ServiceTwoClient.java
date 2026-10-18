package ru.arkhipov.MySecondTestAppSpringBoot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.arkhipov.MySecondTestAppSpringBoot.model.Request;
import ru.arkhipov.MySecondTestAppSpringBoot.model.Response;

@Slf4j
@Service
public class ServiceTwoClient {

    private static final String SERVICE_TWO_URL = "http://localhost:8084/feedback";
    // Время (мс с 1970 г.), когда Сервис 1 получил исходный Request
    public static final String RECEIVED_AT_HEADER = "X-Service1-Received-At";

    private final RestTemplate restTemplate = new RestTemplate();

    public void send(Request request, long service1ReceivedAt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(RECEIVED_AT_HEADER, String.valueOf(service1ReceivedAt));

        HttpEntity<Request> httpEntity = new HttpEntity<>(request, headers);

        log.info("Отправка изменённого request в Сервис 2: {}", request);
        try {
            restTemplate.exchange(SERVICE_TWO_URL, HttpMethod.POST, httpEntity, Response.class);
            log.info("Сервис 2 принял request, uid={}", request.getUid());
        } catch (HttpClientErrorException e) {
            log.error("Сервис 2 ответил {}: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw e;
        }
    }
}
