package in.co.mpwin.rebilling.repositories.investormaster;

import in.co.mpwin.rebilling.beans.investormaster.InvestorMasterBean;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InvestorMasterRepo extends CrudRepository<InvestorMasterBean,Long> {

    List<InvestorMasterBean> findAllByStatus(String status);

    List<InvestorMasterBean> findByLocationIdAndStatus(String locationId,String status);

    InvestorMasterBean save(InvestorMasterBean investorMasterBean);

    InvestorMasterBean findByIdAndStatus(Long id, String status);

    InvestorMasterBean findByInvestorCodeAndStatus(String investorCode, String active);

    @Query(value = "select * from ecell.re_investor_master where investor_code in (Select DISTINCT investor_code from ecell.re_investor_machine_mapping where mfp_id in (SELECT id FROM ecell.re_meter_feeder_plant_mapping where developer_id=:developerId AND plant_code=:plantCode AND status='active') AND status='active') and status=:status", nativeQuery = true)
    List<InvestorMasterBean> findByDeveloperIdAndPalntCodeAndStatus(@Param("developerId") String developerId,@Param("plantCode") String plantCode,@Param("status") String status);

    @Query(value = "SELECT MAX(CAST(SPLIT_PART(investor_code,'C',2)AS INTEGER)) FROM ecell.re_investor_master WHERE investor_code like 'IC%'",nativeQuery = true)
    Integer findMaxInvestorCode();
}
