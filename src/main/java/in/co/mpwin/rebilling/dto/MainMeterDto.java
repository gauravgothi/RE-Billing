package in.co.mpwin.rebilling.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import javax.validation.constraints.Digits;
import java.math.BigDecimal;



@Setter @ToString
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

    public String getMainMeterNumber() {
        return mainMeterNumber;
    }

    public BigDecimal getMainCurrentReading() {
        if (this.mainCurrentReading != null)
            return new BigDecimal(String.valueOf(this.mainCurrentReading));
        return mainCurrentReading;
    }

    public BigDecimal getMainPreviousReading() {
        if (this.mainPreviousReading != null)
            return new BigDecimal(String.valueOf(this.mainPreviousReading));
        return mainPreviousReading;
    }

    public BigDecimal getMainReadingDifference() {
        if (this.mainReadingDifference != null)
            return new BigDecimal(String.valueOf(this.mainReadingDifference));
        return mainReadingDifference;
    }

    public BigDecimal getMainMf() {
        if (this.mainMf != null)
            return new BigDecimal(String.valueOf(this.mainMf));
        return mainMf;
    }

    public BigDecimal getMainAssessment() {
        if (this.mainAssessment != null)
            return new BigDecimal(String.valueOf(this.mainAssessment));
        return mainAssessment;
    }

    public BigDecimal getMainConsumption() {
        if (this.mainConsumption != null)
            return new BigDecimal(String.valueOf(this.mainConsumption));
        return mainConsumption;
    }

    public BigDecimal getMainTotalConsumption() {
        if (this.mainTotalConsumption != null)
            return new BigDecimal(String.valueOf(this.mainTotalConsumption));
        return mainTotalConsumption;
    }
}
