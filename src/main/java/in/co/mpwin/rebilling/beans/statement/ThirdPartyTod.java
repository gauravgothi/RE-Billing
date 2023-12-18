package in.co.mpwin.rebilling.beans.statement;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Setter @ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "ThirdPartyTod")
@Table(name = "re_solar_tp_tod")
//@Table(name = "re_third_party_tod",uniqueConstraints = { @UniqueConstraint(name = "re_third_party_tod_ukey", columnNames={"consumer_code"})})
public class ThirdPartyTod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="tp_code")
    private String tpCode;
    @Column(name="tp_name")
    private String tpName;
    @Column(name="tp_percentage")
    private BigDecimal tpPercentage;
    @Column(name="tp_tod1")
    private BigDecimal tpTod1;
    @Column(name="tp_tod2")
    private BigDecimal tpTod2;
    @Column(name="tp_tod3")
    private BigDecimal tpTod3;
    @Column(name="tp_tod4")
    private BigDecimal tpTod4;
    @Column(name="tp_kwh_export")
    private BigDecimal tpKwhExport;
    @Column(name="tp_adjustment")
    private BigDecimal tpAdjustment;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solar_statement_id",nullable = false,referencedColumnName = "id")
    private SolarStatementBean solarStatementBean;

    //not required to set the solar statement bean in thirdparty tod otherwise recursion occur
//    public SolarStatementBean getSolarStatementBean() {
//        return solarStatementBean;
//    }

    public long getId() {
        return id;
    }

    public String getTpCode() {
        return tpCode;
    }

    public String getTpName() {
        return tpName;
    }

    public BigDecimal getTpPercentage() {
        if (this.tpPercentage != null)
            return new BigDecimal(String.valueOf(this.tpPercentage));
        return tpPercentage;
    }

    public BigDecimal getTpTod1() {
        if (this.tpTod1 != null)
            return new BigDecimal(String.valueOf(this.tpTod1));
        return tpTod1;
    }

    public BigDecimal getTpTod2() {
        if (this.tpTod2 != null)
            return new BigDecimal(String.valueOf(this.tpTod2));
        return tpTod2;
    }

    public BigDecimal getTpTod3() {
        if (this.tpTod3 != null)
            return new BigDecimal(String.valueOf(this.tpTod3));
        return tpTod3;
    }

    public BigDecimal getTpTod4() {
        if (this.tpTod4 != null)
            return new BigDecimal(String.valueOf(this.tpTod4));
        return tpTod4;
    }

    public BigDecimal getTpKwhExport() {
        if (this.tpKwhExport != null)
            return new BigDecimal(String.valueOf(this.tpKwhExport));
        return tpKwhExport;
    }

    public BigDecimal getTpAdjustment() {
        if (this.tpAdjustment != null)
            return new BigDecimal(String.valueOf(this.tpAdjustment));
        return tpAdjustment;
    }

}
