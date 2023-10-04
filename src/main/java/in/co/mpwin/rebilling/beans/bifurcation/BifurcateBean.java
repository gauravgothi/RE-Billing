package in.co.mpwin.rebilling.beans.bifurcation;

import com.fasterxml.jackson.annotation.JsonFormat;
import in.co.mpwin.rebilling.interfaces.BeanInterface;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "BifurcateBean")
@Table(name = "re_bifurcated_reading",uniqueConstraints = @UniqueConstraint(name = "re_bifurcated_reading_ukey", columnNames={"hmeterno","hmonth","linv_code","status"}))
public class BifurcateBean implements BeanInterface {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    private BigDecimal hConsumptionKwh;

    @Column(name="hrkvah")@NotNull
    private BigDecimal hRkvah=BigDecimal.valueOf(0);

    @Column(name="hadjustment")@NotNull
    private BigDecimal hAdjustment;
    @Column(name="hassessment")@NotNull
    private BigDecimal hAssessment;

    @Column(name="hgrand_kwh_active_energy")@NotNull
    private BigDecimal hGrandConsumptionKwh;

    //Line items variable
    @Column(name="linv_code")@NotNull
    private String lInvestorCode;
    @Column(name="linv_name")@NotNull
    private String lInvestorName;

    @Column(name = "lmach_capacity")
    private BigDecimal lMachineCapacity;
    @Column(name = "lactive_rate")
    private BigDecimal lMachineActiveRate;
    @Column(name = "lreactive_rate")
    private BigDecimal lMachineAReactiveRate;
    @Column(name="ldev_kwhactive_energy")@NotNull
    private BigDecimal lDevConsumptionKwh= BigDecimal.valueOf(0);
//    @Column(name="inv_fixed_adjustment_per")@NotNull
//    private BigDecimal investorFixedAdjustmentPercent;
    @Column(name="lfixed_adjustment_per")@NotNull
    private BigDecimal lFixedAdjustmentPer= BigDecimal.valueOf(0);
    @Column(name="lkwh_active_energy")@NotNull
    private BigDecimal lConsumptionKwh;
    @Column(name="lassessment")@NotNull
    private BigDecimal lAssessment;
    @Column(name="lrkvah")@NotNull
    private BigDecimal lrkvah= BigDecimal.valueOf(0);
    @Column(name="ladjustment_unit")@NotNull
    private BigDecimal lAdjustmentUnit;
    @Column(name="ladjustment")@NotNull
    private BigDecimal lAdjustment; //adjustment unit * MF
    @Column(name="lconsumption")@NotNull
    private BigDecimal lConsumptionTotal;
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

    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "updated_by")
    private String updatedBy;
    @Column(name = "updated_on")
    private Timestamp updatedOn;
    @Column(name = "created_on")
    private Timestamp createdOn;
    @Column(name = "status")
    private String status;
    @Column(name = "remark")
    private String remark;

    public BifurcateBean(String lInvestorCode, String lInvestorName) {
        this.lInvestorCode = lInvestorCode;
        this.lInvestorName = lInvestorName;
    }

    public Long getId() {
        return id;
    }

    public String gethDevId() {
        return hDevId;
    }

    public String gethDevUsername() {
        return hDevUsername;
    }

    public String gethDevName() {
        return hDevName;
    }

    public String gethDevPlantcode() {
        return hDevPlantcode;
    }

    public String gethDevPlantName() {
        return hDevPlantName;
    }

    public String gethMeterNumber() {
        return hMeterNumber;
    }

    public String gethCategory() {
        return hCategory;
    }

    public BigDecimal gethMf() {
        if (this.hMf != null)
            return new BigDecimal(String.valueOf(this.hMf));
        return hMf;
    }



    public Date gethReadingDate() {
        return hReadingDate;
    }

    public String getHmonth() {
        return hmonth;
    }

    public BigDecimal gethMaxDemand() {
        if (this.hMaxDemand != null)
            return new BigDecimal(String.valueOf(this.hMaxDemand));
        return hMf;
    }

    public BigDecimal gethConsumptionKwh() {
        if (this.hConsumptionKwh != null)
            return new BigDecimal(String.valueOf(this.hConsumptionKwh));
        return hConsumptionKwh;
    }

    public BigDecimal gethAdjustment() {
        if (this.hAdjustment != null)
            return new BigDecimal(String.valueOf(this.hAdjustment));
        return hAdjustment;
    }

    public BigDecimal gethAssessment() {
        if (this.hAssessment != null)
            return new BigDecimal(String.valueOf(this.hAssessment));
        return hAssessment;
    }

    public BigDecimal gethGrandConsumptionKwh() {
        if (this.hGrandConsumptionKwh != null)
            return new BigDecimal(String.valueOf(this.hGrandConsumptionKwh));
        return hGrandConsumptionKwh;
    }

    public String getlInvestorCode() {
        return lInvestorCode;
    }

    public String getlInvestorName() {
        return lInvestorName;
    }

    public BigDecimal getlMachineCapacity() {
        if (this.lMachineCapacity != null)
            return new BigDecimal(String.valueOf(this.lMachineCapacity));
        return lMachineCapacity;
    }

    public BigDecimal getlMachineActiveRate() {
        if (this.lMachineActiveRate != null)
            return new BigDecimal(String.valueOf(this.lMachineActiveRate));
        return lMachineActiveRate;
    }

    public BigDecimal getlMachineAReactiveRate() {
        if (this.lMachineAReactiveRate != null)
            return new BigDecimal(String.valueOf(this.lMachineAReactiveRate));
        return lMachineAReactiveRate;
    }

    public BigDecimal getlDevConsumptionKwh() {
        if (this.lDevConsumptionKwh != null)
            return new BigDecimal(String.valueOf(this.lDevConsumptionKwh));
        return lDevConsumptionKwh;
    }

