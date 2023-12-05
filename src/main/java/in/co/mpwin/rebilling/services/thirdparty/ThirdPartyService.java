package in.co.mpwin.rebilling.services.thirdparty;

import in.co.mpwin.rebilling.beans.developermaster.DeveloperMasterBean;
import in.co.mpwin.rebilling.beans.feedermaster.FeederMasterBean;
import in.co.mpwin.rebilling.beans.investormaster.InvestorMasterBean;
import in.co.mpwin.rebilling.beans.mapping.MeterFeederPlantMappingBean;
import in.co.mpwin.rebilling.beans.plantmaster.PlantMasterBean;
import in.co.mpwin.rebilling.beans.thirdparty.DeveloperPlantDto;
import in.co.mpwin.rebilling.beans.thirdparty.ThirdPartyBean;
import in.co.mpwin.rebilling.beans.thirdparty.ThirdPartyBeanHistory;
import in.co.mpwin.rebilling.controller.plantmaster.PlantMasterController;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.AuditControlServices;
import in.co.mpwin.rebilling.miscellanious.DateMethods;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ThirdPartyService {
    private static final Logger logger = LoggerFactory.getLogger(ThirdPartyService.class);
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
        final String methodName = "saveThirdPartyBean() : ";
        logger.info(methodName + "called with parameters tpBean={}",tpBean);
        try {
            new AuditControlServices().setInitialAuditControlParameters(tpBean);
            ThirdPartyBean savedBean =  thirdPartyRepo.save(tpBean);
            if(savedBean==null)
                throw new ApiException(HttpStatus.BAD_REQUEST,"Third Party Data could not saved due to some error");
            logger.info(methodName + " return with parameter saveBean : {}",savedBean);
            return savedBean;
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
    }

    public List<ThirdPartyBean> getThirdPartyBeans() {
        final String methodName = "getThirdPartyBeans() : ";
        logger.info(methodName + "called with parameters empty");
        try {
            List<ThirdPartyBean> tpLists = thirdPartyRepo.findAll();
            if (tpLists.isEmpty())
                throw new ApiException(HttpStatus.BAD_REQUEST, "No content found for third party.");
            logger.info(methodName + " return with ThirdPartyBean list of size : {}",tpLists.size());
            return tpLists;
        } catch (ApiException apiException){
            logger.error(methodName+" throw apiException");
            throw apiException;
        }catch (DataIntegrityViolationException d){
            logger.error(methodName+" throw DataIntegrityViolationException");
            throw d;
        } catch (Exception e) {
            logger.error(methodName+" throw Exception");
            throw e;
        }
    }


    public List<ThirdPartyBean> getThirdPartiesByInvestorCode(String investorCode) {
        final String methodName = "getThirdPartiesByInvestorCode() : ";
        logger.info(methodName + "called with parameters investorCode={}",investorCode);
        List<ThirdPartyBean> tpLists = new ArrayList<>();
        try {
                tpLists = thirdPartyRepo.findAllByInvestorCodeAndStatus(investorCode, "active");
                if (tpLists.isEmpty())
                    throw new ApiException(HttpStatus.BAD_REQUEST, "No third party for investor." + investorCode);
        }catch (ApiException apiException){
            logger.error(methodName+" throw apiException :{}",apiException.getMessage());
            ThirdPartyBean thirdPartyBean = new ThirdPartyBean();
            thirdPartyBean.setConsumerCode("  ");
            thirdPartyBean.setConsumerName("   ");
            thirdPartyBean.setAdjustmentUnitPercent(BigDecimal.valueOf(100));
            tpLists.add(thirdPartyBean);
            //throw apiException;
        }catch (DataIntegrityViolationException d){
            logger.error(methodName+" throw DataIntegrityViolationException");
            throw d;
        }catch (Exception e) {
            logger.error(methodName+" throw Exception");
            throw e;
        }
        logger.info(methodName + " return with ThirdPartyBean list of size : {}",tpLists.size());
        return tpLists;
    }
    //find mfp mapping by developer id and plant id order by id desc limit 1 to get only last active mapping
    public DeveloperPlantDto getDeveloperPlantDto(String developerId, String plantCode) {
        final String methodName = "getDeveloperPlantDto() : ";
        logger.info(methodName + "called with parameters developerId={}, plantCode={}",developerId,plantCode);
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
            logger.info(methodName + " return with getDeveloperPlantDto : {}",dpDto);
            return dpDto;
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
    }


    public List<ThirdPartyBean> getThirdPartyByConsumerCode(String consumerCode, String status) {
        final String methodName = "getThirdPartyByConsumerCode() : ";
        logger.info(methodName + "called with parameters consumerCode={}, status={}",consumerCode,status);
        try {
            List<ThirdPartyBean> tpLists = thirdPartyRepo.findByConsumerCodeAndStatus(consumerCode,status);
            if (tpLists.isEmpty())
                throw new ApiException(HttpStatus.BAD_REQUEST, "No record found in third party table for consumer code:"+consumerCode);
            logger.info(methodName + " return with ThirdPartyBean list of size : {}",tpLists.size());
            return tpLists;
            } catch (ApiException apiException){
                logger.error(methodName+"throw apiException");
                throw apiException;
            }catch (DataIntegrityViolationException d){
                logger.error(methodName+" throw DataIntegrityViolationException");
                throw d;
            } catch (Exception e) {
                logger.error(methodName+" throw Exception");
                throw e;
            }
    }

    public List<ThirdPartyBean> getThirdPartiesByInvestorId(String investorCode, String status) {
        final String methodName = "getThirdPartiesByInvestorId() : ";
        logger.info(methodName + "called with parameters investorCode={}, status={}",investorCode,status);
        try {
            List<ThirdPartyBean> tpLists = thirdPartyRepo.findByInvestorCodeAndStatus(investorCode,status);
            if (tpLists.isEmpty())
                throw new ApiException(HttpStatus.BAD_REQUEST, "No record found in third party table for investor id:"+investorCode);
            logger.info(methodName + " return with ThirdPartyBean list of size : {}",tpLists.size());
            return tpLists;
            } catch (ApiException apiException){
                logger.error(methodName+"throw apiException");
                throw apiException;
            }catch (DataIntegrityViolationException d){
                logger.error(methodName+" throw DataIntegrityViolationException");
                throw d;
            } catch (Exception e) {
                logger.error(methodName+" throw Exception");
                throw e;
            }
    }



    public List<ThirdPartyBean> getThirdPartiesByPlantCode(String plantCode, String status) {
        final String methodName = "getThirdPartiesByPlantCode() : ";
        logger.info(methodName + "called with parameters plantCode={}, status={}",plantCode,status);
        try {
            List<ThirdPartyBean> tpLists = thirdPartyRepo.findByPlantCodeAndStatus(plantCode,status);
            if (tpLists.isEmpty())
                throw new ApiException(HttpStatus.BAD_REQUEST, "No record found in third party table for plant code:"+plantCode);
            logger.info(methodName + " return with ThirdPartyBean list of size : {}",tpLists.size());
            return tpLists;
            } catch (ApiException apiException){
                logger.error(methodName+" throw apiException");
                throw apiException;
            }catch (DataIntegrityViolationException d){
                logger.error(methodName+" throw DataIntegrityViolationException");
                throw d;
            } catch (Exception e) {
                logger.error(methodName+" throw Exception");
                throw e;
            }
    }
// get all investor from master where investor mapped with developer id and plant code in meter_feeder_plant table to investor_machine table
    public List<InvestorMasterBean> getInvestorsByDeveloperIdAndPlantCode(String developerId, String plantCode) {
        final String methodName = "getInvestorsByDeveloperIdAndPlantCode() : ";
        logger.info(methodName + "called with parameters developerId={}, plantCode={}",plantCode,plantCode);
        try {
            List<InvestorMasterBean> investorLists = investorMasterService.getAllInvestorByDeveloperIdAndPlantCode(developerId, plantCode, "active");
            logger.info(methodName + " return with InvestorMasterBean list of size : {}",investorLists.size());
            return investorLists;
            } catch (ApiException apiException){
                logger.error(methodName+" throw apiException");
                throw apiException;
            }catch (DataIntegrityViolationException d){
                logger.error(methodName+" throw DataIntegrityViolationException");
                throw d;
            } catch (Exception e) {
                logger.error(methodName+" throw Exception");
                throw e;
            }

    }


    @Transactional
    public ThirdPartyBean setThirdPartyInactive(ThirdPartyBean thirdPartyBean) {
        final String methodName = "setThirdPartyInactive() : ";
        logger.info(methodName + "called with parameters thirdPartyBean={}",thirdPartyBean);
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
            if (result!= null) {
                logger.info(methodName + " return with parameter : {}", result);
                return result;
            }
                else
                throw new ApiException(HttpStatus.BAD_REQUEST, "unable to inactive this third party "
                        + thirdPartyBean.getConsumerCode() + "-" + thirdPartyBean.getConsumerName() + " due to some error.");
            } catch (ApiException apiException){
                logger.error(methodName+" throw apiException");
                throw apiException;
            }catch (DataIntegrityViolationException d){
                logger.error(methodName+" throw DataIntegrityViolationException");
                throw d;
            } catch (Exception e) {
                logger.error(methodName+" throw Exception");
                throw e;
            }
    }

    @Transactional
    public ThirdPartyBean updateThirdParty(ThirdPartyBean thirdPartyBean) {
        final String methodName = "updateThirdParty() : ";
        logger.info(methodName + "called with parameters thirdPartyBean={}",thirdPartyBean);
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
            logger.info(methodName + " return with parameter : {}", result);
            return result;
        } catch (ApiException apiException){
            logger.error(methodName+" throw apiException");
            throw apiException;
        }catch (DataIntegrityViolationException d){
            logger.error(methodName+" throw DataIntegrityViolationException");
            throw d;
        } catch (Exception e) {
            logger.error(methodName+" throw Exception");
            throw e;
        }
    }

    public List<ThirdPartyBean> getThirdPartyBeansByStatus(String status) {
        final String methodName = "getThirdPartyBeansByStatus() : ";
        logger.info(methodName + "called with parameters status={}", status);
        try {
            List<ThirdPartyBean> tpLists = thirdPartyRepo.findAllByStatus(status);
            if (tpLists.isEmpty())
                throw new ApiException(HttpStatus.BAD_REQUEST, "No content found for third party.");
            logger.info(methodName + " return ThirdPartyBean list of size : {}", tpLists.size());
            return tpLists;
            }catch(ApiException apiException){
                logger.error(methodName+" throw apiException");
                throw apiException;
            }catch(DataIntegrityViolationException d){
                logger.error(methodName+" throw DataIntegrityViolationException");
                throw d;
            }catch(Exception e) {
                logger.error(methodName+" throw Exception");
                throw e;
            }

    }

    @Transactional
    public ThirdPartyBean setThirdPartyActive(ThirdPartyBean thirdPartyBean) {
        final String methodName = "setThirdPartyActive() : ";
        logger.info(methodName + "called with parameters thirdPartyBean={}", thirdPartyBean);
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
            if (result!= null) {
                logger.info(methodName + " return with parameter : {}", result);
                return result;
            }
            else
                throw new ApiException(HttpStatus.BAD_REQUEST, "unable to active third party "
                        + thirdPartyBean.getConsumerCode() + "-" + thirdPartyBean.getConsumerName() + " due to some error.");
            }catch(ApiException apiException){
                logger.error(methodName+"throw apiException");
                throw apiException;
            }catch(DataIntegrityViolationException d){
                logger.error(methodName+" throw DataIntegrityViolationException");
                throw d;
            }catch(Exception e) {
                logger.error(methodName+" throw Exception");
                throw e;
            }
    }

    private ThirdPartyBeanHistory mapToHistory(ThirdPartyBean thirdPartyBean) {
        final String methodName = "mapToHistory() : ";
        logger.info(methodName + "called with parameters thirdPartyBean={}", thirdPartyBean);
        //Setup of type map because destination have extra property
        ModelMapper modelMapper = new ModelMapper();
        ThirdPartyBeanHistory historyBean = modelMapper.map(thirdPartyBean, ThirdPartyBeanHistory.class);
        logger.info(methodName + " return with parameter historyBean : {}", historyBean);
        return historyBean;
    }

}

