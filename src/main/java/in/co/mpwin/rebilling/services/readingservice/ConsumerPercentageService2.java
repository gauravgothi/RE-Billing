package in.co.mpwin.rebilling.services.readingservice;

import in.co.mpwin.rebilling.beans.developermaster.DeveloperMasterBean;
import in.co.mpwin.rebilling.beans.mapping.MeterFeederPlantMappingBean;
import in.co.mpwin.rebilling.beans.readingbean.MeterReadingBean;
import in.co.mpwin.rebilling.dto.*;
import in.co.mpwin.rebilling.miscellanious.DateMethods;
import in.co.mpwin.rebilling.services.developermaster.DeveloperMasterService;
import in.co.mpwin.rebilling.services.feedermaster.FeederMasterService;
import in.co.mpwin.rebilling.services.mapping.MeterFeederPlantMappingService;
import in.co.mpwin.rebilling.services.metermaster.MeterMasterService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired private ModelMapper mapper;
    @Autowired private DeveloperMasterService developerMasterService;
    @Autowired private MeterFeederPlantMappingService meterFeederPlantMappingService;
    @Autowired private FeederMasterService feederMasterService;
    @Autowired private DateMethods dateMethods;
    @Autowired private MeterReadingService meterReadingService;
    @Autowired MeterMasterService meterMasterService;

    public List<ConsumptionPercentageDto2> calculatePercentageReport2(Date startDate, Date endDate) throws ParseException{
        List<ConsumptionPercentageDto2> dtoList = new ArrayList<>();
        Date futureEndDate = new SimpleDateFormat("dd-MM-yyyy").parse("01-12-2023");
        List<DeveloperMasterBean> developerList = developerMasterService.getAllDeveloperMasterBean("active");
        for (int k =0;developerList.size()>k;k++){

            List<MeterFeederPlantMappingBean> mappingBeanList = meterFeederPlantMappingService
                    .getMappingByDeveloperIdOrderByEndDate(String.valueOf(developerList.get(k).getId()), "active");
            // now arrange it plant wise in map <PlantCode,List<MFPBean>>
            List<Map<String,List<MeterFeederPlantMappingBean>>> plantMFPList = new ArrayList<>();
            List<String> distinctPlants = meterFeederPlantMappingService.getDistinctPlantCodeByDeveloperId(String.valueOf(developerList.get(k).getId()), "active");
            for (String plant : distinctPlants){
                Map<String,List<MeterFeederPlantMappingBean>> singlePlantMap = new HashMap<>();
                singlePlantMap.put(plant,mappingBeanList.stream().filter(bean -> ((bean.getPlantCode().equals(plant)) &&
                        (((bean.getEndDate().after(startDate)) && (bean.getEndDate().before(endDate))) ||
                                (bean.getEndDate().compareTo(futureEndDate) == 0))))
                        .sorted(Comparator.comparing(MeterFeederPlantMappingBean::getEndDate))
                        .collect(Collectors.toList()));

                plantMFPList.add(singlePlantMap);
            }
            //now run loop on map to calculate main meters consumption
            for(Map<String,List<MeterFeederPlantMappingBean>> plantMFP : plantMFPList){
                ConsumptionPercentageDto2 consumptionPercentageDto2 = new ConsumptionPercentageDto2();
                consumptionPercentageDto2.setDeveloperId(String.valueOf(developerList.get(k).getId()));
                consumptionPercentageDto2.setDeveloperName(developerList.get(k).getDeveloperName());
                consumptionPercentageDto2.setDeveloperSiteAddress(developerList.get(k).getSiteAddress());
                
                List<MainMeterDto> mainMeterDtos = new ArrayList<>();
                List<CheckMeterDto> checkMeterDtos = new ArrayList<>();
                List<MeterFeederPlantMappingBean> MFPBeans = plantMFP.values().iterator().next();

                consumptionPercentageDto2.setFeederCode(MFPBeans.get(0).getFeederCode());
                consumptionPercentageDto2.setFeederName(feederMasterService.getFeederByFeederNumber(
                        MFPBeans.get(0).getFeederCode(),"active").getFeederName());
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
                if (anyCheckMeterReadAbsent || anyCheckMeterReadAbsent )    {
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
                    consumptionPercentageDto2.setPercentage((consumptionPercentageDto2.getMainGrandTotalConsumption()
                            .subtract(consumptionPercentageDto2.getCheckGrandTotalConsumption())
                            .divide(consumptionPercentageDto2.getMainGrandTotalConsumption(), 6, RoundingMode.HALF_DOWN))
                            .multiply(BigDecimal.valueOf(100)).abs());
                    consumptionPercentageDto2.setResult((consumptionPercentageDto2.getPercentage().compareTo(BigDecimal.valueOf(0.5)) <= 0) ? "pass" : "fail");
                }
                dtoList.add(consumptionPercentageDto2);
            }

        }
        return dtoList;
    }
    //set single Main meter DTO value operation
    private MainMeterDto calculateMainMeterCons(String meterNo,Date startDate,Date endDate){
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
            mainMeterDto.setMainAssessment(currentReading.getEAssesment());
            mainMeterDto.setMainReadingDifference(current.subtract(previous));
            mainMeterDto.setMainMf(meterMasterService.getMeterDetailsByMeterNo(meterNo, "active").getMf());
            mainMeterDto.setMainConsumption(mainMeterDto.getMainReadingDifference().multiply(mainMeterDto.getMainMf())
                    .setScale(6, RoundingMode.HALF_DOWN));
            mainMeterDto.setMainTotalConsumption(mainMeterDto.getMainConsumption().add(mainMeterDto.getMainAssessment()));
        }else{
            mainMeterDto.setMainAssessment(BigDecimal.valueOf(-1));
            mainMeterDto.setMainReadingDifference(BigDecimal.valueOf(-1));
            mainMeterDto.setMainMf(meterMasterService.getMeterDetailsByMeterNo(meterNo, "active").getMf());
            mainMeterDto.setMainConsumption(BigDecimal.valueOf(-1));
            mainMeterDto.setMainTotalConsumption(BigDecimal.valueOf(-1));
        }
        return mainMeterDto;
    }

    //set single Check meter DTO value operation
    private CheckMeterDto calculateCheckMeterCons(String meterNo,Date startDate,Date endDate){
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
            checkMeterDto.setCheckAssessment(currentReading.getEAssesment());
            checkMeterDto.setCheckReadingDifference(current.subtract(previous));
            checkMeterDto.setCheckMf(meterMasterService.getMeterDetailsByMeterNo(meterNo, "active").getMf());
            checkMeterDto.setCheckConsumption(checkMeterDto.getCheckReadingDifference().multiply(checkMeterDto.getCheckMf())
                    .setScale(6, RoundingMode.HALF_DOWN));
            checkMeterDto.setCheckTotalConsumption(checkMeterDto.getCheckConsumption().add(checkMeterDto.getCheckAssessment()));
        }else{
            checkMeterDto.setCheckAssessment(BigDecimal.valueOf(-1));
            checkMeterDto.setCheckReadingDifference(BigDecimal.valueOf(-1));
            checkMeterDto.setCheckMf(meterMasterService.getMeterDetailsByMeterNo(meterNo, "active").getMf());
            checkMeterDto.setCheckConsumption(BigDecimal.valueOf(-1));
            checkMeterDto.setCheckTotalConsumption(BigDecimal.valueOf(-1));
        }
        return checkMeterDto;
    }

    //Convert list value beans to string DTOs by passing value bean one by one
    public List<FivePercentageDto> percentageBeanToDto(List<ConsumptionPercentageDto2> consumptionPercentageDto2List){

        return consumptionPercentageDto2List.stream().map((bean)->convertBeanToDto(bean)).collect(Collectors.toList());
    }

    //Convert single value bean to string DTO
    private FivePercentageDto convertBeanToDto(ConsumptionPercentageDto2 bean){
        FivePercentageDto dto = mapper.map(bean,FivePercentageDto.class);

        List<MainMeterDto> mainMeters =  bean.getMainMeterDtos();
        List<CheckMeterDto> checkMeters = bean.getCheckMeterDtos();

        dto.setMainMeterNumber(concatDelim(mainMeters.stream().map(MainMeterDto::getMainMeterNumber)));
        dto.setMainCurrentReading(concatDelim(mainMeters.stream().map(MainMeterDto::getMainCurrentReading)).replaceAll("-1","Reading Not Available"));
        dto.setMainPreviousReading(concatDelim(mainMeters.stream().map(MainMeterDto::getMainPreviousReading)).replaceAll("-1","Reading Not Available"));
        dto.setMainReadingDifference(concatDelim(mainMeters.stream().map(MainMeterDto::getMainReadingDifference)).replaceAll("-1","NA"));
        dto.setMainMf(concatDelim(mainMeters.stream().map(MainMeterDto::getMainMf)).replaceAll("-1","NA"));
        dto.setMainAssessment(concatDelim(mainMeters.stream().map(MainMeterDto::getMainAssessment)).replaceAll("-1","NA"));
        dto.setMainConsumption(concatDelim(mainMeters.stream().map(MainMeterDto::getMainConsumption)).replaceAll("-1","NA"));
        dto.setMainTotalConsumption(String.valueOf(bean.getMainGrandTotalConsumption()).replaceAll("-1","NA"));

        dto.setCheckMeterNumber(concatDelim(checkMeters.stream().map(CheckMeterDto::getCheckMeterNumber)));
        dto.setCheckCurrentReading(concatDelim(checkMeters.stream().map(CheckMeterDto::getCheckCurrentReading)).replaceAll("-1","Reading Not Available"));
        dto.setCheckPreviousReading(concatDelim(checkMeters.stream().map(CheckMeterDto::getCheckPreviousReading)).replaceAll("-1","Reading Not Available"));
        dto.setCheckReadingDifference(concatDelim(checkMeters.stream().map(CheckMeterDto::getCheckReadingDifference)).replaceAll("-1","NA"));
        dto.setCheckMf(concatDelim(checkMeters.stream().map(CheckMeterDto::getCheckMf)).replaceAll("-1","NA"));
        dto.setCheckAssessment(concatDelim(checkMeters.stream().map(CheckMeterDto::getCheckAssessment)).replaceAll("-1","NA"));
        dto.setCheckConsumption(concatDelim(checkMeters.stream().map(CheckMeterDto::getCheckConsumption)).replaceAll("-1","NA"));
        dto.setCheckTotalConsumption(String.valueOf(bean.getCheckGrandTotalConsumption()).replaceAll("-1","NA"));
        
        return dto;
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
