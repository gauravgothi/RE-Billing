package in.co.mpwin.rebilling.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
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
