package in.co.mpwin.rebilling.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class MainMeterDto {


    private String mainMeterNumber;
    //@Digits(integer = 20,fraction = 6)
    private BigDecimal mainCurrentReading;
    //@Digits(integer = 20,fraction = 6)
    private BigDecimal mainPreviousReading;
    //@Digits(integer = 20,fraction = 6)
    private BigDecimal mainReadingDifference;
    //@Digits(integer = 20,fraction = 6)
    private BigDecimal mainMf;
    //@Digits(integer = 20,fraction = 6)
    private BigDecimal mainAssessment;
    //@Digits(integer = 20,fraction = 6)
    private BigDecimal mainConsumption;
    //@Digits(integer = 20,fraction = 6)
    private BigDecimal mainTotalConsumption;
}
