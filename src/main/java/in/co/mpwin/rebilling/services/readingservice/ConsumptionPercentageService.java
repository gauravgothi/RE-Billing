package in.co.mpwin.rebilling.services.readingservice;

import in.co.mpwin.rebilling.beans.developermaster.DeveloperMasterBean;
import in.co.mpwin.rebilling.beans.mapping.MeterFeederPlantMappingBean;
import in.co.mpwin.rebilling.beans.readingbean.MeterReadingBean;
import in.co.mpwin.rebilling.dto.ConsumptionPercentageDto;
import in.co.mpwin.rebilling.miscellanious.DateMethods;
import in.co.mpwin.rebilling.services.developermaster.DeveloperMasterService;
import in.co.mpwin.rebilling.services.feedermaster.FeederMasterService;
import in.co.mpwin.rebilling.services.investormaster.InvestorMasterService;
import in.co.mpwin.rebilling.services.mapping.MeterFeederPlantMappingService;
import in.co.mpwin.rebilling.services.metermaster.MeterMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ConsumptionPercentageService {

    @Autowired
    DeveloperMasterService developerMasterService;

    @Autowired
    MeterFeederPlantMappingService meterFeederPlantMappingService;

    @Autowired
    MeterMasterService meterMasterService;

    @Autowired
    FeederMasterService feederMasterService;

    @Autowired
    InvestorMasterService investorMasterService;

    @Autowired
    DateMethods dateMethods;
    @Autowired
    MeterReadingService meterReadingService;

    public List<ConsumptionPercentageDto> calculatePercentageReport(String month) throws ParseException {
        try {
            List<ConsumptionPercentageDto> dtoList = new ArrayList<>();
            List<DeveloperMasterBean> developerList = developerMasterService.getAllDeveloperMasterBean("active");

            for (int i = 0; i < developerList.size(); i++) {

                List<MeterFeederPlantMappingBean> MFPmappings = meterFeederPlantMappingService.getMappingByDeveloperId(String.valueOf(developerList.get(i).getId()), "active");
                for (int k = 0; k < MFPmappings.size(); k++) {
                    ConsumptionPercentageDto percentageDto = new ConsumptionPercentageDto();
                    percentageDto.setDeveloperId(String.valueOf(developerList.get(i).getId())); //developer id set
                    percentageDto.setDeveloperName(developerList.get(i).getDeveloperName());  //set developer name
                    percentageDto.setDeveloperSiteAddress(developerList.get(i).getSiteAddress());  //set developer site address
                    percentageDto.setMainMeterNumber(MFPmappings.get(k).getMainMeterNo()); //set developer main meter number
                    percentageDto.setCheckMeterNumber(MFPmappings.get(k).getCheckMeterNo()); //set developer check meter number
                    percentageDto.setFeederCode(MFPmappings.get(k).getFeederCode());  //get feeder code from mfp mapping
                    percentageDto.setFeederName(feederMasterService.getFeederByFeederNumber(percentageDto.getFeederCode(),"active").getFeederName());

                    setMainMeterParameters(percentageDto,month);
                    setCheckMeterParameters(percentageDto,month);

                    if (    !(percentageDto.getMainCurrentReading().compareTo(BigDecimal.valueOf(-1)) == 0) &&
                            !(percentageDto.getMainPreviousReading().compareTo(BigDecimal.valueOf(-1)) == 0) &&
                            !(percentageDto.getCheckCurrentReading().compareTo(BigDecimal.valueOf(-1)) == 0) &&
                            !(percentageDto.getCheckPreviousReading().compareTo(BigDecimal.valueOf(-1)) == 0))
                        {
                        //calculate percentage(main total consumption - check total consumption *100)
                        percentageDto.setPercentage((percentageDto.getMainTotalConsumption().subtract(percentageDto.getCheckTotalConsumption())
                                .divide(percentageDto.getMainConsumption(), 6, RoundingMode.HALF_UP)).multiply(BigDecimal.valueOf(100)).abs());
                        percentageDto.setResult((percentageDto.getPercentage().compareTo(BigDecimal.valueOf(0.5)) <= 0) ? "pass" : "fail");
                    }else
                        percentageDto.setResult("withheld");
                    // add dto to list
                    dtoList.add(percentageDto);
                }

            }
            return dtoList;
        }catch (ParseException e){
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }


    }


    //local methods to set main meter parameters
    private void setMainMeterParameters(ConsumptionPercentageDto percentageDto,String month) throws ParseException {
        try {
                Date currentReadDate = dateMethods.getCurrentAndPreviousDate(month).get(0);
                Date previousReadDate = dateMethods.getCurrentAndPreviousDate(month).get(1);

            MeterReadingBean previousReading = meterReadingService.getReadingByMeterNoAndReadingDateAndStatus(percentageDto.getMainMeterNumber()
                                        ,previousReadDate , "active");
            MeterReadingBean currentReading = meterReadingService.getReadingByMeterNoAndReadingDateAndStatus(
                                 percentageDto.getMainMeterNumber(),currentReadDate , "active");
                //set current and previous reading of main meter
                mainMeterReadingValidation(percentageDto,previousReading,currentReading);

                if (!(percentageDto.getMainCurrentReading().compareTo(BigDecimal.valueOf(-1)) == 0) &&
                    !(percentageDto.getMainPreviousReading().compareTo(BigDecimal.valueOf(-1)) == 0)) {
                    //set main meter assessment from main meter current read
                    percentageDto.setMainAssessment((meterReadingService.getReadingByMeterNoAndReadingDateAndStatus(
                            percentageDto.getMainMeterNumber(),currentReadDate,"active").getEAssesment()
                    ));
                    //set main meter readings difference (current read - previous read)
                    percentageDto.setMainReadingDifference((percentageDto.getMainCurrentReading().subtract(percentageDto.getMainPreviousReading())));
                    //set main meter mf from meter master by active meter number
                    percentageDto.setMainMf(meterMasterService.getMeterDetailsByMeterNo(percentageDto.getMainMeterNumber(), "active").getMf());
                    //set main meter consumption (main reading difference multiply with mf)
                    percentageDto.setMainConsumption(percentageDto.getMainReadingDifference().multiply(percentageDto.getMainMf()));
                    //set main meter total consumption (main meter consumption + main meter assessment)
                    percentageDto.setMainTotalConsumption(percentageDto.getMainConsumption().add(percentageDto.getMainAssessment()));
                }
        }catch (ParseException e){
            throw e;
        }catch (Exception e){
            throw e;
        }

        
    }

    //local methods to set check meter parameters
    private void setCheckMeterParameters(ConsumptionPercentageDto percentageDto,String month) throws ParseException {
        try {
                Date currentReadDate = dateMethods.getCurrentAndPreviousDate(month).get(0);
                Date previousReadDate = dateMethods.getCurrentAndPreviousDate(month).get(1);


                MeterReadingBean previousReading = meterReadingService.getReadingByMeterNoAndReadingDateAndStatus(percentageDto.getCheckMeterNumber()
                                                                            ,previousReadDate , "active");
                MeterReadingBean currentReading = meterReadingService.getReadingByMeterNoAndReadingDateAndStatus(
                        percentageDto.getCheckMeterNumber(),currentReadDate , "active");

                    //set current and previous reading of check meter
                    checkMeterReadingValidation(percentageDto,previousReading,currentReading);

                    if (!(percentageDto.getCheckCurrentReading().compareTo(BigDecimal.valueOf(-1)) == 0) &&
                            !(percentageDto.getCheckPreviousReading().compareTo(BigDecimal.valueOf(-1)) == 0)) {
                        //set Check meter assessment from Check meter current read
                        percentageDto.setCheckAssessment((meterReadingService.getReadingByMeterNoAndReadingDateAndStatus(
                                percentageDto.getCheckMeterNumber(), currentReadDate, "active").getEAssesment()
                        ));
                        //set Check meter readings difference (current read - previous read)
                        percentageDto.setCheckReadingDifference((percentageDto.getCheckCurrentReading().subtract(percentageDto.getCheckPreviousReading())));
                        //set Check meter mf from meter master by active meter number
                        percentageDto.setCheckMf(meterMasterService.getMeterDetailsByMeterNo(percentageDto.getCheckMeterNumber(), "active").getMf());
                        //set Check meter consumption (Check reading difference multiply with mf)
                        percentageDto.setCheckConsumption(percentageDto.getCheckReadingDifference().multiply(percentageDto.getCheckMf()));
                        //set Check meter total consumption (Check meter consumption + Check meter assessment)
                        percentageDto.setCheckTotalConsumption(percentageDto.getCheckConsumption().add(percentageDto.getCheckAssessment()));
                    }
        }catch (ParseException e){
            throw e;
        }catch (NullPointerException e){
            e.printStackTrace();
        }catch (Exception e){
            throw e;
        }
    }

    private void checkMeterReadingValidation(ConsumptionPercentageDto percentageDto,MeterReadingBean previousReading,
                                             MeterReadingBean currentReading){
        if (previousReading != null && currentReading !=null) {

            //set check meter previous reading
            percentageDto.setCheckPreviousReading(previousReading.getEActiveEnergy());
            //set Check meter current reading
            percentageDto.setCheckCurrentReading(currentReading.getEActiveEnergy());

        }else if (previousReading ==null && currentReading !=null) {

            //set check meter previous reading
            percentageDto.setCheckPreviousReading(BigDecimal.valueOf(-1));
            //set Check meter current reading
            percentageDto.setCheckCurrentReading(currentReading.getEActiveEnergy());

        } else if (previousReading !=null && currentReading ==null) {

            //set check meter previous reading
            percentageDto.setCheckPreviousReading(previousReading.getEActiveEnergy());
            //set check current meter reading
            percentageDto.setCheckCurrentReading(BigDecimal.valueOf(-1));

        } else if (previousReading ==null && currentReading ==null) {

            //set check meter previous reading
            percentageDto.setCheckPreviousReading(BigDecimal.valueOf(-1));
            //set check current meter reading
            percentageDto.setCheckCurrentReading(BigDecimal.valueOf(-1));

        }
    }

    private void mainMeterReadingValidation(ConsumptionPercentageDto percentageDto,MeterReadingBean previousReading,
                                             MeterReadingBean currentReading){
        if (previousReading != null && currentReading !=null) {

            //set check meter previous reading
            percentageDto.setMainPreviousReading(previousReading.getEActiveEnergy());
            //set Check meter current reading
            percentageDto.setMainCurrentReading(currentReading.getEActiveEnergy());

        }else if (previousReading ==null && currentReading !=null) {

            //set check meter previous reading
            percentageDto.setMainPreviousReading(BigDecimal.valueOf(-1));
            //set Check meter current reading
            percentageDto.setMainCurrentReading(currentReading.getEActiveEnergy());

        } else if (previousReading !=null && currentReading ==null) {

            //set check meter previous reading
            percentageDto.setMainPreviousReading(previousReading.getEActiveEnergy());
            //set check current meter reading
            percentageDto.setMainCurrentReading(BigDecimal.valueOf(-1));

        } else if (previousReading ==null && currentReading ==null) {

            //set check meter previous reading
            percentageDto.setMainPreviousReading(BigDecimal.valueOf(-1));
            //set check current meter reading
            percentageDto.setMainCurrentReading(BigDecimal.valueOf(-1));

        }
    }

}
