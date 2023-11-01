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

    public String getCheckMeterNumber() {
        return checkMeterNumber;
    }

    public BigDecimal getCheckCurrentReading() {
        if (this.checkCurrentReading != null)
            return new BigDecimal(String.valueOf(this.checkCurrentReading));
        return checkCurrentReading;
    }

    public BigDecimal getCheckPreviousReading() {
        if (this.checkPreviousReading != null)
            return new BigDecimal(String.valueOf(this.checkPreviousReading));
        return checkPreviousReading;
    }

    public BigDecimal getCheckReadingDifference() {
        if (this.checkReadingDifference != null)
            return new BigDecimal(String.valueOf(this.checkReadingDifference));
        return checkReadingDifference;
    }

    public BigDecimal getCheckMf() {
        if (this.checkMf != null)
            return new BigDecimal(String.valueOf(this.checkMf));
        return checkMf;
    }

    public BigDecimal getCheckAssessment() {
        if (this.checkAssessment != null)
            return new BigDecimal(String.valueOf(this.checkAssessment));
        return checkAssessment;
    }

    public BigDecimal getCheckConsumption() {
        if (this.checkConsumption != null)
            return new BigDecimal(String.valueOf(this.checkConsumption));
        return checkConsumption;
    }

    public BigDecimal getCheckTotalConsumption() {
        if (this.checkTotalConsumption != null)
            return new BigDecimal(String.valueOf(this.checkTotalConsumption));
        return checkTotalConsumption;
    }
}
