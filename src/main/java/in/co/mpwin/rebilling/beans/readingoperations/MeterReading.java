package in.co.mpwin.rebilling.beans.readingoperations;

import in.co.mpwin.rebilling.interfaces.BeanInterface;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@Entity(name="MeterReading")
@Table(name="re_meter_reading_trx",uniqueConstraints = @UniqueConstraint(name = "re_meter_reading_trx_ukey", columnNames={"meter_no","reading_date","reading_type","status"}))
public class MeterReading implements BeanInterface {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name="meter_no")@NotNull
    private String meterNo;

    @Column(name="mf")@NotNull
    private Double mf;
    
    @Column(name = "reading_date")@NotNull
    private Date readingDate;
    @Column(name = "end_date")@NotNull
    private Date endDate;
    @Column(name="reading_type")@NotNull
    private String readingType;
    @Column(name="read_source")@NotNull
    private String readSource;

    @Column(name="e_tod1")@NotNull
    private Double eTod1;
    @Column(name="e_tod2")@NotNull
    private Double eTod2;
    @Column(name="e_tod3")@NotNull
    private Double eTod3;
    @Column(name="e_tod4")@NotNull
    private Double eTod4;
    @Column(name="e_active_energy")@NotNull
    private Double eActiveEnergy;
    @Column(name="e_reactive_quad1")@NotNull
    private Double eReactiveQuad1;
    @Column(name="e_reactive_quad2")@NotNull
    private Double eReactiveQuad2;
    @Column(name="e_reactive_quad3")@NotNull
    private Double eReactiveQuad3;
    @Column(name="e_reactive_quad4")@NotNull
    private Double eReactiveQuad4;
    @Column(name="e_adjustment")@NotNull
    private Double eAdjustment;
    @Column(name="e_max_demand")@NotNull
    private Double eMaxDemand;
    @Column(name="e_kvah")@NotNull
    private Double eKvah;
    @Column(name="e_assesment")@NotNull
    private Double eAssesment;


    @Column(name="i_tod1")@NotNull
    private Double iTod1;
    @Column(name="i_tod2")@NotNull
    private Double iTod2;
    @Column(name="i_tod3")@NotNull
    private Double iTod3;
    @Column(name="i_tod4")@NotNull
    private Double iTod4;
    @Column(name="i_active_energy")@NotNull
    private Double iActiveEnergy;
    @Column(name="i_reactive_quad1")@NotNull
    private Double iReactiveQuad1;
    @Column(name="i_reactive_quad2")@NotNull
    private Double iReactiveQuad2;
    @Column(name="i_reactive_quad3")@NotNull
    private Double iReactiveQuad3;
    @Column(name="i_reactive_quad4")@NotNull
    private Double iReactiveQuad4;
    @Column(name="i_adjustment")@NotNull
    private Double iAdjustment;
    @Column(name="i_max_demand")@NotNull
    private Double iMaxDemand;
    @Column(name="i_kvah")@NotNull
    private Double iKvah;
    @Column(name="i_assesment")@NotNull
    private Double iAssesment;

    @Column(name = "current_state")
    private String currentState;

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
