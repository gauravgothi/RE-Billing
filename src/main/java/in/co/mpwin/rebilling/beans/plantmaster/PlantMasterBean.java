package in.co.mpwin.rebilling.beans.plantmaster;

import in.co.mpwin.rebilling.interfaces.BeanInterface;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;


@Getter
@Setter

@Entity(name="PlantMasterBean")
@Table(name="re_plant_master",uniqueConstraints =@UniqueConstraint(name = "re_plant_master_ukey", columnNames={"plant_code","status"}))
public class PlantMasterBean implements BeanInterface {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="plant_code")
    String plantCode;

    @Column(name="plant_name")
    String plantName;

    @Column(name="address")
    String address;


    @Column(name="contact_no")
    String contactNo;

    @Column(name="contact_person")
    String contactPerson;

    @Column(name="email")
    String email;

    @Column(name="commissioned_date")
    Date commissionedDate;

    @Column(name="type")
    String  type;

    @Column(name ="location_id")
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
