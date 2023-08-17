package in.co.mpwin.rebilling.services.mapping;

import in.co.mpwin.rebilling.beans.mapping.InvestorMachineMappingBean;
import in.co.mpwin.rebilling.beans.mapping.MeterFeederPlantMappingBean;
import in.co.mpwin.rebilling.miscellanious.AuditControlServices;
import in.co.mpwin.rebilling.miscellanious.ValidatorService;
import in.co.mpwin.rebilling.repositories.mapping.InvestorMachineMappingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class InvestorMachineMappingService {

    @Autowired
    InvestorMachineMappingRepo investorMachineMappingRepo;
    public InvestorMachineMappingBean createMapping(InvestorMachineMappingBean investorMachineMappingBean) {
        System.out.println("mfp_id "+investorMachineMappingBean.getMfpId()+" investor_code = "+investorMachineMappingBean.getInvestorCode());

        InvestorMachineMappingBean imm = new InvestorMachineMappingBean();
        // remove space if any from mfp_id , investor_id and machine_id
        investorMachineMappingBean.setInvestorCode((new ValidatorService().removeSpaceFromString(investorMachineMappingBean.getInvestorCode())));
        investorMachineMappingBean.setMachineCode((new ValidatorService().removeSpaceFromString(investorMachineMappingBean.getMachineCode())));

        try {

            //check for existence of mapping if already exist with same mapping then return null
            InvestorMachineMappingBean temp = investorMachineMappingRepo.findByMFPIdInvestorIdMachineId(investorMachineMappingBean.getMfpId(),investorMachineMappingBean.getInvestorCode(),
                    investorMachineMappingBean.getMachineCode());

            if(temp!=null) {
                return null;
            }
            //Set the Audit control parameters, Globally
            new AuditControlServices().setInitialAuditControlParameters(investorMachineMappingBean);


            imm = investorMachineMappingRepo.save(investorMachineMappingBean);
        }catch (DataIntegrityViolationException ex)
        {
            throw  ex;
        }
        catch (Exception e) {
            throw e;
        }
        return imm;


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
