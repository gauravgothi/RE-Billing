package in.co.mpwin.rebilling.repositories.thirdparty;

import in.co.mpwin.rebilling.beans.thirdparty.ThirdPartyBean;
import in.co.mpwin.rebilling.beans.thirdparty.ThirdPartyBeanHistory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThirdPartyHistoryRepo extends CrudRepository<ThirdPartyBeanHistory, Long> {

}
