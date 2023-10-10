package in.co.mpwin.rebilling.beans.mapping;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "InvestorPpwaMappingBean")
@Table(name = "re_ppwa_invoice",uniqueConstraints = { @UniqueConstraint(name = "re_ppwa_invoice_ukey", columnNames={"investor_code", "ppwa_no", "status"})})
public class InvestorPpwaMappingBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="investor_code")@NotNull
    private String investorCode;

    @Column(name = "ppwa_no")@NotNull
    private String ppwaNo;

    @Column(name = "status")
    private String status;
    @Column(name = "remark")
    private String remark;


}
