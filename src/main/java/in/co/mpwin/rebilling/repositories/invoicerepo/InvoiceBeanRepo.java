package in.co.mpwin.rebilling.repositories.invoicerepo;


import in.co.mpwin.rebilling.beans.invoice.InvoiceBean;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface InvoiceBeanRepo extends CrudRepository<InvoiceBean,Long> {

    @Query(value = "SELECT MAX(CAST (SPLIT_PART(invoiceno,'/',4) AS INTEGER)) FROM ecell.re_invoice_common WHERE billing_year =:billingYear AND ppwa_no=:ppwaNo",nativeQuery = true)
    Integer findMaxInvoiceNumber(@Param("ppwaNo") String ppwaNo,@Param("billingYear") String billingYear);

    @Query(value = "SELECT MAX(CAST (SPLIT_PART(billno,'/',4) AS INTEGER)) FROM ecell.re_invoice_common WHERE billing_year =:billingYear AND investor_code=:investorCode",nativeQuery = true)
    Integer findMaxBillNumber(@Param("investorCode") String investorCode,@Param("billingYear") String billingYear);

    InvoiceBean findByInvestorCodeAndBillingMonthAndStatus(String investorCode,String billingMonth,String status);

    InvoiceBean findByInvoiceNumberAndStatus(String invoiceNumber, String active);
}
