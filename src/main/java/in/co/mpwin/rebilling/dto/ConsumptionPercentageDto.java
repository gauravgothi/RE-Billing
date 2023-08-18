package in.co.mpwin.rebilling.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private BigDecimal mainCurrentReading;
    private BigDecimal mainPreviousReading;
    private BigDecimal mainReadingDifference;
    private BigDecimal mainMf;
    private BigDecimal mainAssessment;
    private BigDecimal mainConsumption;
    private BigDecimal mainTotalConsumption;

    private String checkMeterNumber;
    private BigDecimal checkCurrentReading;
    private BigDecimal checkPreviousReading;
    private BigDecimal checkReadingDifference;
    private BigDecimal checkMf;
    private BigDecimal checkAssessment;
    private BigDecimal checkConsumption;
    private BigDecimal checkTotalConsumption;

    private BigDecimal percentage;

    private String result;
    private String remark;
    
}
