package ru.arkhipov.MySecondTestAppSpringBoot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.arkhipov.MySecondTestAppSpringBoot.model.Request;
import ru.arkhipov.MySecondTestAppSpringBoot.model.Systems;

// Теперь только меняет systemName. Отправка на 8084 вынесена в ServiceTwoClient
@Slf4j
@Service
public class ModifySystemNameRequestService implements ModifyRequestService {

    @Override
    public void modify(Request request) {
        log.info("Изменение systemName: '{}' -> '{}'", request.getSystemName(), Systems.ERP);
        request.setSystemName(Systems.ERP);
    }
}
