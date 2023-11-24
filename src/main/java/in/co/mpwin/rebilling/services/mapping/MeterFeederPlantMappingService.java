package in.co.mpwin.rebilling.services.mapping;

import in.co.mpwin.rebilling.beans.developermaster.DeveloperMasterBean;
import in.co.mpwin.rebilling.beans.feedermaster.FeederMasterBean;
import in.co.mpwin.rebilling.beans.investormaster.InvestorMasterBean;
import in.co.mpwin.rebilling.beans.machinemaster.MachineMasterBean;
import in.co.mpwin.rebilling.beans.mapping.InvestorMachineMappingBean;
import in.co.mpwin.rebilling.beans.mapping.MeterFeederPlantMappingBean;
import in.co.mpwin.rebilling.beans.metermaster.MeterMasterBean;
import in.co.mpwin.rebilling.beans.plantmaster.PlantMasterBean;
import in.co.mpwin.rebilling.beans.thirdparty.DeveloperPlantDto;
import in.co.mpwin.rebilling.dto.CompleteMappingDto;
import in.co.mpwin.rebilling.dto.InvestorMachineMappingDto;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.AuditControlServices;
import in.co.mpwin.rebilling.miscellanious.DateMethods;
import in.co.mpwin.rebilling.miscellanious.TokenInfo;
import in.co.mpwin.rebilling.miscellanious.ValidatorService;
import in.co.mpwin.rebilling.repositories.mapping.MeterFeederPlantMappingRepo;
import in.co.mpwin.rebilling.repositories.metermaster.MeterMasterRepo;
import in.co.mpwin.rebilling.services.developermaster.DeveloperMasterService;
import in.co.mpwin.rebilling.services.feedermaster.FeederMasterService;
import in.co.mpwin.rebilling.services.investormaster.InvestorMasterService;
import in.co.mpwin.rebilling.services.machinemaster.MachineMasterService;
import in.co.mpwin.rebilling.services.metermaster.MeterMasterService;
import in.co.mpwin.rebilling.services.plantmaster.PlantMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

import java.util.*;
import java.util.stream.Collectors;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


import static com.fasterxml.jackson.databind.type.LogicalType.Map;

@Service
public class MeterFeederPlantMappingService {

    @Autowired private MeterFeederPlantMappingRepo meterFeederPlantMappingRepo;
    @Autowired private DeveloperMasterService developerMasterService;
    @Autowired private PlantMasterService plantMasterService;
    @Autowired private FeederMasterService feederMasterService;
    @Autowired private InvestorMasterService investorMasterService;
    @Autowired private MachineMasterService machineMasterService;
    @Autowired private InvestorMachineMappingService investorMachineMappingService;

    @Autowired private MeterMasterRepo meterMasterRepo;


