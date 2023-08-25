package in.co.mpwin.rebilling.services.mapping;

import in.co.mpwin.rebilling.beans.mapping.MeterFeederPlantMappingBean;
import in.co.mpwin.rebilling.miscellanious.AuditControlServices;
import in.co.mpwin.rebilling.miscellanious.ValidatorService;
import in.co.mpwin.rebilling.repositories.mapping.MeterFeederPlantMappingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MeterFeederPlantMappingService {

    @Autowired
    MeterFeederPlantMappingRepo meterFeederPlantMappingRepo;


    public MeterFeederPlantMappingBean createMapping(MeterFeederPlantMappingBean meterFeederPlantMappingBean) {
        MeterFeederPlantMappingBean mfpm = new MeterFeederPlantMappingBean();
        try {

            meterFeederPlantMappingBean.setMainMeterNo(new ValidatorService().removeSpaceFromString(meterFeederPlantMappingBean.getMainMeterNo()));
            meterFeederPlantMappingBean.setCheckMeterNo(new ValidatorService().removeSpaceFromString(meterFeederPlantMappingBean.getCheckMeterNo()));
            meterFeederPlantMappingBean.setStandbyMeterNo(new ValidatorService().removeSpaceFromString(meterFeederPlantMappingBean.getStandbyMeterNo()));
            meterFeederPlantMappingBean.setFeederCode(new ValidatorService().removeSpaceFromString(meterFeederPlantMappingBean.getFeederCode()));
            meterFeederPlantMappingBean.setPlantCode(new ValidatorService().removeSpaceFromString(meterFeederPlantMappingBean.getPlantCode()));
            meterFeederPlantMappingBean.setDeveloperId(new ValidatorService().removeSpaceFromString(meterFeederPlantMappingBean.getDeveloperId()));

            //check for existence of mapping if already exist with same mapping then return null
            MeterFeederPlantMappingBean temp = meterFeederPlantMappingRepo.findByMainMeterNoCheckMeterNoStandbyMeterNoAndDeveloperId(meterFeederPlantMappingBean.getMainMeterNo(),meterFeederPlantMappingBean.getCheckMeterNo(),
                    meterFeederPlantMappingBean.getStandbyMeterNo(),meterFeederPlantMappingBean.getDeveloperId());

            if(temp!=null) {
                return null;
            }
            //Set the Audit control parameters, Globally
            new AuditControlServices().setInitialAuditControlParameters(meterFeederPlantMappingBean);

            mfpm = meterFeederPlantMappingRepo.save(meterFeederPlantMappingBean);
        }catch (Exception e) {
            throw e;
        }
        return mfpm;
    }

    public List<MeterFeederPlantMappingBean> getAllMapping(String status) {

        List<MeterFeederPlantMappingBean> allMappingList = new ArrayList<>();
        try {
            allMappingList= (List<MeterFeederPlantMappingBean>) meterFeederPlantMappingRepo.findByStatus(status);
        } catch (Exception e) {
            throw e;
        }
        return allMappingList;
    }

    public MeterFeederPlantMappingBean getMappingById(Long id, String status) {

        MeterFeederPlantMappingBean mappingBean = new MeterFeederPlantMappingBean();
        try{
            mappingBean= meterFeederPlantMappingRepo.findByIdAndStatus(id,status);
        }catch (Exception e){
            throw e;
        }
        return mappingBean;
    }

    public List<MeterFeederPlantMappingBean> getMappingByMainMeterNo(String mmn, String status) {
        List<MeterFeederPlantMappingBean> mappingBean = new ArrayList<>();
        try{
            mappingBean= meterFeederPlantMappingRepo.findByMainMeterNoAndStatus(mmn,status);
        }catch(Exception e){
           throw e;
        }
        return mappingBean;

    }

    public List<MeterFeederPlantMappingBean> getMappingByCheckMeterNo(String mmn, String status) {
        List<MeterFeederPlantMappingBean> mappingBean = new ArrayList<>();
        try{
            mappingBean= meterFeederPlantMappingRepo.findByCheckMeterNoAndStatus(mmn,status);
        }catch (Exception e){
           throw e;
        }
        return mappingBean;

    }

    public List<MeterFeederPlantMappingBean> getMappingByStandbyMeterNo(String smn, String status) {

        List<MeterFeederPlantMappingBean> mappingBean = new ArrayList<>();
        try{
            mappingBean= meterFeederPlantMappingRepo.findByStandbyMeterNoAndStatus(smn,status);
        }catch (Exception e){
            throw e;
        }
        return mappingBean;
    }

    public List<MeterFeederPlantMappingBean> getMappingByDeveloperId(String di, String status) {
        List<MeterFeederPlantMappingBean> mappingBean = new ArrayList<>();
        try{
            mappingBean= meterFeederPlantMappingRepo.findByDeveloperIdAndStatus(di,status);
        }catch (Exception e){
           throw e;
        }
        return mappingBean;

    }

    public List<MeterFeederPlantMappingBean> getMappingByFeederCode(String fcode, String status) {

        List<MeterFeederPlantMappingBean> mappingBean = new ArrayList<>();
        try{
            mappingBean= meterFeederPlantMappingRepo.findByFeederCodeAndStatus(fcode,status);
        }catch (Exception e){
           throw e;
        }
        return mappingBean;
    }

    public List<MeterFeederPlantMappingBean> getMappingByPlantCode(String plantCode, String status) {

        List<MeterFeederPlantMappingBean> mappingBean = new ArrayList<>();
        try{
            mappingBean= meterFeederPlantMappingRepo.findByPlantCodeAndStatus(plantCode,status);
        }catch (Exception e){
           throw e;
        }
        return mappingBean;
    }

    public List<String> getDistinctPlantCodeByDeveloperId(String developerId, String status) {
        List<String> plants = new ArrayList<>();
        try {
            plants = meterFeederPlantMappingRepo.findDistinctPlantCodeByDeveloperIdAndStatus(developerId,status);
        }catch (Exception e){
            throw e;
        }
        return plants;
    }

    public List<MeterFeederPlantMappingBean> getMappingByDeveloperIdOrderByEndDate(String di, String status) {
        List<MeterFeederPlantMappingBean> mappingBean = new ArrayList<>();
        try{
            mappingBean= meterFeederPlantMappingRepo.findAllByDeveloperIdAndStatusOrderByEndDateAsc(di,status);
        }catch (Exception e){
            throw e;
        }
        return mappingBean;

    }
}
