package in.co.mpwin.rebilling.beans.statement;

import com.fasterxml.jackson.annotation.JsonFormat;
import in.co.mpwin.rebilling.interfaces.BeanInterface;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

@Setter @ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "SolarStatementBean")
@Table(name = "re_solar_statement")
//@Table(name = "re_solar_statement",uniqueConstraints = { @UniqueConstraint(name = "re_solar_statement_ukey", columnNames={"consumer_code"})})
public class SolarStatementBean implements BeanInterface {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="meter_number")
    private String meterNumber;

    @Column(name="meter_install_date")
    private String meterInstallDate;
    @Column(name="meter_mf")
    private BigDecimal meterMf;
    @Column(name="category")
    private String category;
    @Column(name = "month_year")
    private String monthYear;
    @Column(name="feeder_number")
    private String feederNumber;
    @Column(name="feeder_name")
    private String feederName;
    @Column(name="feeder_circuit_voltage")
    private String feederCircuitVoltage;
    @Column(name="feeder_injecting_ss")
    private String feederInjectingSs;

    @Column(name="plant_code")
    private String plantCode;
    @Column(name="plant_name")
    private String plantName;
    @Column(name="plant_site_location")
    private String plantSiteLocation;
    @Column(name="plant_commissioned_date")
    private Date plantCommisionedDate;

    @Column(name="investor_code")
    private String investorCode;
    @Column(name="investor_name")
    private String investorName;
    @Column(name="investor_nldc_number")
    private String investorNldcNumber;
    @Column(name="investor_project_capacity")
    private String investorProjectCapacity;

    @Column(name = "previous_reading_date")@NotNull@JsonFormat(timezone = "IST")
    private Date previousReadingDate;
    @Column(name = "current_reading_date")@NotNull@JsonFormat(timezone = "IST")
    private Date currentReadingDate;


    @Column(name="e_previous_active_energy")@NotNull 
    private BigDecimal ePreviousActiveEnergy;
    @Column(name="e_current_active_energy")@NotNull
    private BigDecimal eCurrentActiveEnergy;
    @Column(name="e_diff_active_energy")@NotNull
    private BigDecimal eDiffActiveEnergy;

    @Column(name="e_adjustment_active_energy")@NotNull
    private BigDecimal eAdjustmentActiveEnergy;
    @Column(name="e_consumption_active_energy")@NotNull
    private BigDecimal eConsumptionActiveEnergy;
    
    @Column(name="i_current_max_demand")@NotNull
    private BigDecimal iCurrentMaxDemand;

    @Column(name="i_previous_active_energy")@NotNull
    private BigDecimal iPreviousActiveEnergy;
    @Column(name="i_current_active_energy")@NotNull
    private BigDecimal iCurrentActiveEnergy;
    @Column(name="i_diff_active_energy")@NotNull
    private BigDecimal iDiffActiveEnergy;
    @Column(name="i_consumption_active_energy")@NotNull
    private BigDecimal iConsumptionActiveEnergy;

    @OneToMany(mappedBy = "solarStatementBean",cascade = CascadeType.ALL)
    //@JoinColumn(name = "solar_tp_fid", referencedColumnName = "id")
    private Set<ThirdPartyTod> thirdPartyTodSet = new HashSet<>();

    public Set<ThirdPartyTod> getThirdPartyTodSet() {
        return thirdPartyTodSet;
    }

    //these field is total of third party of an investor
    @Column(name="total_tod1")
    private BigDecimal totalTod1;
    @Column(name="total_tod2")
    private BigDecimal totalTod2;
    @Column(name="total_tod3")
    private BigDecimal totalTod3;
    @Column(name="total_tod4")
    private BigDecimal totalTod4;
    @Column(name="total_kwh_export")
    private BigDecimal totalKwhExport;
    @Column(name="total_adjustment")
    private BigDecimal totalAdjustment;



    @Column(name="i_previous_reactive_quad1")@NotNull
    private BigDecimal iPreviousReactiveQuad1;
    @Column(name="i_previous_reactive_quad2")@NotNull
    private BigDecimal iPreviousReactiveQuad2;
    @Column(name="i_previous_reactive_quad3")@NotNull
    private BigDecimal iPreviousReactiveQuad3;
    @Column(name="i_previous_reactive_quad4")@NotNull
    private BigDecimal iPreviousReactiveQuad4;

    @Column(name="i_current_reactive_quad1")@NotNull
    private BigDecimal iCurrentReactiveQuad1;
    @Column(name="i_current_reactive_quad2")@NotNull
    private BigDecimal iCurrentReactiveQuad2;
    @Column(name="i_current_reactive_quad3")@NotNull
    private BigDecimal iCurrentReactiveQuad3;
    @Column(name="i_current_reactive_quad4")@NotNull
    private BigDecimal iCurrentReactiveQuad4;

    @Column(name="i_diff_reactive_quad1")@NotNull
    private BigDecimal iDiffReactiveQuad1;
    @Column(name="i_diff_reactive_quad2")@NotNull
    private BigDecimal iDiffReactiveQuad2;
    @Column(name="i_diff_reactive_quad3")@NotNull
    private BigDecimal iDiffReactiveQuad3;
    @Column(name="i_diff_reactive_quad4")@NotNull
    private BigDecimal iDiffReactiveQuad4;

    @Column(name="i_consumption_reactive_quad1")@NotNull
    private BigDecimal iConsumptionReactiveQuad1;
    @Column(name="i_consumption_reactive_quad2")@NotNull
    private BigDecimal iConsumptionReactiveQuad2;
    @Column(name="i_consumption_reactive_quad3")@NotNull
    private BigDecimal iConsumptionReactiveQuad3;
    @Column(name="i_consumption_reactive_quad4")@NotNull
    private BigDecimal iConsumptionReactiveQuad4;

    @Column(name="i_consumption_reactive_quad_total")@NotNull
    private BigDecimal iConsumptionReactiveQuadTotal;

    @Column(name="i_previous_kvah")@NotNull
    private BigDecimal iPreviousKvah;
    @Column(name="i_current_kvah")@NotNull
    private BigDecimal iCurrentKvah;
    @Column(name="i_diff_kvah")@NotNull
    private BigDecimal iDiffKvah;
    @Column(name="i_consumption_kvah")@NotNull
    private BigDecimal iConsumptionKvah;

    @Column(name="e_previous_kvah")@NotNull 
    private BigDecimal ePreviousKvah;
    @Column(name="e_current_kvah")@NotNull
    private BigDecimal eCurrentKvah;
    @Column(name="e_diff_kvah")@NotNull
    private BigDecimal eDiffKvah;
    @Column(name="e_consumption_kvah")@NotNull
    private BigDecimal eConsumptionKvah;

    @Column(name="wheeling_charge_percent")@NotNull
    private BigDecimal wheelingChargePercent;

    @Column(name="generated_by")
    private String generatedBy;

    @Column(name = "generated_on")
    private Timestamp generatedOn;

    @Column(name = "status")
    private String status;

    public long getId() {
        return id;
    }

    public String getMeterNumber() {
        return meterNumber;
    }

    public String getMeterInstallDate() {
        return meterInstallDate;
    }

    public BigDecimal getMeterMf() {
        if (this.meterMf != null)
            return new BigDecimal(String.valueOf(this.meterMf));
        return meterMf;
    }

    public String getCategory() {
        return category;
    }

    public String getMonthYear() {
        return monthYear;
    }

    public String getFeederNumber() {
        return feederNumber;
    }

    public String getFeederName() {
        return feederName;
    }

    public String getFeederCircuitVoltage() {
        return feederCircuitVoltage;
    }

    public String getFeederInjectingSs() {
        return feederInjectingSs;
    }

    public String getPlantCode() {
        return plantCode;
    }

    public String getPlantName() {
        return plantName;
    }

    public String getPlantSiteLocation() {
        return plantSiteLocation;
    }

    public String getInvestorProjectCapacity() {
        return investorProjectCapacity;
    }

    public Date getPlantCommisionedDate() {
        return plantCommisionedDate;
    }

    public String getInvestorCode() {
        return investorCode;
    }

    public String getInvestorName() {
        return investorName;
    }

    public String getInvestorNldcNumber() {
        return investorNldcNumber;
    }

    public Date getPreviousReadingDate() {
        return previousReadingDate;
    }

    public Date getCurrentReadingDate() {
        return currentReadingDate;
    }

    public BigDecimal getePreviousActiveEnergy() {
        if (this.ePreviousActiveEnergy != null)
            return new BigDecimal(String.valueOf(this.ePreviousActiveEnergy));
        return ePreviousActiveEnergy;
    }

    public BigDecimal geteCurrentActiveEnergy() {
        if (this.eCurrentActiveEnergy != null)
            return new BigDecimal(String.valueOf(this.eCurrentActiveEnergy));
        return eCurrentActiveEnergy;
    }

    public BigDecimal geteDiffActiveEnergy() {
        if (this.eDiffActiveEnergy != null)
            return new BigDecimal(String.valueOf(this.eDiffActiveEnergy));
        return eDiffActiveEnergy;
    }

    public BigDecimal geteConsumptionActiveEnergy() {
        if (this.eConsumptionActiveEnergy != null)
            return new BigDecimal(String.valueOf(this.eConsumptionActiveEnergy));
        return eConsumptionActiveEnergy;
    }

    public BigDecimal geteAdjustmentActiveEnergy() {
        if (this.eAdjustmentActiveEnergy != null)
            return new BigDecimal(String.valueOf(this.eAdjustmentActiveEnergy));
        return eAdjustmentActiveEnergy;
    }

    public BigDecimal getiCurrentMaxDemand() {
        if (this.iCurrentMaxDemand != null)
            return new BigDecimal(String.valueOf(this.iCurrentMaxDemand));
        return iCurrentMaxDemand;
    }

    public BigDecimal getiPreviousActiveEnergy() {
        if (this.iPreviousActiveEnergy != null)
            return new BigDecimal(String.valueOf(this.iPreviousActiveEnergy));
        return iPreviousActiveEnergy;
    }

    public BigDecimal getiCurrentActiveEnergy() {
        if (this.iCurrentActiveEnergy != null)
            return new BigDecimal(String.valueOf(this.iCurrentActiveEnergy));
        return iCurrentActiveEnergy;
    }

    public BigDecimal getiDiffActiveEnergy() {
        if (this.iDiffActiveEnergy != null)
            return new BigDecimal(String.valueOf(this.iDiffActiveEnergy));
        return iDiffActiveEnergy;
    }

    public BigDecimal getiConsumptionActiveEnergy() {
        if (this.iConsumptionActiveEnergy != null)
            return new BigDecimal(String.valueOf(this.iConsumptionActiveEnergy));
        return iConsumptionActiveEnergy;
    }


    public BigDecimal getTotalTod1() {
        if (this.totalTod1 != null)
            return new BigDecimal(String.valueOf(this.totalTod1));
        return totalTod1;
    }

    public BigDecimal getTotalTod2() {
        if (this.totalTod2 != null)
            return new BigDecimal(String.valueOf(this.totalTod2));
        return totalTod2;
    }

    public BigDecimal getTotalTod3() {
        if (this.totalTod3 != null)
            return new BigDecimal(String.valueOf(this.totalTod3));
        return totalTod3;
    }

    public BigDecimal getTotalTod4() {
        if (this.totalTod4 != null)
            return new BigDecimal(String.valueOf(this.totalTod4));
        return totalTod4;
    }

    public BigDecimal getTotalKwhExport() {
        if (this.totalKwhExport != null)
            return new BigDecimal(String.valueOf(this.totalKwhExport));
        return totalKwhExport;
    }

    public BigDecimal getTotalAdjustment() {
        if (this.totalAdjustment != null)
            return new BigDecimal(String.valueOf(this.totalAdjustment));
        return totalAdjustment;
    }

    public BigDecimal getiPreviousReactiveQuad1() {
        if (this.iPreviousReactiveQuad1 != null)
            return new BigDecimal(String.valueOf(this.iPreviousReactiveQuad1));
        return iPreviousReactiveQuad1;
    }

    public BigDecimal getiPreviousReactiveQuad2() {
        if (this.iPreviousReactiveQuad2 != null)
            return new BigDecimal(String.valueOf(this.iPreviousReactiveQuad2));
        return iPreviousReactiveQuad2;
    }

    public BigDecimal getiPreviousReactiveQuad3() {
        if (this.iPreviousReactiveQuad3 != null)
            return new BigDecimal(String.valueOf(this.iPreviousReactiveQuad3));
        return iPreviousReactiveQuad3;
    }

    public BigDecimal getiPreviousReactiveQuad4() {
        if (this.iPreviousReactiveQuad4 != null)
            return new BigDecimal(String.valueOf(this.iPreviousReactiveQuad4));
        return iPreviousReactiveQuad4;
    }

    public BigDecimal getiCurrentReactiveQuad1() {
        if (this.iCurrentReactiveQuad1 != null)
            return new BigDecimal(String.valueOf(this.iCurrentReactiveQuad1));
        return iCurrentReactiveQuad1;
    }

    public BigDecimal getiCurrentReactiveQuad2() {
        if (this.iCurrentReactiveQuad2 != null)
            return new BigDecimal(String.valueOf(this.iCurrentReactiveQuad2));
        return iCurrentReactiveQuad2;
    }

    public BigDecimal getiCurrentReactiveQuad3() {
        if (this.iCurrentReactiveQuad3 != null)
            return new BigDecimal(String.valueOf(this.iCurrentReactiveQuad3));
        return iCurrentReactiveQuad3;
    }

    public BigDecimal getiCurrentReactiveQuad4() {
        if (this.iCurrentReactiveQuad4 != null)
            return new BigDecimal(String.valueOf(this.iCurrentReactiveQuad4));
        return iCurrentReactiveQuad4;
    }

    public BigDecimal getiDiffReactiveQuad1() {
        if (this.iDiffReactiveQuad1 != null)
            return new BigDecimal(String.valueOf(this.iDiffReactiveQuad1));
        return iDiffReactiveQuad1;
    }

    public BigDecimal getiDiffReactiveQuad2() {
        if (this.iDiffReactiveQuad2 != null)
            return new BigDecimal(String.valueOf(this.iDiffReactiveQuad2));
        return iDiffReactiveQuad2;
    }

    public BigDecimal getiDiffReactiveQuad3() {
        if (this.iDiffReactiveQuad3 != null)
            return new BigDecimal(String.valueOf(this.iDiffReactiveQuad3));
        return iDiffReactiveQuad3;
    }

    public BigDecimal getiDiffReactiveQuad4() {
        if (this.iDiffReactiveQuad4 != null)
            return new BigDecimal(String.valueOf(this.iDiffReactiveQuad4));
        return iDiffReactiveQuad4;
    }

    public BigDecimal getiConsumptionReactiveQuad1() {
        if (this.iConsumptionReactiveQuad1 != null)
            return new BigDecimal(String.valueOf(this.iConsumptionReactiveQuad1));
        return iConsumptionReactiveQuad1;
    }

    public BigDecimal getiConsumptionReactiveQuad2() {
        if (this.iConsumptionReactiveQuad2 != null)
            return new BigDecimal(String.valueOf(this.iConsumptionReactiveQuad2));
        return iConsumptionReactiveQuad2;
    }

    public BigDecimal getiConsumptionReactiveQuad3() {
        if (this.iConsumptionReactiveQuad3 != null)
            return new BigDecimal(String.valueOf(this.iConsumptionReactiveQuad3));
        return iConsumptionReactiveQuad3;
    }

    public BigDecimal getiConsumptionReactiveQuad4() {
        if (this.iConsumptionReactiveQuad4 != null)
            return new BigDecimal(String.valueOf(this.iConsumptionReactiveQuad4));
        return iConsumptionReactiveQuad4;
    }

    public BigDecimal getiConsumptionReactiveQuadTotal() {
        if (this.iConsumptionReactiveQuadTotal != null)
            return new BigDecimal(String.valueOf(this.iConsumptionReactiveQuadTotal));
        return iConsumptionReactiveQuadTotal;
    }

    public BigDecimal getiPreviousKvah() {
        if (this.iPreviousKvah != null)
            return new BigDecimal(String.valueOf(this.iPreviousKvah));
        return iPreviousKvah;
    }

    public BigDecimal getiCurrentKvah() {
        if (this.iCurrentKvah != null)
            return new BigDecimal(String.valueOf(this.iCurrentKvah));
        return iCurrentKvah;
    }

    public BigDecimal getiDiffKvah() {
        if (this.iDiffKvah != null)
            return new BigDecimal(String.valueOf(this.iDiffKvah));
        return iDiffKvah;
    }

    public BigDecimal getiConsumptionKvah() {
        if (this.iConsumptionKvah != null)
            return new BigDecimal(String.valueOf(this.iConsumptionKvah));
        return iConsumptionKvah;
    }

    public BigDecimal getePreviousKvah() {
        if (this.ePreviousKvah != null)
            return new BigDecimal(String.valueOf(this.ePreviousKvah));
        return ePreviousKvah;
    }

    public BigDecimal geteCurrentKvah() {
        if (this.eCurrentKvah != null)
            return new BigDecimal(String.valueOf(this.eCurrentKvah));
        return eCurrentKvah;
    }

    public BigDecimal geteDiffKvah() {
        if (this.eDiffKvah != null)
            return new BigDecimal(String.valueOf(this.eDiffKvah));
        return eDiffKvah;
    }

    public BigDecimal geteConsumptionKvah() {
        if (this.eConsumptionKvah != null)
            return new BigDecimal(String.valueOf(this.eConsumptionKvah));
        return eConsumptionKvah;
    }

    public BigDecimal getWheelingChargePercent() {
        if (this.wheelingChargePercent != null)
            return new BigDecimal(String.valueOf(this.wheelingChargePercent));
        return wheelingChargePercent;
    }

    public String getGeneratedBy() {
        return generatedBy;
    }

    public Timestamp getGeneratedOn() {
        return generatedOn;
    }

    public String getStatus() {
        return status;
    }

    public static Collection<SolarStatementBean> getSolarBean(){
        List<SolarStatementBean> solarStatementBeanList = new ArrayList<>();
        return solarStatementBeanList;
    }
}
