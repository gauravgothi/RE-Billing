package in.co.mpwin.rebilling.beans;

import in.co.mpwin.rebilling.interfaces.BeanInterface;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "bpm_location_master")
public class LocationMaster implements BeanInterface {

    @Id
    @NotNull @Column(name = "loccode")
    private String locationCode;

    @Column(name = "dc_name")
    private String dcName;

    @Column(name = "division")
    private String division;

    @Column(name = "circle")
    private String circle;

    @Column(name = "region")
    private String region;

    @Column(name = "short_code")
    private String shortCode;

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

    @Column(name = "region_officer_name")
    private String regionOfficerName;

    @Column(name = "region_officer_mbno")
    private String regionOfficerMobile;

    @Column(name = "region_officer_email")
    private String regionOfficerEmail;

    @Column(name = "circle_officer_name")
    private String circleOfficerName;

    @Column(name = "circle_officer_mbno")
    private String circleOfficerMobile;

    @Column(name = "circle_officer_email")
    private String circleOfficerEmail;

    @Column(name = "division_officer_name")
    private String divisionOfficerName;

    @Column(name = "division_officer_mbno")
    private String divisionOfficerMobile;

    @Column(name = "division_officer_email")
    private String divisionOfficerEmail;

    @Column(name = "dc_officer_name")
    private String dcOfficerName;

    @Column(name = "dc_officer_mbno")
    private String dcOfficerMobile;

    @Column(name = "dc_officer_email")
    private String dcOfficerEmail;

}
