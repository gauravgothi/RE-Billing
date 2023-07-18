package in.co.mpwin.rebilling.beans.metermaster;

import jakarta.persistence.*;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@Entity(name="MeterMake")@Table(name="re_meter_make")
public class MeterMake {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    Long id;

    @Column(name="make")
    String make;
    @Column(name="status")
    String status;
    @Column(name="remark")
    String remark;
    @Column(name="created_by")
    String createdBy;
    @Column(name="updated_by")
    String updatedBy;
    @Column(name="created_on")
    Timestamp createdOn;
    @Column(name="updated_on")
    Timestamp updatedOn;

}
