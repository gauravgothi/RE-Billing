package in.co.mpwin.rebilling.repositories.investormaster;

import in.co.mpwin.rebilling.beans.investormaster.InvestorMasterBean;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface InvestorMasterRepo extends CrudRepository<InvestorMasterBean,Long> {

    List<InvestorMasterBean> findAllByStatus(String status);

    List<InvestorMasterBean> findByLocationIdAndStatus(String locationId,String status);

    InvestorMasterBean save(InvestorMasterBean investorMasterBean);

    InvestorMasterBean findByIdAndStatus(Long id, String status);

    InvestorMasterBean findByInvestorCodeAndStatus(String investorCode, String active);
}
