package in.co.mpwin.rebilling.services.thirdparty;

import in.co.mpwin.rebilling.beans.developermaster.DeveloperMasterBean;
import in.co.mpwin.rebilling.beans.feedermaster.FeederMasterBean;
import in.co.mpwin.rebilling.beans.investormaster.InvestorMasterBean;
import in.co.mpwin.rebilling.beans.mapping.MeterFeederPlantMappingBean;
import in.co.mpwin.rebilling.beans.plantmaster.PlantMasterBean;
import in.co.mpwin.rebilling.beans.readingbean.FivePercentBean;
import in.co.mpwin.rebilling.beans.thirdparty.DeveloperPlantDto;
import in.co.mpwin.rebilling.beans.thirdparty.ThirdPartyBean;
import in.co.mpwin.rebilling.beans.thirdparty.ThirdPartyBeanHistory;
import in.co.mpwin.rebilling.dto.ConsumptionPercentageDto2;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.AuditControlServices;
import in.co.mpwin.rebilling.miscellanious.DateMethods;
import in.co.mpwin.rebilling.miscellanious.Message;
import in.co.mpwin.rebilling.miscellanious.TokenInfo;
import in.co.mpwin.rebilling.repositories.mapping.MeterFeederPlantMappingRepo;
import in.co.mpwin.rebilling.repositories.thirdparty.ThirdPartyHistoryRepo;
import in.co.mpwin.rebilling.repositories.thirdparty.ThirdPartyRepo;
import in.co.mpwin.rebilling.services.developermaster.DeveloperMasterService;
import in.co.mpwin.rebilling.services.feedermaster.FeederMasterService;
import in.co.mpwin.rebilling.services.investormaster.InvestorMasterService;
import in.co.mpwin.rebilling.services.machinemaster.MachineMasterService;
import in.co.mpwin.rebilling.services.plantmaster.PlantMasterService;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class ThirdPartyService {
    @Autowired private ThirdPartyRepo thirdPartyRepo;

    @Autowired private ThirdPartyHistoryRepo thirdPartyHistoryRepo;
    @Autowired private MeterFeederPlantMappingRepo meterFeederPlantMappingRepo;
    @Autowired private DeveloperMasterService developerMasterService;
    @Autowired private PlantMasterService plantMasterService;
    @Autowired private FeederMasterService feederMasterService;
    @Autowired private MachineMasterService machineMasterService;
    @Autowired private InvestorMasterService investorMasterService;


    public ThirdPartyService() {
    }

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
            List<ThirdPartyBean> tpLists = thirdPartyRepo.findAll();
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

    //find mfp mapping by developer id and plant id order by id desc limit 1 to get only last active mapping
    public DeveloperPlantDto getDeveloperPlantDto(String developerId, String plantCode) {

        try {

            MeterFeederPlantMappingBean mfpBean = meterFeederPlantMappingRepo.findByDeveloperIdAndPlantIdAndStatus(developerId, plantCode, "active");
            if(mfpBean==null)
                throw new ApiException(HttpStatus.BAD_REQUEST,"meter_feeder_plant mapping not found for this developer id:"+developerId+" and plant code:"+plantCode);
            DeveloperMasterBean developerMasterBean = developerMasterService.getDeveloperById(Long.valueOf(developerId),"active");
            PlantMasterBean plantMasterBean = plantMasterService.getPlantByPlantCode(plantCode,"active");
            if(developerMasterBean==null || plantMasterBean==null)
                throw new ApiException(HttpStatus.BAD_REQUEST,"developer id or plant code not found in master data.");

            FeederMasterBean feederMasterBean = feederMasterService.getFeederByFeederNumber(mfpBean.getFeederCode(),"active");
            if(feederMasterBean==null)
                throw new ApiException(HttpStatus.BAD_REQUEST,"feeder detail not found in master for feeder number:"+mfpBean.getFeederCode());


            DeveloperPlantDto dpDto = new DeveloperPlantDto();
            dpDto.setDeveloperId(String.valueOf(developerMasterBean.getId()));
            dpDto.setDeveloperName(developerMasterBean.getDeveloperName());
            dpDto.setDeveloperType(developerMasterBean.getGeneratorType());
            dpDto.setPlantCode(plantMasterBean.getPlantCode());
            dpDto.setPlantName(plantMasterBean.getPlantName());
            dpDto.setPlantCircle(plantMasterBean.getLocationMaster().getCircleName());
            dpDto.setPlantRegion(plantMasterBean.getLocationMaster().getRegionName());
            dpDto.setPlantDivision(plantMasterBean.getLocationMaster().getDivisionName());
            dpDto.setPlantCommissionDate(String.valueOf(plantMasterBean.getCommissionedDate()));
            dpDto.setMfpId(String.valueOf(mfpBean.getId()));
            dpDto.setMainMeterNo(mfpBean.getMainMeterNo());
            dpDto.setCheckMeterNo(mfpBean.getCheckMeterNo());
            dpDto.setStandbyMeterNo(mfpBean.getStandbyMeterNo());
            dpDto.setFeederNumber(feederMasterBean.getFeederNumber());
            dpDto.setFeederCircuitVoltage(feederMasterBean.getCircuitVoltage());
            dpDto.setFeederInjectingSubstationName(feederMasterBean.getInjectingSsName());
            dpDto.setSiteLocation(plantMasterBean.getAddress());

            return dpDto;
        }catch (ApiException apiException){
            throw apiException;
        }catch (DataIntegrityViolationException d){
            throw d;
        }catch (Exception e) {
            throw e;
        }
    }


    public List<ThirdPartyBean> getThirdPartyByConsumerCode(String consumerCode, String status) {
        try {
            List<ThirdPartyBean> tpLists = thirdPartyRepo.findByConsumerCodeAndStatus(consumerCode,"active");
            if (tpLists.isEmpty())
                throw new ApiException(HttpStatus.BAD_REQUEST, "No record found in third party table for consumer code:"+consumerCode);
            return tpLists;
        } catch (ApiException apiException) {
            throw apiException;
        } catch (DataIntegrityViolationException d) {
            throw d;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<ThirdPartyBean> getThirdPartiesByInvestorId(String investorCode, String status) {

        try {
            List<ThirdPartyBean> tpLists = thirdPartyRepo.findByInvestorCodeAndStatus(investorCode,status);
            if (tpLists.isEmpty())
                throw new ApiException(HttpStatus.BAD_REQUEST, "No record found in third party table for investor id:"+investorCode);
            return tpLists;
        } catch (ApiException apiException) {
            throw apiException;
        } catch (DataIntegrityViolationException d) {
            throw d;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<ThirdPartyBean> getThirdPartiesByPlantCode(String plantCode, String status) {
        try {
            List<ThirdPartyBean> tpLists = thirdPartyRepo.findByPlantCodeAndStatus(plantCode,status);
            if (tpLists.isEmpty())
                throw new ApiException(HttpStatus.BAD_REQUEST, "No record found in third party table for plant code:"+plantCode);
            return tpLists;
        } catch (ApiException apiException) {
            throw apiException;
        } catch (DataIntegrityViolationException d) {
            throw d;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
// get all investor from master where investor mapped with developer id and plant code in meter_feeder_plant table to investor_machine table
    public List<InvestorMasterBean> getInvestorsByDeveloperIdAndPlantCode(String developerId, String plantCode) {

        try {
            List<InvestorMasterBean> investorLists = investorMasterService.getAllInvestorByDeveloperIdAndPlantCode(developerId, plantCode, "active");
            return investorLists;
        } catch (ApiException apiException) {
            throw apiException;
        } catch (DataIntegrityViolationException d) {
            throw d;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Transactional
    public ThirdPartyBean setThirdPartyInactive(ThirdPartyBean thirdPartyBean) {
        try {
            //set Third Party History Before Updating ThirdParty with remark "active to inactive";
            ThirdPartyBeanHistory historyBean = mapToHistory(thirdPartyBean);
            historyBean.setHremark("status active to inactive");
            historyBean.setHcreatedBy(new TokenInfo().getCurrentUsername());
            historyBean.setHcreatedOn(new DateMethods().getServerTime());
            ThirdPartyBeanHistory historyRespBean = thirdPartyHistoryRepo.save(historyBean);

            // now update the third party status from active to inactive
            thirdPartyBean.setStatus("inactive");
            thirdPartyBean.setUpdatedBy(new TokenInfo().getCurrentUsername());
            thirdPartyBean.setUpdatedOn(new DateMethods().getServerTime());
            ThirdPartyBean result = thirdPartyRepo.save(thirdPartyBean);
            if (result!= null)
                return result;
            else
                throw new ApiException(HttpStatus.BAD_REQUEST, "unable to inactive this third party "
                        + thirdPartyBean.getConsumerCode() + "-" + thirdPartyBean.getConsumerName() + " due to some error.");
        } catch (ApiException apiException) {
            throw apiException;
        } catch (DataIntegrityViolationException d) {
            throw d;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public ThirdPartyBean updateThirdParty(ThirdPartyBean thirdPartyBean) {
        try
        {
            //set Third Party History Before Updating ThirdParty with remark "third party data update";
            ThirdPartyBean preUpdateThirdPartyBean = thirdPartyRepo.findById(thirdPartyBean.getId()).orElse(null);
            if(preUpdateThirdPartyBean==null)
                throw new ApiException(HttpStatus.BAD_REQUEST,"third party id:"+thirdPartyBean.getId()+" not found for consumer code: "+thirdPartyBean.getConsumerCode());

            ThirdPartyBeanHistory historyBean = mapToHistory(preUpdateThirdPartyBean);
            historyBean.setHremark("third party data update");
            historyBean.setHcreatedBy(new TokenInfo().getCurrentUsername());
            historyBean.setHcreatedOn(new DateMethods().getServerTime());
            ThirdPartyBeanHistory historyRespBean = thirdPartyHistoryRepo.save(historyBean);

            // now update the third party data with update by and on.

            thirdPartyBean.setUpdatedBy(new TokenInfo().getCurrentUsername());
            thirdPartyBean.setUpdatedOn(new DateMethods().getServerTime());
            ThirdPartyBean result = thirdPartyRepo.save(thirdPartyBean);
            if(result==null)
                throw new ApiException(HttpStatus.BAD_REQUEST,"unable to update third party "
                        +thirdPartyBean.getConsumerCode()+"-"+thirdPartyBean.getConsumerName()+" due to some error.");

            return result;
        }catch (ApiException apiException) {
            throw apiException;
        } catch (DataIntegrityViolationException d) {
            throw d;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<ThirdPartyBean> getThirdPartyBeansByStatus(String status) {
        try {
            List<ThirdPartyBean> tpLists = thirdPartyRepo.findAllByStatus(status);
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

    @Transactional
    public ThirdPartyBean setThirdPartyActive(ThirdPartyBean thirdPartyBean) {

        try {

            //set Third Party History Before Updating ThirdParty with remark "inactive to active";
            ThirdPartyBeanHistory historyBean = mapToHistory(thirdPartyBean);
            historyBean.setHremark("status inactive to active");
            historyBean.setHcreatedBy(new TokenInfo().getCurrentUsername());
            historyBean.setHcreatedOn(new DateMethods().getServerTime());
            ThirdPartyBeanHistory historyRespBean = thirdPartyHistoryRepo.save(historyBean);

            // now update the third party status from inactive to active
            thirdPartyBean.setStatus("active");
            thirdPartyBean.setUpdatedBy(new TokenInfo().getCurrentUsername());
            thirdPartyBean.setUpdatedOn(new DateMethods().getServerTime());
            ThirdPartyBean result = thirdPartyRepo.save(thirdPartyBean);
            if (result!= null)
                return result;
            else
                throw new ApiException(HttpStatus.BAD_REQUEST, "unable to active third party "
                        + thirdPartyBean.getConsumerCode() + "-" + thirdPartyBean.getConsumerName() + " due to some error.");
        } catch (ApiException apiException) {
            throw apiException;
        } catch (DataIntegrityViolationException d) {
            throw d;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ThirdPartyBeanHistory mapToHistory(ThirdPartyBean thirdPartyBean) {
        //Setup of type map because destination have extra property
        ModelMapper modelMapper = new ModelMapper();
        ThirdPartyBeanHistory historyBean = modelMapper.map(thirdPartyBean, ThirdPartyBeanHistory.class);
        return historyBean;
    }
}

