package in.co.mpwin.rebilling.services.machinemaster;

import in.co.mpwin.rebilling.beans.machinemaster.MachineMasterBean;
import in.co.mpwin.rebilling.miscellanious.AuditControlServices;
import in.co.mpwin.rebilling.repositories.machinemaster.MachineMasterRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MachineMasterService {

    @Autowired
    MachineMasterRepo machineMasterRepo;

    public List<MachineMasterBean> getAllMachineMasterBean(String status){
        List<MachineMasterBean> allMachineList = new ArrayList<>();
        try {
            allMachineList= machineMasterRepo.findAllByStatus(status);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allMachineList;
    }

    public MachineMasterBean createMachineMaster(MachineMasterBean machineMasterBean) {
        //int result = -1;
        MachineMasterBean mmb = new MachineMasterBean();
        try {
            //check for existence of machine if already created with same machine code
            MachineMasterBean temp = machineMasterRepo.findByMachineCodeAndStatus(machineMasterBean.getMachineCode(),"active");
            if(temp!=null) {
                return null;
            }
            //Set the Audit control parameters, Globally
            new AuditControlServices().setInitialAuditControlParameters(machineMasterBean);

            //get max sequence id and set  id as id+1
            //machineMasterBean.setId(getMaxSequence()+1);
            // set machine code using id
            machineMasterBean.setMachineCode("M" + (getMaxSequence()+1));

           mmb = machineMasterRepo.save(machineMasterBean);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return mmb;
    }

    public MachineMasterBean getMachineByMachineCode(String machineCode, String status){
        MachineMasterBean machineMasterBean = new MachineMasterBean();
        try{
            machineMasterBean = machineMasterRepo.findByMachineCodeAndStatus(machineCode,status);
        }catch (Exception e){
            e.printStackTrace();
        }
        return machineMasterBean;
    }

    public MachineMasterBean getMachineById(Long id, String status){
        MachineMasterBean machineMasterBean = new MachineMasterBean();
        try{
            machineMasterBean = machineMasterRepo.findByIdAndStatus(id,status);
        }catch (Exception e){
            e.printStackTrace();
        }
        return machineMasterBean;
    }

    public List<MachineMasterBean> getAllMachineByLocationId(String locationId, String status) {
        List<MachineMasterBean> allMachineList = new ArrayList<>();
        try {
            allMachineList = machineMasterRepo.findByLocationIdAndStatus(locationId, status);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allMachineList;
    }

    public Long getMaxSequence()
    {
        return machineMasterRepo.getMaxSequence();

    }

    public List<MachineMasterBean> getAllMachineByMachineCodeList(List<String> machineCodeList, String status) {
        List<MachineMasterBean> allMachineList = new ArrayList<>();
        try {
            allMachineList = machineMasterRepo.findAllMachineByMachineCodeList(machineCodeList, status);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allMachineList;
    }

}
