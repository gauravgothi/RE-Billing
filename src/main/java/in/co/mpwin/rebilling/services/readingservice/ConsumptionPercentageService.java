package in.co.mpwin.rebilling.services.readingservice;

import in.co.mpwin.rebilling.beans.developermaster.DeveloperMasterBean;
import in.co.mpwin.rebilling.beans.mapping.MeterFeederPlantMappingBean;
import in.co.mpwin.rebilling.beans.readingbean.MeterReadingBean;
import in.co.mpwin.rebilling.dto.ConsumptionPercentageDto;
import in.co.mpwin.rebilling.beans.readingbean.FivePercentBean;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConsumptionPercentageService {
    @Autowired private ModelMapper mapper;
    @Autowired
    private DeveloperMasterService developerMasterService;
    @Autowired private MeterFeederPlantMappingService meterFeederPlantMappingService;
    @Autowired private FeederMasterService feederMasterService;
    @Autowired private DateMethods dateMethods;
    @Autowired private MeterReadingService meterReadingService;
    @Autowired MeterMasterService meterMasterService;

    public List<ConsumptionPercentageDto> calculatePercentageReport(String month) throws ParseException {
        List<ConsumptionPercentageDto> dtoList = new ArrayList<>();
        for (DeveloperMasterBean developer : developerMasterService.getAllDeveloperMasterBean("active")) {
            for (MeterFeederPlantMappingBean mapping : meterFeederPlantMappingService.getMappingByDeveloperId(String.valueOf(developer.getId()), "active")) {
                ConsumptionPercentageDto percentageDto = new ConsumptionPercentageDto();
                percentageDto.setDeveloperId(String.valueOf(developer.getId()));
                percentageDto.setDeveloperName(developer.getDeveloperName());
                percentageDto.setDeveloperSiteAddress(developer.getSiteAddress());
                percentageDto.setMainMeterNumber(mapping.getMainMeterNo());
                percentageDto.setCheckMeterNumber(mapping.getCheckMeterNo());
                percentageDto.setFeederCode(mapping.getFeederCode());
                percentageDto.setFeederName(feederMasterService.getFeederByFeederNumber(percentageDto.getFeederCode(),"active").getFeederName());
                // set meter parameters
                setMeterParameters(percentageDto, mapping.getMainMeterNo(), month, true);
                setMeterParameters(percentageDto, mapping.getCheckMeterNo(), month, false);
                if (    percentageDto.getMainPreviousReading().compareTo(BigDecimal.valueOf(-1)) != 0 &&
                        percentageDto.getMainCurrentReading().compareTo(BigDecimal.valueOf(-1)) != 0 &&
                        percentageDto.getCheckPreviousReading().compareTo(BigDecimal.valueOf(-1)) != 0 &&
                        percentageDto.getCheckCurrentReading().compareTo(BigDecimal.valueOf(-1)) != 0)  {
                    //calculate percentage(main total consumption - check total consumption *100)
                    percentageDto.setPercentage((percentageDto.getMainTotalConsumption().subtract(percentageDto.getCheckTotalConsumption())
                            .divide(percentageDto.getMainTotalConsumption(), 2, RoundingMode.HALF_DOWN)).multiply(BigDecimal.valueOf(100)).abs());
                    percentageDto.setResult((percentageDto.getPercentage().compareTo(BigDecimal.valueOf(0.5)) <= 0) ? "pass" : "fail");
                } else {
                    percentageDto.setPercentage(BigDecimal.valueOf(-1));
                    percentageDto.setResult("withheld");
                }
                dtoList.add(percentageDto);
            }
        }
        return dtoList;
    }

    private void setMeterParameters(ConsumptionPercentageDto percentageDto, String meterNumber, String month, boolean isMain) throws ParseException {
        Date previousReadDate = dateMethods.getCurrentAndPreviousDate(month).get(0);
        Date currentReadDate = dateMethods.getCurrentAndPreviousDate(month).get(1);

        MeterReadingBean previousReading = meterReadingService.getReadingByMeterNoAndReadingDateAndStatus(meterNumber, previousReadDate, "active");
        MeterReadingBean currentReading = meterReadingService.getReadingByMeterNoAndReadingDateAndStatus(meterNumber, currentReadDate, "active");

        BigDecimal previous = previousReading != null ? previousReading.getEActiveEnergy() : BigDecimal.valueOf(-1);
        BigDecimal current = currentReading != null ? currentReading.getEActiveEnergy() : BigDecimal.valueOf(-1);

        Boolean isBothReadingAvailable = (previous.compareTo(BigDecimal.valueOf(-1)) != 0 && current.compareTo(BigDecimal.valueOf(-1)) != 0);
        if (isMain ) {
            percentageDto.setMainPreviousReading(previous);
            percentageDto.setMainCurrentReading(current);
            if (isBothReadingAvailable) {
                percentageDto.setMainAssessment(currentReading.getEAssesment());
                percentageDto.setMainReadingDifference(current.subtract(previous).abs());
                percentageDto.setMainMf(meterMasterService.getMeterDetailsByMeterNo(meterNumber, "active").getMf());
                percentageDto.setMainConsumption(percentageDto.getMainReadingDifference().multiply(percentageDto.getMainMf())
                        .setScale(2,RoundingMode.HALF_DOWN));
                percentageDto.setMainTotalConsumption(percentageDto.getMainConsumption().add(percentageDto.getMainAssessment()));

            }else{
                percentageDto.setMainAssessment(BigDecimal.valueOf(-1));
                percentageDto.setMainReadingDifference(BigDecimal.valueOf(-1));
                percentageDto.setMainMf(meterMasterService.getMeterDetailsByMeterNo(meterNumber, "active").getMf());
                percentageDto.setMainConsumption(BigDecimal.valueOf(-1));
                percentageDto.setMainTotalConsumption(BigDecimal.valueOf(-1));
            }

        } else if(!isMain) {
            percentageDto.setCheckPreviousReading(previous);
            percentageDto.setCheckCurrentReading(current);
            if (isBothReadingAvailable) {
                percentageDto.setCheckAssessment(currentReading.getEAssesment());
                percentageDto.setCheckReadingDifference(current.subtract(previous));
                percentageDto.setCheckMf(meterMasterService.getMeterDetailsByMeterNo(meterNumber, "active").getMf());
                percentageDto.setCheckConsumption(percentageDto.getCheckReadingDifference().multiply(percentageDto.getCheckMf())
                        .setScale(2,RoundingMode.HALF_DOWN));
                percentageDto.setCheckTotalConsumption(percentageDto.getCheckConsumption().add(percentageDto.getCheckAssessment()));
            }else{
                percentageDto.setCheckAssessment(BigDecimal.valueOf(-1));
                percentageDto.setCheckReadingDifference(BigDecimal.valueOf(-1));
                percentageDto.setCheckMf(meterMasterService.getMeterDetailsByMeterNo(meterNumber, "active").getMf());
                percentageDto.setCheckConsumption(BigDecimal.valueOf(-1));
                percentageDto.setCheckTotalConsumption(BigDecimal.valueOf(-1));
            }
        }

    }

    public List<FivePercentBean> percentageBeanToDto(List<ConsumptionPercentageDto> consumptionPercentageDtoList){

        return consumptionPercentageDtoList.stream().map((bean)->convertBeanToDto(bean)).collect(Collectors.toList());
    }

    private FivePercentBean convertBeanToDto(ConsumptionPercentageDto bean){
        FivePercentBean dto = mapper.map(bean, FivePercentBean.class);

        //set all big decimal parameters to string for report purpose
        dto.setMainCurrentReading(dto.getMainCurrentReading().replaceAll("-1","Reading Not Available"));
        dto.setMainPreviousReading(dto.getMainPreviousReading().replaceAll("-1","Reading Not Available"));
        dto.setMainReadingDifference(dto.getMainReadingDifference().replaceAll("-1","Reading Not Available"));
        //dto.getMainMf().replaceAll("-1","Reading Not Available");
        dto.setMainAssessment(dto.getMainAssessment().replaceAll("-1","Reading Not Available"));
        dto.setMainConsumption(dto.getMainConsumption().replaceAll("-1","Reading Not Available"));
        dto.setMainTotalConsumption(dto.getMainTotalConsumption().replaceAll("-1","Reading Not Available"));

        //set all big decimal parameters to string for report purpose
        dto.setCheckCurrentReading(dto.getCheckCurrentReading().replaceAll("-1","Reading Not Available"));
        dto.setCheckPreviousReading(dto.getCheckPreviousReading().replaceAll("-1","Reading Not Available"));
        dto.setCheckReadingDifference(dto.getCheckReadingDifference().replaceAll("-1","Reading Not Available"));
        //dto.getCheckMf().replaceAll("-1","Reading Not Available");
        dto.setCheckAssessment(dto.getCheckAssessment().replaceAll("-1","Reading Not Available"));
        dto.setCheckConsumption(dto.getCheckConsumption().replaceAll("-1","Reading Not Available"));
        dto.setCheckTotalConsumption(dto.getCheckTotalConsumption().replaceAll("-1","Reading Not Available"));

        return dto;
    }
}
