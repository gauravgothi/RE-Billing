package in.co.mpwin.rebilling.beans.thirdparty;


import com.fasterxml.jackson.annotation.JsonFormat;
import in.co.mpwin.rebilling.interfaces.BeanInterface;
import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter @ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "ThirdPartyBean")
@Table(name = "re_third_party",uniqueConstraints = { @UniqueConstraint(name = "re_third_party_ukey", columnNames={"consumer_code","status"})})
public class ThirdPartyBean implements BeanInterface  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name="consumer_code")
    private String consumerCode;

    @Column(name="consumer_name")
    private String consumerName;

    @Column(name="consumer_address")
    private String consumerAddress;

    @Column(name="contract_demand")
    private String contractDemand;

    @Column(name="tariff_category")
    private String tariffCategory;

    @Column(name="supply_voltage")
    private String supplyVoltage ;

    @Column(name="region")
    private String  region;

    @Column(name="circle")
    private String circle ;


    @Column(name="division")
    private String division ;

    @Column(name="developer_id")
    private String developerId ;

    @Column(name="developer_name")
    private String developer_name;

    @Column(name="plant_code")
    private String plantCode ;

    @Column(name="plant_name")
    private String plantName;

    @Column(name="plant_capacity")
    private String plantCapacity ;

    @Column(name="investor_code")
    private String investorCode;

    @Column(name="investor_name")
    private String investorName ;

    @Column(name="site_location")
    private String siteLocation ;


    @Column(name="main_meter_no")
    private String mainMeterNo ;


    @Column(name="check_meter_no")
    private String checkMeterNo;

    @Column(name="standby_meter_no")
    private String standbyMeterNo;

    @Column(name="developer_discom_name")
    private String developerDiscomName ;

    @Column(name="commission_date") @JsonFormat(timezone = "IST")
    private Date commissionDate ;

    @Column(name="ppwa_date") @JsonFormat(timezone = "IST")
    private Date ppwaDate ;

    @Column(name="sanction_date") @JsonFormat(timezone = "IST")
    private Date sanctionDate ;

    @Column(name="period_of_ppwa")
    private String periodOfPpwa;

    @Column(name="adjustment_unit_percent")
    private String adjustmentUnitPercent;

    @Column(name="userid")
    private String userid;
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
