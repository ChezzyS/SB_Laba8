package ru.arkhipov.MySecondTestAppSpringBoot.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Request {


    @NotBlank(message = "UID не может быть пустым")
    @Size(max = 32)
    private String uid;


    @NotBlank
    @Size(max = 32)
    private String operationUid;


    private Systems systemName;


    @NotBlank
    private String systemTime;


    private String source;


    private Positions position;



    @PositiveOrZero
    private Double salary;


    @PositiveOrZero
    private Double bonus;


    @Positive
    private Integer workDays;


    @NotNull
    @Min(1)
    @Max(100000)
    private Integer communicationId;


    private Integer templateId;


    private Integer productCode;


    private Integer smsCode;

    @Override
    public String toString() {
        return "{" +
                "uid='" + uid + '\'' +
                ", operationUid='" + operationUid + '\'' +
                ", systemName='" + systemName + '\'' +
                ", systemTime='" + systemTime + '\'' +
                ", source='" + source + '\'' +
                ", position='" + position + '\'' +
                ", salary='" + salary + '\'' +
                ", bonus='" + bonus + '\'' +
                ", workDays='" + workDays + '\'' +
                ", communicationId='" + communicationId + '\'' +
                ", templateId='" + templateId + '\'' +
                ", productCode='" + productCode + '\'' +
                ", smsCode='" + smsCode + '\'' +
                '}';
    }
}
