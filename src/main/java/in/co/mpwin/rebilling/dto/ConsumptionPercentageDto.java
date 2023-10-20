package in.co.mpwin.rebilling.dto;

import lombok.*;

import javax.validation.constraints.Digits;
import java.math.BigDecimal;

@Getter
@Setter @ToString
@NoArgsConstructor
@AllArgsConstructor
public class ConsumptionPercentageDto {
    
    private String developerId,developerName;
    private String investorId,investorName;
    private String developerSiteAddress;
    private String feederCode,feederName;
    
    private String mainMeterNumber;
    //@Digits(integer = 14,fraction = 6)
    private BigDecimal mainCurrentReading;
    //@Digits(integer = 14,fraction = 6)
    private BigDecimal mainPreviousReading;
    //@Digits(integer = 14,fraction = 6)
    private BigDecimal mainReadingDifference;
    //@Digits(integer = 14,fraction = 6)
    private BigDecimal mainMf;
    //@Digits(integer = 14,fraction = 6)
    private BigDecimal mainAssessment;
    //@Digits(integer = 14,fraction = 6)
    private BigDecimal mainConsumption;
    //@Digits(integer = 14,fraction = 6)
    private BigDecimal mainTotalConsumption;

    //@Digits(integer = 14,fraction = 6)
    private String checkMeterNumber;
    //@Digits(integer = 14,fraction = 6)
    private BigDecimal checkCurrentReading;
    //@Digits(integer = 14,fraction = 6)
    private BigDecimal checkPreviousReading;
    //@Digits(integer = 14,fraction = 6)
    private BigDecimal checkReadingDifference;
    //@Digits(integer = 14,fraction = 6)
    private BigDecimal checkMf;
    //@Digits(integer = 14,fraction = 6)
    private BigDecimal checkAssessment;
    //@Digits(integer = 14,fraction = 6)
    private BigDecimal checkConsumption;
    //@Digits(integer = 14,fraction = 6)
    private BigDecimal checkTotalConsumption;

    //@Digits(integer = 14,fraction = 6)
    private BigDecimal percentage;

    private String result;
    private String remark;
    
}
