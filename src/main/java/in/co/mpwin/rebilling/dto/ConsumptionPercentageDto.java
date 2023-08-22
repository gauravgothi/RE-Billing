package in.co.mpwin.rebilling.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Digits;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsumptionPercentageDto {
    
    private String developerId,developerName;
    private String investorId,investorName;
    private String developerSiteAddress;
    private String feederCode,feederName;
    
    private String mainMeterNumber;
    @Digits(integer = 20,fraction = 6)
    private BigDecimal mainCurrentReading;
    @Digits(integer = 20,fraction = 6)
    private BigDecimal mainPreviousReading;
    @Digits(integer = 20,fraction = 6)
    private BigDecimal mainReadingDifference;
    @Digits(integer = 20,fraction = 6)
    private BigDecimal mainMf;
    @Digits(integer = 20,fraction = 6)
    private BigDecimal mainAssessment;
    @Digits(integer = 20,fraction = 6)
    private BigDecimal mainConsumption;
    @Digits(integer = 20,fraction = 6)
    private BigDecimal mainTotalConsumption;

    @Digits(integer = 20,fraction = 6)
    private String checkMeterNumber;
    @Digits(integer = 20,fraction = 6)
    private BigDecimal checkCurrentReading;
    @Digits(integer = 20,fraction = 6)
    private BigDecimal checkPreviousReading;
    @Digits(integer = 20,fraction = 6)
    private BigDecimal checkReadingDifference;
    @Digits(integer = 20,fraction = 6)
    private BigDecimal checkMf;
    @Digits(integer = 20,fraction = 6)
    private BigDecimal checkAssessment;
    @Digits(integer = 20,fraction = 6)
    private BigDecimal checkConsumption;
    @Digits(integer = 20,fraction = 6)
    private BigDecimal checkTotalConsumption;

    @Digits(integer = 20,fraction = 6)
    private BigDecimal percentage;

    private String result;
    private String remark;
    
}
