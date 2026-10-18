package ru.arkhipov.MySecondTestAppSpringBoot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.arkhipov.MySecondTestAppSpringBoot.model.Request;

@Slf4j
@Service
public class ModifySourceRequestService implements ModifyRequestService {

    @Override
    public void modify(Request request) {
        log.info("Изменение source: '{}' -> 'Service 1'", request.getSource());
        request.setSource("Service 1");
    }
}
