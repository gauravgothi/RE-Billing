package in.co.mpwin.rebilling.beans.feedermaster;

import in.co.mpwin.rebilling.beans.locationmaster.LocationMaster;
import in.co.mpwin.rebilling.interfaces.BeanInterface;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter @Setter @ToString
@Entity(name="FeederMasterBean")@Table(name="re_feeder_master",uniqueConstraints = @UniqueConstraint(name = "re_feeder_master_ukey", columnNames={"feeder_no", "status"}))
public class FeederMasterBean implements BeanInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="feeder_no")
    String feederNumber;

    @Column(name="feeder_name")
    String feederName;

    @Column(name="injecting_ss_code")
    String injectingSsCode;

    @Column(name="injecting_ss_name")
    String injectingSsName;

    @Column(name="circuit_voltage")
    String circuitVoltage;


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

    @Column(name = "location_id")
    String locationId;

    @OneToOne
    @JoinColumn(name = "location_id", insertable=false, updatable=false)
    private LocationMaster locationMaster;


}
