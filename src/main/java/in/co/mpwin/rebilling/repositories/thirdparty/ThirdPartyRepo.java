package in.co.mpwin.rebilling.repositories.thirdparty;

import in.co.mpwin.rebilling.beans.thirdparty.ThirdPartyBean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThirdPartyRepo extends CrudRepository<ThirdPartyBean, Long> {
    List<ThirdPartyBean> findAllByStatus(String status);

    List<ThirdPartyBean> findAllByInvestorCodeAndStatus(String investorCode, String status);
}
