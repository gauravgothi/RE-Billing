package in.co.mpwin.rebilling.beans.readingbean;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter
@NoArgsConstructor@AllArgsConstructor
@Entity(name = "FivePercentBean")
@Table(name = "re_5perc_report_trx",uniqueConstraints = @UniqueConstraint(name = "re_5perc_report_trx_ukey", columnNames={"plantCode","monthYear"}))
public class FivePercentBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String developerId,developerName;
    private String investorId,investorName;
    private String developerSiteAddress;
    private String feederCode,feederName;

    //plant code inserted in extra
    private String plantCode;
    //flag for is main meter ,check meter or both selected
    private String meterSelectedFlag;
    private String mainMeterNumber;
    private String mainCurrentReading;
    private String mainPreviousReading;
    private String mainReadingDifference;
    private String mainMf;
    private String mainAssessment;
    private String mainConsumption;
    private String mainTotalConsumption;

    private String checkMeterNumber;
    private String checkCurrentReading;
    private String checkPreviousReading;
    private String checkReadingDifference;
    private String checkMf;
    private String checkAssessment;
    private String checkConsumption;
    private String checkTotalConsumption;

    private String monthYear;//extra
    private String percentage;
    private String result;
    private String remark="NA";
}
