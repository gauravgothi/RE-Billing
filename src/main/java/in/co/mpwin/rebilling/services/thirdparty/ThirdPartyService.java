package in.co.mpwin.rebilling.services.thirdparty;

import in.co.mpwin.rebilling.beans.thirdparty.ThirdPartyBean;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.AuditControlServices;
import in.co.mpwin.rebilling.miscellanious.Message;
import in.co.mpwin.rebilling.repositories.thirdparty.ThirdPartyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThirdPartyService {
    @Autowired private ThirdPartyRepo thirdPartyRepo;

    public ThirdPartyBean saveThirdPartyBean(ThirdPartyBean tpBean) {
        try {
            new AuditControlServices().setInitialAuditControlParameters(tpBean);
            ThirdPartyBean savedBean =  thirdPartyRepo.save(tpBean);
            if(savedBean==null)
                throw new ApiException(HttpStatus.BAD_REQUEST,"Third Party Data could not saved due to some error");
            return savedBean;
        }catch (ApiException apiException){
            throw apiException;
        }catch (DataIntegrityViolationException d){
            throw d;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<ThirdPartyBean> getThirdPartyBeans() {
        try {
            List<ThirdPartyBean> tpLists = thirdPartyRepo.findAllByStatus("active");
            if (tpLists.isEmpty())
                throw new ApiException(HttpStatus.BAD_REQUEST, "No content found for third party.");
            return tpLists;
        } catch (ApiException apiException) {
            throw apiException;
        } catch (DataIntegrityViolationException d) {
            throw d;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

