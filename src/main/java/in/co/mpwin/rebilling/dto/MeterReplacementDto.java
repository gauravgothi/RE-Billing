package in.co.mpwin.rebilling.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Digits;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter @ToString
@AllArgsConstructor
@NoArgsConstructor
public class MeterReplacementDto {
    private String oldMeterNumber;
    private String newMeterNumber;
    private BigDecimal oldMeterFR;
    private BigDecimal oldMeterAssessment;
    private BigDecimal newMeterSR;
    private Date replaceDate;

}
