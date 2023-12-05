package in.co.mpwin.rebilling.services.mapping;

import in.co.mpwin.rebilling.beans.investormaster.InvestorMasterBean;
import in.co.mpwin.rebilling.beans.machinemaster.MachineMasterBean;
import in.co.mpwin.rebilling.beans.mapping.InvestorMachineMappingBean;
import in.co.mpwin.rebilling.beans.mapping.MeterFeederPlantMappingBean;
import in.co.mpwin.rebilling.controller.readingcontroller.MeterReadingPunchingController;
import in.co.mpwin.rebilling.dto.InvestorMachineMappingDto;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.AuditControlServices;
import in.co.mpwin.rebilling.miscellanious.ValidatorService;
import in.co.mpwin.rebilling.repositories.mapping.InvestorMachineMappingRepo;
import in.co.mpwin.rebilling.repositories.mapping.MeterFeederPlantMappingRepo;
import in.co.mpwin.rebilling.services.investormaster.InvestorMasterService;
import in.co.mpwin.rebilling.services.machinemaster.MachineMasterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Service
public class InvestorMachineMappingService {

    private static final Logger logger = LoggerFactory.getLogger(InvestorMachineMappingService.class);

    @Autowired private InvestorMachineMappingRepo investorMachineMappingRepo;

    @Autowired private InvestorMasterService investorMasterService;

    @Autowired private MachineMasterService machineMasterService;

    @Autowired private MeterFeederPlantMappingRepo meterFeederPlantMappingRepo;



    @Transactional
    public String createMapping(InvestorMachineMappingDto investorMachineMappingDto) {
        final String methodName = "createMapping() : ";
        logger.info(methodName + "called with parameters investorMachineMappingDto={}",investorMachineMappingDto);
               try {
                List<String> machineCodes = new ArrayList<>();
                LocalDate endDate = LocalDate.now();
                MeterFeederPlantMappingBean mfpBean =meterFeederPlantMappingRepo.findByDeveloperIdAndPlantIdAndEndDateAndStatus(String.valueOf(investorMachineMappingDto.getDeveloperMasterBean().getId()),
                        investorMachineMappingDto.getPlantMasterBean().getPlantCode(), endDate,"active");
                       if(mfpBean==null)
                           throw new ApiException(HttpStatus.BAD_REQUEST,"mfp mapping not found for developer id "+investorMachineMappingDto.getDeveloperMasterBean().getId()+" and plant code "+investorMachineMappingDto.getPlantMasterBean().getPlantCode());
                InvestorMasterBean investorBean = investorMasterService.getInvestorById(investorMachineMappingDto.getInvestorMasterBean().getId(), "active");
                investorMachineMappingDto.getMachineMasterBeanList().forEach(i -> machineCodes.add(i.getMachineCode()));
                List<MachineMasterBean> machineMasterBeans = machineMasterService.getAllMachineByMachineCodeList(machineCodes, "active");
                int machineMasterCount = machineMasterBeans.size();
                int frontEndMachineCount = investorMachineMappingDto.getMachineMasterBeanList().size();
                if (frontEndMachineCount != machineMasterCount)
                    throw new ApiException(HttpStatus.BAD_REQUEST, "All the selected machines are not present in machine master.");
                for (MachineMasterBean machine : machineMasterBeans) {
                    InvestorMachineMappingBean newBean = new InvestorMachineMappingBean();
                    newBean.setMfpId(mfpBean.getId());
                    newBean.setInvestorCode(investorBean.getInvestorCode());
                    newBean.setMachineCode(machine.getMachineCode());
                    new AuditControlServices().setInitialAuditControlParameters(newBean);
                    investorMachineMappingRepo.save(newBean);
                }
                logger.info(methodName + " return with message= investor machine mapping has been created successfully.");
                return "investor machine mapping has been created successfully.";
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

    public List<InvestorMachineMappingBean> getAllMapping(String status) {
        final String methodName = "getAllMapping() : ";
        logger.info(methodName + "called with parameters status={}",status);
        List<InvestorMachineMappingBean> allMappingList = new ArrayList<>();
        try {
            allMappingList= (List<InvestorMachineMappingBean>) investorMachineMappingRepo.findByStatus(status);
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
        logger.info(methodName + " return with InvestorMachineMappingBean list of size : {}",allMappingList.size());
        return allMappingList;

    }

    public InvestorMachineMappingBean getMappingById(Long id, String status) {
        final String methodName = "getMappingById() : ";
        logger.info(methodName + "called with parameters id={}, status={}",id,status);
        InvestorMachineMappingBean mappingBean = new InvestorMachineMappingBean();
        try{
            mappingBean= investorMachineMappingRepo.findByIdAndStatus(id,status);
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
        logger.info(methodName + " return with InvestorMachineMappingBean : {}",mappingBean);
        return mappingBean;
    }

    public List<InvestorMachineMappingBean> getMappingByInvestorCode(String investorCode, String status) {
        final String methodName = " getMappingByInvestorCode() : ";
        logger.info(methodName + "called with parameters  investorCode={}, status={}", investorCode,status);
        List<InvestorMachineMappingBean> mappingBeans = new ArrayList<>();
        try{
            mappingBeans= investorMachineMappingRepo.findByInvestorCodeAndStatus(investorCode,status);
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
        logger.info(methodName + " return with InvestorMachineMappingBean list of size : {}",mappingBeans.size());
        return mappingBeans;
    }

    public List<InvestorMachineMappingBean> getMappingByMFPId(Long mfpId, String status) {
        final String methodName = " getMappingByMFPId() : ";
        logger.info(methodName + "called with parameters  mfpId={}, status={}", mfpId,status);
        List<InvestorMachineMappingBean> mappingBeans = new ArrayList<>();
        try{
            mappingBeans= investorMachineMappingRepo.findByMfpIdAndStatus(mfpId,status);
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
        logger.info(methodName + " return with InvestorMachineMappingBean list of size : {}",mappingBeans.size());
        return mappingBeans;
    }

    public List<InvestorMachineMappingBean> getMappingByMachineCode(String machineCode, String status) {
        final String methodName = " getMappingByMachineCode() : ";
        logger.info(methodName + "called with parameters machineCode={}, status={}", machineCode,status);
        List<InvestorMachineMappingBean> mappingBeans = new ArrayList<>();
        try{
            mappingBeans= investorMachineMappingRepo.findByMachineCodeAndStatus(machineCode,status);
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
        logger.info(methodName + " return with InvestorMachineMappingBean list of size : {}",mappingBeans.size());
        return mappingBeans;
    }
}
