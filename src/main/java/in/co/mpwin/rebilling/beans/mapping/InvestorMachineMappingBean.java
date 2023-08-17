package in.co.mpwin.rebilling.beans.mapping;


import in.co.mpwin.rebilling.interfaces.BeanInterface;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Entity(name="InvestorMachineMappingBean")
@Table(name="re_investor_machine_mapping", uniqueConstraints = @UniqueConstraint(name ="re_investor_machine_mapping_ukey", columnNames={"mfp_id","investor_code", "machine_code","status"}))
public class InvestorMachineMappingBean implements BeanInterface
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="mfp_id")
    private Long mfpId;
//    @ManyToOne()
//    @JoinColumn(name = "mfp_id",referencedColumnName = "id", updatable = false, insertable = false)
//    private MeterFeederPlantMappingBean meterFeederPlantMappingBean;

    @Column(name="investor_code")
    private String investorCode;
//    @ManyToOne()
//    @JoinColumn(name = "investor_code", referencedColumnName = "investorCode,", updatable = false, insertable = false)
//    private InvestorMasterBean investorMasterBean;


   @Column(name="machine_code")
    private String machineCode;
//    @OneToOne()
 //   @JoinColumn(name = "machine_code", referencedColumnName = "machineCode", updatable = false, insertable = false)
//    private MachineMasterBean machineMasterBean;

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

}