//    public BigDecimal getInvestorFixedAdjustmentPercent() {
//        if (this.investorFixedAdjustmentPercent != null)
//            return new BigDecimal(String.valueOf(this.investorFixedAdjustmentPercent));
//        return investorFixedAdjustmentPercent;
//    }

    public BigDecimal getlFixedAdjustmentPer() {
        if (this.lFixedAdjustmentPer != null)
            return new BigDecimal(String.valueOf(this.lFixedAdjustmentPer));
        return lFixedAdjustmentPer;
    }

    public BigDecimal getlConsumptionKwh() {
        if (this.lConsumptionKwh != null)
            return new BigDecimal(String.valueOf(this.lConsumptionKwh));
        return lConsumptionKwh;
    }

    public BigDecimal getlAssessment() {
        if (this.lAssessment != null)
            return new BigDecimal(String.valueOf(this.lAssessment));
        return lAssessment;
    }

    public BigDecimal getLrkvah() {
        if (this.lrkvah != null)
            return new BigDecimal(String.valueOf(this.lrkvah));
        return lrkvah;
    }

    public BigDecimal getlAdjustmentUnit() {
        if (this.lAdjustmentUnit != null)
            return new BigDecimal(String.valueOf(this.lAdjustmentUnit));
        return lAdjustmentUnit;
    }

    public BigDecimal getlAdjustment() {
        if (this.lAdjustment != null)
            return new BigDecimal(String.valueOf(this.lAdjustment));
        return lAdjustment;
    }

    public BigDecimal getlConsumptionTotal() {
        if (this.lConsumptionTotal != null)
            return new BigDecimal(String.valueOf(this.lConsumptionTotal));
        return lConsumptionTotal;
    }

    public BigDecimal getfSumConsumptionKwh() {
        if (this.fSumConsumptionKwh != null)
            return new BigDecimal(String.valueOf(this.fSumConsumptionKwh));
        return fSumConsumptionKwh;
    }

    public BigDecimal getfSumAssessment() {
        if (this.fSumAssessment != null)
            return new BigDecimal(String.valueOf(this.fSumAssessment));
        return fSumAssessment;
    }

    public BigDecimal getfSumFixedAdjustmentValue() {
        if (this.fSumFixedAdjustmentValue != null)
            return new BigDecimal(String.valueOf(this.fSumFixedAdjustmentValue));
        return fSumFixedAdjustmentValue;
    }

    public BigDecimal getfSumAdjustment() {
        if (this.fSumAdjustment != null)
            return new BigDecimal(String.valueOf(this.fSumAdjustment));
        return fSumAdjustment;
    }

    public BigDecimal getfGrandConsumptionKwh() {
        if (this.fGrandConsumptionKwh != null)
            return new BigDecimal(String.valueOf(this.fGrandConsumptionKwh));
        return fGrandConsumptionKwh;
    }

    public BigDecimal getfUnallocatedConsumptionKwh() {
        if (this.fUnallocatedConsumptionKwh != null)
            return new BigDecimal(String.valueOf(this.fUnallocatedConsumptionKwh));
        return fUnallocatedConsumptionKwh;
    }
}