    @Transactional
    public MeterFeederPlantMappingBean createNewMapping(MeterFeederPlantMappingBean meterFeederPlantMappingBean) throws ParseException {

        try {

//            meterFeederPlantMappingBean.setMainMeterNo(new ValidatorService().removeSpaceFromString(meterFeederPlantMappingBean.getMainMeterNo()));
//            meterFeederPlantMappingBean.setCheckMeterNo(new ValidatorService().removeSpaceFromString(meterFeederPlantMappingBean.getCheckMeterNo()));
//            meterFeederPlantMappingBean.setStandbyMeterNo(new ValidatorService().removeSpaceFromString(meterFeederPlantMappingBean.getStandbyMeterNo()));
//            meterFeederPlantMappingBean.setFeederCode(new ValidatorService().removeSpaceFromString(meterFeederPlantMappingBean.getFeederCode()));
//            meterFeederPlantMappingBean.setPlantCode(new ValidatorService().removeSpaceFromString(meterFeederPlantMappingBean.getPlantCode()));
//            meterFeederPlantMappingBean.setDeveloperId(new ValidatorService().removeSpaceFromString(meterFeederPlantMappingBean.getDeveloperId()));

            //check for existence of mapping if already exist with same mapping then throw api exception
            MeterFeederPlantMappingBean temp = meterFeederPlantMappingRepo.findByMainMeterNoCheckMeterNoStandbyMeterNoAndDeveloperId(meterFeederPlantMappingBean.getMainMeterNo(),meterFeederPlantMappingBean.getCheckMeterNo(),
                    meterFeederPlantMappingBean.getStandbyMeterNo(),meterFeederPlantMappingBean.getDeveloperId());
            if(temp!=null) {
                throw new ApiException(HttpStatus.BAD_REQUEST,"developer, plant, feeder and meters mapping is already exit.");
            }
            //Set the Audit control parameters, Globally
            new AuditControlServices().setInitialAuditControlParameters(meterFeederPlantMappingBean);
            // set end date of mapping is future date "2024-12-31". this end_date is used to end the mapping due to meter replacement.
            Date futureEndDate = new SimpleDateFormat("yyyy-MM-dd").parse("2024-12-31");
            meterFeederPlantMappingBean.setEndDate(futureEndDate);
            //save mfp mapping
            MeterFeederPlantMappingBean mfpm = meterFeederPlantMappingRepo.save(meterFeederPlantMappingBean);
            //set main , check and standby meter is_mapped=yes
            MeterMasterBean mainMeterBean, checkMeterBean ,standbyMeterBean;
           if(mfpm!=null) {
               // set mapped yes to main meter
               mainMeterBean = meterMasterRepo.findByMeterNumberAndStatus(mfpm.getMainMeterNo(), "active");
               if(mainMeterBean==null)
                   throw new ApiException(HttpStatus.BAD_REQUEST,"main meter no. "+mfpm.getMainMeterNo()+" is not found in meter master in active status.");
               mainMeterBean.setIsMapped("yes");
               mainMeterBean.setUpdatedBy(new TokenInfo().getCurrentUsername());
               mainMeterBean.setUpdatedOn(new DateMethods().getServerTime());
               meterMasterRepo.save(mainMeterBean);

               // set mapped yes to check meter
               checkMeterBean = meterMasterRepo.findByMeterNumberAndStatus(mfpm.getCheckMeterNo(),"active");
               if(checkMeterBean==null)
                   throw new ApiException(HttpStatus.BAD_REQUEST,"check meter no. "+mfpm.getCheckMeterNo()+" is not found in meter master in active status.");
                   checkMeterBean.setIsMapped("yes");
                   checkMeterBean.setUpdatedBy(new TokenInfo().getCurrentUsername());
                   checkMeterBean.setUpdatedOn(new DateMethods().getServerTime());
                   meterMasterRepo.save(checkMeterBean);

               // set mapped yes to standby meter if standby meter no is present in mfp mapping
               standbyMeterBean = meterMasterRepo.findByMeterNumberAndStatus(mfpm.getStandbyMeterNo(),"active");
               if(standbyMeterBean!=null)
                   if(mfpm.getStandbyMeterNo().equals(standbyMeterBean.getMeterNumber()))
                   {
                       standbyMeterBean.setIsMapped("yes");
                       standbyMeterBean.setUpdatedBy(new TokenInfo().getCurrentUsername());
                       standbyMeterBean.setUpdatedOn(new DateMethods().getServerTime());
                       meterMasterRepo.save(checkMeterBean);
                   }
               }
            return mfpm;
        }catch(ApiException apiException) {
            throw apiException;
        } catch(DataIntegrityViolationException d) {
            throw d;
        } catch (NullPointerException ex)
        {
            throw ex;
        } catch(Exception e) {
            throw e;
        }
    }

    public List<MeterFeederPlantMappingBean> getAllMapping(String status) {

        List<MeterFeederPlantMappingBean> allMappingList;
        try {
            allMappingList=(List<MeterFeederPlantMappingBean>) meterFeederPlantMappingRepo.findByStatus(status);
        }  catch(ApiException apiException) {
            throw apiException;
        } catch(DataIntegrityViolationException d) {
            throw d;
        } catch (NullPointerException ex)
        {
            throw ex;
        } catch(Exception e) {
            throw e;
        }
        return allMappingList;
    }

    public MeterFeederPlantMappingBean getMappingById(Long id, String status) {

        MeterFeederPlantMappingBean mappingBean =null ;
        try{
            mappingBean= meterFeederPlantMappingRepo.findByIdAndStatus(id,status);
        } catch(ApiException apiException) {
            throw apiException;
        } catch(DataIntegrityViolationException d) {
            throw d;
        } catch (NullPointerException ex)
        {
            throw ex;
        } catch(Exception e) {
            throw e;
        }
        return mappingBean;
    }

    public List<MeterFeederPlantMappingBean> getMappingByMainMeterNo(String mmn, String status) {
        List<MeterFeederPlantMappingBean> mappingBean = new ArrayList<>();
        try{
            mappingBean= meterFeederPlantMappingRepo.findByMainMeterNoAndStatus(mmn,status);
        } catch(DataIntegrityViolationException d) {
            throw d;
        } catch(ApiException apiException) {
            throw apiException;
        } catch (NullPointerException ex)
        {
            throw ex;
        } catch(Exception e) {
            throw e;
        }
        return mappingBean;

    }

