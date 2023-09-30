package in.co.mpwin.rebilling.services.bifurcateservice;

import in.co.mpwin.rebilling.beans.bifurcation.BifurcateBean;
import in.co.mpwin.rebilling.beans.investormaster.InvestorMasterBean;
import in.co.mpwin.rebilling.beans.machinemaster.MachineMasterBean;
import in.co.mpwin.rebilling.beans.mapping.InvestorMachineMappingBean;
import in.co.mpwin.rebilling.beans.mapping.MeterFeederPlantMappingBean;
import in.co.mpwin.rebilling.beans.readingbean.MeterReadingBean;
import in.co.mpwin.rebilling.dto.BifurcateConsumptionDto;
import in.co.mpwin.rebilling.dto.BifurcateInvestorDto;
import in.co.mpwin.rebilling.dto.MeterConsumptionDto;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.DateMethods;
import in.co.mpwin.rebilling.miscellanious.TokenInfo;
import in.co.mpwin.rebilling.repositories.bifurcaterepo.BifurcateBeanRepo;
import in.co.mpwin.rebilling.repositories.developermaster.DeveloperMasterRepo;
import in.co.mpwin.rebilling.repositories.plantmaster.PlantMasterRepo;
import in.co.mpwin.rebilling.services.investormaster.InvestorMasterService;
import in.co.mpwin.rebilling.services.machinemaster.MachineMasterService;
import in.co.mpwin.rebilling.services.mapping.InvestorMachineMappingService;
import in.co.mpwin.rebilling.services.mapping.MeterFeederPlantMappingService;
import in.co.mpwin.rebilling.services.plantmaster.PlantMasterService;
import in.co.mpwin.rebilling.services.readingservice.MeterReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BifurcateConsumptionService {
    @Autowired
    private MeterReadingService meterReadingService;
    @Autowired
    private DeveloperMasterRepo developerMasterRepo;
    @Autowired private InvestorMachineMappingService investorMachineMappingService;
    @Autowired private InvestorMasterService investorMasterService;
    @Autowired private MachineMasterService machineMasterService;
    @Autowired private PlantMasterService plantMasterService;
    @Autowired
    private MeterFeederPlantMappingService mfpService;

    @Autowired
    private BifurcateBeanRepo bifurcateBeanRepo;

    private String plantCode;
    private Long mfpBeanId;

    public BifurcateConsumptionDto getBifurcateDto(MeterConsumptionDto dto) {
        //get current username and developer id and developer name
        //If reading is in valid current state, and not already bifurcated for given meter or plant then perform make bifurcation dto only
        try {
            if (bifurcateValidation(dto).equals("valid"))
                return makeBifurcateDto(dto);
            else if(bifurcateValidation(dto).equals("invalid"))
                throw new ApiException(HttpStatus.BAD_REQUEST,"Validation failed for fetching ");
            else
                throw new ApiException(HttpStatus.BAD_REQUEST,"Somethimg wrong! contact to IT team ");
        }catch (ApiException apiException){
            throw apiException;
        }catch (DataIntegrityViolationException d){
            throw d;
        }catch (Exception e){
            throw e;
        }
    }
    private String bifurcateValidation(MeterConsumptionDto dto){
        boolean isReadingCurrentStateValid = false;
        boolean isAlreadyBifurcated = true;
        boolean isAlreadyBifurcatedForSamePlant=true;

        List<String> validCurrentState = List.of("dev_accept","circle_reject");

        MeterReadingBean currentReadingBean = meterReadingService.getReadingByMeterNoAndReadingDateAndStatus
                (dto.getMeterNo(),dto.getCurrentReadingDate(),"active");

        MeterReadingBean previousReadingBean =
                meterReadingService.getReadingByMeterNoAndReadingDateAndStatus
                        (dto.getMeterNo(),dto.getPreviousReadingDate(),"active");
        try {

            if (currentReadingBean == null || previousReadingBean == null)
                throw new ApiException(HttpStatus.BAD_REQUEST, "Current and Previous reading must be present");

            //First check that reading is developer accept or not
            if (validCurrentState.contains(currentReadingBean.getCurrentState()))
                isReadingCurrentStateValid = true;
            else
                throw new ApiException(HttpStatus.BAD_REQUEST, "Reading of meter for date " + dto.getCurrentReadingDate() +
                        " must be accept first");

            // Second check is meter already bifurcated for given month then again bifucation not possible
            List<BifurcateBean> bifurcateBeans = bifurcateBeanRepo.findAllByHMeterNumberAndHReadingDateAndStatus(
                    dto.getCategory(), dto.getCurrentReadingDate(), "active");
            if (bifurcateBeans.size() == 0)
                isAlreadyBifurcated = false;
            else
                throw new ApiException(HttpStatus.BAD_REQUEST, "Consumption of meter " + dto.getMeterNo() +
                        " is already bifurcated for reading date " + dto.getCurrentReadingDate());

            //Third check is meter is only one out of two meter present in mapping , means if check already done then main not possible or vice versa
            MeterFeederPlantMappingBean mfpBean = mfpService.getByAnyMeterNoAndStatus(dto.getMeterNo(), "active");
            plantCode = mfpBean.getPlantCode();
            mfpBeanId = mfpBean.getId();//This is used for getting investor related to plant
            List<BifurcateBean> bifurcateBeansByMainMeter = bifurcateBeanRepo.findAllByHMeterNumberAndHReadingDateAndStatus(
                    mfpBean.getMainMeterNo(), dto.getCurrentReadingDate(), "active");
            List<BifurcateBean> bifurcateBeansByCheckMeter = bifurcateBeanRepo.findAllByHMeterNumberAndHReadingDateAndStatus(
                    mfpBean.getCheckMeterNo(), dto.getCurrentReadingDate(), "active");
            if (bifurcateBeansByMainMeter.size() == 0 && bifurcateBeansByCheckMeter.size() == 0)
                isAlreadyBifurcatedForSamePlant = false;
            else
                throw new ApiException(HttpStatus.BAD_REQUEST, "Same Plant is already bifurcated by either main or check meter " +
                        "for given reading date " + dto.getCurrentReadingDate());
        }catch (ApiException apiException){
            throw apiException;
        }catch (DataIntegrityViolationException d){
            throw d;
        }catch (Exception e){
            throw e;
        }

        if (isReadingCurrentStateValid && !(isAlreadyBifurcated) && !(isAlreadyBifurcatedForSamePlant))
            return "valid";
        else
            return "invalid";
    }

    private BifurcateConsumptionDto makeBifurcateDto(MeterConsumptionDto meterConsumptionDto){
        BifurcateConsumptionDto bifurcateDto = new BifurcateConsumptionDto();
        //get current username and developer id and developer name
        String username = new TokenInfo().getCurrentUsername();
        String developerId  = String.valueOf(developerMasterRepo.findIdByDeveloperUsername(username));
        String developerName = developerMasterRepo.findByIdAndStatus(Long.valueOf(developerId),"active").getDeveloperName();

        bifurcateDto.setHDevUsername(username);
        bifurcateDto.setHDevId(developerId);
        bifurcateDto.setHDevName(developerName);
        bifurcateDto.setHDevPlantcode(plantCode);
        bifurcateDto.setHDevPlantName(plantMasterService.getPlantByPlantCode(bifurcateDto.getHDevPlantcode(),"active").getPlantName());
        bifurcateDto.setHMeterNumber(meterConsumptionDto.getMeterNo());
        bifurcateDto.setHCategory(meterConsumptionDto.getCategory());
        bifurcateDto.setHMf(meterConsumptionDto.getMf());
        bifurcateDto.setHMaxDemand(meterConsumptionDto.getEConsumptionMaxDemand());
        bifurcateDto.setHReadingDate(meterConsumptionDto.getCurrentReadingDate());
        bifurcateDto.setHmonth(new DateMethods().getMonthYear(bifurcateDto.getHReadingDate()));
        bifurcateDto.setHConsumptionKwh(meterConsumptionDto.getEConsumptionActiveEnergy());
        bifurcateDto.setHRkvah(BigDecimal.valueOf(0));
        bifurcateDto.setHAssessment(meterConsumptionDto.getEConsumptionAssesment());
        bifurcateDto.setHAdjustment(meterConsumptionDto.getEAdjustment().multiply(meterConsumptionDto.getMf()));
        bifurcateDto.setHGrandConsumptionKwh(bifurcateDto.getHConsumptionKwh().add(bifurcateDto.getHAssessment()
                                            .subtract(bifurcateDto.getHAdjustment())));

        //Fetch Distinct Investor list from InvestorMachine Mapping belongs by Meter Feeder Mapping id
        List<InvestorMachineMappingBean> investorMachineMappingBeans = investorMachineMappingService.getMappingByMFPId(mfpBeanId,"active");
        if (investorMachineMappingBeans.size()==0)
            throw new ApiException(HttpStatus.BAD_REQUEST,"Not Any Investor Mapped to this plant..");

        List<String> investorCodes = investorMachineMappingBeans.stream().map(InvestorMachineMappingBean::getInvestorCode)
                .distinct().collect(Collectors.toList());
        //make the bifurcation dto of investor on line
        List<BifurcateInvestorDto> bifurcateInvestorDtoList = getBifurcateInvestorDtoList(investorCodes,bifurcateDto.getHMf());
        bifurcateDto.setBifurcateInvestorDtoList(bifurcateInvestorDtoList);

        //calculate the footer lines but for dto get front is default value 0
        bifurcateDto.setFSumConsumptionKwh(BigDecimal.valueOf(0));
        bifurcateDto.setFSumFixedAdjustmentValue(BigDecimal.valueOf(0));
        bifurcateDto.setFSumAssessment(BigDecimal.valueOf(0));
        bifurcateDto.setFSumAdjustment(BigDecimal.valueOf(0));
        bifurcateDto.setFGrandConsumptionKwh(BigDecimal.valueOf(0));
        bifurcateDto.setFUnallocatedConsumptionKwh(BigDecimal.valueOf(0));

        return bifurcateDto;

    }

    private List<BifurcateInvestorDto> getBifurcateInvestorDtoList(List<String> investorCodes,BigDecimal meterMf){
        List<BifurcateInvestorDto> bifurcateInvestorDtoList = new ArrayList<>();
        for (String investor : investorCodes){
            BifurcateInvestorDto dto = new BifurcateInvestorDto();
            InvestorMasterBean investorMasterBean = investorMasterService.getInvestorByInvestorCode(investor,"active");

            dto.setLInvestorCode(investor);
            dto.setLInvestorName(investorMasterBean.getInvestorName());


            //Get machine code by investor code
            List<String> machineCodes = investorMachineMappingService.getMappingByInvestorCode(investor,"active").stream()
                    .map(InvestorMachineMappingBean::getMachineCode).collect(Collectors.toList());
            // after getting machine codes of an investor then sum machine capacity
            List<MachineMasterBean> machineMasterBeanList = machineMasterService.getAllMachineByMachineCodeList(machineCodes,"active");
            BigDecimal investorMachineTotalCapacity = BigDecimal.valueOf(0);

            //BigDecimal investorMachineActiveRate = BigDecimal.valueOf(0);
            machineMasterBeanList.stream().forEach(s -> {
                new BigDecimal(s.getCapacity()).add(investorMachineTotalCapacity);
                //new BigDecimal(s.getActiveRate()).add(investorMachineActiveRate);
            });
            //if (investorMachineActiveRate.divide(BigDecimal.valueOf(machineCodes.size())) == machineMasterBean.getActiveRate().)


            dto.setLMachineCapacity(investorMachineTotalCapacity);
            dto.setLMachineActiveRate(new BigDecimal(machineMasterBeanList.get(0).getActiveRate()));
            dto.setLMachineAReactiveRate(new BigDecimal(machineMasterBeanList.get(0).getReactiveRate()));
            //future use field set 0 by default
            dto.setLDevConsumptionKwh(BigDecimal.valueOf(0));
            dto.setLFixedAdjustmentPer(BigDecimal.valueOf(0));
            dto.setLrkvah(BigDecimal.valueOf(0));

            //now set the kwh export consumption active energy by investor default to 0 for entering by user
            dto.setLConsumptionKwh(BigDecimal.valueOf(0));
            dto.setLAssessment(BigDecimal.valueOf(0));
            dto.setLAdjustmentUnit(BigDecimal.valueOf(0));
            dto.setLAdjustment(BigDecimal.valueOf(0));
            dto.setLConsumptionTotal(BigDecimal.valueOf(0));
//            dto.setLAdjustment(dto.getLAdjustmentUnit().multiply(meterMf));
//            dto.setLConsumptionTotal(dto.getLConsumptionKwh()
//                    .add(dto.getLAssessment())
//                    .subtract(dto.getLAdjustment()));

            bifurcateInvestorDtoList.add(dto);
        }
        return bifurcateInvestorDtoList;
    }
}
