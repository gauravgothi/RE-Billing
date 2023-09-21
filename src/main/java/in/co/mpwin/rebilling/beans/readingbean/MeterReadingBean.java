package in.co.mpwin.rebilling.beans.readingbean;

import com.fasterxml.jackson.annotation.JsonFormat;
import in.co.mpwin.rebilling.interfaces.BeanInterface;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
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

    //e_attribute1 to 10 is reserved for future use

}