    public List<MeterFeederPlantMappingBean> getMappingByCheckMeterNo(String mmn, String status) {
        List<MeterFeederPlantMappingBean> mappingBean = new ArrayList<>();
        try{
            mappingBean= meterFeederPlantMappingRepo.findByCheckMeterNoAndStatus(mmn,status);
        }catch(ApiException apiException) {
            throw apiException;
        } catch(DataIntegrityViolationException d) {
            throw d;
        } catch (NullPointerException ex)
        {
            throw ex;
        } catch(Exception e) {
            throw e;
        }
        return mappingBean;

    }

    public List<MeterFeederPlantMappingBean> getMappingByStandbyMeterNo(String smn, String status) {

        List<MeterFeederPlantMappingBean> mappingBean = new ArrayList<>();
        try{
            mappingBean= meterFeederPlantMappingRepo.findByStandbyMeterNoAndStatus(smn,status);
        }catch(ApiException apiException) {
            throw apiException;
        } catch(DataIntegrityViolationException d) {
            throw d;
        } catch (NullPointerException ex)
        {
            throw ex;
        } catch(Exception e) {
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
        }catch(ApiException apiException) {
            throw apiException;
        } catch(DataIntegrityViolationException d) {
            throw d;
        } catch (NullPointerException ex)
        {
            throw ex;
        } catch(Exception e) {
            throw e;
        }
        return mappingBean;
    }

    public List<MeterFeederPlantMappingBean> getMappingByDeveloperId(String di, String status) {
        List<MeterFeederPlantMappingBean> mappingBean = new ArrayList<>();
        try{
            mappingBean= meterFeederPlantMappingRepo.findByDeveloperIdAndStatus(di,status);
        }catch(ApiException apiException) {
            throw apiException;
        } catch(DataIntegrityViolationException d) {
            throw d;
        } catch (NullPointerException ex)
        {
            throw ex;
        } catch(Exception e) {
            throw e;
        }
        return mappingBean;

    }

    public List<MeterFeederPlantMappingBean> getMappingByFeederCode(String fcode, String status) {

        List<MeterFeederPlantMappingBean> mappingBean = new ArrayList<>();
        try{
            mappingBean= meterFeederPlantMappingRepo.findByFeederCodeAndStatus(fcode,status);
        }catch(ApiException apiException) {
            throw apiException;
        } catch(DataIntegrityViolationException d) {
            throw d;
        } catch (NullPointerException ex)
        {
            throw ex;
        } catch(Exception e) {
            throw e;
        }
        return mappingBean;
    }

