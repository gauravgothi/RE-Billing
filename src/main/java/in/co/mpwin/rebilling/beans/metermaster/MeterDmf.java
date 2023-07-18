package in.co.mpwin.rebilling.beans.metermaster;

import jakarta.persistence.*;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@Entity(name="MeterDmf")@Table(name="re_meter_dmf")
public class MeterDmf {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    Long id;

    @Column(name="capacity")
    String capacity;
    @Column(name="value")
    String value;
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
