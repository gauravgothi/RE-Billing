package in.co.mpwin.rebilling.beans.mapping;


import com.fasterxml.jackson.annotation.JsonFormat;
import in.co.mpwin.rebilling.beans.feedermaster.FeederMasterBean;
import in.co.mpwin.rebilling.beans.metermaster.MeterMasterBean;
import in.co.mpwin.rebilling.beans.plantmaster.PlantMasterBean;
import in.co.mpwin.rebilling.interfaces.BeanInterface;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Date;

@Getter @ToString
@Setter
@Entity(name="MeterFeederPlantMappingBean")
@Table(name="re_meter_feeder_plant_mapping",uniqueConstraints = @UniqueConstraint(name = "re_meter_feeder_plant_mapping_ukey", columnNames={"main_meter_no", "check_meter_no", "standby_meter_no", "developer_id"}))
public class MeterFeederPlantMappingBean implements BeanInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name="main_meter_no")
    String mainMeterNo;

//    @ManyToOne
//    @JoinColumns({ @JoinColumn(name="main_meter_no", referencedColumnName = "meter_number",insertable=false, updatable=false) , @JoinColumn(name="status",referencedColumnName = "status",insertable=false, updatable=false) })
//    private MeterMasterBean meterMasterBean;

    @NotNull
    @Column(name="check_meter_no")
    String checkMeterNo;
//    @ManyToOne
//    @JoinColumns({ @JoinColumn(name="check_meter_no", referencedColumnName = "meter_number",insertable=false, updatable=false) ,
//            @JoinColumn(name="status",referencedColumnName = "status",insertable=false, updatable=false) })
//   private MeterMasterBean checkMeterMasterBean;

    @NotNull
    @Column(name="standby_meter_no")
    String standbyMeterNo;
//    @ManyToOne
//        @JoinColumns({ @JoinColumn(name="standby_meter_no", referencedColumnName = "meter_number",insertable=false, updatable=false) ,
//        @JoinColumn(name="status",referencedColumnName = "status",insertable=false, updatable=false) })
//        private MeterMasterBean standbyMeterMasterBean;

    @NotNull
    @Column(name="feeder_code")
    String feederCode ;
//    @ManyToOne
//    @JoinColumn(name="feeder_code", referencedColumnName ="feeder_no", updatable = false, insertable=false)
//    private FeederMasterBean feederMasterBean;

    @NotNull
    @Column(name="plant_code")
    String plantCode ;

//    @ManyToOne
//    @JoinColumn(name="plant_code", referencedColumnName = "plant_code",updatable = false, insertable = false)
//    private PlantMasterBean plantMasterBean;

    @NotNull
    @Column(name="developer_id")
    String developerId ;
 //    @ManyToOne
//    @JoinColumn(name="developer_id", referencedColumnName = "id",updatable = false, insertable = false)
//    private DeveloperMasterBean developerMasterBean;


    @NotNull
    @Column(name="end_date") @NotNull @JsonFormat(pattern = "yyyy-MM-dd",timezone = "IST")
    Date endDate ;
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
