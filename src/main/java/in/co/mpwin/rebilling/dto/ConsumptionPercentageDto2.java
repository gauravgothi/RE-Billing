package in.co.mpwin.rebilling.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsumptionPercentageDto2 {
    private String developerId,developerName;
    private String investorId,investorName;
    private String developerSiteAddress;
    private String feederCode,feederName;

    //plant code inserted in extra
    private String plantCode;
    //flag for is main meter ,check meter or both selected
    private String meterSelectedFlag;

    private List<MainMeterDto> mainMeterDtos;
    private List<CheckMeterDto> checkMeterDtos;

    @Digits(integer = 20,fraction = 6)
    private BigDecimal mainGrandTotalConsumption;
    @Digits(integer = 20,fraction = 6)
    private BigDecimal checkGrandTotalConsumption;
    @Digits(integer = 20,fraction = 6)
    private BigDecimal percentage;

    private String monthYear;
    private String result;
    private String remark;
}
