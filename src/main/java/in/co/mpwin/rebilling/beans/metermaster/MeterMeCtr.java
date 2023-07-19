package in.co.mpwin.rebilling.beans.metermaster;

import in.co.mpwin.rebilling.interfaces.BeanInterface;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;


@Getter
@Setter
@Entity(name="MeterMeCtr")@Table(name="re_meter_me_ctr")
public class MeterMeCtr implements BeanInterface {
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
