package in.co.mpwin.rebilling.services.bifurcateservice;

import in.co.mpwin.rebilling.beans.bifurcation.BifurcateBean;
import in.co.mpwin.rebilling.beans.investormaster.InvestorMasterBean;
import in.co.mpwin.rebilling.beans.machinemaster.MachineMasterBean;
import in.co.mpwin.rebilling.beans.mapping.InvestorMachineMappingBean;
import in.co.mpwin.rebilling.beans.mapping.MeterFeederPlantMappingBean;
import in.co.mpwin.rebilling.beans.readingbean.FivePercentBean;
import in.co.mpwin.rebilling.beans.readingbean.MeterReadingBean;
import in.co.mpwin.rebilling.dto.BifurcateConsumptionDto;
import in.co.mpwin.rebilling.dto.BifurcateInvestorDto;
import in.co.mpwin.rebilling.dto.ConsumptionPercentageDto2;
import in.co.mpwin.rebilling.dto.MeterConsumptionDto;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.AuditControlServices;
import in.co.mpwin.rebilling.miscellanious.DateMethods;
import in.co.mpwin.rebilling.miscellanious.TokenInfo;
import in.co.mpwin.rebilling.repositories.bifurcaterepo.BifurcateBeanRepo;
import in.co.mpwin.rebilling.repositories.developermaster.DeveloperMasterRepo;
import in.co.mpwin.rebilling.repositories.plantmaster.PlantMasterRepo;
import in.co.mpwin.rebilling.services.developermaster.DeveloperMasterService;
import in.co.mpwin.rebilling.services.investormaster.InvestorMasterService;
import in.co.mpwin.rebilling.services.machinemaster.MachineMasterService;
import in.co.mpwin.rebilling.services.mapping.InvestorMachineMappingService;
import in.co.mpwin.rebilling.services.mapping.InvestorPpwaMappingService;
import in.co.mpwin.rebilling.services.mapping.MeterFeederPlantMappingService;
import in.co.mpwin.rebilling.services.plantmaster.PlantMasterService;
import in.co.mpwin.rebilling.services.readingservice.MeterReadingService;
import jakarta.persistence.Tuple;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BifurcateConsumptionService {
    @Autowired
    private MeterReadingService meterReadingService;
    @Autowired
    private DeveloperMasterRepo developerMasterRepo;
    @Autowired private InvestorMachineMappingService investorMachineMappingService;
    @Autowired private InvestorMasterService investorMasterService;
    @Autowired private InvestorPpwaMappingService investorPpwaMappingService;
    @Autowired private MachineMasterService machineMasterService;
    @Autowired private PlantMasterService plantMasterService;
    @Autowired
    private MeterFeederPlantMappingService mfpService;

    @Autowired
    private BifurcateBeanRepo bifurcateBeanRepo;

    private String plantCode;
    private Long mfpBeanId;

    private static final Logger logger = LoggerFactory.getLogger(BifurcateConsumptionService.class);

    public BifurcateConsumptionDto getBifurcateDto(MeterConsumptionDto dto) {
        //get current username and developer id and developer name
        //If reading is in valid current state, and not already bifurcated for given meter or plant then perform make bifurcation dto only
        final String methodName = "getBifurcateDto() : ";
        logger.info(methodName + "called with parameters MeterConsumptionDto = {}",dto);
        try {
            if (bifurcateValidation(dto).equals("valid")) {
                logger.info(methodName + "call the method makeBifurcateDto(dto) ");
                return makeBifurcateDto(dto);
            }
            else if(bifurcateValidation(dto).equals("invalid"))
                throw new ApiException(HttpStatus.BAD_REQUEST,"Validation failed for fetching ");
            else
                throw new ApiException(HttpStatus.BAD_REQUEST,"Somethimg wrong! contact to IT team ");
        }catch (ApiException apiException){
            logger.error(methodName+"throw apiException");
            throw apiException;
        }catch (DataIntegrityViolationException d){
            logger.error(methodName+"throw DataIntegrityViolationException");
            throw d;
        }catch (Exception e){
            logger.error(methodName+"throw Exception");
            throw e;
        }
    }
    private String bifurcateValidation(MeterConsumptionDto dto){
        final String methodName = "bifurcateValidation() : ";
        logger.info(methodName + "called with parameters MeterConsumptionDto = {}",dto);

        boolean isReadingCurrentStateValid = false;
        boolean isAlreadyBifurcated = true;
        boolean isAlreadyBifurcatedForSamePlant=true;

        List<String> validCurrentState = List.of("dev_accept","circle_reject");

        logger.info(methodName + "getting current reading bean by calling meter reading service..");
        MeterReadingBean currentReadingBean = meterReadingService.getReadingByMeterNoAndReadingDateAndStatus
                (dto.getMeterNo(),dto.getCurrentReadingDate(),"active");

        logger.info(methodName + "getting previous reading bean by calling meter reading service..");
        MeterReadingBean previousReadingBean =
                meterReadingService.getReadingByMeterNoAndReadingDateAndStatus
                        (dto.getMeterNo(),dto.getPreviousReadingDate(),"active");
        try {

            if (currentReadingBean == null || previousReadingBean == null)
                throw new ApiException(HttpStatus.BAD_REQUEST, "Current and Previous reading must be present");

            //First check that reading is developer accept or not
            if ((validCurrentState.contains(currentReadingBean.getCurrentState())) && (validCurrentState.contains(previousReadingBean.getCurrentState())))
                isReadingCurrentStateValid = true;
            else
                throw new ApiException(HttpStatus.BAD_REQUEST, "Reading of meter for date " + dto.getCurrentReadingDate() + " and " +
                        dto.getPreviousReadingDate() +" must be accept first");

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
            logger.error(methodName+"throw apiException");
            throw apiException;
        }catch (DataIntegrityViolationException d){
            logger.error(methodName+"throw DataIntegrityViolationException");
            throw d;
        }catch (Exception e){
            logger.error(methodName+"throw Exception");
            throw e;
        }

        if (isReadingCurrentStateValid && !(isAlreadyBifurcated) && !(isAlreadyBifurcatedForSamePlant)) {
            logger.info(methodName + "method return valid");
            return "valid";
        }
        else {
            logger.info(methodName + "method return invalid");
            return "invalid";
        }
    }

    private BifurcateConsumptionDto makeBifurcateDto(MeterConsumptionDto meterConsumptionDto){
        final String methodName = "makeBifurcateDto() : ";
        logger.info(methodName + "called with parameters MeterConsumptionDto = {}",meterConsumptionDto);
         try {
             BifurcateConsumptionDto bifurcateDto = new BifurcateConsumptionDto();

             //get current username and developer id and developer name
             String username = new TokenInfo().getCurrentUsername();
             logger.info(methodName + "got the current user "+username);
             String developerId = String.valueOf(developerMasterRepo.findIdByDeveloperUsername(username));
             String developerName = developerMasterRepo.findByIdAndStatus(Long.valueOf(developerId), "active").getDeveloperName();

             bifurcateDto.setHDevUsername(username);
             bifurcateDto.setHDevId(developerId);
             bifurcateDto.setHDevName(developerName);
             bifurcateDto.setHDevPlantcode(plantCode);
             logger.info(methodName + "getting the plant name and circle name by passing plant code "+plantCode);
             bifurcateDto.setHDevPlantName(plantMasterService.getPlantByPlantCode(bifurcateDto.getHDevPlantcode(), "active").getPlantName());
             bifurcateDto.setHCircleName(plantMasterService.getPlantByPlantCode(bifurcateDto.getHDevPlantcode(), "active")
                     .getLocationMaster().getCircleName());
             bifurcateDto.setHMeterNumber(meterConsumptionDto.getMeterNo());
             bifurcateDto.setHCategory(meterConsumptionDto.getCategory());
             bifurcateDto.setHMf(meterConsumptionDto.getMf());
             bifurcateDto.setHMaxDemand(meterConsumptionDto.getEConsumptionMaxDemand());
             bifurcateDto.setHReadingDate(meterConsumptionDto.getCurrentReadingDate());
             bifurcateDto.setHmonth(meterConsumptionDto.getMonthYear());
             bifurcateDto.setHConsumptionKwh(meterConsumptionDto.getEConsumptionActiveEnergy());
             bifurcateDto.setHRkvah(BigDecimal.valueOf(0));
             bifurcateDto.setHAssessment(meterConsumptionDto.getEConsumptionAssesment());
             bifurcateDto.setHAdjustment(meterConsumptionDto.getEAdjustment().multiply(meterConsumptionDto.getMf()).setScale(2, RoundingMode.HALF_DOWN));
             bifurcateDto.setHGrandConsumptionKwh(bifurcateDto.getHConsumptionKwh().add(bifurcateDto.getHAssessment()
                     .subtract(bifurcateDto.getHAdjustment())));

             //Fetch Distinct Investor list from InvestorMachine Mapping belongs by Meter Feeder Mapping id
             logger.info(methodName + "Fetch Distinct Investor list from InvestorMachine Mapping belongs by Meter Feeder Mapping id "+mfpBeanId);
             List<InvestorMachineMappingBean> investorMachineMappingBeans = investorMachineMappingService.getMappingByMFPId(mfpBeanId, "active");
             if (investorMachineMappingBeans.size() == 0)
                 throw new ApiException(HttpStatus.BAD_REQUEST, "Not Any Investor Mapped to this plant..");

             List<String> investorCodes = investorMachineMappingBeans.stream().map(InvestorMachineMappingBean::getInvestorCode)
                     .distinct().collect(Collectors.toList());
             //make the bifurcation dto of investor on line
             logger.info(methodName + "make the BifurcateInvestorDto list of investor on line by passing "+investorCodes);
             List<BifurcateInvestorDto> bifurcateInvestorDtoList = getBifurcateInvestorDtoList(investorCodes, bifurcateDto.getHMf());
             bifurcateDto.setBifurcateInvestorDtoList(bifurcateInvestorDtoList);

             //calculate the footer lines but for dto get front is default value 0
             bifurcateDto.setFSumConsumptionKwh(BigDecimal.valueOf(0));
             bifurcateDto.setFSumFixedAdjustmentValue(BigDecimal.valueOf(0));
             bifurcateDto.setFSumAssessment(BigDecimal.valueOf(0));
             bifurcateDto.setFSumAdjustment(BigDecimal.valueOf(0));
             bifurcateDto.setFGrandConsumptionKwh(BigDecimal.valueOf(0));
             bifurcateDto.setFUnallocatedConsumptionKwh(BigDecimal.valueOf(0));

             logger.info(methodName + "return BifurcateInvestorDto "+bifurcateDto);
             return bifurcateDto;
         }catch (ApiException apiException){
             logger.error(methodName+"throw apiException");
             throw apiException;
         }catch (DataIntegrityViolationException d){
             logger.error(methodName+"throw DataIntegrityViolationException");
             throw d;
         }catch (Exception e){
             logger.error(methodName+"throw Exception");
             throw e;
         }
    }

    private List<BifurcateInvestorDto> getBifurcateInvestorDtoList(List<String> investorCodes,BigDecimal meterMf){
        final String methodName2 = "getBifurcateInvestorDtoList() : ";
        logger.info(methodName2 + "called with parameters investorCodes of size = {}",investorCodes.size());
        List<BifurcateInvestorDto> bifurcateInvestorDtoList = new ArrayList<>();
        logger.info(methodName2 + "loop on each investor and set dto");
        for (String investor : investorCodes){
            logger.info(methodName2 + investor + "investor on loop iteration");
            BifurcateInvestorDto dto = new BifurcateInvestorDto();
            InvestorMasterBean investorMasterBean = investorMasterService.getInvestorByInvestorCode(investor,"active");
            if(investorMasterBean == null)
                throw new ApiException(HttpStatus.BAD_REQUEST,investor + " No investor present in investor master..");
            dto.setLInvestorCode(investor);
            dto.setLInvestorName(investorMasterBean.getInvestorName());
            dto.setPpwaNo(investorPpwaMappingService.getPpwaNoByInvestorCode(investor,"active"));


            //Get machine code by investor code
            logger.info(methodName2 + "Get machine code by investor code " + investor);
            List<String> machineCodes = investorMachineMappingService.getMappingByInvestorCode(investor,"active").stream()
                    .map(InvestorMachineMappingBean::getMachineCode).collect(Collectors.toList());
            // after getting machine codes of an investor then sum machine capacity
            logger.info(methodName2 + "sum machine capacity.. ");
            List<MachineMasterBean> machineMasterBeanList = machineMasterService.getAllMachineByMachineCodeList(machineCodes,"active");
            BigDecimal investorMachineTotalCapacity = BigDecimal.valueOf(0);

            //BigDecimal investorMachineActiveRate = BigDecimal.valueOf(0);
            for(MachineMasterBean m : machineMasterBeanList){
                BigDecimal temp = new BigDecimal(m.getCapacity());
                investorMachineTotalCapacity = investorMachineTotalCapacity.add(temp);
            }
//            machineMasterBeanList.stream().forEach(s -> {
//                new BigDecimal(s.getCapacity()).add(investorMachineTotalCapacity);
//                //new BigDecimal(s.getActiveRate()).add(investorMachineActiveRate);
//            });
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

            logger.info(methodName2 + "bifurcateInvestorDto dto is added to list.. ");
            bifurcateInvestorDtoList.add(dto);
        }
        logger.info(methodName2 + "return bifurcateInvestorDto list iof size "+bifurcateInvestorDtoList.size());
        return bifurcateInvestorDtoList;
    }

    @Transactional
    public BifurcateConsumptionDto saveBifurcateDto(BifurcateConsumptionDto dto) {
        final String methodName = "saveBifurcateDto() : ";
        logger.info(methodName + "called with parameters BifurcateConsumptionDto = {}",dto);
        try {
                //First check the validation of Bifurcate Dto provided by front end
                if ((dto.getHConsumptionKwh().compareTo(BigDecimal.valueOf(0)) == 0) || (dto.getFSumConsumptionKwh().compareTo(BigDecimal.valueOf(0)) == 0))
                    throw new ApiException(HttpStatus.BAD_REQUEST,"Kwh Export must not be zero.");
                //commented out due to case where adjustment unit is present
//                else if (dto.getHConsumptionKwh().compareTo(dto.getFSumConsumptionKwh()) < 0) //kwh export must be equal or less than footer sum
//                    throw new ApiException(HttpStatus.BAD_REQUEST,"Kwh Export must be equal or greater to Total Kwh Export of investors.");
//                else if (dto.getHAdjustment().compareTo(dto.getFSumAdjustment()) != 0) //total adjustment header and footer sum must be equal
//                    throw new ApiException(HttpStatus.BAD_REQUEST,"Total adjustment unit must be equal to Total Adjustment unit of investors.");
                else if (dto.getHAssessment().compareTo(dto.getFSumAssessment()) != 0) //total assessment header and footer must be equal
                    throw new ApiException(HttpStatus.BAD_REQUEST,"Total assessment unit must be equal to Total assessment unit of investors.");
                else if (dto.getHGrandConsumptionKwh().compareTo(dto.getFGrandConsumptionKwh()) < 0) //grand total must be less or equal to footer
                    throw new ApiException(HttpStatus.BAD_REQUEST,"Grand Total must be equal or less than Grand total of investors.");
                else if (dto.getHGrandConsumptionKwh().compareTo(dto.getFGrandConsumptionKwh().add(dto.getFUnallocatedConsumptionKwh())) != 0) //unallocated and grand footer must be equal to header grand
                    throw new ApiException(HttpStatus.BAD_REQUEST,"Grand Kwh Export of Meter must be equal to Sum of Grand total of investors and unallocated units.  ");
                    //if all condition fail then save the bean
                else {
                    //get the investor bifurcated dtos
                    logger.info(methodName + "looping BifurcateInvestorDtoList and get the investor bifurcated dtos. ");
                    for (BifurcateInvestorDto bifurcateInvestorDto : dto.getBifurcateInvestorDtoList()){
                        //Setup of type map because destination and source have different property
                        ModelMapper modelMapper = new ModelMapper();
                        modelMapper.addMappings(new PropertyMap<BifurcateConsumptionDto, BifurcateBean>() {
                            @Override
                            protected void configure() {
                                skip(destination.getId());
                                //skip(source.getBifurcateInvestorDtoList());
                            }
                        });

                        BifurcateBean bean = modelMapper.map(dto,BifurcateBean.class);
                        modelMapper.map(bifurcateInvestorDto,bean);
                        //save audit trails
                        logger.info(methodName + "set the audit trails then save the bean. ");
                        new AuditControlServices().setInitialAuditControlParameters(bean);
                        bifurcateBeanRepo.save(bean);

                    }
            }
        }catch (ApiException apiException){
            logger.error(methodName+"throw apiException");
            throw apiException;
        }catch (DataIntegrityViolationException d){
            logger.error(methodName+"throw DataIntegrityViolationException");
            throw d;
        }catch (Exception e){
            logger.error(methodName+"throw Exception");
            throw e;
        }
        logger.info(methodName + "return the dto of class BifurcateConsumptionDto");
        return dto;
    }

    //On invoice generate page it is used to lov view of investor list by developer username from bifurcated consumption table
    public List<Map<String,String>> getInvestorListByDeveloperId(){
        try {
            List<Map<String,String>> investors = new ArrayList<>();
            //get current username and developer id and developer name
            String username = new TokenInfo().getCurrentUsername();
            String role = new TokenInfo().getCurrentUserRole();
            if (!role.equals("DEVELOPER"))
                throw new ApiException(HttpStatus.BAD_REQUEST,"Login user should be developer");
                List<BifurcateBean> bifurcateBeanList = bifurcateBeanRepo.findDistinctInvestorCodeByDeveloperUsername(username);
                if (bifurcateBeanList.size() == 0)
                    throw new ApiException(HttpStatus.BAD_REQUEST,"Investor not bifurcated yet for given month..");
                for (BifurcateBean row : bifurcateBeanList){
                    Map<String,String> m = new HashMap<>();
                    m.put("investorCode", row.getlInvestorCode());
                    m.put("investorName", row.getlInvestorName());
                    investors.add(m);
                }
                return investors;

        }catch (ApiException apiException){
            throw apiException;
        }catch (DataIntegrityViolationException d){
            throw d;
        }catch (Exception e){
            throw e;
        }
    }

    //On invoice generate page it is used to lov view of meter list by developer username from bifurcated consumption table
    public List<Map<String,String>> getMeterListByDeveloperId(){
        try {
            List<Map<String,String>> meters = new ArrayList<>();
            //get current username and developer id and developer name
            String username = new TokenInfo().getCurrentUsername();
            String role = new TokenInfo().getCurrentUserRole();
            if (!role.equals("DEVELOPER"))
                throw new ApiException(HttpStatus.BAD_REQUEST,"Login user should be developer");

            List<BifurcateBean> bifurcateBeanList = bifurcateBeanRepo.findDistinctMeterNumberByDeveloperUsername(username);
            if (bifurcateBeanList.size() == 0)
                throw new ApiException(HttpStatus.BAD_REQUEST,"No Any Meter Bifurcated by user till date..");
            for (BifurcateBean row : bifurcateBeanList){
                Map<String,String> m = new HashMap<>();
                m.put("meterNumber", row.gethMeterNumber());
                m.put("meterCategory", row.gethCategory());
                meters.add(m);
            }
            return meters;

        }catch (ApiException apiException){
            throw apiException;
        }catch (DataIntegrityViolationException d){
            throw d;
        }catch (Exception e){
            throw e;
        }
    }

    //On invoice approval page it is used to lov view of meter list by circle name from bifurcated consumption table
    public List<Map<String,String>> getMeterListByCircleName(){
        try {
            List<Map<String,String>> meters = new ArrayList<>();
            //current username which must be circle name
            String username = new TokenInfo().getCurrentUsername();
            String role = new TokenInfo().getCurrentUserRole();
            if (!role.equals("CIRCLE"))
                throw new ApiException(HttpStatus.BAD_REQUEST,"Login user should be circle");

            List<BifurcateBean> bifurcateBeanList = bifurcateBeanRepo.findDistinctMeterNumberByCircleName(username);
            if (bifurcateBeanList.size() == 0)
                throw new ApiException(HttpStatus.BAD_REQUEST,"No Any Meter Bifurcated by user till date..");
            for (BifurcateBean row : bifurcateBeanList){
                Map<String,String> m = new HashMap<>();
                m.put("meterNumber", row.gethMeterNumber());
                m.put("meterCategory", row.gethCategory());
                meters.add(m);
            }
            return meters;

        }catch (ApiException apiException){
            throw apiException;
        }catch (DataIntegrityViolationException d){
            throw d;
        }catch (Exception e){
            throw e;
        }
    }

    public List<Map<String,String>> getAllMeters(){
        try {
            List<Map<String,String>> meters = new ArrayList<>();
            //current username which must be circle name
            String username = new TokenInfo().getCurrentUsername();
            String role = new TokenInfo().getCurrentUserRole();
            if (!(role.equals("AMRCELL") || role.equals("HTCELL")) )
                throw new ApiException(HttpStatus.BAD_REQUEST,"Login user should be either AMR or HT Cell user");

            List<BifurcateBean> bifurcateBeanList = bifurcateBeanRepo.findDistinctMeterNumber();
            if (bifurcateBeanList.size() == 0)
                throw new ApiException(HttpStatus.BAD_REQUEST,"Meter Consumption is not bifurcated yet for given month..");
            for (BifurcateBean row : bifurcateBeanList){
                Map<String,String> m = new HashMap<>();
                m.put("meterNumber", row.gethMeterNumber());
                m.put("meterCategory", row.gethCategory());
                meters.add(m);
            }
            return meters;

        }catch (ApiException apiException){
            throw apiException;
        }catch (DataIntegrityViolationException d){
            throw d;
        }catch (Exception e){
            throw e;
        }
    }

    public BifurcateBean getBifurcateBeanByInvestorCodeAndMonth(String investorCode,String monthYear,String status){
        final String methodName = "getBifurcateBeanByInvestorCodeAndMonth() : ";
        logger.info(methodName + "called with parameters investorCode = {}, monthYear = {} and status = {}",investorCode,monthYear,status);
        try {
            logger.info(methodName + "return BifurcateBean bean ");
            return bifurcateBeanRepo.findByLInvestorCodeAndHmonthAndStatus(investorCode,monthYear,"active");
        }catch (Exception exception){
            logger.error(methodName+"throw Exception");
            throw exception;
        }
    }

    public boolean isExistsInvestorInBifurcateBean(String investorCode,String monthYear,String status) {
        final String methodName = "isExistsInvestorInBifurcateBean() : ";
        logger.info(methodName + "called with parameters investorCode = {}, monthYear = {} and status = {}",investorCode,monthYear,status);
        try {
            logger.info(methodName + "return boolean value ");
            return bifurcateBeanRepo.isExistsInvestorInBifurcateBean(investorCode,monthYear,status);
        }catch (Exception exception){
            logger.error(methodName+"throw Exception");
            throw exception;
        }
    }

    public List<BifurcateBean> getBifurcateBeanByMeterNoAndMonth(String meterNo,String monthYear) {
        final String methodName = "getBifurcateBeanByMeterNoAndMonth() : ";
        logger.info(methodName + "called with parameters meterNo = {}, monthYear = {}",meterNo,monthYear);
        try {
                logger.info(methodName + "return BifurcateBean list ");
               return bifurcateBeanRepo.findAllByHMeterNumberAndHMonthAndStatus(meterNo,monthYear,"active");
        }catch (Exception exception){
            logger.error(methodName+"throw Exception");
            throw exception;
        }
    }

    public void setInactiveBifurcateBean(String investorCode,String monthYear,String status){
        final String methodName = "setInactiveBifurcateBean() : ";
        logger.info(methodName + "called with parameters investorCode = {}, monthYear = {} and status ={}",investorCode,monthYear,status);
        //make bifurcation also invalid
        try {
            BifurcateBean bifurcateBean = bifurcateBeanRepo.findByLInvestorCodeAndHmonthAndStatus
                    (investorCode,monthYear,status);
            bifurcateBean.setStatus("inactive_"+new DateMethods().getServerTime());
            bifurcateBean.setUpdatedBy(new TokenInfo().getCurrentUsername());
            bifurcateBean.setUpdatedOn(new DateMethods().getServerTime());
            bifurcateBeanRepo.save(bifurcateBean);
            logger.info(methodName + "mark inactive the bifurcate bean "+bifurcateBean);
        }catch (Exception exception){
            logger.error(methodName+"throw Exception");
            throw exception;
        }
    }

    public List<String> getDistinctMeterByPpwaNoAndMonthYear(String ppwaNo, String monthYear, String status) {
        try {
                List<String> distinctMeters  = bifurcateBeanRepo.findDistinctMeterByPpwaNoAndMonthYear(ppwaNo,monthYear,status);
                return distinctMeters;
        }catch (Exception exception){
            throw exception;
        }
    }

    public BifurcateConsumptionDto getAlreadyBifurcatedBeanDto(String meterNo, String monthYear) {
        final String methodName = "getAlreadyBifurcatedBeanDto() : ";
        logger.info(methodName + "called with parameters meterNo = {}, monthYear = {}",meterNo,monthYear);
        try {
                logger.info(methodName + "call the repo and get bifurcateBeanList");
                List<BifurcateBean> bifurcateBeanList = bifurcateBeanRepo.findAllByHMeterNumberAndHMonthAndStatus
                        (meterNo,monthYear,"active");
                if (bifurcateBeanList.size() == 0)
                    throw new ApiException(HttpStatus.BAD_REQUEST,meterNo + " meter is not bifurcated for given month");
                //set the investor bifurcated dtos
                List<BifurcateInvestorDto> bifurcateInvestorDtoList = new ArrayList<>();
                logger.info(methodName + "looping on bifurcateBeanList and set the list of BifurcateInvestorDto");
                for (BifurcateBean bifurcateBean : bifurcateBeanList){
                    BifurcateInvestorDto bifurcateInvestorDto = new BifurcateInvestorDto();

                    ModelMapper modelMapper = new ModelMapper();

                    bifurcateInvestorDto = modelMapper.map(bifurcateBean,BifurcateInvestorDto.class);
                    bifurcateInvestorDtoList.add(bifurcateInvestorDto);
                }
                BifurcateConsumptionDto bifurcateConsumptionDto = new BifurcateConsumptionDto();
                bifurcateConsumptionDto.setHDevId(bifurcateBeanList.get(0).gethDevId());
                bifurcateConsumptionDto.setHDevUsername(bifurcateBeanList.get(0).gethDevUsername());
                bifurcateConsumptionDto.setHDevName(bifurcateBeanList.get(0).gethDevName());
                bifurcateConsumptionDto.setHDevPlantcode(bifurcateBeanList.get(0).gethDevPlantcode());
                bifurcateConsumptionDto.setHDevPlantName(bifurcateBeanList.get(0).gethDevPlantName());
                bifurcateConsumptionDto.setHCircleName(bifurcateBeanList.get(0).gethCircleName());
                bifurcateConsumptionDto.setHMeterNumber(bifurcateBeanList.get(0).gethMeterNumber());
                bifurcateConsumptionDto.setHCategory(bifurcateBeanList.get(0).gethCategory());
                bifurcateConsumptionDto.setHMf(bifurcateBeanList.get(0).gethMf());
                bifurcateConsumptionDto.setHReadingDate(bifurcateBeanList.get(0).gethReadingDate());
                bifurcateConsumptionDto.setHmonth(bifurcateBeanList.get(0).getHmonth());
                bifurcateConsumptionDto.setHMaxDemand(bifurcateBeanList.get(0).gethMaxDemand());
                bifurcateConsumptionDto.setHConsumptionKwh(bifurcateBeanList.get(0).gethConsumptionKwh());
                bifurcateConsumptionDto.setHRkvah(bifurcateBeanList.get(0).gethRkvah());
                bifurcateConsumptionDto.setHAdjustment(bifurcateBeanList.get(0).gethAdjustment());
                bifurcateConsumptionDto.setHAssessment(bifurcateBeanList.get(0).gethAssessment());
                bifurcateConsumptionDto.setHGrandConsumptionKwh(bifurcateBeanList.get(0).gethGrandConsumptionKwh());
                bifurcateConsumptionDto.setFSumConsumptionKwh(bifurcateBeanList.get(0).getfSumConsumptionKwh());
                bifurcateConsumptionDto.setFSumAssessment(bifurcateBeanList.get(0).getfSumAssessment());
                bifurcateConsumptionDto.setFSumFixedAdjustmentValue(bifurcateBeanList.get(0).getfSumFixedAdjustmentValue());
                bifurcateConsumptionDto.setFSumAdjustment(bifurcateBeanList.get(0).getfSumAdjustment());
                bifurcateConsumptionDto.setFGrandConsumptionKwh(bifurcateBeanList.get(0).getfGrandConsumptionKwh());
                bifurcateConsumptionDto.setFUnallocatedConsumptionKwh(bifurcateBeanList.get(0).getfUnallocatedConsumptionKwh());

                bifurcateConsumptionDto.setBifurcateInvestorDtoList(bifurcateInvestorDtoList);

                logger.info(methodName + "return the BifurcateInvestorDto = {}",bifurcateConsumptionDto);
                return bifurcateConsumptionDto;

        }catch (ApiException apiException){
            logger.error(methodName+"throw ApiException");
            throw apiException;
        }catch (DataIntegrityViolationException d){
            logger.error(methodName+"throw DataIntegrityViolationException");
            throw d;
        }catch (Exception e){
            logger.error(methodName+"throw Exception");
            throw e;
        }
    }
}
