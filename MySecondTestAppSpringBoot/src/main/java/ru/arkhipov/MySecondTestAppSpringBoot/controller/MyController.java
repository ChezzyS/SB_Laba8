package ru.arkhipov.MySecondTestAppSpringBoot.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.arkhipov.MySecondTestAppSpringBoot.exception.ValidationFailedException;
import ru.arkhipov.MySecondTestAppSpringBoot.model.*;
import ru.arkhipov.MySecondTestAppSpringBoot.service.AnnualBonusService;
import ru.arkhipov.MySecondTestAppSpringBoot.service.ModifyRequestService;
import ru.arkhipov.MySecondTestAppSpringBoot.service.ModifyResponseService;
import ru.arkhipov.MySecondTestAppSpringBoot.service.ServiceTwoClient;
import ru.arkhipov.MySecondTestAppSpringBoot.service.ValidationService;
import ru.arkhipov.MySecondTestAppSpringBoot.util.DateTimeUtil;

import java.util.Date;

@Slf4j
@RestController
public class MyController {
    private final ValidationService validationService;
    private final ModifyResponseService modifyResponseService;
    private final ModifyRequestService modifySystemNameRequestService;
    private final ModifyRequestService modifySourceRequestService;
    private final ServiceTwoClient serviceTwoClient;
    private final AnnualBonusService annualBonusService;

    @Autowired
    public MyController(ValidationService validationService,
                        @Qualifier("ModifySystemTimeResponseService") ModifyResponseService modifyResponseService,
                        @Qualifier("modifySystemNameRequestService") ModifyRequestService modifySystemNameRequestService,
                        @Qualifier("modifySourceRequestService") ModifyRequestService modifySourceRequestService,
                        ServiceTwoClient serviceTwoClient,
                        AnnualBonusService annualBonusService) {
        this.validationService = validationService;
        this.modifyResponseService = modifyResponseService;
        this.modifySystemNameRequestService = modifySystemNameRequestService;
        this.modifySourceRequestService = modifySourceRequestService;
        this.serviceTwoClient = serviceTwoClient;
        this.annualBonusService = annualBonusService;
    }

    @PostMapping("/feedback")
    public ResponseEntity<Response> feedback(@Valid @RequestBody Request request, BindingResult bindingResult) {
        long receivedAt = System.currentTimeMillis();
        log.info("Получен request: {}", request);

        Response response = createResponse(request);
        log.info("Создан response: {}", response);

        try {
            validationService.isValid(bindingResult);
            if ("123".equals(request.getUid())) {
                throw new ValidationFailedException("Указанный uid не поддерживается");
            }

            response.setAnnualBonus(calculateAnnualBonus(request));

            modifySystemNameRequestService.modify(request);
            modifySourceRequestService.modify(request);
            serviceTwoClient.send(request, receivedAt);
        } catch (ValidationFailedException e) {
            bindingResult.getAllErrors().forEach(error -> log.error("Ошибка в bindingResult: {}", error.getDefaultMessage()));
            return failed(response, HttpStatus.BAD_REQUEST, ErrorCodes.VALIDAION_EXCEPTION, ErrorMessages.VALIDATION, e);
        } catch (Exception e) {
            return failed(response, HttpStatus.INTERNAL_SERVER_ERROR, ErrorCodes.UNKNOWN_EXCEPTION, ErrorMessages.UNKNOWN, e);
        }

        modifyResponseService.modify(response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private Response createResponse(Request request) {
        return Response.builder()
                .uid(request.getUid())
                .operationUid(request.getOperationUid())
                .systemTime(DateTimeUtil.getCustomFormat().format(new Date()))
                .code(Codes.SUCCESS)
                .errorCode(ErrorCodes.EMPTY)
                .errorMessage(ErrorMessages.EMPTY)
                .build();
    }

    // Премия считается, только если в request переданы все нужные для расчёта данные
    private Double calculateAnnualBonus(Request request) {
        if (request.getPosition() == null || request.getSalary() == null
                || request.getBonus() == null || request.getWorkDays() == null) {
            log.info("Данных для расчёта годовой премии нет, расчёт пропущен, uid={}", request.getUid());
            return null;
        }
        double annualBonus = annualBonusService.calculate(
                request.getPosition(), request.getSalary(), request.getBonus(), request.getWorkDays());
        log.info("Годовая премия для uid={}: {}", request.getUid(), annualBonus);
        return annualBonus;
    }

    private ResponseEntity<Response> failed(Response response, HttpStatus status,
                                            ErrorCodes errorCode, ErrorMessages errorMessage, Exception e) {
        log.error("{}, uid={}", errorMessage, response.getUid(), e);
        response.setCode(Codes.FAILED);
        response.setErrorCode(errorCode);
        response.setErrorMessage(errorMessage);
        log.info("response изменён после ошибки: {}", response);
        return new ResponseEntity<>(response, status);
    }
}
