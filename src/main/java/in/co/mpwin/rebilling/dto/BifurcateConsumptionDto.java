package in.co.mpwin.rebilling.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BifurcateConsumptionDto {
    @Column(name="hdev_id")@NotNull
    private String hDevId;
    @Column(name="hdev_username")@NotNull
    private String hDevUsername;
    @Column(name="hdev_name")@NotNull
    private String hDevName;
    @Column(name="hdev_plant_code")@NotNull
    private String hDevPlantcode;
    @Column(name="hdev_plant_name")@NotNull
    private String hDevPlantName;
    @Column(name="hcircle_name")@NotNull
    private String hCircleName;
    @Column(name="hmeterno")
    private String hMeterNumber;
    @Column(name="hcategory")
    private String hCategory;
    @Column(name="hmf")
    private BigDecimal hMf;

    @Column(name = "hreading_date")@NotNull
    @JsonFormat(timezone = "IST")
    private Date hReadingDate;
    @Column(name = "hmonth")@NotNull
    private String hmonth;

    @Column(name="hmax_demand")@NotNull
    private BigDecimal hMaxDemand;

    @Column(name="hkwh_active_energy")@NotNull
    private BigDecimal hConsumptionKwh;  //this is kwh export in previous re generator form

    @Column(name="hrkvah")@NotNull
    private BigDecimal hRkvah=BigDecimal.valueOf(0);

    @Column(name="hadjustment")@NotNull
    private BigDecimal hAdjustment;
    @Column(name="hassessment")@NotNull
    private BigDecimal hAssessment;

    @Column(name="hgrand_kwh_active_energy")@NotNull
    private BigDecimal hGrandConsumptionKwh;

    private List<BifurcateInvestorDto> bifurcateInvestorDtoList;

    @Column(name="fsum_kwh_active_energy")@NotNull
    private BigDecimal fSumConsumptionKwh;
    @Column(name="fsum_assessment")@NotNull
    private BigDecimal fSumAssessment;
    @Column(name="fsum_fixed_adjustment_val")@NotNull
    private BigDecimal fSumFixedAdjustmentValue;
    @Column(name="fsum_adjustment")@NotNull
    private BigDecimal fSumAdjustment; //Adjustment Unit Total on line

    @Column(name="fgrand_kwh_active_energy")@NotNull
    private BigDecimal fGrandConsumptionKwh;
    @Column(name="funallocated_kwh_active_energy")@NotNull
    private BigDecimal fUnallocatedConsumptionKwh;

    @Override
    public String toString() {
        return "BifurcateConsumptionDto{" +
                "hDevId='" + hDevId + '\'' +
                ", hDevUsername='" + hDevUsername + '\'' +
                ", hDevName='" + hDevName + '\'' +
                ", hDevPlantcode='" + hDevPlantcode + '\'' +
                ", hDevPlantName='" + hDevPlantName + '\'' +
                ", hCircleName='" + hCircleName + '\'' +
                ", hMeterNumber='" + hMeterNumber + '\'' +
                ", hCategory='" + hCategory + '\'' +
                ", hMf=" + hMf +
                ", hReadingDate=" + hReadingDate +
                ", hmonth='" + hmonth + '\'' +
                ", hMaxDemand=" + hMaxDemand +
                ", hConsumptionKwh=" + hConsumptionKwh +
                ", hRkvah=" + hRkvah +
                ", hAdjustment=" + hAdjustment +
                ", hAssessment=" + hAssessment +
                ", hGrandConsumptionKwh=" + hGrandConsumptionKwh +
                ", bifurcateInvestorDtoList=" + bifurcateInvestorDtoList +
                ", fSumConsumptionKwh=" + fSumConsumptionKwh +
                ", fSumAssessment=" + fSumAssessment +
                ", fSumFixedAdjustmentValue=" + fSumFixedAdjustmentValue +
                ", fSumAdjustment=" + fSumAdjustment +
                ", fGrandConsumptionKwh=" + fGrandConsumptionKwh +
                ", fUnallocatedConsumptionKwh=" + fUnallocatedConsumptionKwh +
                '}';
    }
}
