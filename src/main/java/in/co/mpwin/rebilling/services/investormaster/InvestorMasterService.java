package in.co.mpwin.rebilling.services.investormaster;

import in.co.mpwin.rebilling.beans.feedermaster.FeederMasterBean;
import in.co.mpwin.rebilling.beans.investormaster.InvestorMasterBean;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.AuditControlServices;
import in.co.mpwin.rebilling.miscellanious.ValidatorService;
import in.co.mpwin.rebilling.repositories.investormaster.InvestorMasterRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InvestorMasterService {
    @Autowired
    InvestorMasterRepo investorMasterRepo;

    public List<InvestorMasterBean> getAllInvestorMasterBean(String status){
        List<InvestorMasterBean> investorMasterBeanList = new ArrayList<>();
        try {
            investorMasterBeanList = investorMasterRepo.findAllByStatus(status);
        }catch (Exception e){
            e.printStackTrace();
        }
        return investorMasterBeanList;
    }

    public List<InvestorMasterBean> getAllInvestorByLocationId(String locationId,String status){
        List<InvestorMasterBean> investorMasterBeanList = new ArrayList<>();
        try {
            investorMasterBeanList = investorMasterRepo.findByLocationIdAndStatus(locationId,status);
        }catch (Exception e){
            e.printStackTrace();
        }
        return investorMasterBeanList;
    }

    public InvestorMasterBean getInvestorById(Long id, String status){
        InvestorMasterBean investorMasterBean = new InvestorMasterBean();
        try {
            investorMasterBean = investorMasterRepo.findByIdAndStatus(id, status);
            if(investorMasterBean==null)
                throw new ApiException(HttpStatus.BAD_REQUEST, "Investor id "+id+" does not exist.\"");
        }catch (ApiException apiException){
            throw apiException;
        }catch (DataIntegrityViolationException d){
            throw d;
        }catch (Exception e){
            throw e;
        }
        return investorMasterBean;
    }

    public InvestorMasterBean getInvestorByInvestorCode(String investorCode, String status){
        InvestorMasterBean investorMasterBean = new InvestorMasterBean();
        try {
            investorMasterBean = investorMasterRepo.findByInvestorCodeAndStatus(investorCode, status);
        }catch (Exception e){
            e.printStackTrace();
        }
        return investorMasterBean;
    }

    public InvestorMasterBean createInvestorMaster(InvestorMasterBean investorMasterBean){
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
            // set investor code
            investorMasterBean.setInvestorCode("IC"+String.format("%03d",getMaxInvestorCode()+1));
            imb = investorMasterRepo.save(investorMasterBean);
        }catch (ApiException apiException) {
            throw apiException;
        } catch (DataIntegrityViolationException d) {
            throw d;
        } catch (Exception e) {
            throw e;
        }
        return imb;
    }

    public List<InvestorMasterBean> getAllInvestorByDeveloperIdAndPlantCode(String developerId, String plantCode, String status) {
        List<InvestorMasterBean> investorMasterBeanList = new ArrayList<>();
        try {
            investorMasterBeanList = investorMasterRepo.findByDeveloperIdAndPalntCodeAndStatus(developerId,plantCode,status);
            if(investorMasterBeanList.isEmpty())
                throw new ApiException(HttpStatus.BAD_REQUEST,"Investor not found against developer id "+developerId+" and plant code "+plantCode);

        }catch (ApiException apiException) {
            throw apiException;
        } catch (DataIntegrityViolationException d) {
            throw d;
        } catch (Exception e) {
            throw e;
        }
        return investorMasterBeanList;
    }

    public Integer getMaxInvestorCode()
    {
        Integer max = investorMasterRepo.findMaxInvestorCode();
        if(max==null)
            max=0;
        return max;
    }
}
