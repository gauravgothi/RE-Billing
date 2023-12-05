package in.co.mpwin.rebilling.services.developermaster;

import in.co.mpwin.rebilling.beans.developermaster.DeveloperMasterBean;
import in.co.mpwin.rebilling.beans.readingbean.MeterReadingBean;
import in.co.mpwin.rebilling.dto.MeterConsumptionDto;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.AuditControlServices;
import in.co.mpwin.rebilling.miscellanious.ValidatorService;
import in.co.mpwin.rebilling.repositories.developermaster.DeveloperMasterRepo;
import in.co.mpwin.rebilling.services.readingservice.MeterReadingService;
import in.co.mpwin.rebilling.services.thirdparty.ThirdPartyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeveloperMasterService {
    private static final Logger logger = LoggerFactory.getLogger(DeveloperMasterService.class);

    @Autowired
    DeveloperMasterRepo developerMasterRepo;
//    @Autowired
//    MeterReadingService meterReadingService;

    public List<DeveloperMasterBean> getAllDeveloperMasterBean(String status)   {
            final String methodName = "getAllDeveloperMasterBean() : ";
            logger.info(methodName + "called with parameters status={}",status);
            List<DeveloperMasterBean> developerMasterBeanList = new ArrayList<>();
            try {
                developerMasterBeanList = developerMasterRepo.findAllByStatus(status);
            }catch (ApiException apiException){
                logger.error(methodName+"throw apiException");
                throw apiException;
            }catch (DataIntegrityViolationException d){
                logger.error(methodName+"DataIntegrityViolationException");
                throw d;
            } catch (Exception e) {
                logger.error(methodName+"Exception");
                throw e;
            }
            logger.info(methodName + "return with DeveloperMasterBean list of size : {}",developerMasterBeanList.size());
            return developerMasterBeanList;
    }

    public List<DeveloperMasterBean> getAllDeveloperByLocationId(String locationId,String status)   {
            final String methodName = "getAllDeveloperByLocationId() : ";
            logger.info(methodName + "called with parameters locationId={}, status={}",locationId,status);
            List<DeveloperMasterBean> developerMasterBeanList = new ArrayList<>();
            try {
                developerMasterBeanList = developerMasterRepo.findByLocationIdAndStatus(locationId,status);
            }catch (ApiException apiException){
                logger.error(methodName+"throw apiException");
                throw apiException;
            }catch (DataIntegrityViolationException d){
                logger.error(methodName+"DataIntegrityViolationException");
                throw d;
            } catch (Exception e) {
                logger.error(methodName+"Exception");
                throw e;
            }
            logger.info(methodName + "return with DeveloperMasterBean list of size : {}",developerMasterBeanList.size());
            return developerMasterBeanList;
    }

    public DeveloperMasterBean getDeveloperById(Long id, String status){
            final String methodName = "getDeveloperById() : ";
            logger.info(methodName + "called with parameters id={}, status={}",id,status);
            DeveloperMasterBean developerMasterBean = new DeveloperMasterBean();
            try {
                developerMasterBean = developerMasterRepo.findByIdAndStatus(id, status);
            }catch (ApiException apiException){
                logger.error(methodName+"throw apiException");
                throw apiException;
            }catch (DataIntegrityViolationException d){
                logger.error(methodName+"DataIntegrityViolationException");
                throw d;
            } catch (Exception e) {
                logger.error(methodName+"Exception");
                throw e;
            }
            logger.info(methodName + "return with DeveloperMasterBean : {}",developerMasterBean);
            return developerMasterBean;
    }

    public DeveloperMasterBean createDeveloperMaster(DeveloperMasterBean developerMasterBean){
            final String methodName = " createDeveloperMaster() : ";
            logger.info(methodName + "called with parameters developerMasterBean={}",developerMasterBean);
            DeveloperMasterBean dmb = new DeveloperMasterBean();
            try {

            //Set the Audit control parameters, Globally
            new AuditControlServices().setInitialAuditControlParameters(developerMasterBean);
            //Validate the meterno remove the space.
            developerMasterBean.setCin(new ValidatorService().removeSpaceFromString(developerMasterBean.getCin()));
            developerMasterBean.setOfficeContactNo(new ValidatorService().removeSpaceFromString(developerMasterBean.getOfficeContactNo()));
            developerMasterBean.setSiteContactNo(new ValidatorService().removeSpaceFromString(developerMasterBean.getSiteContactNo()));
            dmb = developerMasterRepo.save(developerMasterBean);
            }catch (ApiException apiException){
                logger.error(methodName+"throw apiException");
                throw apiException;
            }catch (DataIntegrityViolationException d){
                logger.error(methodName+"DataIntegrityViolationException");
                throw d;
            } catch (Exception e) {
                logger.error(methodName+"Exception");
                throw e;
            }
            logger.info(methodName + "return with DeveloperMasterBean : {}",dmb);
            return dmb;
    }


    public Long getDeveloperIdByUsername(String username) {
            final String methodName = "getDeveloperIdByUsername() : ";
            logger.info(methodName + "called with parameters username={}",username);
            try {
                Long developerId = developerMasterRepo.findIdByDeveloperUsername(username);
                if (developerId == null)
                    throw new ApiException(HttpStatus.BAD_REQUEST,"Developer "+username+" is not present in developer master");
                else {
                    logger.info(methodName + "return with DeveloperMasterBean : {}",developerId);
                    return developerId;
                }
            }catch (ApiException apiException){
                logger.error(methodName+"throw apiException");
                throw apiException;
            }catch (DataIntegrityViolationException d){
                logger.error(methodName+"DataIntegrityViolationException");
                throw d;
            } catch (Exception e) {
                logger.error(methodName+"Exception");
                throw e;
            }
    }

//    public DeveloperMasterBean getBifurcateDto(MeterConsumptionDto dto) {
//        List<String> validCurrentState = List.of("dev_accept","circle_accept","circle_reject");
//
//       MeterReadingBean currentReadingBean =
//               meterReadingService.getReadingByMeterNoAndReadingDateAndStatus
//                       (dto.getMeterNo(),dto.getCurrentReadingDate(),"active");
//
//       MeterReadingBean previousReadingBean =
//               meterReadingService.getReadingByMeterNoAndReadingDateAndStatus
//                       (dto.getMeterNo(),dto.getPreviousReadingDate(),"active");
//
//       //currentReadingBean.getCurrentState().
//    }

   }
