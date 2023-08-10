package in.co.mpwin.rebilling.beans.readingoperations;

import in.co.mpwin.rebilling.interfaces.BeanInterface;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Getter
@Setter
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
    @Column(name="main_current_kwh")@NotNull
    private Double mainCurrentKwh;
    @Column(name="main_previous_kwh")@NotNull
    private Double mainPreviousKwh;
    @Column(name="main_kwh_difference")@NotNull
    private Double mainKwhDifference;
    @Column(name="main_mf")@NotNull
    private Double mainMf;
    @Column(name="main_consumption")@NotNull
    private Double mainConsumption;
    @Column(name="main_assesment")@NotNull
    private Double mainAssesment;
    @Column(name="main_total_consumption")@NotNull
    private Double mainTotalConsumption;

    @Column(name="check_meter_no")@NotNull
    private String checkMeterNo;
    @Column(name="check_current_kwh")@NotNull
    private Double checkCurrentKwh;
    @Column(name="check_previous_kwh")@NotNull
    private Double checkPreviousKwh;
    @Column(name="check_kwh_difference")@NotNull
    private Double checkKwhDifference;
    @Column(name="check_mf")@NotNull
    private Double checkMf;
    @Column(name="check_consumption")@NotNull
    private Double checkConsumption;
    @Column(name="check_assesment")@NotNull
    private Double checkAssesment;
    @Column(name="check_total_consumption")@NotNull
    private Double checkTotalConsumption;

    @Column(name="percentage")@NotNull
    private Double percentage;
    @Column(name="result")@NotNull
    private Double result;

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
