package in.co.mpwin.rebilling.services.mapping;

import in.co.mpwin.rebilling.beans.investormaster.InvestorMasterBean;
import in.co.mpwin.rebilling.beans.machinemaster.MachineMasterBean;
import in.co.mpwin.rebilling.beans.mapping.InvestorMachineMappingBean;
import in.co.mpwin.rebilling.beans.mapping.MeterFeederPlantMappingBean;
import in.co.mpwin.rebilling.dto.InvestorMachineMappingDto;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.AuditControlServices;
import in.co.mpwin.rebilling.miscellanious.ValidatorService;
import in.co.mpwin.rebilling.repositories.mapping.InvestorMachineMappingRepo;
import in.co.mpwin.rebilling.repositories.mapping.MeterFeederPlantMappingRepo;
import in.co.mpwin.rebilling.services.investormaster.InvestorMasterService;
import in.co.mpwin.rebilling.services.machinemaster.MachineMasterService;
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

    @Autowired private InvestorMachineMappingRepo investorMachineMappingRepo;

    @Autowired private InvestorMasterService investorMasterService;

    @Autowired private MachineMasterService machineMasterService;

    @Autowired private MeterFeederPlantMappingRepo meterFeederPlantMappingRepo;



    @Transactional
    public String createMapping(InvestorMachineMappingDto investorMachineMappingDto) {
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
            return "investor machine mapping has been created successfully.";
        } catch (ApiException apiException) {
            throw apiException;
        } catch (DataIntegrityViolationException d) {
            throw d;
        } catch (Exception e) {
            throw e;
        }
    }

    public List<InvestorMachineMappingBean> getAllMapping(String status) {
        List<InvestorMachineMappingBean> allMappingList = new ArrayList<>();
        try {
            allMappingList= (List<InvestorMachineMappingBean>) investorMachineMappingRepo.findByStatus(status);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allMappingList;

    }

    public InvestorMachineMappingBean getMappingById(Long id, String status) {

        InvestorMachineMappingBean mappingBean = new InvestorMachineMappingBean();
        try{
            mappingBean= investorMachineMappingRepo.findByIdAndStatus(id,status);
        }catch (Exception e){
           throw e;
        }
        return mappingBean;
    }

    public List<InvestorMachineMappingBean> getMappingByInvestorCode(String investorCode, String status) {
        List<InvestorMachineMappingBean> mappingBean = new ArrayList<>();
        try{
            mappingBean= investorMachineMappingRepo.findByInvestorCodeAndStatus(investorCode,status);
        }catch (Exception e){
            throw e;
        }
        return mappingBean;
    }

    public List<InvestorMachineMappingBean> getMappingByMFPId(Long mfpId, String status) {

        List<InvestorMachineMappingBean> mappingBean = new ArrayList<>();
        try{
            mappingBean= investorMachineMappingRepo.findByMfpIdAndStatus(mfpId,status);
        }catch (Exception e){
            throw e;
        }
        return mappingBean;
    }

    public List<InvestorMachineMappingBean> getMappingByMachineCode(String machineCode, String status) {
        List<InvestorMachineMappingBean> mappingBean = new ArrayList<>();
        try{
            mappingBean= investorMachineMappingRepo.findByMachineCodeAndStatus(machineCode,status);
        }catch (Exception e){
            throw e;
        }
        return mappingBean;
    }
}
