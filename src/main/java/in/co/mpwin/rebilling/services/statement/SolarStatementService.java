package in.co.mpwin.rebilling.services.statement;

import in.co.mpwin.rebilling.beans.feedermaster.FeederMasterBean;
import in.co.mpwin.rebilling.beans.investormaster.InvestorMasterBean;
import in.co.mpwin.rebilling.beans.machinemaster.MachineMasterBean;
import in.co.mpwin.rebilling.beans.mapping.InvestorMachineMappingBean;
import in.co.mpwin.rebilling.beans.mapping.MeterFeederPlantMappingBean;
import in.co.mpwin.rebilling.beans.plantmaster.PlantMasterBean;
import in.co.mpwin.rebilling.beans.statement.SolarStatementBean;
import in.co.mpwin.rebilling.beans.statement.ThirdPartyTod;
import in.co.mpwin.rebilling.beans.thirdparty.ThirdPartyBean;
import in.co.mpwin.rebilling.dto.BifurcateInvestorDto;
import in.co.mpwin.rebilling.dto.MeterConsumptionDto;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.repositories.metermaster.MeterMasterRepo;
import in.co.mpwin.rebilling.services.feedermaster.FeederMasterService;
import in.co.mpwin.rebilling.services.investormaster.InvestorMasterService;
import in.co.mpwin.rebilling.services.machinemaster.MachineMasterService;
import in.co.mpwin.rebilling.services.mapping.InvestorMachineMappingService;
import in.co.mpwin.rebilling.services.mapping.MeterFeederPlantMappingService;
import in.co.mpwin.rebilling.services.plantmaster.PlantMasterService;
import in.co.mpwin.rebilling.services.readingservice.MeterReadingService;
import in.co.mpwin.rebilling.services.thirdparty.ThirdPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SolarStatementService {

    @Autowired
    private MeterFeederPlantMappingService mfpService;
    @Autowired
    private InvestorMachineMappingService investorMachineMappingService;
    @Autowired
    private InvestorMasterService investorMasterService;
    @Autowired
    private MachineMasterService machineMasterService;
    @Autowired
    private PlantMasterService plantMasterService;
    @Autowired
    private MeterReadingService meterReadingService;
    @Autowired
    private ThirdPartyService thirdPartyService;

    @Autowired
    private FeederMasterService feederMasterService;
    @Autowired
    private MeterMasterRepo meterMasterRepo;

    public List<SolarStatementBean> getSolarStatement(String meterNo, String monthYear) throws ParseException {
        try {
            List<SolarStatementBean> solarStatementBeanList = new ArrayList<>();
            //Third check is meter is only one out of two meter present in mapping , means if check already done then main not possible or vice versa
            MeterFeederPlantMappingBean mfpBean = mfpService.getByAnyMeterNoAndStatus(meterNo, "active");
            FeederMasterBean feederMasterBean = feederMasterService.getFeederByFeederNumber(mfpBean.getFeederCode(), "active");
            if (feederMasterBean == null)
                throw new ApiException(HttpStatus.BAD_REQUEST, "Not Any Feeder Present in Feeder master table..");
            String plantCode = mfpBean.getPlantCode();
            PlantMasterBean plantMasterBean = plantMasterService.getPlantByPlantCode(plantCode, "active");
            if (plantMasterBean == null)
                throw new ApiException(HttpStatus.BAD_REQUEST, "Not Any Plant Present in plant master table..");
            Long mfpBeanId = mfpBean.getId();//This is used for getting investor related to plant

            //Fetch Distinct Investor list from InvestorMachine Mapping belongs by Meter Feeder Mapping id
            List<InvestorMachineMappingBean> investorMachineMappingBeans = investorMachineMappingService.getMappingByMFPId(mfpBeanId, "active");
            if (investorMachineMappingBeans.size() == 0)
                throw new ApiException(HttpStatus.BAD_REQUEST, "Not Any Investor Mapped to this plant..");

            List<String> investorCodes = investorMachineMappingBeans.stream().map(InvestorMachineMappingBean::getInvestorCode)
                    .distinct().collect(Collectors.toList());
//        if (investorCodes.size() > 1)
//            throw new ApiException(HttpStatus.BAD_REQUEST, "More than one investor present on solar power plant");

            for (String investor : investorCodes) {
                SolarStatementBean solarStatementBean = new SolarStatementBean();
                InvestorMasterBean investorMasterBean = investorMasterService.getInvestorByInvestorCode(investor, "active");
                if (investorMasterBean == null)
                    throw new ApiException(HttpStatus.BAD_REQUEST, investor + " No investor present in investor master..");

                solarStatementBean.setMeterNumber(meterNo);
                solarStatementBean.setMonthYear(monthYear);
                solarStatementBean.setPlantCode(plantMasterBean.getPlantCode());
                solarStatementBean.setPlantName(plantMasterBean.getPlantName());
                solarStatementBean.setPlantSiteLocation(plantMasterBean.getAddress());
                solarStatementBean.setPlantCommisionedDate(plantMasterBean.getCommissionedDate());
                solarStatementBean.setFeederNumber(feederMasterBean.getFeederNumber());
                solarStatementBean.setFeederName(feederMasterBean.getFeederName());
                solarStatementBean.setFeederCircuitVoltage(feederMasterBean.getCircuitVoltage());
                solarStatementBean.setFeederInjectingSs(feederMasterBean.getInjectingSsName());
                solarStatementBean.setInvestorCode(investorMasterBean.getInvestorCode());
                solarStatementBean.setInvestorName(investorMasterBean.getInvestorName());
                solarStatementBean.setInvestorNldcNumber(investorMasterBean.getNldc());

                //Get machine code by investor code
                List<String> machineCodes = investorMachineMappingService.getMappingByInvestorCode(investor, "active").stream()
                        .map(InvestorMachineMappingBean::getMachineCode).collect(Collectors.toList());
                // after getting machine codes of an investor then sum machine capacity
                List<MachineMasterBean> machineMasterBeanList = machineMasterService.getAllMachineByMachineCodeList(machineCodes, "active");
                BigDecimal investorMachineTotalCapacity = BigDecimal.valueOf(0);
                //BigDecimal investorMachineActiveRate = BigDecimal.valueOf(0);
                machineMasterBeanList.stream().forEach(s -> {
                    new BigDecimal(s.getCapacity()).add(investorMachineTotalCapacity);
                    //new BigDecimal(s.getActiveRate()).add(investorMachineActiveRate);
                });

                solarStatementBean.setInvestorProjectCapacity(String.valueOf(investorMachineTotalCapacity));

                //call meter consumption report service and get meterconsumptiondto
                MeterConsumptionDto meterConsumptionDto = meterReadingService.getMeterConsumptionByMonth(meterNo, monthYear);
                solarStatementBean.setCategory(meterConsumptionDto.getCategory());
                solarStatementBean.setMeterMf(meterConsumptionDto.getMf());
                solarStatementBean.setPreviousReadingDate(meterConsumptionDto.getPreviousReadingDate());
                solarStatementBean.setCurrentReadingDate(meterConsumptionDto.getCurrentReadingDate());

                solarStatementBean.setEPreviousActiveEnergy(meterConsumptionDto.getEPreviousActiveEnergy());
                solarStatementBean.setECurrentActiveEnergy(meterConsumptionDto.getECurrentActiveEnergy());
                solarStatementBean.setEDiffActiveEnergy(meterConsumptionDto.getEDiffActiveEnergy());
                solarStatementBean.setEConsumptionActiveEnergy(meterConsumptionDto.getEConsumptionActiveEnergy());
                solarStatementBean.setEAdjustmentActiveEnergy(BigDecimal.valueOf(0));

                solarStatementBean.setICurrentMaxDemand(meterConsumptionDto.getICurrentMaxDemand());

                solarStatementBean.setIPreviousActiveEnergy(meterConsumptionDto.getIPreviousActiveEnergy());
                solarStatementBean.setICurrentActiveEnergy(meterConsumptionDto.getICurrentActiveEnergy());
                solarStatementBean.setIDiffActiveEnergy(meterConsumptionDto.getIDiffActiveEnergy());
                solarStatementBean.setIConsumptionActiveEnergy(meterConsumptionDto.getIConsumptionActiveEnergy());

                solarStatementBean.setIPreviousReactiveQuad1(meterConsumptionDto.getIPreviousReactiveQuad1());
                solarStatementBean.setICurrentReactiveQuad1(meterConsumptionDto.getICurrentReactiveQuad1());
                solarStatementBean.setIDiffReactiveQuad1(meterConsumptionDto.getIDiffReactiveQuad1());
                solarStatementBean.setIConsumptionReactiveQuad1(meterConsumptionDto.getIConsumptionReactiveQuad1());

                solarStatementBean.setIPreviousReactiveQuad2(meterConsumptionDto.getIPreviousReactiveQuad2());
                solarStatementBean.setICurrentReactiveQuad2(meterConsumptionDto.getICurrentReactiveQuad2());
                solarStatementBean.setIDiffReactiveQuad2(meterConsumptionDto.getIDiffReactiveQuad2());
                solarStatementBean.setIConsumptionReactiveQuad2(meterConsumptionDto.getIConsumptionReactiveQuad2());

                solarStatementBean.setIPreviousReactiveQuad3(meterConsumptionDto.getIPreviousReactiveQuad3());
                solarStatementBean.setICurrentReactiveQuad3(meterConsumptionDto.getICurrentReactiveQuad3());
                solarStatementBean.setIDiffReactiveQuad3(meterConsumptionDto.getIDiffReactiveQuad3());
                solarStatementBean.setIConsumptionReactiveQuad3(meterConsumptionDto.getIConsumptionReactiveQuad3());

                solarStatementBean.setIPreviousReactiveQuad4(meterConsumptionDto.getIPreviousReactiveQuad4());
                solarStatementBean.setICurrentReactiveQuad4(meterConsumptionDto.getICurrentReactiveQuad4());
                solarStatementBean.setIDiffReactiveQuad4(meterConsumptionDto.getIDiffReactiveQuad4());
                solarStatementBean.setIConsumptionReactiveQuad4(meterConsumptionDto.getIConsumptionReactiveQuad4());

                solarStatementBean.setIConsumptionReactiveQuadTotal(
                        solarStatementBean.getiConsumptionReactiveQuad1()
                                .add(solarStatementBean.getiConsumptionReactiveQuad2())
                                .add(solarStatementBean.getiConsumptionReactiveQuad3())
                                .add(solarStatementBean.getiConsumptionReactiveQuad4()));

                solarStatementBean.setIPreviousKvah(meterConsumptionDto.getIPreviousKvah());
                solarStatementBean.setICurrentKvah(meterConsumptionDto.getICurrentKvah());
                solarStatementBean.setIDiffKvah(meterConsumptionDto.getIDiffKvah());
                solarStatementBean.setIConsumptionKvah(meterConsumptionDto.getIConsumptionKvah());

                solarStatementBean.setEPreviousKvah(meterConsumptionDto.getEPreviousKvah());
                solarStatementBean.setECurrentKvah(meterConsumptionDto.getECurrentKvah());
                solarStatementBean.setEDiffKvah(meterConsumptionDto.getEDiffKvah());
                solarStatementBean.setEConsumptionKvah(meterConsumptionDto.getEConsumptionKvah());

                solarStatementBean.setWheelingChargePercent(BigDecimal.valueOf(5.91));

                BigDecimal meterTod1 = meterConsumptionDto.getEConsumptionTod1();
                BigDecimal meterTod2 = meterConsumptionDto.getEConsumptionTod2();
                BigDecimal meterTod3 = meterConsumptionDto.getEConsumptionTod3();
                BigDecimal meterTod4 = meterConsumptionDto.getEConsumptionTod4();
                BigDecimal meterEKwhConsumption = meterConsumptionDto.getEConsumptionActiveEnergy();
                BigDecimal meterTodSum = meterTod1.add(meterTod2).add(meterTod3).add(meterTod4);
                BigDecimal toBeAdjust = BigDecimal.valueOf(0);
                if (meterEKwhConsumption.compareTo(meterTodSum) >= 0) {
                    toBeAdjust = meterEKwhConsumption.subtract(meterTodSum); //this will be subtract from ekwh
                    solarStatementBean.setEConsumptionActiveEnergy(meterEKwhConsumption.subtract(toBeAdjust));
                    solarStatementBean.setEAdjustmentActiveEnergy(toBeAdjust);

                    //if e kwh is grater than tod sum then ekwh will be adjusted tod will as it is
                    solarStatementBean.setTotalTod3(meterConsumptionDto.getEConsumptionTod3());
                    solarStatementBean.setTotalAdjustment(BigDecimal.valueOf(0)); //tod adjustment as it is
                    solarStatementBean.setTotalTod1(meterConsumptionDto.getEConsumptionTod1());
                    solarStatementBean.setTotalTod2(meterConsumptionDto.getEConsumptionTod2());
                    solarStatementBean.setTotalTod4(meterConsumptionDto.getEConsumptionTod4());
                    solarStatementBean.setTotalKwhExport(solarStatementBean.geteConsumptionActiveEnergy());
                } else {

                    toBeAdjust = meterTodSum.subtract(meterEKwhConsumption); //this will be subtract from tod 3
                    solarStatementBean.setEConsumptionActiveEnergy(meterEKwhConsumption);// as it is
                    //e adjustment active energy remain 0 and tod3 will decreased
                    //if e kwh is less then tod sum then tod will be adjusted, ekwh will as it is
                    solarStatementBean.setTotalTod3(meterConsumptionDto.getEConsumptionTod3().subtract(toBeAdjust));
                    solarStatementBean.setTotalAdjustment(toBeAdjust);
                    solarStatementBean.setTotalTod1(meterConsumptionDto.getEConsumptionTod1());
                    solarStatementBean.setTotalTod2(meterConsumptionDto.getEConsumptionTod2());
                    solarStatementBean.setTotalTod4(meterConsumptionDto.getEConsumptionTod4());
                    solarStatementBean.setTotalKwhExport(solarStatementBean.geteConsumptionActiveEnergy());

                }

                Set<ThirdPartyTod> thirdPartyTodSet = new HashSet<>();

                List<ThirdPartyBean> thirdPartyBeanListOfInvestor = thirdPartyService.getThirdPartiesByInvestorCode(investor);
                for (ThirdPartyBean thirdPartyBean : thirdPartyBeanListOfInvestor) {
                    ThirdPartyTod thirdPartyTod = new ThirdPartyTod();
                    thirdPartyTod.setTpCode(thirdPartyBean.getConsumerCode());
                    thirdPartyTod.setTpName(thirdPartyBean.getConsumerName());
                    thirdPartyTod.setTpPercentage(new BigDecimal(thirdPartyBean.getAdjustmentUnitPercent()));

                    //if tod adjstment 0 then individual adjustment is also 0
                    if (solarStatementBean.getTotalAdjustment().compareTo(BigDecimal.valueOf(0)) >= 0){
                        thirdPartyTod.setTpAdjustment(thirdPartyTod.getTpPercentage().multiply(solarStatementBean.getTotalAdjustment()
                        ).divide(BigDecimal.valueOf(100)).setScale(2));
                        thirdPartyTod.setTpTod3(thirdPartyTod.getTpPercentage().multiply(solarStatementBean.getTotalTod3())
                                .divide(BigDecimal.valueOf(100)).setScale(2));
                        thirdPartyTod.setTpTod1(thirdPartyTod.getTpPercentage().multiply(solarStatementBean.getTotalTod1())
                                .divide(BigDecimal.valueOf(100)).setScale(2));
                        thirdPartyTod.setTpTod2(thirdPartyTod.getTpPercentage().multiply(solarStatementBean.getTotalTod2())
                                .divide(BigDecimal.valueOf(100)).setScale(2));
                        thirdPartyTod.setTpTod4(thirdPartyTod.getTpPercentage().multiply(solarStatementBean.getTotalTod4())
                                .divide(BigDecimal.valueOf(100)).setScale(2));
                        thirdPartyTod.setTpKwhExport(thirdPartyTod.getTpPercentage().multiply(solarStatementBean.geteConsumptionActiveEnergy())
                                .divide(BigDecimal.valueOf(100)).setScale(2));
                    }

                    thirdPartyTodSet.add(thirdPartyTod);
                }
                solarStatementBean.setThirdPartyTodSet(thirdPartyTodSet);
                solarStatementBeanList.add(solarStatementBean);
            }
            return solarStatementBeanList;
        }catch (ApiException apiException) {
            throw apiException;
        } catch (DataIntegrityViolationException d) {
            throw d;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
