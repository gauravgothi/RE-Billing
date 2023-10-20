package in.co.mpwin.rebilling.dto;

import jakarta.persistence.Column;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BifurcateInvestorDto {
    @Column(name="linv_code")@NotNull
    private String lInvestorCode;
    @Column(name="linv_name")@NotNull
    private String lInvestorName;

    @Column(name="ppwa_no")@NotNull
    private String ppwaNo;

    @Column(name = "lmach_capacity")
    private BigDecimal lMachineCapacity;
    @Column(name = "lactive_rate")
    private BigDecimal lMachineActiveRate;
    @Column(name = "lreactive_rate")
    private BigDecimal lMachineAReactiveRate;
    @Column(name="ldev_kwhactive_energy")@NotNull
    private BigDecimal lDevConsumptionKwh= BigDecimal.valueOf(0);  //future use
//    @Column(name="inv_fixed_adjustment_per")@NotNull
//    private BigDecimal investorFixedAdjustmentPercent =BigDecimal.valueOf(0);
    @Column(name="lfixed_adjustment_per")@NotNull
    private BigDecimal lFixedAdjustmentPer= BigDecimal.valueOf(0); //fixed adjustment percent for an investor future use
    @Column(name="lkwh_active_energy")@NotNull
    private BigDecimal lConsumptionKwh;
    @Column(name="lrkvah")@NotNull
    private BigDecimal lrkvah= BigDecimal.valueOf(0);
    @Column(name="lassessment")@NotNull
    private BigDecimal lAssessment;
    @Column(name="ladjustment_unit")@NotNull
    private BigDecimal lAdjustmentUnit;
    @Column(name="ladjustment")@NotNull
    private BigDecimal lAdjustment; //adjustment unit * MF
    @Column(name="lconsumption")@NotNull
    private BigDecimal lConsumptionTotal;

    @Override
    public String toString() {
        return "BifurcateInvestorDto{" +
                "lInvestorCode='" + lInvestorCode + '\'' +
                ", lInvestorName='" + lInvestorName + '\'' +
                ", ppwaNo='" + ppwaNo + '\'' +
                ", lMachineCapacity=" + lMachineCapacity +
                ", lMachineActiveRate=" + lMachineActiveRate +
                ", lMachineAReactiveRate=" + lMachineAReactiveRate +
                ", lDevConsumptionKwh=" + lDevConsumptionKwh +
                ", lFixedAdjustmentPer=" + lFixedAdjustmentPer +
                ", lConsumptionKwh=" + lConsumptionKwh +
                ", lrkvah=" + lrkvah +
                ", lAssessment=" + lAssessment +
                ", lAdjustmentUnit=" + lAdjustmentUnit +
                ", lAdjustment=" + lAdjustment +
                ", lConsumptionTotal=" + lConsumptionTotal +
                '}';
    }
}
