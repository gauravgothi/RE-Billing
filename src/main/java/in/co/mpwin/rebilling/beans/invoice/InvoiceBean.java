package in.co.mpwin.rebilling.beans.invoice;

import com.fasterxml.jackson.annotation.JsonFormat;
import in.co.mpwin.rebilling.interfaces.BeanInterface;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "InvoiceBean")
@Table(name = "re_invoice_common",uniqueConstraints = { @UniqueConstraint(name = "re_invoice_common_ukey", columnNames={"investor_code","billing_month","status"}),
                                                        @UniqueConstraint(name = "re_invoice_common_ukey2",columnNames = {"billno"})})
public class InvoiceBean implements BeanInterface {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="invoiceno")@NotNull
    private String invoiceNumber;

    @Column(name="invoice_date")@NotNull @JsonFormat(timezone = "IST")
    private Date invoiceDate;
    @Column(name="billno")@NotNull
    private String billNo;
    @Column(name="hmeterno")@NotNull
    private String hMeterNo;
    @Column(name="hcategory")@NotNull
    private String hCategory;
    @Column(name="hmf")@NotNull
    private String hMf;
    @Column(name="hreading_date")@NotNull
    private String hReadingDate;
    @Column(name="billing_month")@NotNull
    private String billingMonth;
    @Column(name="billing_year")@NotNull
    private String billingYear;
    @Column(name="investor_code")@NotNull
    private String investorCode;
    @Column(name="investor_name")@NotNull
    private String investerName;

    @Column(name="ppwa_no")@NotNull
    private String ppwaNo;
    @Column(name="gst_no")@NotNull
    private String gstNo;
    @Column(name="cin")@NotNull
    private String cin;
    @Column(name="tin")@NotNull
    private String tin;
    @Column(name="vat")@NotNull
    private String vat;
    @Column(name="office_address")@NotNull
    private String officeAddress;
    @Column(name="circle_name")@NotNull
    private String circleName;
    @Column(name="ppa_letter_no")@NotNull
    private String ppaLetterNo;
    @Column(name="ppa_date")@NotNull
    private String ppaDate;
    @Column(name="commissioned_date")@NotNull
    private String commissionedDate;
    @Column(name="particulars")@NotNull
    private String particulars;
    @Column(name="lkwh_active_energy")@NotNull
    private BigDecimal lKwhActiveEnergy;
    @Column(name="lrkvah")@NotNull
    private BigDecimal lRkvah;
    @Column(name="lfixed_adjustment_val")@NotNull
    private BigDecimal lFixedAdjustmentVal;
    @Column(name="ladjustment")@NotNull
    private BigDecimal lAdjustment;

    @Column(name="lfree_unit")@NotNull
    private BigDecimal lFreeUnit;
    @Column(name="lactive_rate")@NotNull
    private BigDecimal lActiveRate;
    @Column(name="lreactive_rate")@NotNull
    private BigDecimal lReactiveRate;
    @Column(name="account_name")@NotNull
    private String accountName;
    @Column(name="bank_name")@NotNull
    private String bankName;
    @Column(name="account_no")@NotNull
    private String accountNo;
    @Column(name="ifsc_code")@NotNull
    private String ifscCode;
    @Column(name="micr")@NotNull
    private String micr;
    @Column(name="lcdev_id")@NotNull
    private String lcdevId;
    @Column(name="type")@NotNull
    private String type;  //WIND AND SOLAR from Plant
    @Column(name="user_id")
    private String userId;
    @Column(name="line_kwh_amount")@NotNull
    private BigDecimal lineKwhAmount;
    @Column(name="line_rkvah_amount")@NotNull
    private BigDecimal lineRkvahAmount;
    @Column(name="line_fix_adj_amt")@NotNull
    private BigDecimal lineFixAdjAmt;
    @Column(name="line_adjustment_unit_amt")@NotNull
    private BigDecimal lineAdjustmentUnitAmt;

    @Column(name="line_free_unit_amt")@NotNull
    private BigDecimal lineFreeUnitAmt;
    @Column(name="total_amount")@NotNull
    private BigDecimal totalAmount;

    @Column(name="grand_total_amount")@NotNull
    private BigDecimal grandTotalAmount;

    @Column(name="grand_total_amount_rounded")@NotNull
    private BigDecimal grandTotalAmountRounded;

    @Column(name="amount_words")@NotNull
    private String amountWords;

    @Column(name = "invoice_stage")@NotNull
    private String invoiceStage;

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

    public long getId() {
        return id;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public String getBillNo() {
        return billNo;
    }

    public String gethMeterNo() {
        return hMeterNo;
    }

    public String gethCategory() {
        return hCategory;
    }

    public String gethMf() {
        return hMf;
    }

    public String gethReadingDate() {
        return hReadingDate;
    }

    public String getBillingMonth() {
        return billingMonth;
    }

    public String getBillingYear() {
        return billingYear;
    }

    public String getInvestorCode() {
        return investorCode;
    }

    public String getInvesterName() {
        return investerName;
    }

    public String getPpwaNo() {
        return ppwaNo;
    }

    public String getInvoiceStage() {
        return invoiceStage;
    }

    public String getGstNo() {
        return gstNo;
    }

    public String getCin() {
        return cin;
    }

    public String getTin() {
        return tin;
    }

    public String getVat() {
        return vat;
    }

    public String getOfficeAddress() {
        return officeAddress;
    }

    public String getCircleName() {
        return circleName;
    }

    public String getPpaLetterNo() {
        return ppaLetterNo;
    }

    public String getPpaDate() {
        return ppaDate;
    }

    public String getCommissionedDate() {
        return commissionedDate;
    }

    public String getParticulars() {
        return particulars;
    }

    public BigDecimal getlKwhActiveEnergy() {
        if (this.lKwhActiveEnergy != null)
            return new BigDecimal(String.valueOf(this.lKwhActiveEnergy));
        return lKwhActiveEnergy;
    }

    public BigDecimal getlRkvah() {
        if (this.lRkvah != null)
            return new BigDecimal(String.valueOf(this.lRkvah));
        return lRkvah;
    }

    public BigDecimal getlFixedAdjustmentVal() {
        if (this.lFixedAdjustmentVal != null)
            return new BigDecimal(String.valueOf(this.lFixedAdjustmentVal));
        return lFixedAdjustmentVal;
    }

    public BigDecimal getlAdjustment() {
        if (this.lAdjustment != null)
            return new BigDecimal(String.valueOf(this.lAdjustment));
        return lAdjustment;
    }

    public BigDecimal getlFreeUnit() {
        if (this.lFreeUnit != null)
            return new BigDecimal(String.valueOf(this.lFreeUnit));
        return lFreeUnit;
    }

    public BigDecimal getlActiveRate() {
        if (this.lActiveRate != null)
            return new BigDecimal(String.valueOf(this.lActiveRate));
        return lActiveRate;
    }

    public BigDecimal getlReactiveRate() {
        if (this.lReactiveRate != null)
            return new BigDecimal(String.valueOf(this.lReactiveRate));
        return lReactiveRate;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getBankName() {
        return bankName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public String getMicr() {
        return micr;
    }

    public String getLcdevId() {
        return lcdevId;
    }

    public String getType() {
        return type;
    }

    public String getUserId() {
        return userId;
    }

    public BigDecimal getLineKwhAmount() {
        if (this.lineKwhAmount != null)
            return new BigDecimal(String.valueOf(this.lineKwhAmount));
        return lineKwhAmount;
    }

    public BigDecimal getLineRkvahAmount() {
        if (this.lineRkvahAmount != null)
            return new BigDecimal(String.valueOf(this.lineRkvahAmount));
        return lineRkvahAmount;
    }

    public BigDecimal getLineFixAdjAmt() {
        if (this.lineFixAdjAmt != null)
            return new BigDecimal(String.valueOf(this.lineFixAdjAmt));
        return lineFixAdjAmt;
    }

    public BigDecimal getLineAdjustmentUnitAmt() {
        if (this.lineAdjustmentUnitAmt != null)
            return new BigDecimal(String.valueOf(this.lineAdjustmentUnitAmt));
        return lineAdjustmentUnitAmt;
    }

    public BigDecimal getLineFreeUnitAmt() {
        if (this.lineFreeUnitAmt != null)
            return new BigDecimal(String.valueOf(this.lineFreeUnitAmt));
        return lineFreeUnitAmt;
    }

    public BigDecimal getTotalAmount() {
        if (this.totalAmount != null)
            return new BigDecimal(String.valueOf(this.totalAmount));
        return totalAmount;
    }

    public BigDecimal getGrandTotalAmount() {
        if (this.grandTotalAmount != null)
            return new BigDecimal(String.valueOf(this.grandTotalAmount));
        return grandTotalAmount;
    }

    public BigDecimal getGrandTotalAmountRounded() {
        if (this.grandTotalAmountRounded != null)
            return new BigDecimal(String.valueOf(this.grandTotalAmountRounded));
        return grandTotalAmountRounded;
    }

    public String getAmountWords() {
        return amountWords;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public Timestamp getUpdatedOn() {
        return updatedOn;
    }

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public String getStatus() {
        return status;
    }

    public String getRemark() {
        return remark;
    }
}
