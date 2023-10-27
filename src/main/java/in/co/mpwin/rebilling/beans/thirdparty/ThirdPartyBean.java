package in.co.mpwin.rebilling.beans.thirdparty;


import com.fasterxml.jackson.annotation.JsonFormat;
import in.co.mpwin.rebilling.interfaces.BeanInterface;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
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
    private String developerName;

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
    @Column(name="mfp_id")
    private String mfpId;
    @Column(name="feeder_number")
    private  String feederNumber;
    @Column(name="feeder_injecting_substation_name")
    private String feederInjectingSubstationName;
    @Column(name="feeder_circuit_voltage")
    private String feederCircuitVoltage;

    @Column(name="developer_discom_name")
    private String developerDiscomName ;

    @Column(name="commission_date")@NotNull @JsonFormat(pattern = "yyyy-MM-dd",timezone = "IST")
    private Date commissionDate ;

    @Column(name="ppwa_date")@NotNull @JsonFormat(pattern = "yyyy-MM-dd",timezone = "IST")
    private Date ppwaDate ;

    @Column(name="sanction_date")@NotNull @JsonFormat(pattern = "yyyy-MM-dd",timezone = "IST")
    private Date sanctionDate ;

    @Column(name="period_of_ppwa")
    private String periodOfPpwa;

    @Column(name="adjustment_unit_percent") @Digits(integer = 5,fraction = 2)
    private BigDecimal adjustmentUnitPercent;

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
