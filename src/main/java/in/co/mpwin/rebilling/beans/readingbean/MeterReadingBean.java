package in.co.mpwin.rebilling.beans.readingbean;

import com.fasterxml.jackson.annotation.JsonFormat;
import in.co.mpwin.rebilling.interfaces.BeanInterface;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;


@Setter @ToString
@Entity(name="MeterReading")
@Table(name="re_meter_reading_trx",uniqueConstraints = @UniqueConstraint(name = "re_meter_reading_trx_ukey", columnNames={"meter_no","reading_date","reading_type","status"}))
public class MeterReadingBean implements BeanInterface {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name="meter_no")@NotNull
    private String meterNo;

    @Column(name="mf")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal mf;
    
    @Column(name = "reading_date")@NotNull@JsonFormat(pattern = "yyyy-MM-dd",timezone = "IST")
    private Date readingDate;
    @Column(name = "end_date")@NotNull@JsonFormat(pattern = "yyyy-MM-dd",timezone = "IST")
    private Date endDate;
    @Column(name="reading_type")@NotNull
    private String readingType;
    @Column(name="read_source")@NotNull
    private String readSource;

    @Column(name="e_tod1")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal eTod1;
    @Column(name="e_tod2")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal eTod2;
    @Column(name="e_tod3")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal eTod3;
    @Column(name="e_tod4")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal eTod4;
    @Column(name="e_active_energy")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal eActiveEnergy;
    @Column(name="e_reactive_quad1")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal eReactiveQuad1;
    @Column(name="e_reactive_quad2")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal eReactiveQuad2;
    @Column(name="e_reactive_quad3")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal eReactiveQuad3;
    @Column(name="e_reactive_quad4")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal eReactiveQuad4;
    @Column(name="e_adjustment")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal eAdjustment;
    @Column(name="e_max_demand")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal eMaxDemand;
    @Column(name="e_kvah")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal eKvah;
    @Column(name="e_assesment")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal eAssesment;


    @Column(name="i_tod1")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iTod1;
    @Column(name="i_tod2")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iTod2;
    @Column(name="i_tod3")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iTod3;
    @Column(name="i_tod4")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iTod4;
    @Column(name="i_active_energy")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iActiveEnergy;
    @Column(name="i_reactive_quad1")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iReactiveQuad1;
    @Column(name="i_reactive_quad2")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iReactiveQuad2;
    @Column(name="i_reactive_quad3")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iReactiveQuad3;
    @Column(name="i_reactive_quad4")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iReactiveQuad4;
    @Column(name="i_adjustment")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iAdjustment;
    @Column(name="i_max_demand")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iMaxDemand;
    @Column(name="i_kvah")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iKvah;
    @Column(name="i_assesment")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iAssesment;

    @Column(name = "current_state")
    private String currentState;

    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "updated_by")
    private String updatedBy;
    @Column(name = "updated_on")@JsonFormat(timezone = "IST")
    private Timestamp updatedOn;
    @Column(name = "created_on")@JsonFormat(timezone = "IST")
    private Timestamp createdOn;
    @Column(name = "status")
    private String status;
    @Column(name = "remark")
    private String remark;

    @Column(name = "e_attribute1")
    private String eAttribute1;
    @Column(name = "e_attribute2")
    private String eAttribute2;
    @Column(name = "e_attribute3")
    private String eAttribute3;
    @Column(name = "e_attribute4")
    private String eAttribute4;
    @Column(name = "e_attribute5")
    private String eAttribute5;
    @Column(name = "i_attribute1")
    private String iAttribute1;
    @Column(name = "i_attribute2")
    private String iAttribute2;
    @Column(name = "i_attribute3")
    private String iAttribute3;
    @Column(name = "i_attribute4")
    private String iAttribute4;
    @Column(name = "i_attribute5")
    private String iAttribute5;

    public Long getId() {
        return id;
    }

    public String getMeterNo() {
        return meterNo;
    }

    public BigDecimal getMf() {
        if (this.mf != null)
            return new BigDecimal(String.valueOf(this.mf));
        return mf;
    }

    public Date getReadingDate() {
        return readingDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getReadingType() {
        return readingType;
    }

    public String getReadSource() {
        return readSource;
    }

    public BigDecimal getETod1() {
        if (this.eTod1 != null)
            return new BigDecimal(String.valueOf(this.eTod1));
        return eTod1;
    }

    public BigDecimal getETod2() {
        if (this.eTod2 != null)
            return new BigDecimal(String.valueOf(this.eTod2));
        return eTod2;
    }

    public BigDecimal getETod3() {
        if (this.eTod3 != null)
            return new BigDecimal(String.valueOf(this.eTod3));
        return eTod3;
    }

    public BigDecimal getETod4() {
        if (this.eTod4 != null)
            return new BigDecimal(String.valueOf(this.eTod4));
        return eTod4;
    }

    public BigDecimal getEActiveEnergy() {
        if (this.eActiveEnergy != null)
            return new BigDecimal(String.valueOf(this.eActiveEnergy));
        return eActiveEnergy;
    }

    public BigDecimal getEReactiveQuad1() {
        if (this.eReactiveQuad1 != null)
            return new BigDecimal(String.valueOf(this.eReactiveQuad1));
        return eReactiveQuad1;
    }

    public BigDecimal getEReactiveQuad2() {
        if (this.eReactiveQuad2 != null)
            return new BigDecimal(String.valueOf(this.eReactiveQuad2));
        return eReactiveQuad2;
    }

    public BigDecimal getEReactiveQuad3() {
        if (this.eReactiveQuad3 != null)
            return new BigDecimal(String.valueOf(this.eReactiveQuad3));
        return eReactiveQuad3;
    }

    public BigDecimal getEReactiveQuad4() {
        if (this.eReactiveQuad4 != null)
            return new BigDecimal(String.valueOf(this.eReactiveQuad4));
        return eReactiveQuad4;
    }

    public BigDecimal getEAdjustment() {
        if (this.eAdjustment != null)
            return new BigDecimal(String.valueOf(this.eAdjustment));
        return eAdjustment;
    }

    public BigDecimal getEMaxDemand() {
        if (this.eMaxDemand != null)
            return new BigDecimal(String.valueOf(this.eMaxDemand));
        return eMaxDemand;
    }

    public BigDecimal getEKvah() {
        if (this.eKvah != null)
            return new BigDecimal(String.valueOf(this.eKvah));
        return eKvah;
    }

    public BigDecimal getEAssesment() {
        if (this.eAssesment != null)
            return new BigDecimal(String.valueOf(this.eAssesment));
        return eAssesment;
    }

    public BigDecimal getITod1() {
        if (this.iTod1 != null)
            return new BigDecimal(String.valueOf(this.iTod1));
        return iTod1;
    }

    public BigDecimal getITod2() {
        if (this.iTod2 != null)
            return new BigDecimal(String.valueOf(this.iTod2));
        return iTod2;
    }

    public BigDecimal getITod3() {
        if (this.iTod3 != null)
            return new BigDecimal(String.valueOf(this.iTod3));
        return iTod3;
    }

    public BigDecimal getITod4() {
        if (this.iTod4 != null)
            return new BigDecimal(String.valueOf(this.iTod4));
        return iTod4;
    }

    public BigDecimal getIActiveEnergy() {
        if (this.iActiveEnergy != null)
            return new BigDecimal(String.valueOf(this.iActiveEnergy));
        return iActiveEnergy;
    }

    public BigDecimal getIReactiveQuad1() {
        if (this.iReactiveQuad1 != null)
            return new BigDecimal(String.valueOf(this.iReactiveQuad1));
        return iReactiveQuad1;
    }

    public BigDecimal getIReactiveQuad2() {
        if (this.iReactiveQuad2 != null)
            return new BigDecimal(String.valueOf(this.iReactiveQuad2));
        return iReactiveQuad2;
    }

    public BigDecimal getIReactiveQuad3() {
        if (this.iReactiveQuad3 != null)
            return new BigDecimal(String.valueOf(this.iReactiveQuad3));
        return iReactiveQuad3;
    }

    public BigDecimal getIReactiveQuad4() {
        if (this.iReactiveQuad4 != null)
            return new BigDecimal(String.valueOf(this.iReactiveQuad4));
        return iReactiveQuad4;
    }

    public BigDecimal getIAdjustment() {
        if (this.iAdjustment != null)
            return new BigDecimal(String.valueOf(this.iAdjustment));
        return iAdjustment;
    }

    public BigDecimal getIMaxDemand() {
        if (this.iMaxDemand != null)
            return new BigDecimal(String.valueOf(this.iMaxDemand));
        return iMaxDemand;
    }

    public BigDecimal getIKvah() {
        if (this.iKvah != null)
            return new BigDecimal(String.valueOf(this.iKvah));
        return iKvah;
    }

    public BigDecimal getIAssesment() {
        if (this.iAssesment != null)
            return new BigDecimal(String.valueOf(this.iAssesment));
        return iAssesment;
    }

    public String getCurrentState() {
        return currentState;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public Timestamp getUpdatedOn() {
        return updatedOn;
    }

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public String getStatus() {
        return status;
    }

    public String getRemark() {
        return remark;
    }

    //e_attribute1 to 10 is reserved for future use

}
