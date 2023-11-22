package in.co.mpwin.rebilling.controller.invoicecontroller;

import in.co.mpwin.rebilling.beans.invoice.InvoiceBean;
import in.co.mpwin.rebilling.dto.InvoiceInvestorDto;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.Message;
import in.co.mpwin.rebilling.services.invoiceservice.InvoiceService;
import in.co.mpwin.rebilling.services.mapping.InvestorPpwaMappingService;
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

    @Autowired private InvoiceService invoiceService;
    @Autowired private InvestorPpwaMappingService investorPpwaMappingService;

    @GetMapping("/load/meterNo/{meterNo}/monthYear/{monthYear}")
    public ResponseEntity<?> loadInvoiceDetailOfMeter(@PathVariable("meterNo") String meterNo,@PathVariable ("monthYear") String monthYear){
        ResponseEntity loadInvoiceResp = null;
        try {

                List<InvoiceInvestorDto> invoiceInvestorDtoList = invoiceService.loadInvoiceDetailOfMeter(meterNo,monthYear);
                loadInvoiceResp = new ResponseEntity(invoiceInvestorDtoList,HttpStatus.OK);
        }catch (ApiException apiException) {
            loadInvoiceResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
        } catch (DataIntegrityViolationException d) {
            loadInvoiceResp = new ResponseEntity<>(new Message("Data Integrity Violation : "+d.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            loadInvoiceResp = new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return loadInvoiceResp;
    }

    @GetMapping("/generate/investor/{investor}/monthYear/{monthYear}")
    public ResponseEntity<?> generateInvoice(@PathVariable ("investor") String investor,@PathVariable ("monthYear") String monthYear){
        ResponseEntity invoiceGenerateResp = null;
        try {
                    List<InvoiceBean> invoiceBeanList = new ArrayList<>();
                     String ppwaNo = investorPpwaMappingService.getPpwaNoByInvestorCode(investor,"active");
                        if (ppwaNo.equals("NA"))
                            invoiceBeanList.add(invoiceService.generateInvoiceNonPPWA(investor,monthYear));
                        else
                            invoiceBeanList = invoiceService.generateInvoicePPWA(ppwaNo,monthYear);
              invoiceGenerateResp = new ResponseEntity<>(invoiceBeanList,HttpStatus.OK);
        }catch (ApiException apiException) {
            invoiceGenerateResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
        } catch (DataIntegrityViolationException d) {
            invoiceGenerateResp = new ResponseEntity<>(new Message("Data Integrity Violation: "+d.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            invoiceGenerateResp = new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return invoiceGenerateResp;
    }

    @GetMapping("/generate/ppwaNo/{ppwaNo}/monthYear/{monthYear}")
    public ResponseEntity<?> generateInvoicePPWA(@PathVariable ("ppwaNo") String ppwaNo,@PathVariable ("monthYear") String monthYear){
        ResponseEntity invoiceGenerateResp = null;
        try {
                List<InvoiceBean> invoiceBeanList = invoiceService.generateInvoicePPWA(ppwaNo,monthYear);
                invoiceGenerateResp = new ResponseEntity<>(invoiceBeanList,HttpStatus.OK);
        }catch (ApiException apiException) {
            invoiceGenerateResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
        } catch (DataIntegrityViolationException d) {
            invoiceGenerateResp = new ResponseEntity<>(new Message("Data Integrity Violation: "+d.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            invoiceGenerateResp = new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return invoiceGenerateResp;
    }

    @PostMapping("/generate/save-non-ppwa")
    public ResponseEntity<?> saveInvoiceNonPpwa(@RequestBody InvoiceBean invoiceBean){
        ResponseEntity invoiceSaveResp = null;
        try {
                String responseMessage = invoiceService.saveInvoiceNonPpwa(invoiceBean);
                invoiceSaveResp = new ResponseEntity<>(new Message(responseMessage + " invoice saved."),HttpStatus.OK);
        }catch (ApiException apiException) {
            invoiceSaveResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
        } catch (DataIntegrityViolationException d) {
            invoiceSaveResp = new ResponseEntity<>(new Message("Data Integrity Violation: "+d.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            invoiceSaveResp = new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return invoiceSaveResp;
    }

    @PostMapping("/generate/save-ppwa")
    public ResponseEntity<?> saveInvoicePpwa(@RequestBody List<InvoiceBean> invoiceBeanList){
        ResponseEntity invoiceSaveResp = null;
        try {
            String responseMessage = invoiceService.saveInvoicePpwa(invoiceBeanList);
            invoiceSaveResp = new ResponseEntity<>(new Message(responseMessage + " invoice saved."),HttpStatus.OK);
        }catch (ApiException apiException) {
            invoiceSaveResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
        } catch (DataIntegrityViolationException d) {
            invoiceSaveResp = new ResponseEntity<>(new Message("Data Integrity Violation: "+d.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            invoiceSaveResp = new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return invoiceSaveResp;
    }

    //This api will be used by developer or circle to view non ppwa investor invoice
    @GetMapping("/view/non-ppwa/invoiceNumber/{invoiceNumber}")
    public ResponseEntity<?> viewInvoiceNonPpwa(@PathVariable("invoiceNumber") String invoiceNumber){
        ResponseEntity invoiceViewResp = null;
        try {

            InvoiceBean invoiceBean = invoiceService.viewInvoiceNonPpwa(invoiceNumber);
            invoiceViewResp = new ResponseEntity<>(invoiceBean,HttpStatus.OK);
        }catch (ApiException apiException) {
            invoiceViewResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
        } catch (DataIntegrityViolationException d) {
            invoiceViewResp = new ResponseEntity<>(new Message("Data Integrity Violation: "+d.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            invoiceViewResp = new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return invoiceViewResp;
    }

    //This api will be used by developer or circle to view ppwa investor invoice
    @GetMapping("/view/invoiceNumber/{invoiceNumber}")
    public ResponseEntity<?> viewInvoicePpwa(@PathVariable("invoiceNumber") String invoiceNumber){
        ResponseEntity invoiceViewResp = null;
        try {
            List<InvoiceBean> invoiceBeanList = invoiceService.viewInvoicePpwa(invoiceNumber);
            invoiceViewResp = new ResponseEntity<>(invoiceBeanList,HttpStatus.OK);
        }catch (ApiException apiException) {
            invoiceViewResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
        } catch (DataIntegrityViolationException d) {
            invoiceViewResp = new ResponseEntity<>(new Message("Data Integrity Violation: "+d.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            invoiceViewResp = new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return invoiceViewResp;
    }

    //This api will be used by developer to submit invoices for circle approval
    @PostMapping("/submit")
    public ResponseEntity<?> submitInvoice(@RequestBody List<InvoiceInvestorDto> invoiceInvestorDtoList){
        ResponseEntity invoiceSubmitResp = null;
        try {
             String message = invoiceService.submitInvoice(invoiceInvestorDtoList);
            invoiceSubmitResp = new ResponseEntity<>(new Message(message),HttpStatus.OK);
        }catch (ApiException apiException) {
            invoiceSubmitResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
        } catch (DataIntegrityViolationException d) {
            invoiceSubmitResp = new ResponseEntity<>(new Message("Data Integrity Violation: "+d.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            invoiceSubmitResp = new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return invoiceSubmitResp;
    }

    //This api will be used by circle to approve invoices
    @PostMapping("/approve")
    public ResponseEntity<?> approveInvoice(@RequestBody List<InvoiceInvestorDto> invoiceInvestorDtoList){
        ResponseEntity invoiceApproveResp = null;
        try {
            String message = invoiceService.approveInvoice(invoiceInvestorDtoList);
            invoiceApproveResp = new ResponseEntity<>(new Message(message),HttpStatus.OK);
        }catch (ApiException apiException) {
            invoiceApproveResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
        } catch (DataIntegrityViolationException d) {
            invoiceApproveResp = new ResponseEntity<>(new Message("Data Integrity Violation: "+d.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            invoiceApproveResp = new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return invoiceApproveResp;
    }

    //This api will be used by circle to reject invoices
    @PostMapping("/reject")
    public ResponseEntity<?> rejectInvoice(@RequestBody List<InvoiceInvestorDto> invoiceInvestorDtoList){
        ResponseEntity invoiceApproveResp = null;
        try {
            String message = invoiceService.rejectInvoice(invoiceInvestorDtoList);
            invoiceApproveResp = new ResponseEntity<>(new Message(message),HttpStatus.OK);
        }catch (ApiException apiException) {
            invoiceApproveResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
        } catch (DataIntegrityViolationException d) {
            invoiceApproveResp = new ResponseEntity<>(new Message("Data Integrity Violation: "+d.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            invoiceApproveResp = new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return invoiceApproveResp;
    }

}
