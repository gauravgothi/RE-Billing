package in.co.mpwin.rebilling.beans.machinemaster;

import in.co.mpwin.rebilling.beans.mapping.InvestorMachineMappingBean;
import in.co.mpwin.rebilling.interfaces.BeanInterface;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Setter
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


    @Column(name = "commissioned_date")
    Date commissionedDate;

    @Column(name = "active_rate")
    String activeRate;

    @Column(name ="reactive_rate")
    String  reactiveRate;


    @Column(name="ppa_letter_no")
    String  ppaLetterNo;

    @Column(name="ppa_date")
    String   ppaDate;

    @Column(name="particulars")
    String  particulars;

    @Column(name="location_id")
    String locationId;

    @Column(name ="created_by")
    private String createdBy;
    @Column(name ="updated_by")
    private String updatedBy;
    @Column(name ="updated_on")
    private Timestamp updatedOn;
    @Column(name ="created_on")
    private Timestamp createdOn;
    @Column(name ="status")
    private String status;
    @Column(name ="remark")
    private String remark;


}
