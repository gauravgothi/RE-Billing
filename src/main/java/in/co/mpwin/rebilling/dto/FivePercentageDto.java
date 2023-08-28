package in.co.mpwin.rebilling.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Digits;

@Getter@Setter
@NoArgsConstructor@AllArgsConstructor
public class FivePercentageDto {
    private String developerId,developerName;
    private String investorId,investorName;
    private String developerSiteAddress;
    private String feederCode,feederName;

    private String mainMeterNumber;
    @Digits(integer = 20,fraction = 6)
    private String mainCurrentReading;
    @Digits(integer = 20,fraction = 6)
    private String mainPreviousReading;
    @Digits(integer = 20,fraction = 6)
    private String mainReadingDifference;
    @Digits(integer = 20,fraction = 6)
    private String mainMf;
    @Digits(integer = 20,fraction = 6)
    private String mainAssessment;
    @Digits(integer = 20,fraction = 6)
    private String mainConsumption;
    @Digits(integer = 20,fraction = 6)
    private String mainTotalConsumption;

    @Digits(integer = 20,fraction = 6)
    private String checkMeterNumber;
    @Digits(integer = 20,fraction = 6)
    private String checkCurrentReading;
    @Digits(integer = 20,fraction = 6)
    private String checkPreviousReading;
    @Digits(integer = 20,fraction = 6)
    private String checkReadingDifference;
    @Digits(integer = 20,fraction = 6)
    private String checkMf;
    @Digits(integer = 20,fraction = 6)
    private String checkAssessment;
    @Digits(integer = 20,fraction = 6)
    private String checkConsumption;
    @Digits(integer = 20,fraction = 6)
    private String checkTotalConsumption;

    @Digits(integer = 20,fraction = 6)
    private String percentage;

    private String result;
    private String remark;
}
