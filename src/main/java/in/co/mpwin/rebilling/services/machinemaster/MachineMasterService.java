package in.co.mpwin.rebilling.services.machinemaster;

import in.co.mpwin.rebilling.beans.machinemaster.MachineMasterBean;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.AuditControlServices;
import in.co.mpwin.rebilling.repositories.machinemaster.MachineMasterRepo;
import in.co.mpwin.rebilling.repositories.mapping.InvestorMachineMappingRepo;
import in.co.mpwin.rebilling.services.locationmaster.LocationMasterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MachineMasterService {
    private static final Logger logger = LoggerFactory.getLogger(MachineMasterService.class);
    @Autowired
    MachineMasterRepo machineMasterRepo;

    @Autowired private InvestorMachineMappingRepo investorMachineMappingRepo;

    public List<MachineMasterBean> getAllMachineMasterBean(String status){
        final String methodName = "getAllMachineMasterBean() : ";
        logger.info(methodName + "called with parameters status={}",status);
        List<MachineMasterBean> allMachineList = new ArrayList<>();
        try {
            allMachineList= machineMasterRepo.findAllByStatus(status);
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
        logger.info(methodName + " return with MachineMasterBean list of size : {}",allMachineList.size());
        return allMachineList;
    }

    public MachineMasterBean createMachineMaster(MachineMasterBean machineMasterBean) {
        final String methodName = "createMachineMaster() : ";
        logger.info(methodName + "called with parameters machineMasterBean={}",machineMasterBean);
        //int result = -1;
        MachineMasterBean mmb = new MachineMasterBean();
        try {
            //check for existence of machine if already created with same machine name then return api exception msg
            //findByFirstNameIgnoreCase jpa
            MachineMasterBean temp = machineMasterRepo.findByMachineNameIgnoreCaseAndStatus(machineMasterBean.getMachineName(),"active");
            if(temp!=null) {
                throw new ApiException(HttpStatus.BAD_REQUEST,"Machine with same name is already exist with machine code: "+temp.getMachineCode()+" and machine name: "+temp.getMachineName());
            }
            //Set the Audit control parameters, Globally
            new AuditControlServices().setInitialAuditControlParameters(machineMasterBean);

            //get max machine code and set new code as code +1
            machineMasterBean.setMachineCode("MNC"+String.format("%04d",getMaxMachineCode()+1));
            mmb = machineMasterRepo.save(machineMasterBean);
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
        logger.info(methodName + " return with MachineMasterBean : {}",mmb);
        return mmb;
    }

    public MachineMasterBean getMachineByMachineCode(String machineCode, String status){
        final String methodName = "getMachineByMachineCode() : ";
        logger.info(methodName + "called with parameters machineCode={}, status={}",machineCode,status);
        MachineMasterBean machineMasterBean = new MachineMasterBean();
        try{
            machineMasterBean = machineMasterRepo.findByMachineCodeAndStatus(machineCode,status);
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
        logger.info(methodName + " return with MachineMasterBean : {}",machineMasterBean);
        return machineMasterBean;
    }

    public MachineMasterBean getMachineById(Long id, String status){
        final String methodName = "getMachineById() : ";
        logger.info(methodName + "called with parameters id={}, status={}",id,status);
        MachineMasterBean machineMasterBean = new MachineMasterBean();
        try{
            machineMasterBean = machineMasterRepo.findByIdAndStatus(id,status);
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
        logger.info(methodName + " return with MachineMasterBean : {}",machineMasterBean);
        return machineMasterBean;
    }

    public List<MachineMasterBean> getAllMachineByLocationId(String locationId, String status) {
        final String methodName = "getAllMachineByLocationId() : ";
        logger.info(methodName + "called with parameters locationId={}, status={}",locationId,status);
        List<MachineMasterBean> allMachineList = new ArrayList<>();
        try {
            allMachineList = machineMasterRepo.findByLocationIdAndStatus(locationId, status);
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
        logger.info(methodName + " return with MachineMasterBean list of size : {}",allMachineList.size());
        return allMachineList;
    }

    public Integer getMaxMachineCode()
    {
        final String methodName = "getMaxMachineCode() : ";
        logger.info(methodName + "called with parameters empty");
        Integer max = machineMasterRepo.findMaxMachineCode();
        if(max==null)
            max=0;
        logger.info(methodName + " return with max number : {}",max);
        return max;
    }

    public List<MachineMasterBean> getAllMachineByMachineCodeList(List<String> machineCodeList, String status) {
        final String methodName = "getAllMachineByMachineCodeList() : ";
        logger.info(methodName + "called with parameters machineCodeList size={}, status={}",machineCodeList.size(),status);
        List<MachineMasterBean> allMachineList = new ArrayList<>();
        try {
            allMachineList = machineMasterRepo.findAllMachineByMachineCodeList(machineCodeList, status);
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
        logger.info(methodName + " return with MachineMasterBean list of size : {}",allMachineList.size());
        return allMachineList;
    }



    public List<MachineMasterBean> getUnmappedMachineBeans() {
        final String methodName = "getUnmappedMachineBeans() : ";
        logger.info(methodName + "called with parameters empty");
        List<MachineMasterBean> unmappedMachineBeans;
        try{
            List<String> mappedMachineCode = investorMachineMappingRepo.findMachineCodesByStatus("active");
            if(mappedMachineCode.isEmpty())
                unmappedMachineBeans = machineMasterRepo.findAllByStatus("active");
            else
                unmappedMachineBeans = machineMasterRepo.findAllMachineExcludingMappedMachineCodeList(mappedMachineCode,"active");
            if(unmappedMachineBeans.isEmpty())
                throw new ApiException(HttpStatus.BAD_REQUEST,"unmapped machine list are not found in machine master.");
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
        logger.info(methodName + " return with MachineMasterBean list of size : {}",unmappedMachineBeans.size());
        return unmappedMachineBeans;
    }



}
