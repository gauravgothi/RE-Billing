package in.co.mpwin.rebilling.beans.developermaster;

import in.co.mpwin.rebilling.beans.locationmaster.LocationMaster;
import in.co.mpwin.rebilling.interfaces.BeanInterface;
import in.co.mpwin.rebilling.miscellanious.ConstantField;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import java.sql.Timestamp;

@Getter @Setter
@Entity(name="DeveloperMasterBean")
@Table(name="re_developer_master")
public class DeveloperMasterBean implements BeanInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="developer_name")
    String developerName;

    @Column(name="cin")
    String cin;

    @Column(name="office_address")
    String officeAddress;

    @Column(name="office_contact_person")
    String officeContactPerson;

    @Column(name="office_email")
    @Email(regexp = ConstantField.emailRegex,message = "Email must be valid")
    String officeEmail;

    @Column(name = "office_contact_no")
    String officeContactNo;

    @Column(name = "site_address")
    private String siteAddress;
    @Column(name = "site_contact_person")
    private String siteContactPerson;
    @Column(name = "site_email")
    @Email(regexp = ConstantField.emailRegex,message = "Email must be valid")
    private String siteEmail;
    @Column(name = "site_contact_no")
    private String siteContactNo;
    @Column(name = "rmd_eau_developers")
    private String rmdEauDevelopers;
    @Column(name = "generator_type")
    private String generatorType;

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
