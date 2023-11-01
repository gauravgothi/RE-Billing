package in.co.mpwin.rebilling.beans.readingbean;

import in.co.mpwin.rebilling.interfaces.BeanInterface;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter @ToString
@Entity(name="AmrValidatedReading")
@Table(name="re_amr_validated_reading_trx")
public class AmrValidatedReading implements BeanInterface {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="developer_id")@NotNull
    private String developerId;
    @Column(name="investor_id")@NotNull
    private String investorId;
    @Column(name="developer_address")@NotNull
    private String developerAddress;
    @Column(name="feeder_code")@NotNull
    private String feederCode;

    @Column(name="main_meter_no")@NotNull
    private String mainMeterNo;
    @Column(name="main_current_kwh")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal mainCurrentKwh;
    @Column(name="main_previous_kwh")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal mainPreviousKwh;
    @Column(name="main_kwh_difference")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal mainKwhDifference;
    @Column(name="main_mf")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal mainMf;
    @Column(name="main_consumption")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal mainConsumption;
    @Column(name="main_assesment")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal mainAssesment;
    @Column(name="main_total_consumption")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal mainTotalConsumption;

    @Column(name="check_meter_no")@NotNull
    private String checkMeterNo;
    @Column(name="check_current_kwh")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal checkCurrentKwh;
    @Column(name="check_previous_kwh")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal checkPreviousKwh;
    @Column(name="check_kwh_difference")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal checkKwhDifference;
    @Column(name="check_mf")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal checkMf;
    @Column(name="check_consumption")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal checkConsumption;
    @Column(name="check_assesment")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal checkAssesment;
    @Column(name="check_total_consumption")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal checkTotalConsumption;

    @Column(name="percentage")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal percentage;
    @Column(name="result")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal result;

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

    //e_attribute1 to 10 is reserved for future use
    
}
