package in.co.mpwin.rebilling.beans.metermaster;


import in.co.mpwin.rebilling.interfaces.BeanInterface;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@Entity(name="MeterReplacementBean")@Table(name="re_meter_replacement", uniqueConstraints = @UniqueConstraint(name = "re_meter_replacement_ukey", columnNames={"old_meter_number", "new_meter_number","replace_date"}))
public class MeterReplacementBean implements BeanInterface {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    Long id;
    @Column(name="old_meter_number")
    private String oldMeterNumber;

    @Column(name="new_meter_number")
    private String newMeterNumber;

    @Column(name="replace_date")
    private Date replaceDate;

    @Column(name="developer_id")
    private String developerId;

    @Column(name="feeder_code")
    private String feederCode;

    @Column(name="plant_code")
    private String plantCode;
    @Column(name="created_by")
    String createdBy;
    @Column(name="updated_by")
    String updatedBy;
    @Column(name="created_on")
    Timestamp createdOn;
    @Column(name="updated_on")
    Timestamp updatedOn;
    @Column(name="status")
    String status;
    @Column(name="remark")
    String remark;

}
