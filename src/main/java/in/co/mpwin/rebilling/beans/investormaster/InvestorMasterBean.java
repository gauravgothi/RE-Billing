package in.co.mpwin.rebilling.beans.investormaster;

import in.co.mpwin.rebilling.beans.locationmaster.LocationMaster;
import in.co.mpwin.rebilling.interfaces.BeanInterface;
import in.co.mpwin.rebilling.miscellanious.ConstantField;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import java.sql.Timestamp;

@Getter
@Setter
@Entity(name="InvestorMasterBean")
@Table(name="re_investor_master")
public class InvestorMasterBean implements BeanInterface {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "investor_code")
    String investorCode;
    @Column(name = "investor_name")
    String investorName;
    @Column(name="cin")
    String cin;
    @Column(name="tin")
    String tin;
    @Column(name = "gst_no")
    String gstNo;

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

    @Column(name = "vat")
    private String vat;
    @Column(name = "buyer")
    private String buyer;
    @Column(name = "fixed_adjust")
    private String fixedAdjust;
    @Column(name = "bank_name")
    private String bankName;
    @Column(name = "account_holder_name")
    private String accountHolderName;
    @Column(name = "account_no")
    private String accountNo;
    @Column(name = "ifsc_code")
    private String ifscCode;
    @Column(name = "micr")
    private String micr;
    @Column(name = "nldc")
    private String nldc;


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
