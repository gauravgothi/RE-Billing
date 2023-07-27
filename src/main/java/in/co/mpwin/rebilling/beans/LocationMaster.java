package in.co.mpwin.rebilling.beans;

import in.co.mpwin.rebilling.interfaces.BeanInterface;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "re_location_master",uniqueConstraints = @UniqueConstraint(name = "divisioncode_status", columnNames={"division_code", "status"}))
public class LocationMaster implements BeanInterface {

    @Id
    @NotNull @Column(name = "id")
    private Long id;

    @Column(name = "region_code")
    private String regionCode;
    @Column(name = "region_name")
    private String regionName;

    @Column(name = "circle_code")
    private String circleCode;
    @Column(name = "circle_name")
    private String circleName;

    @Column(name = "division_code")
    private String divisionCode;
    @Column(name = "division_name")
    private String divisionName;

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
