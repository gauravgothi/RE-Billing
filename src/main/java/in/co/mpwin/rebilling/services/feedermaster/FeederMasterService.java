package in.co.mpwin.rebilling.services.feedermaster;

import in.co.mpwin.rebilling.beans.feedermaster.FeederMasterBean;
import in.co.mpwin.rebilling.beans.metermaster.MeterMasterBean;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.AuditControlServices;
import in.co.mpwin.rebilling.miscellanious.Message;
import in.co.mpwin.rebilling.miscellanious.ValidatorService;
import in.co.mpwin.rebilling.repositories.feedermaster.FeederMasterRepo;
import in.co.mpwin.rebilling.services.thirdparty.ThirdPartyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FeederMasterService {
    private static final Logger logger = LoggerFactory.getLogger(FeederMasterService.class);
    @Autowired
    FeederMasterRepo feederMasterRepo;

    public List<FeederMasterBean> getAllFeederMasterBean(String status){
            final String methodName = "getAllFeederMasterBean() : ";
            logger.info(methodName + "called with parameters status={}",status);
            List<FeederMasterBean> allFeederList = new ArrayList<>();
            try {
                allFeederList= feederMasterRepo.findAllByStatus(status);
            }catch (ApiException apiException){
                logger.error(methodName+" throw apiException");
                throw apiException;
            }catch (DataIntegrityViolationException d){
                logger.error(methodName+" throw DataIntegrityViolationException");
                throw d;
            } catch (Exception e) {
                logger.error(methodName+" throw Exception");
                throw e;
            }
            logger.info(methodName + " return with FeederMasterBean list of size : {}",allFeederList.size());
            return allFeederList;
        }

    public List<FeederMasterBean> getAllFeederByLocationId(String locationId,String status){
            final String methodName = "getAllFeederByLocationId() : ";
            logger.info(methodName + "called with parameters locationId={}, status={}",locationId,status);
            List<FeederMasterBean> allFeederByLocation = new ArrayList<>();
            try {
                allFeederByLocation = feederMasterRepo.findByLocationIdAndStatus(locationId,status);
                if(allFeederByLocation.isEmpty())
                    throw new ApiException(HttpStatus.BAD_REQUEST,"feeder list not found for location id : "+locationId);
            }catch (ApiException apiException){
                logger.error(methodName+"throw apiException");
                throw apiException;
            }catch (DataIntegrityViolationException d){
                logger.error(methodName+" throw DataIntegrityViolationException");
                throw d;
            } catch (Exception e) {
                logger.error(methodName+" throw Exception");
                throw e;
            }
            logger.info(methodName + " return with FeederMasterBean list of size : {}",allFeederByLocation.size());
            return allFeederByLocation;
    }

    public FeederMasterBean createFeederMaster(FeederMasterBean feederMasterBean) {
            final String methodName = "createFeederMaster() : ";
            logger.info(methodName + "called with parameters feederMasterBean={}",feederMasterBean);
            //int result = -1;
            FeederMasterBean fmb = new FeederMasterBean();
            try {
            //check for existence of feeder if already created with same feeder number
            FeederMasterBean temp = feederMasterRepo.findByFeederNumberAndStatus(feederMasterBean.getFeederNumber(),"active");
            if(temp!=null) {
                throw new ApiException(HttpStatus.BAD_REQUEST,temp.getFeederNumber() + " is already exist.");
            }
            //Set the Audit control parameters, Globally
            new AuditControlServices().setInitialAuditControlParameters(feederMasterBean);

            //Validate the meterno remove the space.
            feederMasterBean.setFeederNumber(new ValidatorService().removeSpaceFromString(feederMasterBean.getFeederNumber()));
            fmb = feederMasterRepo.save(feederMasterBean);
            }catch (ApiException apiException){
                logger.error(methodName+"throw apiException");
                throw apiException;
            }catch (DataIntegrityViolationException d){
                logger.error(methodName+" throw DataIntegrityViolationException");
                throw d;
            } catch (Exception e) {
                logger.error(methodName+" throw Exception");
                throw e;
            }
            logger.info(methodName + " return with FeederMasterBean : {}",fmb);
            return fmb;
    }

    public FeederMasterBean getFeederByFeederNumber(String feederNumber, String status){
            final String methodName = "getFeederByFeederNumber() : ";
            logger.info(methodName + "called with parameters feederNumber={}, status={}",feederNumber,status);
            FeederMasterBean feederMasterBean = new FeederMasterBean();
            try{
                feederMasterBean = feederMasterRepo.findByFeederNumberAndStatus(feederNumber,status);
                if (feederMasterBean == null)
                    throw new ApiException(HttpStatus.BAD_REQUEST,"Feeder Number not exist in feeder master..");
            }catch (ApiException apiException){
                logger.error(methodName+" throw apiException");
                throw apiException;
            }catch (DataIntegrityViolationException d){
                logger.error(methodName+" throw DataIntegrityViolationException");
                throw d;
            } catch (Exception e) {
                logger.error(methodName+" throw Exception");
                throw e;
            }
            logger.info(methodName + " return with FeederMasterBean : {}",feederMasterBean);
            return feederMasterBean;
    }

    public FeederMasterBean getFeederById(Long id, String status){
        final String methodName = "getFeederById() : ";
        logger.info(methodName + "called with parameters id={}, status={}",id,status);
        FeederMasterBean feederMasterBean = new FeederMasterBean();
        try{
            feederMasterBean = feederMasterRepo.findByIdAndStatus(id,status);
        }catch (ApiException apiException){
            logger.error(methodName+" throw apiException");
            throw apiException;
        }catch (DataIntegrityViolationException d){
            logger.error(methodName+" throw DataIntegrityViolationException");
            throw d;
        } catch (Exception e) {
            logger.error(methodName+" throw Exception");
            throw e;
        }
        logger.info(methodName + " return with FeederMasterBean : {}",feederMasterBean);
        return feederMasterBean;
    }
}
