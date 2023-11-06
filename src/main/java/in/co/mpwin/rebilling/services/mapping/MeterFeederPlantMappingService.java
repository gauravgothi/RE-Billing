package in.co.mpwin.rebilling.services.mapping;

import in.co.mpwin.rebilling.beans.developermaster.DeveloperMasterBean;
import in.co.mpwin.rebilling.beans.feedermaster.FeederMasterBean;
import in.co.mpwin.rebilling.beans.investormaster.InvestorMasterBean;
import in.co.mpwin.rebilling.beans.machinemaster.MachineMasterBean;
import in.co.mpwin.rebilling.beans.mapping.InvestorMachineMappingBean;
import in.co.mpwin.rebilling.beans.mapping.MeterFeederPlantMappingBean;
import in.co.mpwin.rebilling.beans.plantmaster.PlantMasterBean;
import in.co.mpwin.rebilling.beans.thirdparty.DeveloperPlantDto;
import in.co.mpwin.rebilling.dto.CompleteMappingDto;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.AuditControlServices;
import in.co.mpwin.rebilling.miscellanious.ValidatorService;
import in.co.mpwin.rebilling.repositories.mapping.MeterFeederPlantMappingRepo;
import in.co.mpwin.rebilling.services.developermaster.DeveloperMasterService;
import in.co.mpwin.rebilling.services.feedermaster.FeederMasterService;
import in.co.mpwin.rebilling.services.investormaster.InvestorMasterService;
import in.co.mpwin.rebilling.services.machinemaster.MachineMasterService;
import in.co.mpwin.rebilling.services.plantmaster.PlantMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MeterFeederPlantMappingService {

    @Autowired private MeterFeederPlantMappingRepo meterFeederPlantMappingRepo;
    @Autowired private DeveloperMasterService developerMasterService;
    @Autowired private PlantMasterService plantMasterService;
    @Autowired private FeederMasterService feederMasterService;
    @Autowired private InvestorMasterService investorMasterService;
    @Autowired private MachineMasterService machineMasterService;
    @Autowired private InvestorMachineMappingService investorMachineMappingService;


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

    public MeterFeederPlantMappingBean getByAnyMeterNoAndStatus(String meterNo, String status) {

        MeterFeederPlantMappingBean mappingBean=null;
        try{
            mappingBean= meterFeederPlantMappingRepo.findByAnyMeterNoAndStatus(meterNo,status);
            if (mappingBean==null)
                throw new ApiException(HttpStatus.BAD_REQUEST,"No active mapping of Plant found for given meter..");
        }catch (ApiException apiException){
            throw apiException;
        }catch (DataIntegrityViolationException d){
            throw d;
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

    public MeterFeederPlantMappingBean getLastMFPMappingByMeterNo(String meterNumber, String category, String status) {

        MeterFeederPlantMappingBean mfpMapping = null;
        switch(category)
        {
            case "MAIN":
                System.out.println("switch case : main to find last mapping by main meter");
                mfpMapping = meterFeederPlantMappingRepo.findLastMFPMappingByMainMeterNo(meterNumber,status);
                break;
            case "CHECK":
                mfpMapping =   meterFeederPlantMappingRepo.findLastMFPMappingByCheckMeterNo(meterNumber,status);
                break;
            case "STANDBY":
                mfpMapping =  meterFeederPlantMappingRepo.findLastMFPMappingByStandbyMeterNo(meterNumber,status);
                break;
            default:
                break;
        }
     return mfpMapping;
    }

    public void updateMFPMapping(Long id, Date replaceDate) {

        System.out.println("calling mfp end date update method");

       meterFeederPlantMappingRepo.updateMappingEndDatebyId(id,replaceDate);
    }

    public MeterFeederPlantMappingBean updateMFPMapping(MeterFeederPlantMappingBean newMFPMapping) {
        System.out.println("calling new mfp save method");
        return meterFeederPlantMappingRepo.save(newMFPMapping);
    }


        public List<String> getDistinctPlantCodeByDeveloperId (String developerId, String status){
            List<String> plants = new ArrayList<>();
            try {
                plants = meterFeederPlantMappingRepo.findDistinctPlantCodeByDeveloperIdAndStatus(developerId, status);
                if(plants.size()==0)
                    throw new ApiException(HttpStatus.BAD_REQUEST,"Developer "+developerId +" not have any plant mapping");
            } catch (ApiException apiException){
                throw apiException;
            }
            catch (Exception e) {
                throw e;
            }
            return plants;
        }

        public List<MeterFeederPlantMappingBean> getMappingByDeveloperIdOrderByEndDate (String di, String status){
            List<MeterFeederPlantMappingBean> mappingBean = new ArrayList<>();
            try {
                mappingBean = meterFeederPlantMappingRepo.findAllByDeveloperIdAndStatusOrderByEndDateAsc(di, status);
                if (mappingBean.size() == 0)
                    throw new ApiException(HttpStatus.BAD_REQUEST,"Developer "+ di+" is not mapped to any plant..");
            } catch (ApiException apiException) {
                apiException.printStackTrace();
                System.out.println(apiException.getMessage());
            }
            catch (Exception e) {
                throw e;
            }
            return mappingBean;


        }

        public CompleteMappingDto getCompleteMappingByMeterNumber(String meterNumber) {
            CompleteMappingDto completeMappingDto = new CompleteMappingDto();
            try {
                    MeterFeederPlantMappingBean mfp1= meterFeederPlantMappingRepo
                            .findByAnyMeterNoAndStatus(meterNumber,"active");
                    if (mfp1==null)
                        throw new ApiException(HttpStatus.BAD_REQUEST,"No active mapping of Plant found for given meter..");
                    //Set meters of that plant which is selected meter
                    completeMappingDto.setMeterNumber(meterNumber);
                    completeMappingDto.setMainMeterNumber(mfp1.getMainMeterNo());
                    completeMappingDto.setCheckMeterNumber(mfp1.getCheckMeterNo());
                    completeMappingDto.setStandbyMeterNumber(mfp1.getStandbyMeterNo());
                    //Set Feeder of meter which is selected ultimately it is plant's feeder
                    FeederMasterBean feederMasterBean = feederMasterService.getFeederByFeederNumber(mfp1.getFeederCode(),"active");
                    completeMappingDto.setFeederMasterBean(feederMasterBean);
                    //Set the Developer and plant of selected meter
                    DeveloperMasterBean developerMasterBean = developerMasterService.getDeveloperById(Long.valueOf(mfp1.getDeveloperId()),"active");
                    PlantMasterBean plantMasterBean = plantMasterService.getPlantByPlantCode(mfp1.getPlantCode(),"active");
                    completeMappingDto.setDeveloperMasterBean(developerMasterBean);
                    completeMappingDto.setPlantMasterBean(plantMasterBean);
                    //Set Investor list of plant
                    List<InvestorMasterBean> investorMasterBeanList = new ArrayList<>();
                    List<Map<String,List<MachineMasterBean>>> machinesOfInvestors = new ArrayList<>();
                    //Fetch Distinct Investor list from InvestorMachine Mapping belongs by Meter Feeder Mapping id
                    List<InvestorMachineMappingBean> investorMachineMappingBeans = investorMachineMappingService.getMappingByMFPId(mfp1.getId(), "active");
                    if (investorMachineMappingBeans.size() == 0)
                        throw new ApiException(HttpStatus.BAD_REQUEST, "Not Any Investor Mapped to this plant..");
                    //get distinct investor codes
                    List<String> investorCodes = investorMachineMappingBeans.stream().map(InvestorMachineMappingBean::getInvestorCode)
                        .distinct().collect(Collectors.toList());
                    for (String investorCode:investorCodes){
                            Map<String,List<MachineMasterBean>> machinesOfAnInvestor = new HashMap<>();
                            InvestorMasterBean investorMasterBean =
                                    investorMasterService.getInvestorByInvestorCode(investorCode,"active");
                            //add investor master bean wrt investor code
                            investorMasterBeanList.add(investorMasterBean);
                            //Get machine codes by investor code
                            List<String> machineCodes = investorMachineMappingService.getMappingByInvestorCode(investorCode, "active").
                                stream().map(InvestorMachineMappingBean::getMachineCode).collect(Collectors.toList());
                            // after getting machine codes of an investor then take machine master list
                            List<MachineMasterBean> machineMasterBeanList = machineMasterService.getAllMachineByMachineCodeList(machineCodes, "active");
                            machinesOfAnInvestor.put(investorCode,machineMasterBeanList);
                                machinesOfInvestors.add(machinesOfAnInvestor);
                    }
                completeMappingDto.setInvestorMasterBeanList(investorMasterBeanList);
                completeMappingDto.setMachinesOfInvestors(machinesOfInvestors);
                return completeMappingDto;

            }catch (ApiException apiException){
                throw apiException;
            }catch (DataIntegrityViolationException d){
                throw d;
            }catch (Exception e){
                throw e;
        }
    }
}