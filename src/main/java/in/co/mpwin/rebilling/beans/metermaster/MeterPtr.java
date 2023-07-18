package in.co.mpwin.rebilling.beans.metermaster;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="re_meter_ptr")
public class MeterPtr {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    Long id;

    @Column(name="capacity")
    private String capacity;

    @Column(name="value")
    private String value;

    @Column(name="status")
    private  String status;

    @Column(name="remark")
    private String remark;

    @Column(name="created_by")
    String createdBy;
    @Column(name="updated_by")
    String updatedBy;
    @Column(name="created_on")
    Timestamp createdOn;
    @Column(name="updated_on")
    Timestamp updatedOn;
}
