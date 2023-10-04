package in.co.mpwin.rebilling.services.invoiceservice;

import in.co.mpwin.rebilling.beans.bifurcation.BifurcateBean;
import in.co.mpwin.rebilling.beans.investormaster.InvestorMasterBean;
import in.co.mpwin.rebilling.beans.invoice.InvoiceBean;
import in.co.mpwin.rebilling.beans.plantmaster.PlantMasterBean;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.AuditControlServices;
import in.co.mpwin.rebilling.miscellanious.DateMethods;
import in.co.mpwin.rebilling.miscellanious.TokenInfo;
import in.co.mpwin.rebilling.repositories.bifurcaterepo.BifurcateBeanRepo;
import in.co.mpwin.rebilling.repositories.invoicerepo.InvoiceBeanRepo;
import in.co.mpwin.rebilling.services.bifurcateservice.BifurcateConsumptionService;
import in.co.mpwin.rebilling.services.developermaster.DeveloperMasterService;
import in.co.mpwin.rebilling.services.investormaster.InvestorMasterService;
import in.co.mpwin.rebilling.services.plantmaster.PlantMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class InvoiceService {

    @Autowired private InvoiceBeanRepo invoiceBeanRepo;
    @Autowired private DeveloperMasterService developerMasterService;
    @Autowired private PlantMasterService plantMasterService;
    @Autowired private InvestorMasterService investorMasterService;
    @Autowired private BifurcateConsumptionService bifurcateConsumptionService;
    @Autowired
    private BifurcateBeanRepo bifurcateBeanRepo;

    public InvoiceBean generateInvoice(String investorCode, String monthYear){
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

            //set invoice number
            Integer maxInvoiceNumberOfInvestorOnYear = invoiceBeanRepo.findMaxInvoiceNumber(investorCode,monthYear.substring(4,8));
            if(maxInvoiceNumberOfInvestorOnYear == null)
                maxInvoiceNumberOfInvestorOnYear = 0;
            String newInvoiceNumber = "MPWZ/"+monthYear.substring(4,8)+"/"+invoiceBean.getInvestorCode()+"/"+String.format("%03d",maxInvoiceNumberOfInvestorOnYear+1);
            invoiceBean.setInvoiceNumber(newInvoiceNumber);
            invoiceBean.setBillNo(newInvoiceNumber);
            invoiceBean.setInvoiceDate(new DateMethods().getServerTime());

        }
        return invoiceBean;
    }

    @Transactional
    public InvoiceBean saveInvoice(InvoiceBean invoiceBean) {
        try {
            //save audit trails
            new AuditControlServices().setInitialAuditControlParameters(invoiceBean);
            InvoiceBean savedBean = invoiceBeanRepo.save(invoiceBean);
            return savedBean;
        }catch (ApiException apiException){
            throw apiException;
        }catch (DataIntegrityViolationException d){
            throw d;
        }catch (Exception e){
            throw e;
        }
    }
}
