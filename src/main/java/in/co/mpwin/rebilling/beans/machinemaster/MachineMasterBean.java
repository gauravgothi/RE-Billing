package in.co.mpwin.rebilling.beans.machinemaster;

import com.fasterxml.jackson.annotation.JsonFormat;
import in.co.mpwin.rebilling.beans.locationmaster.LocationMaster;
import in.co.mpwin.rebilling.interfaces.BeanInterface;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Setter
@ToString
@Entity(name="MachineMasterBean")
@Table(name="re_machine_master",uniqueConstraints = @UniqueConstraint(name = "re_machine_master_ukey", columnNames={"machine_code", "status"}))
public class MachineMasterBean implements BeanInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name = "machine_code")
    String  machineCode;

    @Column(name = "machine_name")
    String machineName;

    @Column(name = "capacity")
    String capacity;

    public String getCapacity() {
        if(capacity == null || capacity.matches("%[a-zA-Z]%"))
            return "0";
        return capacity;
    }

    @Column(name = "commissioned_date") @JsonFormat(pattern = "yyyy-MM-dd",timezone = "IST")
    Date commissionedDate;

    @Column(name = "active_rate")
    String activeRate;

    public String getActiveRate() {
        if(activeRate == null || activeRate.matches("%[a-zA-Z]%"))
            return "0";
        return activeRate;
    }

    @Column(name ="reactive_rate")
    String  reactiveRate;


    @Column(name ="created_by")
    private String createdBy;
    @Column(name ="updated_by")
    private String updatedBy;
    @Column(name ="updated_on")@JsonFormat(timezone = "IST")
    private Timestamp updatedOn;
    @Column(name ="created_on")@JsonFormat(timezone = "IST")
    private Timestamp createdOn;
    @Column(name ="status")
    private String status;
    @Column(name ="remark")
    private String remark;

    @Column(name = "location_id")
    String locationId;

    @OneToOne
    @JoinColumn(name = "location_id", insertable=false, updatable=false)
    private LocationMaster locationMaster;
}
