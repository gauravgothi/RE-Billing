package in.co.mpwin.rebilling.controller.invoicecontroller;

import in.co.mpwin.rebilling.beans.invoice.InvoiceBean;
import in.co.mpwin.rebilling.controller.investormaster.InvestorMasterController;
import in.co.mpwin.rebilling.dto.InvoiceInvestorDto;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.Message;
import in.co.mpwin.rebilling.services.invoiceservice.InvoiceService;
import in.co.mpwin.rebilling.services.mapping.InvestorPpwaMappingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/invoice")
@CrossOrigin(origins="*")
public class InvoiceController {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceController.class);

    @Autowired private InvoiceService invoiceService;
    @Autowired private InvestorPpwaMappingService investorPpwaMappingService;

    @GetMapping("/load/meterNo/{meterNo}/monthYear/{monthYear}")
    public ResponseEntity<?> loadInvoiceDetailOfMeter(@PathVariable("meterNo") String meterNo,@PathVariable ("monthYear") String monthYear){
        final String methodName = "loadInvoiceDetailOfMeter() : ";
        logger.info(methodName + "called. with parameters meterNo: {}, monthYear: {}",meterNo,monthYear);
        ResponseEntity loadInvoiceResp = null;
        try {

                List<InvoiceInvestorDto> invoiceInvestorDtoList = invoiceService.loadInvoiceDetailOfMeter(meterNo,monthYear);
                loadInvoiceResp = new ResponseEntity(invoiceInvestorDtoList,HttpStatus.OK);
            logger.info(methodName + "return. invoiceInvestorDtoList of size: {} ",invoiceInvestorDtoList.size());
        }catch (ApiException apiException) {
            loadInvoiceResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
            logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
        } catch (DataIntegrityViolationException d) {
            loadInvoiceResp = new ResponseEntity<>(new Message("Data Integrity Violation : "+d.getMessage()), HttpStatus.BAD_REQUEST);
            logger.error(methodName+"Data Integrity Violation Exception occurred: {}", d.getMessage());
        } catch (Exception e) {
            loadInvoiceResp = new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);
        }
        return loadInvoiceResp;
    }

    @GetMapping("/generate/investor/{investor}/monthYear/{monthYear}")
    public ResponseEntity<?> generateInvoice(@PathVariable ("investor") String investor,@PathVariable ("monthYear") String monthYear){
        final String methodName = "generateInvoice() : ";
        logger.info(methodName + "called. with parameters investor: {}, monthYear: {}",investor,monthYear);
        ResponseEntity invoiceGenerateResp = null;
        try {
                    List<InvoiceBean> invoiceBeanList = new ArrayList<>();
                     String ppwaNo = investorPpwaMappingService.getPpwaNoByInvestorCode(investor,"active");
                        if (ppwaNo.equals("NA"))
                            invoiceBeanList.add(invoiceService.generateInvoiceNonPPWA(investor,monthYear));
                        else
                            invoiceBeanList = invoiceService.generateInvoicePPWA(ppwaNo,monthYear);
              invoiceGenerateResp = new ResponseEntity<>(invoiceBeanList,HttpStatus.OK);
            logger.info(methodName + "return. invoiceBeanList of size: {} ",invoiceBeanList.size());
        }catch (ApiException apiException) {
            invoiceGenerateResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
            logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
        } catch (DataIntegrityViolationException d) {
            invoiceGenerateResp = new ResponseEntity<>(new Message("Data Integrity Violation: "+d.getMessage()), HttpStatus.BAD_REQUEST);
            logger.error(methodName+"Data Integrity Violation Exception occurred: {}", d.getMessage());
        } catch (Exception e) {
            invoiceGenerateResp = new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);
        }
        return invoiceGenerateResp;
    }

    @GetMapping("/generate/ppwaNo/{ppwaNo}/monthYear/{monthYear}")
    public ResponseEntity<?> generateInvoicePPWA(@PathVariable ("ppwaNo") String ppwaNo,@PathVariable ("monthYear") String monthYear){
        final String methodName = "generateInvoicePPWA() : ";
        logger.info(methodName + "called. with parameters ppwaNo: {}, monthYear: {}",ppwaNo,monthYear);
        ResponseEntity invoiceGenerateResp = null;
        try {
                List<InvoiceBean> invoiceBeanList = invoiceService.generateInvoicePPWA(ppwaNo,monthYear);
                invoiceGenerateResp = new ResponseEntity<>(invoiceBeanList,HttpStatus.OK);
                logger.info(methodName + "return. invoiceBeanList of size: {} ",invoiceBeanList.size());
        }catch (ApiException apiException) {
            invoiceGenerateResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
            logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
        } catch (DataIntegrityViolationException d) {
            invoiceGenerateResp = new ResponseEntity<>(new Message("Data Integrity Violation: "+d.getMessage()), HttpStatus.BAD_REQUEST);
            logger.error(methodName+"Data Integrity Violation Exception occurred: {}", d.getMessage());
        } catch (Exception e) {
            invoiceGenerateResp = new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);
        }
        return invoiceGenerateResp;
    }

    @PostMapping("/generate/save-non-ppwa")
    public ResponseEntity<?> saveInvoiceNonPpwa(@RequestBody InvoiceBean invoiceBean){
        final String methodName = "saveInvoiceNonPpwa() : ";
        logger.info(methodName + "called. with parameters InvoiceBean invoiceBean: {}",invoiceBean.toString());
        ResponseEntity invoiceSaveResp = null;
        try {
                String responseMessage = invoiceService.saveInvoiceNonPpwa(invoiceBean);
                invoiceSaveResp = new ResponseEntity<>(new Message(responseMessage + " invoice saved."),HttpStatus.OK);
                logger.info(methodName + "return. responseMessage : {} ",responseMessage);
        }catch (ApiException apiException) {
            invoiceSaveResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
            logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
        } catch (DataIntegrityViolationException d) {
            invoiceSaveResp = new ResponseEntity<>(new Message("Data Integrity Violation: "+d.getMessage()), HttpStatus.BAD_REQUEST);
            logger.error(methodName+"Data Integrity Violation Exception occurred: {}", d.getMessage());
        } catch (Exception e) {
            invoiceSaveResp = new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);
        }
        return invoiceSaveResp;
    }

    @PostMapping("/generate/save-ppwa")
    public ResponseEntity<?> saveInvoicePpwa(@RequestBody List<InvoiceBean> invoiceBeanList){
        final String methodName = "saveInvoicePpwa() : ";
        logger.info(methodName + "called. with request body of invoiceBeanList of size: {}, ",invoiceBeanList.size());
        ResponseEntity invoiceSaveResp = null;
        try {
            String responseMessage = invoiceService.saveInvoicePpwa(invoiceBeanList);
            invoiceSaveResp = new ResponseEntity<>(new Message(responseMessage + " invoice saved."),HttpStatus.OK);
            logger.info(methodName + "return. responseMessage : {} ",responseMessage);
        }catch (ApiException apiException) {
            invoiceSaveResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
            logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
        } catch (DataIntegrityViolationException d) {
            invoiceSaveResp = new ResponseEntity<>(new Message("Data Integrity Violation: "+d.getMessage()), HttpStatus.BAD_REQUEST);
            logger.error(methodName+"Data Integrity Violation Exception occurred: {}", d.getMessage());
        } catch (Exception e) {
            invoiceSaveResp = new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);
        }
        return invoiceSaveResp;
    }

    //This api will be used by developer or circle to view non ppwa investor invoice
    @GetMapping("/view/non-ppwa/invoiceNumber/{invoiceNumber}")
    public ResponseEntity<?> viewInvoiceNonPpwa(@PathVariable("invoiceNumber") String invoiceNumber){
        final String methodName = "viewInvoiceNonPpwa() : ";
        logger.info(methodName + "called. with parameters invoiceNumber: {}",invoiceNumber);
        ResponseEntity invoiceViewResp = null;
        try {

            InvoiceBean invoiceBean = invoiceService.viewInvoiceNonPpwa(invoiceNumber);
            invoiceViewResp = new ResponseEntity<>(invoiceBean,HttpStatus.OK);
            logger.info(methodName + "return. InvoiceBean invoiceBean : {} ",invoiceBean.toString());
        }catch (ApiException apiException) {
            invoiceViewResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
            logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
        } catch (DataIntegrityViolationException d) {
            invoiceViewResp = new ResponseEntity<>(new Message("Data Integrity Violation: "+d.getMessage()), HttpStatus.BAD_REQUEST);
            logger.error(methodName+"Data Integrity Violation Exception occurred: {}", d.getMessage());
        } catch (Exception e) {
            invoiceViewResp = new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);
        }
        return invoiceViewResp;
    }

    //This api will be used by developer or circle to view ppwa investor invoice
    @GetMapping("/view/invoiceNumber/{invoiceNumber}")
    public ResponseEntity<?> viewInvoicePpwa(@PathVariable("invoiceNumber") String invoiceNumber){
        final String methodName = "viewInvoicePpwa() : ";
        logger.info(methodName + "called. with parameters invoiceNumber: {}",invoiceNumber);
        ResponseEntity invoiceViewResp = null;
        try {
            List<InvoiceBean> invoiceBeanList = invoiceService.viewInvoicePpwa(invoiceNumber);
            invoiceViewResp = new ResponseEntity<>(invoiceBeanList,HttpStatus.OK);
            logger.info(methodName + "return. invoiceBeanList of size : {} ",invoiceBeanList.size());
        }catch (ApiException apiException) {
            invoiceViewResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
            logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
        } catch (DataIntegrityViolationException d) {
            invoiceViewResp = new ResponseEntity<>(new Message("Data Integrity Violation: "+d.getMessage()), HttpStatus.BAD_REQUEST);
            logger.error(methodName+"Data Integrity Violation Exception occurred: {}", d.getMessage());
        } catch (Exception e) {
            invoiceViewResp = new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);
        }
        return invoiceViewResp;
    }

    //This api will be used by developer to submit invoices for circle approval
    @PostMapping("/submit")
    public ResponseEntity<?> submitInvoice(@RequestBody List<InvoiceInvestorDto> invoiceInvestorDtoList){
        final String methodName = "submitInvoice() : ";
        logger.info(methodName + "called. with Request body of invoiceInvestorDtoList of size: {}",invoiceInvestorDtoList.size());
        ResponseEntity invoiceSubmitResp = null;
        try {
             String message = invoiceService.submitInvoice(invoiceInvestorDtoList);
            invoiceSubmitResp = new ResponseEntity<>(new Message(message),HttpStatus.OK);
            logger.info(methodName + "return. string message variable : {} ",message);
        }catch (ApiException apiException) {
            invoiceSubmitResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
            logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
        } catch (DataIntegrityViolationException d) {
            invoiceSubmitResp = new ResponseEntity<>(new Message("Data Integrity Violation: "+d.getMessage()), HttpStatus.BAD_REQUEST);
            logger.error(methodName+"Data Integrity Violation Exception occurred: {}", d.getMessage());
        } catch (Exception e) {
            invoiceSubmitResp = new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);
        }
        return invoiceSubmitResp;
    }

    //This api will be used by circle to approve invoices
    @PostMapping("/approve")
    public ResponseEntity<?> approveInvoice(@RequestBody List<InvoiceInvestorDto> invoiceInvestorDtoList){
        final String methodName = "approveInvoice() : ";
        logger.info(methodName + "called. with Request body of invoiceInvestorDtoList of size: {}",invoiceInvestorDtoList.size());
        ResponseEntity invoiceApproveResp = null;
        try {
            String message = invoiceService.approveInvoice(invoiceInvestorDtoList);
            invoiceApproveResp = new ResponseEntity<>(new Message(message),HttpStatus.OK);
            logger.info(methodName + "return. string message variable : {} ",message);
        }catch (ApiException apiException) {
            invoiceApproveResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
            logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
        } catch (DataIntegrityViolationException d) {
            invoiceApproveResp = new ResponseEntity<>(new Message("Data Integrity Violation: "+d.getMessage()), HttpStatus.BAD_REQUEST);
            logger.error(methodName+"Data Integrity Violation Exception occurred: {}", d.getMessage());
        } catch (Exception e) {
            invoiceApproveResp = new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);
        }
        return invoiceApproveResp;
    }

    //This api will be used by circle to reject invoices
    @PostMapping("/reject")
    public ResponseEntity<?> rejectInvoice(@RequestBody List<InvoiceInvestorDto> invoiceInvestorDtoList){
        final String methodName = "rejectInvoice() : ";
        logger.info(methodName + "called. with Request body of invoiceInvestorDtoList of size: {}",invoiceInvestorDtoList.size());
        ResponseEntity invoiceApproveResp = null;
        try {
            String message = invoiceService.rejectInvoice(invoiceInvestorDtoList);
            invoiceApproveResp = new ResponseEntity<>(new Message(message),HttpStatus.OK);
            logger.info(methodName + "return. string message variable : {} ",message);
        }catch (ApiException apiException) {
            invoiceApproveResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
            logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
        } catch (DataIntegrityViolationException d) {
            invoiceApproveResp = new ResponseEntity<>(new Message("Data Integrity Violation: "+d.getMessage()), HttpStatus.BAD_REQUEST);
            logger.error(methodName+"Data Integrity Violation Exception occurred: {}", d.getMessage());
        } catch (Exception e) {
            invoiceApproveResp = new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);
        }
        return invoiceApproveResp;
    }

}
