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
import in.co.mpwin.rebilling.miscellanious.AuditControlServices;
import in.co.mpwin.rebilling.miscellanious.ConstantField;
import in.co.mpwin.rebilling.miscellanious.DateMethods;
import in.co.mpwin.rebilling.miscellanious.TokenInfo;
import in.co.mpwin.rebilling.repositories.metermaster.MeterMasterRepo;
import in.co.mpwin.rebilling.repositories.statement.SolarStatementRepo;
import in.co.mpwin.rebilling.services.feedermaster.FeederMasterService;
import in.co.mpwin.rebilling.services.investormaster.InvestorMasterService;
import in.co.mpwin.rebilling.services.machinemaster.MachineMasterService;
import in.co.mpwin.rebilling.services.mapping.InvestorMachineMappingService;
import in.co.mpwin.rebilling.services.mapping.MeterFeederPlantMappingService;
import in.co.mpwin.rebilling.services.metermaster.MeterMasterService;
import in.co.mpwin.rebilling.services.plantmaster.PlantMasterService;
import in.co.mpwin.rebilling.services.readingservice.MeterReadingService;
import in.co.mpwin.rebilling.services.thirdparty.ThirdPartyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SolarStatementService {

    private static final Logger logger = LoggerFactory.getLogger(SolarStatementService.class);

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
    private MeterMasterService meterMasterService;

    @Autowired SolarStatementRepo solarStatementRepo;
    @Autowired
    private FeederMasterService feederMasterService;
    @Autowired
    private MeterMasterRepo meterMasterRepo;

    @Transactional
    public List<SolarStatementBean> getSolarStatement(String meterNo, String monthYear) throws ParseException {
        final String methodName = "getSolarStatement() : ";
        logger.info(methodName + " called with parameters meter={} and month = {}",meterNo,monthYear);
        try {
            //check if solar statement is already exist if exist then return beanlist otherwise generate bean and return
            List<SolarStatementBean> alreadyExistStatementBeanList = solarStatementRepo.findAllByMeterNumberAndMonthYearAndStatus(meterNo,monthYear,"active");
            if (alreadyExistStatementBeanList.size() != 0) {
                logger.info(methodName + " return the already existed solar statement from db");
                return alreadyExistStatementBeanList;
            }
            List<SolarStatementBean> solarStatementBeanList = new ArrayList<>();
            //Third check is meter is only one out of two meter present in mapping , means if check already done then main not possible or vice versa
            logger.info(methodName + " validating if either check or main already done then other not possible");
            MeterFeederPlantMappingBean mfpBean = mfpService.getByAnyMeterNoAndStatus(meterNo, "active");
            FeederMasterBean feederMasterBean = feederMasterService.getFeederByFeederNumber(mfpBean.getFeederCode(), "active");
            if (feederMasterBean == null)
                throw new ApiException(HttpStatus.BAD_REQUEST, "Not Any Feeder Present in Feeder master table..");
            String plantCode = mfpBean.getPlantCode();
            PlantMasterBean plantMasterBean = plantMasterService.getPlantByPlantCode(plantCode, "active");
            if (plantMasterBean == null)
                throw new ApiException(HttpStatus.BAD_REQUEST, "Not Any Plant Present in plant master table..");
//            if (!plantMasterBean.getType().equals("SOLAR"))
//                throw new ApiException(HttpStatus.BAD_REQUEST, "Solar Statement only for Solar Plant not for Wind..");
            Long mfpBeanId = mfpBean.getId();//This is used for getting investor related to plant

            //Fetch Distinct Investor list from InvestorMachine Mapping belongs by Meter Feeder Mapping id
            List<InvestorMachineMappingBean> investorMachineMappingBeans = investorMachineMappingService.getMappingByMFPId(mfpBeanId, "active");
            if (investorMachineMappingBeans.size() == 0)
                throw new ApiException(HttpStatus.BAD_REQUEST, "Not Any Investor Mapped to this plant..");

            List<String> investorCodes = investorMachineMappingBeans.stream().map(InvestorMachineMappingBean::getInvestorCode)
                    .distinct().collect(Collectors.toList());
            if (investorCodes.size() > 1)
            throw new ApiException(HttpStatus.BAD_REQUEST, "More than one investor present on solar power plant");

            for (String investor : investorCodes) {
                SolarStatementBean solarStatementBean = new SolarStatementBean();
                InvestorMasterBean investorMasterBean = investorMasterService.getInvestorByInvestorCode(investor, "active");
                if (investorMasterBean == null)
                    throw new ApiException(HttpStatus.BAD_REQUEST, investor + " No investor present in investor master..");

                //set audit trails
                String username = new TokenInfo().getCurrentUsername();
                solarStatementBean.setGeneratedBy(username);
                solarStatementBean.setGeneratedOn(new DateMethods().getServerTime());
                solarStatementBean.setMeterNumber(meterNo);
                solarStatementBean.setMeterInstallDate(meterMasterService.getMeterDetailsByMeterNo(meterNo,"active").getInstallDate());
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
                Double aDouble = machineMasterBeanList.stream()
                        .filter(o -> o.getCapacity() != null).mapToDouble(o -> Double.parseDouble(o.getCapacity())).sum();
//                machineMasterBeanList.stream().forEach(s -> {
//                    investorMachineTotalCapacity.add(new BigDecimal(s.getCapacity()));
//                    //new BigDecimal(s.getCapacity()).add(investorMachineTotalCapacity);
//                    //new BigDecimal(s.getActiveRate()).add(investorMachineActiveRate);
//                });

                solarStatementBean.setInvestorProjectCapacity(String.valueOf(aDouble));

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

                solarStatementBean.setWheelingChargePercent(new BigDecimal(ConstantField.wheelingChargePercent));
                solarStatementBean.setStatus("active");

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
                    logger.info(methodName + " e kwh is grater than tod sum then ekwh will be adjusted tod will as it is.");
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
                    logger.info(methodName + " e kwh is less than tod sum so tod will adjust , ekwh will as it is.");
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
                    thirdPartyTod.setTpPercentage(thirdPartyBean.getAdjustmentUnitPercent());
                    thirdPartyTod.setSolarStatementBean(solarStatementBean);
                    //solarStatementBean.setInvestorProjectCapacity(thirdPartyBean.getPlantCapacity());
                    //if tod adjstment 0 then individual adjustment is also 0
                    if (solarStatementBean.getTotalAdjustment().compareTo(BigDecimal.valueOf(0)) >= 0){
                        logger.info(methodName + " tod adjustment is 0 , so individual adjustment is also 0..");
                        thirdPartyTod.setTpAdjustment(thirdPartyTod.getTpPercentage().multiply(solarStatementBean.getTotalAdjustment()
                        ).divide(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_DOWN));
                        thirdPartyTod.setTpTod3(thirdPartyTod.getTpPercentage().multiply(solarStatementBean.getTotalTod3())
                                .divide(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_DOWN));
                        thirdPartyTod.setTpTod1(thirdPartyTod.getTpPercentage().multiply(solarStatementBean.getTotalTod1())
                                .divide(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_DOWN));
                        thirdPartyTod.setTpTod2(thirdPartyTod.getTpPercentage().multiply(solarStatementBean.getTotalTod2())
                                .divide(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_DOWN));
                        thirdPartyTod.setTpTod4(thirdPartyTod.getTpPercentage().multiply(solarStatementBean.getTotalTod4())
                                .divide(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_DOWN));
                        thirdPartyTod.setTpKwhExport(thirdPartyTod.getTpPercentage().multiply(solarStatementBean.geteConsumptionActiveEnergy())
                                .divide(BigDecimal.valueOf(100)).setScale(2,RoundingMode.HALF_DOWN));
                    }

                    thirdPartyTodSet.add(thirdPartyTod);
                }
                solarStatementBean.setThirdPartyTodSet(thirdPartyTodSet);
                solarStatementBeanList.add(solarStatementBean);
            }
            List<SolarStatementBean> savedBeanList = (List<SolarStatementBean>) solarStatementRepo.saveAll(solarStatementBeanList);
            logger.info(methodName + " return solar statement bean list .");
            return savedBeanList;
        }catch (ApiException apiException) {
            logger.error(methodName+" throw apiException");
            throw apiException;
        } catch (DataIntegrityViolationException d) {
            logger.error(methodName+" throw DataIntegrityViolationException");
            throw d;
        } catch (Exception e) {
            logger.error(methodName+" throw common exception..");
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public List<SolarStatementBean> downloadSolarStatement(String meterNo, String monthYear) throws ParseException {
        final String methodName = "downloadSolarStatement() : ";
        logger.info(methodName + " called with parameters meter={} and month = {}",meterNo,monthYear);
        try {
            //check if solar statement is already exist if exist then return beanlist otherwise generate bean and return
            List<SolarStatementBean> alreadyExistStatementBeanList = solarStatementRepo.findAllByMeterNumberAndMonthYearAndStatus(meterNo,monthYear,"active");
            if (alreadyExistStatementBeanList.size() != 0) {
                logger.info(methodName + " return the already existed solar statement from db");
                return alreadyExistStatementBeanList;
            }else   {
                logger.info(methodName + "solar statement not generated for meter ={} , month = {}",meterNo,monthYear);
                throw new ApiException(HttpStatus.BAD_REQUEST,"Solar Statement is not generated yet for given meter: " + meterNo + " and month: "+monthYear);
            }
        }catch (ApiException apiException) {
            logger.error(methodName+" throw apiException");
            throw apiException;
        } catch (DataIntegrityViolationException d) {
            logger.error(methodName+" throw DataIntegrityViolationException");
            throw d;
        } catch (Exception e) {
            logger.error(methodName+" throw common exception..");
            throw new RuntimeException(e);
        }
    }

}
