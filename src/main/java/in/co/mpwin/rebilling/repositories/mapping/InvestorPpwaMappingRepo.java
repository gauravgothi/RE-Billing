package in.co.mpwin.rebilling.repositories.mapping;

import in.co.mpwin.rebilling.beans.mapping.InvestorPpwaMappingBean;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InvestorPpwaMappingRepo extends CrudRepository<InvestorPpwaMappingBean,Long> {

    @Query(value = "SELECT investor_code FROM ecell.re_ppwa_invoice WHERE ppwa_no=:ppwaNo AND status=:status",nativeQuery = true)
    List<String> findAllInvestorCodeByPpwaNo(@Param("ppwaNo") String ppwaNo,@Param("status") String status);

    @Query(value = "SELECT ppwa_no FROM ecell.re_ppwa_invoice WHERE investor_code=:investorCode AND status=:status",nativeQuery = true)
    String findPpwaNoByInvestorCode(@Param("investorCode") String investorCode,@Param("status")  String status);
}
