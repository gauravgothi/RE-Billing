package in.co.mpwin.rebilling.services.readingservice;

import in.co.mpwin.rebilling.beans.developermaster.DeveloperMasterBean;
import in.co.mpwin.rebilling.beans.mapping.MeterFeederPlantMappingBean;
import in.co.mpwin.rebilling.beans.readingbean.FivePercentBean;
import in.co.mpwin.rebilling.beans.readingbean.MeterReadingBean;
import in.co.mpwin.rebilling.dto.*;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.DateMethods;
import in.co.mpwin.rebilling.repositories.readingrepo.FivePercentRepo;
import in.co.mpwin.rebilling.services.developermaster.DeveloperMasterService;
import in.co.mpwin.rebilling.services.feedermaster.FeederMasterService;
import in.co.mpwin.rebilling.services.mapping.MeterFeederPlantMappingService;
import in.co.mpwin.rebilling.services.metermaster.MeterMasterService;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ConsumerPercentageService2 {

    private static final Logger logger = LoggerFactory.getLogger(ConsumerPercentageService2.class);
    @Autowired private DeveloperMasterService developerMasterService;
    @Autowired private MeterFeederPlantMappingService meterFeederPlantMappingService;
    @Autowired private FeederMasterService feederMasterService;
    @Autowired private DateMethods dateMethods;
    @Autowired private MeterReadingService meterReadingService;
    @Autowired MeterMasterService meterMasterService;
    @Autowired FivePercentService fivePercentService;
    @Autowired
    private FivePercentRepo fivePercentRepo;

    public List<ConsumptionPercentageDto2> calculatePercentageReport2(Date startDate, Date endDate) throws ParseException{
        final String methodName = "calculatePercentageReport2() : ";
        logger.info(methodName + " called with parameters startDate={} and endDate",startDate,endDate);
        List<ConsumptionPercentageDto2> dtoList = new ArrayList<>();
        Date futureEndDate = new SimpleDateFormat("dd-MM-yyyy").parse("31-12-2024");
        String monthYear = new DateMethods().getMonthYear(endDate);
        try {
            List<DeveloperMasterBean> developerList = developerMasterService.getAllDeveloperMasterBean("active");
            logger.info(methodName + "all active developers fetched..");
            logger.info(methodName + "looping on developers and set mfp beans with respect to plant code in map..");
            for (int k =0;developerList.size()>k;k++){

                List<MeterFeederPlantMappingBean> mappingBeanList = meterFeederPlantMappingService
                        .getMappingByDeveloperIdOrderByEndDate(String.valueOf(developerList.get(k).getId()), "active");
                if(mappingBeanList.size() == 0)
                    continue;
                // now arrange it plant wise in map <PlantCode,List<MFPBean>>
                List<Map<String,List<MeterFeederPlantMappingBean>>> plantMFPList = new ArrayList<>();
                List<String> distinctPlants = meterFeederPlantMappingService.getDistinctPlantCodeByDeveloperId(String.valueOf(developerList.get(k).getId()), "active");
                logger.info(methodName + "looping on distinct plants..");
                for (String plant : distinctPlants){
                    //If calculation is already done for plant then simple continue
                    Boolean isAlreadyExist = fivePercentService.isAlreadyExistRecord(plant,monthYear);
                    if (isAlreadyExist) {
                        logger.info(methodName + "0.5% already calculated for plant = {}",plant);
                        continue;
                    }
                    //for each plant we have list of mapping, calculate the consumption for each plant
                    Map<String,List<MeterFeederPlantMappingBean>> singlePlantMap = new HashMap<>();
                    singlePlantMap.put(plant,mappingBeanList.stream().filter(bean -> ((bean.getPlantCode().equals(plant)) &&
                                    (((bean.getEndDate().after(startDate)) && (bean.getEndDate().before(endDate))) ||
                                            (bean.getEndDate().compareTo(futureEndDate) == 0))))
                            .sorted(Comparator.comparing(MeterFeederPlantMappingBean::getEndDate))
                            .collect(Collectors.toList()));

                    plantMFPList.add(singlePlantMap);
                }
                //now run loop on map to calculate main meters consumption
                logger.info(methodName + "calculated 0.5% report in loop for remaining plants which reading newly inserted..");
                for(Map<String,List<MeterFeederPlantMappingBean>> plantMFP : plantMFPList){
                    ConsumptionPercentageDto2 consumptionPercentageDto2 = new ConsumptionPercentageDto2();
                    consumptionPercentageDto2.setDeveloperId(String.valueOf(developerList.get(k).getId()));
                    consumptionPercentageDto2.setDeveloperName(developerList.get(k).getDeveloperName());
                    consumptionPercentageDto2.setDeveloperSiteAddress(developerList.get(k).getSiteAddress());
                    consumptionPercentageDto2.setPlantCode(plantMFP.keySet().toArray()[0].toString());
                    consumptionPercentageDto2.setMonthYear(monthYear);

                    List<MainMeterDto> mainMeterDtos = new ArrayList<>();
                    List<CheckMeterDto> checkMeterDtos = new ArrayList<>();
                    List<MeterFeederPlantMappingBean> MFPBeans = plantMFP.values().iterator().next();

                    //IF DEVELOPER HAVE NOT ANY MAPPING THEN SIMPLY CONTINUE THE LOOP
                    if(MFPBeans.size()==0){
                        logger.info(methodName + "developer ={} not have any mapping then simply continue..",developerList.get(k).getDeveloperName());
                        continue;
                    }
                    consumptionPercentageDto2.setFeederCode(MFPBeans.get(0).getFeederCode());
                    consumptionPercentageDto2.setFeederName(feederMasterService.getFeederByFeederNumber(
                            MFPBeans.get(0).getFeederCode(), "active").getFeederName());

                    //consumptionPercentageDto2.setFeederName();
                    if (MFPBeans.size()>1){
                        Date mainStartDate = startDate;
                        Date checkStartDate = startDate;
                        for (int i = 0 ; i < MFPBeans.size();i++){
                            if (i+1 == MFPBeans.size()) {
                                mainMeterDtos.add(calculateMainMeterCons(MFPBeans.get(i).getMainMeterNo(),mainStartDate,endDate));
                            } else if(!(MFPBeans.get(i).getMainMeterNo().equals(MFPBeans.get(i+1).getMainMeterNo()))){
                                mainMeterDtos.add(calculateMainMeterCons(MFPBeans.get(i).getMainMeterNo(),mainStartDate,MFPBeans.get(i).getEndDate()));
                                mainStartDate = MFPBeans.get(i).getEndDate();
                            } else if (MFPBeans.get(i).getMainMeterNo().equals(MFPBeans.get(i+1).getMainMeterNo())){
                                continue;
                            }
                        }
                        for (int j = 0 ; j < MFPBeans.size();j++){
                            if (j+1 == MFPBeans.size()) {
                                checkMeterDtos.add(calculateCheckMeterCons(MFPBeans.get(j).getCheckMeterNo(),checkStartDate,endDate));
                            } else if(!(MFPBeans.get(j).getCheckMeterNo().equals(MFPBeans.get(j+1).getCheckMeterNo()))){
                                checkMeterDtos.add(calculateCheckMeterCons(MFPBeans.get(j).getCheckMeterNo(),checkStartDate,MFPBeans.get(j).getEndDate()));
                                checkStartDate = MFPBeans.get(j).getEndDate();
                            } else if (MFPBeans.get(j).getCheckMeterNo().equals(MFPBeans.get(j+1).getCheckMeterNo())){
                                continue;
                            }
                        }
                    } else if (MFPBeans.size()==1) {
                        mainMeterDtos.add(calculateMainMeterCons(MFPBeans.get(0).getMainMeterNo(),startDate,endDate));
                        checkMeterDtos.add(calculateCheckMeterCons(MFPBeans.get(0).getCheckMeterNo(),startDate,endDate));
                    }
                    consumptionPercentageDto2.setMainMeterDtos(mainMeterDtos);
                    consumptionPercentageDto2.setCheckMeterDtos(checkMeterDtos);
                    //calculate percentage(main total consumption - check total consumption *100)
                    Boolean anyMainMeterReadAbsent = consumptionPercentageDto2.getMainMeterDtos().stream()
                            .anyMatch(b -> (b.getMainTotalConsumption().compareTo(BigDecimal.valueOf(-1)) == 0));
                    Boolean anyCheckMeterReadAbsent = consumptionPercentageDto2.getCheckMeterDtos().stream()
                            .anyMatch(b->(b.getCheckTotalConsumption().compareTo(BigDecimal.valueOf(-1)) == 0));
                    if (anyMainMeterReadAbsent || anyCheckMeterReadAbsent )    {
                        consumptionPercentageDto2.setMainGrandTotalConsumption(BigDecimal.valueOf(-1));
                        consumptionPercentageDto2.setCheckGrandTotalConsumption(BigDecimal.valueOf(-1));
                        consumptionPercentageDto2.setPercentage(BigDecimal.valueOf(-1));
                        consumptionPercentageDto2.setResult("withheld");
                    }else {
                        BigDecimal totalMainMetersConsumption = consumptionPercentageDto2.getMainMeterDtos().stream()
                                .map(MainMeterDto::getMainTotalConsumption).reduce(BigDecimal.ZERO,BigDecimal::add);
                        BigDecimal totalCheckMeterConsumption = consumptionPercentageDto2.getCheckMeterDtos().stream()
                                .map(CheckMeterDto::getCheckTotalConsumption).reduce(BigDecimal.ZERO,BigDecimal::add);
                        consumptionPercentageDto2.setMainGrandTotalConsumption(totalMainMetersConsumption);
                        consumptionPercentageDto2.setCheckGrandTotalConsumption(totalCheckMeterConsumption);
                        consumptionPercentageDto2.setPercentage(((consumptionPercentageDto2.getMainGrandTotalConsumption()
                                .subtract(consumptionPercentageDto2.getCheckGrandTotalConsumption()))
                                .divide(consumptionPercentageDto2.getMainGrandTotalConsumption(), 2, RoundingMode.HALF_DOWN))
                                .multiply(BigDecimal.valueOf(100)).abs());
                        consumptionPercentageDto2.setResult((consumptionPercentageDto2.getPercentage().compareTo(BigDecimal.valueOf(0.5)) <= 0) ? "pass" : "fail");
                        consumptionPercentageDto2.setRemark("calculated");
                        consumptionPercentageDto2.setMeterSelectedFlag("NA");
                    }
                    dtoList.add(consumptionPercentageDto2);
                }

            }
        }catch (ApiException apiException) {
            logger.error(methodName+" throw apiException");
            throw apiException;
        } catch (DataIntegrityViolationException d) {
            logger.error(methodName+" throw DataIntegrityViolationException");
            throw d;
        } catch (Exception e) {
            logger.error(methodName+" throw common Exception");
            throw e;
            // Handle the exception or log the error as needed
        }
        logger.info(methodName + "end and return 0.5% report dto(s)..");
        return dtoList;
    }
    //set single Main meter DTO value operation
    private MainMeterDto calculateMainMeterCons(String meterNo,Date startDate,Date endDate){
        final String methodName1 = "calculateMainMeterCons() : ";
        logger.info(methodName1 + " called with parameters meterNo={}, startDate = {} and endDate ={}",meterNo,startDate,endDate);
        MainMeterDto mainMeterDto = new MainMeterDto();
        mainMeterDto.setMainMeterNumber(meterNo);

        MeterReadingBean previousReading = meterReadingService.getReadingByMeterNoAndReadingDateAndStatus(
                mainMeterDto.getMainMeterNumber(),startDate,"active");
        MeterReadingBean currentReading = meterReadingService.getReadingByMeterNoAndReadingDateAndStatus(
                mainMeterDto.getMainMeterNumber(),endDate,"active");

        BigDecimal previous = previousReading != null ? previousReading.getEActiveEnergy() : BigDecimal.valueOf(-1);
        BigDecimal current = currentReading != null ? currentReading.getEActiveEnergy() : BigDecimal.valueOf(-1);

        Boolean isBothReadingAvailable = (previous.compareTo(BigDecimal.valueOf(-1)) != 0 && current.compareTo(BigDecimal.valueOf(-1)) != 0);
        
        mainMeterDto.setMainPreviousReading(previous);
        mainMeterDto.setMainCurrentReading(current);
        if (isBothReadingAvailable){
            logger.info(methodName1 + "both reading available for main meter so calculating 0.5% dto");
            mainMeterDto.setMainAssessment(currentReading.getEAssesment());
            mainMeterDto.setMainReadingDifference(current.subtract(previous));
            mainMeterDto.setMainMf(meterMasterService.getMeterDetailsByMeterNo(meterNo, "active").getMf());
            mainMeterDto.setMainConsumption(mainMeterDto.getMainReadingDifference().multiply(mainMeterDto.getMainMf())
                    .setScale(2, RoundingMode.HALF_DOWN));
            mainMeterDto.setMainTotalConsumption(mainMeterDto.getMainConsumption().add(mainMeterDto.getMainAssessment()));
        }else{
            logger.info(methodName1 + "both reading not available for main meter so set value to -1..");
            mainMeterDto.setMainAssessment(BigDecimal.valueOf(-1));
            mainMeterDto.setMainReadingDifference(BigDecimal.valueOf(-1));
            mainMeterDto.setMainMf(meterMasterService.getMeterDetailsByMeterNo(meterNo, "active").getMf());
            mainMeterDto.setMainConsumption(BigDecimal.valueOf(-1));
            mainMeterDto.setMainTotalConsumption(BigDecimal.valueOf(-1));
        }
        logger.info(methodName1 + "returned mainMeterDto success..");
        return mainMeterDto;
    }

    //set single Check meter DTO value operation
    private CheckMeterDto calculateCheckMeterCons(String meterNo,Date startDate,Date endDate){
        final String methodName1 = "calculateCheckMeterCons() : ";
        logger.info(methodName1 + " called with parameters meterNo={}, startDate = {} and endDate ={}",meterNo,startDate,endDate);
        CheckMeterDto checkMeterDto = new CheckMeterDto();
        checkMeterDto.setCheckMeterNumber(meterNo);

        MeterReadingBean previousReading = meterReadingService.getReadingByMeterNoAndReadingDateAndStatus(
                checkMeterDto.getCheckMeterNumber(),startDate,"active");
        MeterReadingBean currentReading = meterReadingService.getReadingByMeterNoAndReadingDateAndStatus(
                checkMeterDto.getCheckMeterNumber(),endDate,"active");

        BigDecimal previous = previousReading != null ? previousReading.getEActiveEnergy() : BigDecimal.valueOf(-1);
        BigDecimal current = currentReading != null ? currentReading.getEActiveEnergy() : BigDecimal.valueOf(-1);

        Boolean isBothReadingAvailable = (previous.compareTo(BigDecimal.valueOf(-1)) != 0 && current.compareTo(BigDecimal.valueOf(-1)) != 0);

        checkMeterDto.setCheckPreviousReading(previous);
        checkMeterDto.setCheckCurrentReading(current);
        if (isBothReadingAvailable){
            logger.info(methodName1 + "both reading available for check meter so calculating 0.5% dto");
            checkMeterDto.setCheckAssessment(currentReading.getEAssesment());
            checkMeterDto.setCheckReadingDifference(current.subtract(previous));
            checkMeterDto.setCheckMf(meterMasterService.getMeterDetailsByMeterNo(meterNo, "active").getMf());
            checkMeterDto.setCheckConsumption(checkMeterDto.getCheckReadingDifference().multiply(checkMeterDto.getCheckMf())
                    .setScale(2, RoundingMode.HALF_DOWN));
            checkMeterDto.setCheckTotalConsumption(checkMeterDto.getCheckConsumption().add(checkMeterDto.getCheckAssessment()));
        }else{
            logger.info(methodName1 + "both reading not available for check meter so set value to -1..");
            checkMeterDto.setCheckAssessment(BigDecimal.valueOf(-1));
            checkMeterDto.setCheckReadingDifference(BigDecimal.valueOf(-1));
            checkMeterDto.setCheckMf(meterMasterService.getMeterDetailsByMeterNo(meterNo, "active").getMf());
            checkMeterDto.setCheckConsumption(BigDecimal.valueOf(-1));
            checkMeterDto.setCheckTotalConsumption(BigDecimal.valueOf(-1));
        }
        logger.info(methodName1 + "returned checkMeterDto success..");
        return checkMeterDto;
    }

    //Convert list ConsumptionPercentDto to string FivePercentBean by passing value bean one by one
    public List<FivePercentBean> consumptionPercentageDto2ToFivePercentageBean(List<ConsumptionPercentageDto2> consumptionPercentageDto2List, String month){
        //If calculation is already done for plant then simple fetch beans and convert to dto, after combine and collect in set
        //List<FivePercentBean> alreadyExistBeans = fivePercentService.getByMonth(month);
        final String methodName1 = "consumptionPercentageDto2ToFivePercentageBean() : ";
        logger.info(methodName1 + " called with parameters consumptionPercentageDto2List={}, month = {}",consumptionPercentageDto2List,month);

        List<FivePercentBean> alreadyExistButNotApproved = fivePercentService.getByMonthAndRemarkEqualTo(month,"calculated");
        List<FivePercentBean> newlyCalculatedBeans = consumptionPercentageDto2List.stream().map((value)->convertDtoToBean(value)).collect(Collectors.toList());
        List<FivePercentBean> combinedBeans = new ArrayList<>(alreadyExistButNotApproved);
                            combinedBeans.addAll(newlyCalculatedBeans);
        Set<FivePercentBean> fivePercentBeanSet = new HashSet<>(combinedBeans);

        logger.info(methodName1 + "return list of FivePercentBean class");
        return fivePercentBeanSet.stream().sorted(Comparator.comparing(FivePercentBean::getDeveloperId)).collect(Collectors.toList());

    }

    //Convert single consumptionpercentagedto to string consumptionpercentagebean
    private FivePercentBean convertDtoToBean(ConsumptionPercentageDto2 dto2){
        //Setup of type map because destination have extra property
        final String methodName1 = "convertDtoToBean() : ";
        logger.info(methodName1 + " Convert single consumptionpercentagedto to string consumptionpercentagebean");

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<ConsumptionPercentageDto2, FivePercentBean>() {
            @Override
            protected void configure() {
                skip(destination.getId());
//                skip(destination.getMonthYear());
            }
        });

        FivePercentBean bean = modelMapper.map(dto2, FivePercentBean.class);

        List<MainMeterDto> mainMeters =  dto2.getMainMeterDtos();
        List<CheckMeterDto> checkMeters = dto2.getCheckMeterDtos();

        bean.setMainMeterNumber(concatDelim(mainMeters.stream().map(MainMeterDto::getMainMeterNumber)));
        bean.setMainCurrentReading(concatDelim(mainMeters.stream().map(m -> m.getMainCurrentReading().toPlainString())).replaceAll("-1","Reading Not Available"));
        bean.setMainPreviousReading(concatDelim(mainMeters.stream().map(m -> m.getMainPreviousReading().toPlainString())).replaceAll("-1","Reading Not Available"));
        bean.setMainReadingDifference(concatDelim(mainMeters.stream().map(m -> m.getMainReadingDifference().toPlainString())).replaceAll("-1","NA"));
        bean.setMainMf(concatDelim(mainMeters.stream().map(m -> m.getMainMf().toPlainString())).replaceAll("-1","NA"));
        bean.setMainAssessment(concatDelim(mainMeters.stream().map(m -> m.getMainAssessment().toPlainString())).replaceAll("-1","NA"));
        bean.setMainConsumption(concatDelim(mainMeters.stream().map(m -> m.getMainConsumption().toPlainString())).replaceAll("-1","NA"));
        bean.setMainTotalConsumption(String.valueOf(dto2.getMainGrandTotalConsumption().toPlainString()).replaceAll("-1","NA"));

        bean.setCheckMeterNumber(concatDelim(checkMeters.stream().map(CheckMeterDto::getCheckMeterNumber)));
        bean.setCheckCurrentReading(concatDelim(checkMeters.stream().map(c -> c.getCheckCurrentReading().toPlainString())).replaceAll("-1","Reading Not Available"));
        bean.setCheckPreviousReading(concatDelim(checkMeters.stream().map(c -> c.getCheckPreviousReading().toPlainString())).replaceAll("-1","Reading Not Available"));
        bean.setCheckReadingDifference(concatDelim(checkMeters.stream().map(c -> c.getCheckReadingDifference().toPlainString())).replaceAll("-1","NA"));
        bean.setCheckMf(concatDelim(checkMeters.stream().map(c -> c.getCheckMf().toPlainString())).replaceAll("-1","NA"));
        bean.setCheckAssessment(concatDelim(checkMeters.stream().map(c -> c.getCheckAssessment().toPlainString())).replaceAll("-1","NA"));
        bean.setCheckConsumption(concatDelim(checkMeters.stream().map(c -> c.getCheckConsumption().toPlainString())).replaceAll("-1","NA"));
        bean.setCheckTotalConsumption(String.valueOf(dto2.getCheckGrandTotalConsumption().toPlainString()).replaceAll("-1","NA"));

        logger.info(methodName1 + "return five percent bean..");
        return bean;
    }

    // for converting string and Concatnate # for multiple entry present
    private static String concatDelim(Stream<Object> stream) {
        return stream.map(Object::toString)
                .collect(Collectors.collectingAndThen(
                        Collectors.joining("#"),
                        result -> result.isEmpty() ? "" : result
                ));
    }


}

