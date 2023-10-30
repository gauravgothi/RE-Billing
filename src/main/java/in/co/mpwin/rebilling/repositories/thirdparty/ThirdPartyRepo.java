package in.co.mpwin.rebilling.repositories.thirdparty;

import in.co.mpwin.rebilling.beans.thirdparty.ThirdPartyBean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThirdPartyRepo extends CrudRepository<ThirdPartyBean, Long> {
    List<ThirdPartyBean> findAll();
    List<ThirdPartyBean> findAllByStatus(String status);
    List<ThirdPartyBean> findByConsumerCodeAndStatus(String consumerCode, String active);

    List<ThirdPartyBean> findByInvestorCodeAndStatus(String investorId, String status);

    List<ThirdPartyBean> findByPlantCodeAndStatus(String plantCode, String status);
}
