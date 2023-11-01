package in.co.mpwin.rebilling.dto;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import javax.validation.constraints.Digits;
import java.math.BigDecimal;

@Getter
@Setter @ToString
@NoArgsConstructor
@AllArgsConstructor
public class CheckMeterDto {

    //@Digits(integer = 20,fraction = 6)
    private String checkMeterNumber;
    //@Digits(integer = 20,fraction = 6)
    private BigDecimal checkCurrentReading;
    //@Digits(integer = 20,fraction = 6)
    private BigDecimal checkPreviousReading;
    //@Digits(integer = 20,fraction = 6)
    private BigDecimal checkReadingDifference;
    //@Digits(integer = 20,fraction = 6)
    private BigDecimal checkMf;
    //@Digits(integer = 20,fraction = 6)
    private BigDecimal checkAssessment;
    //@Digits(integer = 20,fraction = 6)
    private BigDecimal checkConsumption;
    //@Digits(integer = 20,fraction = 6)
    private BigDecimal checkTotalConsumption;
}
