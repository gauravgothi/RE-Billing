package in.co.mpwin.rebilling.repositories.mapping;

import in.co.mpwin.rebilling.beans.mapping.InvestorMachineMappingBean;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvestorMachineMappingRepo extends CrudRepository<InvestorMachineMappingBean, Long> {

    @Query(value="select * from ecell.re_investor_machine_mapping where mfp_id=?1 and investor_code=?2 and machine_code=?3", nativeQuery = true)
    InvestorMachineMappingBean findByMFPIdInvestorIdMachineId(Long mfpId, String investorCode, String machineCode);

    List<InvestorMachineMappingBean> findByStatus(String status);

    InvestorMachineMappingBean findByIdAndStatus(Long id, String status);

    List<InvestorMachineMappingBean> findByInvestorCodeAndStatus(String investorCode, String status);


   // @Query(value = "select * from ecell.re_investor_machine_mapping where mfp_id=?1 and status=?2", nativeQuery = true)
    List<InvestorMachineMappingBean> findByMfpIdAndStatus(Long mfpId, String status);

    List<InvestorMachineMappingBean> findByMachineCodeAndStatus(String machineCode, String status);
}
