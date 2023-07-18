package in.co.mpwin.rebilling.beans.metermaster;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@Getter
@Entity
@Table(name="re_meter_me_ptr")
public class MeterMePtr {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="capacity")
    private String capacity;

    @Column(name="value")
    private String value;

    @Column(name="status")
    private  String status;

    @Column(name="remark")
    private String remark;

    @Column(name="created_by")
    private String createdBy;
    @Column(name="updated_by")
    private String updatedBy;
    @Column(name="created_on")
    private Timestamp createdOn;
    @Column(name="updated_on")
    private Timestamp updatedOn;
}