    public List<MeterFeederPlantMappingBean> getMappingByPlantCode(String plantCode, String status) {

        List<MeterFeederPlantMappingBean> mappingBean = new ArrayList<>();
        try{
            mappingBean= meterFeederPlantMappingRepo.findByPlantCodeAndStatus(plantCode,status);
        }catch(ApiException apiException) {
            throw apiException;
        } catch(DataIntegrityViolationException d) {
            throw d;
        } catch (NullPointerException ex)
        {
            throw ex;
        } catch(Exception e) {
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
        meterFeederPlantMappingRepo.updateMappingEndDatebyId(id,replaceDate);
    }

    public MeterFeederPlantMappingBean updateMFPMapping(MeterFeederPlantMappingBean newMFPMapping) {
           return meterFeederPlantMappingRepo.save(newMFPMapping);
    }


        public List<String> getDistinctPlantCodeByDeveloperId (String developerId, String status){
            List<String> plants = new ArrayList<>();
            try {
                plants = meterFeederPlantMappingRepo.findDistinctPlantCodeByDeveloperIdAndStatus(developerId, status);
                if(plants.size()==0)
                    throw new ApiException(HttpStatus.BAD_REQUEST,"Developer "+developerId +" not have any plant mapping");
            } catch(ApiException apiException) {
                throw apiException;
            } catch(DataIntegrityViolationException d) {
                throw d;
            } catch (NullPointerException ex)
            {
                throw ex;
            } catch(Exception e) {
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
            } catch(DataIntegrityViolationException d) {
                throw d;
            } catch (NullPointerException ex)
            {
                throw ex;
            } catch(Exception e) {
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
                    completeMappingDto.setMainMeterNumberBean(meterMasterRepo.findByMeterNumberAndStatus(mfp1.getMainMeterNo(),"active"));
                    completeMappingDto.setCheckMeterNumberBean(meterMasterRepo.findByMeterNumberAndStatus(mfp1.getCheckMeterNo(),"active"));
                    if (mfp1.getStandbyMeterNo().equalsIgnoreCase("na"))
                        completeMappingDto.setStandbyMeterNumberBean(null);
                    else
                        completeMappingDto.setStandbyMeterNumberBean(meterMasterRepo.findByMeterNumberAndStatus(mfp1.getStandbyMeterNo(),"active"));
                    //Set Feeder of meter which is selected ultimately it is plant's feeder
                    FeederMasterBean feederMasterBean = feederMasterService.getFeederByFeederNumber(mfp1.getFeederCode(),"active");
                    completeMappingDto.setFeederMasterBean(feederMasterBean);
                    //Set the Developer and plant of selected meter
                    DeveloperMasterBean developerMasterBean = developerMasterService.getDeveloperById(Long.valueOf(mfp1.getDeveloperId()),"active");
                    PlantMasterBean plantMasterBean = plantMasterService.getPlantByPlantCode(mfp1.getPlantCode(),"active");
                    completeMappingDto.setDeveloperMasterBean(developerMasterBean);
                    completeMappingDto.setPlantMasterBean(plantMasterBean);
                    //Set Investor list of plant
                    //List<InvestorMasterBean> investorMasterBeanList = new ArrayList<>();
                    //List<Map<String,List<MachineMasterBean>>> machinesOfInvestors = new ArrayList<>();

                    //Fetch Distinct Investor list from InvestorMachine Mapping belongs by Meter Feeder Mapping id
                    List<InvestorMachineMappingBean> investorMachineMappingBeans = investorMachineMappingService.getMappingByMFPId(mfp1.getId(), "active");
                    if (investorMachineMappingBeans.size() == 0)
                        throw new ApiException(HttpStatus.BAD_REQUEST, "Not Any Investor Mapped to this plant..");
                    //get distinct investor codes
                    List<String> investorCodes = investorMachineMappingBeans.stream().map(InvestorMachineMappingBean::getInvestorCode)
                        .distinct().collect(Collectors.toList());
                    //make empty investor machine mapping dto list
                    List<InvestorMachineMappingDto> investorMachineMappingDtoList = new ArrayList<>();
                    for (String investorCode:investorCodes){
                            InvestorMachineMappingDto investorMachineMappingDto = new InvestorMachineMappingDto();
                            InvestorMasterBean investorMasterBean =
                                    investorMasterService.getInvestorByInvestorCode(investorCode,"active");
                            //add investor master bean wrt investor code
                            investorMachineMappingDto.setInvestorMasterBean(investorMasterBean);
                            //investorMasterBeanList.add(investorMasterBean);
                            //Get machine codes by investor code
                            List<String> machineCodes = investorMachineMappingService.getMappingByInvestorCode(investorCode, "active").
                                stream().map(InvestorMachineMappingBean::getMachineCode).collect(Collectors.toList());
                            // after getting machine codes of an investor then take machine master list
                            List<MachineMasterBean> machineMasterBeanList = machineMasterService.getAllMachineByMachineCodeList(machineCodes, "active");
                            investorMachineMappingDto.setMachineMasterBeanList(machineMasterBeanList);
                            //machinesOfAnInvestor.put(investorCode,machineMasterBeanList);
                                //machinesOfInvestors.add(machinesOfAnInvestor);

                            investorMachineMappingDtoList.add(investorMachineMappingDto);
                    }
                //completeMappingDto.setInvestorMasterBeanList(investorMasterBeanList);//set investor list
                //completeMappingDto.setMachinesOfInvestors(machinesOfInvestors);//set machines on key of investor

                completeMappingDto.setInvestorMachineMappingDtoList(investorMachineMappingDtoList);//set investors and machines of investor in dto list
                return completeMappingDto;

            }catch(ApiException apiException) {
                throw apiException;
            } catch(DataIntegrityViolationException d) {
                throw d;
            } catch (NullPointerException ex)
            {
                throw ex;
            } catch(Exception e) {
                throw e;
            }
    }

    public List<String> findMappedMeterListByEndDate(LocalDate endDate) {
        List<MeterFeederPlantMappingBean> mfpMapping =  meterFeederPlantMappingRepo.findMappedMeterListByEndDate(endDate,"active");
        if(mfpMapping.isEmpty())
            return null;
        List<String> meterList =new ArrayList<>();
        for(MeterFeederPlantMappingBean row : mfpMapping)
        {
            meterList.add(row.getMainMeterNo());
            meterList.add(row.getCheckMeterNo());
        }
        return meterList;
    }


    public MeterFeederPlantMappingBean getMfpMappingByDeveloperAndPlantAndEndDate(String developerID, String plantCode, LocalDate endDate) {

        MeterFeederPlantMappingBean mfpBean = meterFeederPlantMappingRepo.findByDeveloperIdAndPlantIdAndEndDateAndStatus(developerID,plantCode,endDate,"active");
        if(mfpBean==null)
            throw new ApiException(HttpStatus.BAD_REQUEST,"mfp mapping not found for developer id "+developerID+" and plant code "+plantCode);
        return mfpBean;
    }
}