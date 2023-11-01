package in.co.mpwin.rebilling.dto;

import lombok.*;

import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.util.List;


@Setter @ToString
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

    //@Digits(integer = 20,fraction = 6)
    private BigDecimal mainGrandTotalConsumption;
    //@Digits(integer = 20,fraction = 6)
    private BigDecimal checkGrandTotalConsumption;
    //@Digits(integer = 20,fraction = 6)
    private BigDecimal percentage;

    private String monthYear;
    private String result;
    private String remark;

    public String getDeveloperId() {
        return developerId;
    }

    public String getDeveloperName() {
        return developerName;
    }

    public String getInvestorId() {
        return investorId;
    }

    public String getInvestorName() {
        return investorName;
    }

    public String getDeveloperSiteAddress() {
        return developerSiteAddress;
    }

    public String getFeederCode() {
        return feederCode;
    }

    public String getFeederName() {
        return feederName;
    }

    public String getPlantCode() {
        return plantCode;
    }

    public String getMeterSelectedFlag() {
        return meterSelectedFlag;
    }

    public List<MainMeterDto> getMainMeterDtos() {
        return mainMeterDtos;
    }

    public List<CheckMeterDto> getCheckMeterDtos() {
        return checkMeterDtos;
    }

    public BigDecimal getMainGrandTotalConsumption() {
        if (this.mainGrandTotalConsumption != null)
            return new BigDecimal(String.valueOf(this.mainGrandTotalConsumption));
        return mainGrandTotalConsumption;
    }

    public BigDecimal getCheckGrandTotalConsumption() {
        if (this.checkGrandTotalConsumption != null)
            return new BigDecimal(String.valueOf(this.checkGrandTotalConsumption));
        return checkGrandTotalConsumption;
    }

    public BigDecimal getPercentage() {
        if (this.percentage != null)
            return new BigDecimal(String.valueOf(this.percentage));
        return percentage;
    }

    public String getMonthYear() {
        return monthYear;
    }

    public String getResult() {
        return result;
    }

    public String getRemark() {
        return remark;
    }
}
