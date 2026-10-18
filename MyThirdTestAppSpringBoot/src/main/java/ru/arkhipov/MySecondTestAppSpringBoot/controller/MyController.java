package ru.arkhipov.MySecondTestAppSpringBoot.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.arkhipov.MySecondTestAppSpringBoot.exception.ValidationFailedException;
import ru.arkhipov.MySecondTestAppSpringBoot.model.*;
import ru.arkhipov.MySecondTestAppSpringBoot.service.ValidationService;
import ru.arkhipov.MySecondTestAppSpringBoot.util.DateTimeUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@RestController
public class MyController {
    private final ValidationService validationService;

    @Autowired
    public MyController(ValidationService validationService){
        this.validationService=validationService;
    }

    @PostMapping(value = "/feedback")
    public ResponseEntity<Response> feedback(@Valid @RequestBody Request request,
                                             BindingResult bindingResult,
                                             @RequestHeader(value = "X-Service1-Received-At", required = false) Long service1ReceivedAt) {

        long service2ReceivedAt = System.currentTimeMillis();
        if (service1ReceivedAt != null) {
            log.info("Время от получения Request Сервисом 1 до получения Сервисом 2: {} мс",
                    service2ReceivedAt - service1ReceivedAt);
        }
        log.info("Получен request: {}", request);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));

        Response response = Response.builder()
                .uid(request.getUid())
                .operationUid(request.getOperationUid())
                .systemTime(DateTimeUtil.getCustomFormat().format(new Date()))
                .code(Codes.SUCCESS)
                .errorCode(ErrorCodes.EMPTY)
                .errorMessage(ErrorMessages.EMPTY)
                .build();

        log.info("Создан response: {}", response);

        try {
            validationService.isValid(bindingResult);

            if ("123".equals(request.getUid())) {
                throw new ValidationFailedException("Указанный uid не поддерживается");
            }
        } catch (ValidationFailedException e) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                log.error("Ошибка в bindingResult: {}", error.getDefaultMessage());
            }
            log.error("Ошибка валидации, uid={}", request.getUid(), e);
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.VALIDAION_EXCEPTION);
            response.setErrorMessage(ErrorMessages.VALIDATION);
            log.info("response изменён после ValidationService: {}", response);
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            log.error("Непредвиденная ошибка, uid={}", request.getUid(), e);
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.UNKNOWN_EXCEPTION);
            response.setErrorMessage(ErrorMessages.UNKNOWN);
            log.info("response изменён после непредвиденной ошибки: {}", response);
            return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

}
