package in.co.mpwin.rebilling.services.investormaster;

import in.co.mpwin.rebilling.beans.feedermaster.FeederMasterBean;
import in.co.mpwin.rebilling.beans.investormaster.InvestorMasterBean;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.AuditControlServices;
import in.co.mpwin.rebilling.miscellanious.ValidatorService;
import in.co.mpwin.rebilling.repositories.investormaster.InvestorMasterRepo;
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
public class InvestorMasterService {
        private static final Logger logger = LoggerFactory.getLogger(InvestorMasterService.class);
    @Autowired
    InvestorMasterRepo investorMasterRepo;

    public List<InvestorMasterBean> getAllInvestorMasterBean(String status){
        final String methodName = "getAllInvestorMasterBean() : ";
        logger.info(methodName + "called with parameters status={}",status);
        List<InvestorMasterBean> investorMasterBeanList = new ArrayList<>();
        try {
            investorMasterBeanList = investorMasterRepo.findAllByStatus(status);
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
        logger.info(methodName + " return with InvestorMasterBean list of size : {}",investorMasterBeanList.size());
        return investorMasterBeanList;
    }

    public List<InvestorMasterBean> getAllInvestorByLocationId(String locationId,String status){
        final String methodName = "getAllInvestorByLocationId() : ";
        logger.info(methodName + "called with parameters locationId={}, status={}",locationId,status);
        List<InvestorMasterBean> investorMasterBeanList = new ArrayList<>();
        try {
            investorMasterBeanList = investorMasterRepo.findByLocationIdAndStatus(locationId,status);
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
        logger.info(methodName + " return with InvestorMasterBean list of size : {}",investorMasterBeanList.size());
        return investorMasterBeanList;
    }

    public InvestorMasterBean getInvestorById(Long id, String status){
        final String methodName = "getInvestorById() : ";
        logger.info(methodName + "called with parameters id",id);
        InvestorMasterBean investorMasterBean = new InvestorMasterBean();
        try {
            investorMasterBean = investorMasterRepo.findByIdAndStatus(id, status);
            if(investorMasterBean==null)
                throw new ApiException(HttpStatus.BAD_REQUEST, "Investor id "+id+" does not exist.\"");
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
        logger.info(methodName + " return InvestorMasterBean : {}",investorMasterBean);
        return investorMasterBean;
    }

    public InvestorMasterBean getInvestorByInvestorCode(String investorCode, String status){
        final String methodName = "getInvestorByInvestorCode() : ";
        logger.info(methodName + "called with parameters investorCode={}, status={}",investorCode,status);
        InvestorMasterBean investorMasterBean = new InvestorMasterBean();
        try {
            investorMasterBean = investorMasterRepo.findByInvestorCodeAndStatus(investorCode, status);
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
        logger.info(methodName + " return InvestorMasterBean : {}",investorMasterBean);
        return investorMasterBean;
    }

    public InvestorMasterBean createInvestorMaster(InvestorMasterBean investorMasterBean){
        final String methodName = "createInvestorMaster() : ";
        logger.info(methodName + "called with parameters investorMasterBean={}",investorMasterBean);
        InvestorMasterBean imb = new InvestorMasterBean();
        try {
            //check for existence of investor by name if already created with same investor name throw api exception
            InvestorMasterBean temp = investorMasterRepo.findByInvestorNameIgnoreCaseAndStatus(investorMasterBean.getInvestorName(),"active");
            if(temp!=null) {
                throw new ApiException(HttpStatus.BAD_REQUEST,"Investor with same name is already exist with investor code: "+temp.getInvestorCode()+" and plant name: "+temp.getInvestorName());
            }
            //Set the Audit control parameters, Globally
            new AuditControlServices().setInitialAuditControlParameters(investorMasterBean);
            // remove the space.
            investorMasterBean.setCin(new ValidatorService().removeSpaceFromString(investorMasterBean.getCin()));
            investorMasterBean.setTin(new ValidatorService().removeSpaceFromString(investorMasterBean.getTin()));
            investorMasterBean.setGstNo(new ValidatorService().removeSpaceFromString(investorMasterBean.getGstNo()));
            investorMasterBean.setOfficeContactNo(new ValidatorService().removeSpaceFromString(investorMasterBean.getOfficeContactNo()));
            investorMasterBean.setSiteContactNo(new ValidatorService().removeSpaceFromString(investorMasterBean.getSiteContactNo()));
            investorMasterBean.setAccountNo(new ValidatorService().removeSpaceFromString(investorMasterBean.getAccountNo()));
            investorMasterBean.setIfscCode(new ValidatorService().removeSpaceFromString(investorMasterBean.getIfscCode()));
            investorMasterBean.setMicr(new ValidatorService().removeSpaceFromString(investorMasterBean.getMicr()));
            investorMasterBean.setNldc(new ValidatorService().removeSpaceFromString(investorMasterBean.getNldc()));
            investorMasterBean.setPpaFreeUnit(new ValidatorService().removeSpaceFromString(investorMasterBean.getPpaFreeUnit()));
            // set investor code
            investorMasterBean.setInvestorCode("IC"+String.format("%03d",getMaxInvestorCode()+1));
            imb = investorMasterRepo.save(investorMasterBean);
        }catch(ApiException apiException){
            logger.error(methodName+" throw apiException");
            throw apiException;
        }catch(DataIntegrityViolationException d){
            logger.error(methodName+" throw DataIntegrityViolationException");
            throw d;
        } catch(Exception e) {
            logger.error(methodName+" throw Exception");
            throw e;
        }
        logger.info(methodName + " return InvestorMasterBean : {}",imb);
        return imb;
    }

    public List<InvestorMasterBean> getAllInvestorByDeveloperIdAndPlantCode(String developerId, String plantCode, String status) {
        final String methodName = "getAllInvestorByDeveloperIdAndPlantCode() : ";
        logger.info(methodName + "called with parameters developerId={}, plantCode={},status={}",developerId,plantCode,status);
        List<InvestorMasterBean> investorMasterBeanList = new ArrayList<>();
        try {
            investorMasterBeanList = investorMasterRepo.findByDeveloperIdAndPalntCodeAndStatus(developerId,plantCode,status);
            if(investorMasterBeanList.isEmpty())
                throw new ApiException(HttpStatus.BAD_REQUEST,"Investor not found against developer id "+developerId+" and plant code "+plantCode);

        }catch(ApiException apiException){
            logger.error(methodName+" throw apiException");
            throw apiException;
        }catch(DataIntegrityViolationException d){
            logger.error(methodName+" throw DataIntegrityViolationException");
            throw d;
        } catch(Exception e) {
            logger.error(methodName+" throw Exception");
            throw e;
        }
        logger.info(methodName + " return with InvestorMasterBean list of size : {}",investorMasterBeanList.size());
        return investorMasterBeanList;
    }

    public Integer getMaxInvestorCode()
    {
        final String methodName = "getMaxInvestorCode() : ";
        logger.info(methodName + "called with parameters empty");
        Integer max = investorMasterRepo.findMaxInvestorCode();
        if(max==null)
            max=0;
        logger.info(methodName + " return with parameter max : {}",max);
        return max;
    }
}
