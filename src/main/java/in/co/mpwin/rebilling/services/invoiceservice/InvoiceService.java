package in.co.mpwin.rebilling.services.invoiceservice;

import in.co.mpwin.rebilling.beans.bifurcation.BifurcateBean;
import in.co.mpwin.rebilling.beans.investormaster.InvestorMasterBean;
import in.co.mpwin.rebilling.beans.invoice.InvoiceBean;
import in.co.mpwin.rebilling.beans.plantmaster.PlantMasterBean;
import in.co.mpwin.rebilling.dto.InvoiceInvestorDto;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.AuditControlServices;
import in.co.mpwin.rebilling.miscellanious.Currency;
import in.co.mpwin.rebilling.miscellanious.DateMethods;
import in.co.mpwin.rebilling.miscellanious.TokenInfo;
import in.co.mpwin.rebilling.repositories.bifurcaterepo.BifurcateBeanRepo;
import in.co.mpwin.rebilling.repositories.invoicerepo.InvoiceBeanRepo;
import in.co.mpwin.rebilling.repositories.metermaster.MeterMasterRepo;
import in.co.mpwin.rebilling.services.bifurcateservice.BifurcateConsumptionService;
import in.co.mpwin.rebilling.services.developermaster.DeveloperMasterService;
import in.co.mpwin.rebilling.services.investormaster.InvestorMasterService;
import in.co.mpwin.rebilling.services.mapping.InvestorPpwaMappingService;
import in.co.mpwin.rebilling.services.plantmaster.PlantMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class InvoiceService {

    @Autowired private InvoiceBeanRepo invoiceBeanRepo;
    @Autowired private DeveloperMasterService developerMasterService;
    @Autowired private PlantMasterService plantMasterService;
    @Autowired private InvestorMasterService investorMasterService;
    @Autowired private InvestorPpwaMappingService investorPpwaMappingService;
    @Autowired private BifurcateConsumptionService bifurcateConsumptionService;
    @Autowired
    private BifurcateBeanRepo bifurcateBeanRepo;
    @Autowired
    private MeterMasterRepo meterMasterRepo;

    public InvoiceBean generateInvoiceNonPPWA(String investorCode, String monthYear){
        try {
            InvoiceBean invoiceBean = new InvoiceBean();
            //validation for invoice generation
            // 1.investor must be bifurcated for given month   2.investor must not be third party
            boolean isBifurcationDone = bifurcateConsumptionService.isExistsInvestorInBifurcateBean(investorCode,monthYear,"active");
            InvestorMasterBean investorMasterBean = investorMasterService.getInvestorByInvestorCode(investorCode,"active");
            boolean isBuyerThirdParty = investorMasterBean.getBuyer().equals("Third Party");
            InvoiceBean alreadyExistInvoice = invoiceBeanRepo
                    .findByInvestorCodeAndBillingMonthAndStatus(investorCode,monthYear,"active");
            if (!isBifurcationDone)
                throw new ApiException(HttpStatus.BAD_REQUEST,"Bifurcation is not done for given investor and month");
            else if (isBuyerThirdParty)
                throw new ApiException(HttpStatus.BAD_REQUEST,"Since Investor is third Party so invoice can not be generated");
            else if(alreadyExistInvoice != null)
                throw new ApiException(HttpStatus.BAD_REQUEST,"Investor "+investorCode+" invoice already generated for month "
                        +monthYear+" and invoice number is "+alreadyExistInvoice.getInvoiceNumber());
            else if (isBifurcationDone && !(isBuyerThirdParty)){

                BifurcateBean bifurcateBean = bifurcateConsumptionService.getBifurcateBeanByInvestorCodeAndMonth(investorCode,monthYear,"active");
                invoiceBean.setHMeterNo(bifurcateBean.gethMeterNumber());
                invoiceBean.setHCategory(bifurcateBean.gethCategory());
                invoiceBean.setHMf(String.valueOf(bifurcateBean.gethMf()));
                invoiceBean.setHReadingDate(String.valueOf(bifurcateBean.gethReadingDate()));
                invoiceBean.setBillingMonth(bifurcateBean.getHmonth());
                invoiceBean.setBillingYear(bifurcateBean.getHmonth().substring(4,8));
                invoiceBean.setInvestorCode(bifurcateBean.getlInvestorCode());
                invoiceBean.setInvesterName(bifurcateBean.getlInvestorName());
                invoiceBean.setPpwaNo(bifurcateBean.getPpwaNo());
                invoiceBean.setGstNo(investorMasterBean.getGstNo());
                invoiceBean.setCin(investorMasterBean.getCin());
                invoiceBean.setTin(investorMasterBean.getTin());
                invoiceBean.setVat(investorMasterBean.getVat());
                invoiceBean.setOfficeAddress(investorMasterBean.getOfficeAddress());
                invoiceBean.setCircleName(investorMasterBean.getLocationMaster().getCircleName());
                invoiceBean.setPpaLetterNo(investorMasterBean.getPpaLetterNo());
                invoiceBean.setPpaDate(String.valueOf(investorMasterBean.getPpaDate()));
                //set plant commissioned date
                PlantMasterBean plantMasterBean = plantMasterService.getPlantByPlantCode(bifurcateBean.gethDevPlantcode(),"active");
                invoiceBean.setCommissionedDate(
                        String.valueOf(plantMasterBean
                                .getCommissionedDate()));
                invoiceBean.setParticulars(investorMasterBean.getParticulars());
                invoiceBean.setLKwhActiveEnergy(bifurcateBean.getlConsumptionKwh()); //this is the kwh export unit
                invoiceBean.setLRkvah(bifurcateBean.getLrkvah());
                invoiceBean.setLFixedAdjustmentVal(bifurcateBean.getlFixedAdjustmentPer()); //adjustment percent and value clearity needed
                invoiceBean.setLAdjustment(bifurcateBean.getlAdjustment());
                invoiceBean.setLActiveRate(bifurcateBean.getlMachineActiveRate());
                invoiceBean.setLReactiveRate(bifurcateBean.getlMachineAReactiveRate());
                invoiceBean.setBankName(investorMasterBean.getBankName());
                invoiceBean.setAccountName(investorMasterBean.getAccountHolderName());
                invoiceBean.setAccountNo(investorMasterBean.getAccountNo());
                invoiceBean.setIfscCode(investorMasterBean.getIfscCode());
                invoiceBean.setMicr(investorMasterBean.getMicr());
                invoiceBean.setLcdevId(bifurcateBean.gethDevId()); //developer id clarity needed
                invoiceBean.setType(plantMasterBean.getType());
                //set active energy amount = unit * active rate
                invoiceBean.setLineKwhAmount(invoiceBean.getlKwhActiveEnergy().multiply(invoiceBean.getlActiveRate()).setScale(6));
                //set rkvah charges = unit * reactive rate
                invoiceBean.setLineRkvahAmount(invoiceBean.getlRkvah().multiply(invoiceBean.getlReactiveRate()).setScale(6));
                //set fix adjustment amount = fix adjustment value * active rate
                invoiceBean.setLineFixAdjAmt(invoiceBean.getlFixedAdjustmentVal().multiply(invoiceBean.getlActiveRate()).setScale(6));
                //set adjustment unit amount = total adjustment line unit * active rate
                invoiceBean.setLineAdjustmentUnitAmt(invoiceBean.getlAdjustment().multiply(invoiceBean.getlActiveRate()).setScale(6));
                invoiceBean.setTotalAmount(invoiceBean.getLineKwhAmount().add(invoiceBean.getLineRkvahAmount())
                        .subtract(invoiceBean.getLineFixAdjAmt()).subtract(invoiceBean.getLineAdjustmentUnitAmt()));
                invoiceBean.setGrandTotalAmount(invoiceBean.getTotalAmount());
                invoiceBean.setGrandTotalAmountRounded(BigDecimal.valueOf(invoiceBean.getGrandTotalAmount().longValue()));
                invoiceBean.setAmountWords(Currency.convertToIndianCurrency(invoiceBean.getGrandTotalAmountRounded()));

                //set invoice number
                Integer maxBillNumberOfInvestorOnYear = invoiceBeanRepo.findMaxBillNumber(investorCode,monthYear.substring(4,8));
                if(maxBillNumberOfInvestorOnYear == null)
                    maxBillNumberOfInvestorOnYear = 0;
                String newBillNumber = "MPWZ-"+monthYear.substring(4,8)+"-"+invoiceBean.getInvestorCode()+"-"+String.format("%03d",maxBillNumberOfInvestorOnYear+1);
                invoiceBean.setInvoiceNumber(newBillNumber);
                invoiceBean.setBillNo(newBillNumber);
                invoiceBean.setInvoiceDate(new DateMethods().getServerTime());

            }
            return invoiceBean;
        }catch (ApiException apiException){
            throw apiException;
        }catch (DataIntegrityViolationException d){
            throw d;
        }catch (Exception e){
            throw e;
        }
    }

    public List<InvoiceBean> generateInvoicePPWA(String ppwaNo, String monthYear){
        try {
            List<InvoiceBean> invoiceBeanList = new ArrayList<>();
            //validation for invoice generation
            // 1.all investors related to ppwa must be bifurcated for given month
            // 2.investor must not be third party
            List<String> investorCodeLIst = investorPpwaMappingService.getInvestorCodeListByPpwaNo(ppwaNo,"active");
            for (String investorCode : investorCodeLIst){
                boolean isBifurcationDone = bifurcateConsumptionService.isExistsInvestorInBifurcateBean(investorCode,monthYear,"active");
                InvestorMasterBean investorMasterBean = investorMasterService.getInvestorByInvestorCode(investorCode,"active");
                if (investorMasterBean == null)
                    throw new ApiException(HttpStatus.BAD_REQUEST,"Investor "+investorCode+" is not present in investor master");
                boolean isBuyerThirdParty = investorMasterBean.getBuyer().equals("Third Party");
                InvoiceBean alreadyExistInvoice = invoiceBeanRepo
                        .findByInvestorCodeAndBillingMonthAndStatus(investorCode,monthYear,"active");
                if (!isBifurcationDone)
                    throw new ApiException(HttpStatus.BAD_REQUEST,"Bifurcation is not done for given investor " +investorCode+ " and month "+monthYear);
                else if (isBuyerThirdParty)
                    throw new ApiException(HttpStatus.BAD_REQUEST,"Since Investor is third Party so invoice can not be generated");
                else if(alreadyExistInvoice != null)
                    throw new ApiException(HttpStatus.BAD_REQUEST,"Investor "+investorCode+" invoice already generated for month "
                            +monthYear+" and invoice number is "+alreadyExistInvoice.getInvoiceNumber());
                else if (isBifurcationDone && !(isBuyerThirdParty)) {
                    InvoiceBean invoiceBean = new InvoiceBean();
                    BifurcateBean bifurcateBean = bifurcateConsumptionService.getBifurcateBeanByInvestorCodeAndMonth(investorCode, monthYear, "active");
                    invoiceBean.setHMeterNo(bifurcateBean.gethMeterNumber());
                    invoiceBean.setHCategory(bifurcateBean.gethCategory());
                    invoiceBean.setHMf(String.valueOf(bifurcateBean.gethMf()));
                    invoiceBean.setHReadingDate(String.valueOf(bifurcateBean.gethReadingDate()));
                    invoiceBean.setBillingMonth(bifurcateBean.getHmonth());
                    invoiceBean.setBillingYear(bifurcateBean.getHmonth().substring(4, 8));
                    invoiceBean.setInvestorCode(bifurcateBean.getlInvestorCode());
                    invoiceBean.setInvesterName(bifurcateBean.getlInvestorName());
                    invoiceBean.setPpwaNo(bifurcateBean.getPpwaNo());
                    invoiceBean.setGstNo(investorMasterBean.getGstNo());
                    invoiceBean.setCin(investorMasterBean.getCin());
                    invoiceBean.setTin(investorMasterBean.getTin());
                    invoiceBean.setVat(investorMasterBean.getVat());
                    invoiceBean.setOfficeAddress(investorMasterBean.getOfficeAddress());
                    invoiceBean.setCircleName(investorMasterBean.getLocationMaster().getCircleName());
                    invoiceBean.setPpaLetterNo(investorMasterBean.getPpaLetterNo());
                    invoiceBean.setPpaDate(String.valueOf(investorMasterBean.getPpaDate()));
                    //set plant commissioned date
                    PlantMasterBean plantMasterBean = plantMasterService.getPlantByPlantCode(bifurcateBean.gethDevPlantcode(), "active");
                    invoiceBean.setCommissionedDate(
                            String.valueOf(plantMasterBean
                                    .getCommissionedDate()));
                    invoiceBean.setParticulars(investorMasterBean.getParticulars());
                    invoiceBean.setLKwhActiveEnergy(bifurcateBean.getlConsumptionKwh()); //this is the kwh export unit
                    invoiceBean.setLRkvah(bifurcateBean.getLrkvah());
                    invoiceBean.setLFixedAdjustmentVal(bifurcateBean.getlFixedAdjustmentPer()); //adjustment percent and value clearity needed
                    invoiceBean.setLAdjustment(bifurcateBean.getlAdjustment());
                    invoiceBean.setLActiveRate(bifurcateBean.getlMachineActiveRate());
                    invoiceBean.setLReactiveRate(bifurcateBean.getlMachineAReactiveRate());
                    invoiceBean.setBankName(investorMasterBean.getBankName());
                    invoiceBean.setAccountName(investorMasterBean.getAccountHolderName());
                    invoiceBean.setAccountNo(investorMasterBean.getAccountNo());
                    invoiceBean.setIfscCode(investorMasterBean.getIfscCode());
                    invoiceBean.setMicr(investorMasterBean.getMicr());
                    invoiceBean.setLcdevId(bifurcateBean.gethDevId()); //developer id clarity needed
                    invoiceBean.setType(plantMasterBean.getType());
                    //set active energy amount = unit * active rate
                    invoiceBean.setLineKwhAmount(invoiceBean.getlKwhActiveEnergy().multiply(invoiceBean.getlActiveRate()).setScale(6));
                    //set rkvah charges = unit * reactive rate
                    invoiceBean.setLineRkvahAmount(invoiceBean.getlRkvah().multiply(invoiceBean.getlReactiveRate()).setScale(6));
                    //set fix adjustment amount = fix adjustment value * active rate
                    invoiceBean.setLineFixAdjAmt(invoiceBean.getlFixedAdjustmentVal().multiply(invoiceBean.getlActiveRate()).setScale(6));
                    //set adjustment unit amount = total adjustment line unit * active rate
                    invoiceBean.setLineAdjustmentUnitAmt(invoiceBean.getlAdjustment().multiply(invoiceBean.getlActiveRate()).setScale(6));
                    invoiceBean.setTotalAmount(invoiceBean.getLineKwhAmount().add(invoiceBean.getLineRkvahAmount())
                            .subtract(invoiceBean.getLineFixAdjAmt()).subtract(invoiceBean.getLineAdjustmentUnitAmt()));


                    //set bill number
                    Integer maxBillNumberOfInvestorOnYear = invoiceBeanRepo.findMaxBillNumber(investorCode,monthYear.substring(4,8));
                    if(maxBillNumberOfInvestorOnYear == null)
                        maxBillNumberOfInvestorOnYear = 0;
                    String newBillNumber = "MPWZ-"+monthYear.substring(4,8)+"-"+invoiceBean.getInvestorCode()+"-"+String.format("%03d",maxBillNumberOfInvestorOnYear+1);
                    //set invoice number
                    Integer maxInvoiceNumberofPpwaNo = invoiceBeanRepo.findMaxInvoiceNumber(invoiceBean.getPpwaNo(),monthYear.substring(4,8));
                    if (maxInvoiceNumberofPpwaNo == null)
                        maxInvoiceNumberofPpwaNo = 0;
                    String newInvoiceNumber = "MPWZ-"+monthYear.substring(4,8)+"-"+invoiceBean.getPpwaNo()+"-"+String.format("%03d",maxInvoiceNumberofPpwaNo+1);

                    invoiceBean.setBillNo(newBillNumber);
                    invoiceBean.setInvoiceNumber(newInvoiceNumber);
                    invoiceBean.setInvoiceDate(new DateMethods().getServerTime());

                    invoiceBeanList.add(invoiceBean);
                }
            }
            BigDecimal grandTotal = new BigDecimal(0);
            for (InvoiceBean invoiceBean : invoiceBeanList){
                grandTotal = invoiceBean.getTotalAmount().add(grandTotal);
            }
            BigDecimal grandTotAmtRod = BigDecimal.valueOf(grandTotal.longValue());
            BigDecimal grandTotAmt = grandTotal;
            invoiceBeanList.stream().forEach(i -> {i.setGrandTotalAmount(grandTotAmt);
                                             i.setGrandTotalAmountRounded(grandTotAmtRod);
                                             i.setAmountWords(Currency.convertToIndianCurrency(grandTotAmtRod));});
            return invoiceBeanList;
        }catch (ApiException apiException){
            throw apiException;
        }catch (DataIntegrityViolationException d){
            throw d;
        }catch (Exception e){
            throw e;
        }
    }

    @Transactional
    public String saveInvoiceNonPpwa(InvoiceBean invoiceBean) {
        try {   if (invoiceBean != null) {
                //save audit trails
                new AuditControlServices().setInitialAuditControlParameters(invoiceBean);
                invoiceBean.setInvoiceStage("invoice_freezed");
                InvoiceBean savedBean = invoiceBeanRepo.save(invoiceBean);
                return savedBean.getInvoiceNumber();
                }
            else
                throw new ApiException(HttpStatus.BAD_REQUEST,"No Invoices Bean List Provided");
        }catch (ApiException apiException){
            throw apiException;
        }catch (DataIntegrityViolationException d){
            throw d;
        }catch (Exception e){
            throw e;
        }
    }

    @Transactional
    public String saveInvoicePpwa(List<InvoiceBean> invoiceBeanList) {
        try {   if (invoiceBeanList.size()>0)
                    for (InvoiceBean bean : invoiceBeanList) {
                        //save audit trails
                        new AuditControlServices().setInitialAuditControlParameters(bean);
                        bean.setInvoiceStage("invoice_freezed");
                        InvoiceBean savedBean = invoiceBeanRepo.save(bean);

                    }
                 else
                     throw new ApiException(HttpStatus.BAD_REQUEST,"No Invoices Bean List Provided");
                return "Invoice number " + invoiceBeanList.get(0).getInvoiceNumber()+ " generated successfully.";
        }catch (ApiException apiException){
            throw apiException;
        }catch (DataIntegrityViolationException d){
            throw d;
        }catch (Exception e){
            throw e;
        }
    }

    public List<InvoiceInvestorDto> loadInvoiceDetailOfMeter(String meterNo, String monthYear) {

        try {
                //First make meter set by traversing each bifurcate dto wrt to meter if ppwa found then search distinct meter
                //by ppwa number add these meters to newmeterset and on exit of outer foreach loop set finalsetsize equal to newmeterset size
                //at last loop no any meter further left to add in newmeterset then value of both initial and final set size will same
                LinkedHashSet<String> meterSet = new LinkedHashSet<>();
                meterSet.add(meterNo);
                LinkedHashSet<String> newMeterSet = new LinkedHashSet<>(meterSet);
                int initialSetSize,finalSetSize;

                do {
                    meterSet = new LinkedHashSet<>(newMeterSet);
                    initialSetSize = meterSet.size();
                    for (String meter : meterSet) {
                        List<BifurcateBean> bifurcateBeanList = bifurcateConsumptionService
                                .getBifurcateBeanByMeterNoAndMonth(meter, monthYear);
                        if (bifurcateBeanList.isEmpty()) {
                            throw new ApiException(HttpStatus.BAD_REQUEST, "Bifurcation is not done yet for " + meter + " meter number for the given month");
                        }

                        for (BifurcateBean b : bifurcateBeanList) {
                            if (!"NA".equals(b.getPpwaNo())) {
                                List<String> distinctMeters = bifurcateConsumptionService.getDistinctMeterByPpwaNoAndMonthYear(b.getPpwaNo(), monthYear, "active");
                                newMeterSet.addAll(distinctMeters);
                            }
                        }
                    }
                        finalSetSize = newMeterSet.size();
                } while (!(initialSetSize == finalSetSize)) ;


                //after picking all related meter then start to load investor invoice dto
                List<InvoiceInvestorDto> invoiceInvestorDtoList = new ArrayList<>();

                    for (String meter : meterSet) {
                        List<BifurcateBean> bifurcateBeanList = bifurcateConsumptionService
                                .getBifurcateBeanByMeterNoAndMonth(meter, monthYear);
                        if (bifurcateBeanList.size() == 0)
                            throw new ApiException(HttpStatus.BAD_REQUEST, "Bifurcation is not done yet for " + meter + " meter number for given month");

                        for (BifurcateBean b : bifurcateBeanList) {
                            InvoiceInvestorDto invoiceInvestorDto = new InvoiceInvestorDto();
                            invoiceInvestorDto.setPlantCode(b.gethDevPlantcode());
                            invoiceInvestorDto.setPlantName(b.gethDevPlantName());
                            invoiceInvestorDto.setMeterNumber(b.gethMeterNumber());
                            invoiceInvestorDto.setInvestorCode(b.getlInvestorCode());
                            invoiceInvestorDto.setInvestorName(b.getlInvestorName());
                            invoiceInvestorDto.setBifurcateInvestorKwhConsumption(b.getlConsumptionKwh());
                            invoiceInvestorDto.setBifurcateTotalKwhConsumption(b.gethConsumptionKwh());
                            invoiceInvestorDto.setPpwaNo(b.getPpwaNo());

                            InvoiceBean invoiceBean = invoiceBeanRepo.findByInvestorCodeAndBillingMonthAndStatus
                                    (invoiceInvestorDto.getInvestorCode(), monthYear, "active");
                            if (invoiceBean == null) {
                                invoiceInvestorDto.setInvoiceNumber(null);
                                invoiceInvestorDto.setInvoiceDate(null);
                                invoiceInvestorDto.setInvoiceAmount(null);
                                invoiceInvestorDto.setInvoiceStage(null);
                            } else {
                                invoiceInvestorDto.setInvoiceNumber(invoiceBean.getInvoiceNumber());
                                invoiceInvestorDto.setInvoiceDate(String.valueOf(invoiceBean.getInvoiceDate()));
                                invoiceInvestorDto.setInvoiceStage(invoiceBean.getInvoiceStage());
                                invoiceInvestorDto.setInvoiceAmount(invoiceBean.getTotalAmount());
                            }
                            invoiceInvestorDtoList.add(invoiceInvestorDto);
                        }
                }


                return invoiceInvestorDtoList;

        }catch (ApiException apiException){
            throw apiException;
        }catch (DataIntegrityViolationException d){
            throw d;
        }catch (Exception e){
            throw e;
        }
    }

    public InvoiceBean viewInvoiceNonPpwa(String invoiceNumber) {
        try {
                List<InvoiceBean> invoiceBeanList = invoiceBeanRepo.findAllByInvoiceNumberAndStatus(invoiceNumber,"active");
                if (invoiceBeanList.size() == 0)
                    throw new ApiException(HttpStatus.BAD_REQUEST,"Invoice number " +invoiceNumber+" is invalid.");
                return invoiceBeanList.get(0);
        }catch (ApiException apiException){
            throw apiException;
        }catch (DataIntegrityViolationException d){
            throw d;
        }catch (Exception e){
            throw e;
        }
    }

    public List<InvoiceBean> viewInvoicePpwa(String invoiceNumber){
        try {
            List<InvoiceBean> invoiceBeanList = invoiceBeanRepo.findAllByInvoiceNumberAndStatus(invoiceNumber,"active");
            if (invoiceBeanList.size() == 0)
                throw new ApiException(HttpStatus.BAD_REQUEST,"Invoice number " +invoiceNumber+" is invalid.");
            return invoiceBeanList;
        }catch (ApiException apiException){
            throw apiException;
        }catch (DataIntegrityViolationException d){
            throw d;
        }catch (Exception e){
            throw e;
        }
    }

    @Transactional
    public String submitInvoice(List<InvoiceInvestorDto> invoiceInvestorDtoList) {
        //check for invoiceInvestorDto
        try {
            for (InvoiceInvestorDto dto : invoiceInvestorDtoList) {
                InvoiceBean bean = invoiceBeanRepo.findByInvoiceNumberAndStatus(dto.getInvoiceNumber(), "active");
                if (bean == null)
                    throw new ApiException(HttpStatus.BAD_REQUEST, "Invoice " + dto.getInvoiceNumber() +" not exist in database");

                if (bean.getInvoiceStage().equals("invoice_freezed")) {
                    bean.setInvoiceStage("invoice_submitted");
                    //save update audit trail
                    bean.setUpdatedBy(new TokenInfo().getCurrentUsername());
                    bean.setUpdatedOn(new DateMethods().getServerTime());
                    invoiceBeanRepo.save(bean);
                } else
                    throw new ApiException(HttpStatus.BAD_REQUEST, "Invoice " +bean.getInvoiceNumber()+" not in freezed stage for sending approval and current stage is "
                                        +bean.getInvoiceStage());
            }
            return "Invoices submitted successfully..";
        } catch (ApiException apiException) {
            throw apiException;
        } catch (DataIntegrityViolationException d) {
            throw d;
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public String approveInvoice(List<InvoiceInvestorDto> invoiceInvestorDtoList) {
        //check for invoiceInvestorDto
        try {
            for (InvoiceInvestorDto dto : invoiceInvestorDtoList) {
                InvoiceBean bean = invoiceBeanRepo.findByInvoiceNumberAndStatus(dto.getInvoiceNumber(), "active");
                if (bean == null)
                    throw new ApiException(HttpStatus.BAD_REQUEST,"Invoice " + dto.getInvoiceNumber() +" not exist in database");

                if (bean.getInvoiceStage().equals("invoice_submitted")) {
                    //save update audit trail
                    bean.setUpdatedBy(new TokenInfo().getCurrentUsername());
                    bean.setUpdatedOn(new DateMethods().getServerTime());
                    bean.setInvoiceStage("invoice_approved");
                    invoiceBeanRepo.save(bean);
                } else
                    throw new ApiException(HttpStatus.BAD_REQUEST, "Invoice " +bean.getInvoiceNumber()+" not in submitted stage for approval and current stage is "+
                            bean.getInvoiceStage());
            }
            return "Invoices approved successfully..";
        } catch (ApiException apiException) {
            throw apiException;
        } catch (DataIntegrityViolationException d) {
            throw d;
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public String rejectInvoice(List<InvoiceInvestorDto> invoiceInvestorDtoList) {
        //check for invoiceInvestorDto
        try {
            for (InvoiceInvestorDto dto : invoiceInvestorDtoList) {
                InvoiceBean bean = invoiceBeanRepo.findByInvoiceNumberAndStatus(dto.getInvoiceNumber(), "active");
                if (bean == null)
                    throw new ApiException(HttpStatus.BAD_REQUEST, "Invoice " + dto.getInvoiceNumber() +" not exist in database");

                if (bean.getInvoiceStage().equals("invoice_submitted")) {
                    bean.setInvoiceStage("invoice_rejected");
                    bean.setStatus("inactive"+new DateMethods().getServerTime());
                    //make bifurcation also invalid
                    bifurcateConsumptionService.setInactiveBifurcateBean(bean.getInvestorCode(), bean.getBillingMonth(), "active");
                    //save update audit trail
                    bean.setUpdatedBy(new TokenInfo().getCurrentUsername());
                    bean.setUpdatedOn(new DateMethods().getServerTime());
                    invoiceBeanRepo.save(bean);
                } else
                    throw new ApiException(HttpStatus.BAD_REQUEST, "Invoice " +bean.getInvoiceNumber()+" not in submitted stage for reject action and current stage is "+
                            bean.getInvoiceStage());
            }
            return "Invoices rejected successfully..";
        } catch (ApiException apiException) {
            throw apiException;
        } catch (DataIntegrityViolationException d) {
            throw d;
        } catch (Exception e) {
            throw e;
        }
    }


}
