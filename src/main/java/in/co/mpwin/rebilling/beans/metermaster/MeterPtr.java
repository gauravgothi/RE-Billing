package in.co.mpwin.rebilling.beans.metermaster;


import in.co.mpwin.rebilling.interfaces.BeanInterface;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="re_meter_ptr")
public class MeterPtr implements BeanInterface {

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
