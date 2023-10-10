package in.co.mpwin.rebilling.controller.invoicecontroller;

import in.co.mpwin.rebilling.beans.invoice.InvoiceBean;
import in.co.mpwin.rebilling.dto.InvoiceInvestorDto;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.Message;
import in.co.mpwin.rebilling.services.invoiceservice.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invoice")
@CrossOrigin(origins="*")
public class InvoiceController {

    @Autowired private InvoiceService invoiceService;

    @GetMapping("/load/meterNo/{meterNo}/monthYear/{monthYear}")
    public ResponseEntity<?> loadInvoiceDetailOfMeter(@PathVariable("meterNo") String meterNo,@PathVariable ("monthYear") String monthYear){
        ResponseEntity loadInvoiceResp = null;
        try {
                List<InvoiceInvestorDto> invoiceInvestorDtoList = invoiceService.loadInvoiceDetailOfMeter(meterNo,monthYear);
                loadInvoiceResp = new ResponseEntity(invoiceInvestorDtoList,HttpStatus.OK);
        }catch (ApiException apiException) {
            loadInvoiceResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
        } catch (DataIntegrityViolationException d) {
            loadInvoiceResp = new ResponseEntity<>(new Message("Data Integrity Violation"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loadInvoiceResp;
    }

    @GetMapping("/generate/investor/{investor}/monthYear/{monthYear}")
    public ResponseEntity<?> generateInvoiceNonPPWA(@PathVariable ("investor") String investor,@PathVariable ("monthYear") String monthYear){
        ResponseEntity invoiceGenerateResp = null;
        try {
              InvoiceBean invoiceBean = invoiceService.generateInvoiceNonPPWA(investor,monthYear);
              invoiceGenerateResp = new ResponseEntity<>(invoiceBean,HttpStatus.OK);
        }catch (ApiException apiException) {
            invoiceGenerateResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
        } catch (DataIntegrityViolationException d) {
            invoiceGenerateResp = new ResponseEntity<>(new Message("Data Integrity Violation"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
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
            invoiceGenerateResp = new ResponseEntity<>(new Message("Data Integrity Violation"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return invoiceGenerateResp;
    }

    @PostMapping("/generate/save-non-ppwa")
    public ResponseEntity<?> saveInvoiceNonPpwa(@RequestBody InvoiceBean invoiceBean){
        ResponseEntity invoiceSaveResp = null;
        try {
                String responseMessage = invoiceService.saveInvoiceNonPpwa(invoiceBean);
                invoiceSaveResp = new ResponseEntity<>(responseMessage,HttpStatus.OK);
        }catch (ApiException apiException) {
            invoiceSaveResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
        } catch (DataIntegrityViolationException d) {
            invoiceSaveResp = new ResponseEntity<>(new Message("Data Integrity Violation"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return invoiceSaveResp;
    }

    @PostMapping("/generate/save-ppwa")
    public ResponseEntity<?> saveInvoicePpwa(@RequestBody List<InvoiceBean> invoiceBeanList){
        ResponseEntity invoiceSaveResp = null;
        try {
            String responseMessage = invoiceService.saveInvoicePpwa(invoiceBeanList);
            invoiceSaveResp = new ResponseEntity<>(responseMessage,HttpStatus.OK);
        }catch (ApiException apiException) {
            invoiceSaveResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
        } catch (DataIntegrityViolationException d) {
            invoiceSaveResp = new ResponseEntity<>(new Message("Data Integrity Violation"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return invoiceSaveResp;
    }

    //This api will be used by developer to submit invoices for circle approval
    @PostMapping("/submit")
    public ResponseEntity<?> submitInvoice(@RequestBody List<InvoiceInvestorDto> invoiceInvestorDtoList){
        ResponseEntity invoiceSubmitResp = null;
        try {
             String message = invoiceService.submitInvoice(invoiceInvestorDtoList);
            invoiceSubmitResp = new ResponseEntity<>(message,HttpStatus.OK);
        }catch (ApiException apiException) {
            invoiceSubmitResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
        } catch (DataIntegrityViolationException d) {
            invoiceSubmitResp = new ResponseEntity<>(new Message("Data Integrity Violation"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return invoiceSubmitResp;
    }

    //This api will be used by circle to approve invoices
    @PostMapping("/approve")
    public ResponseEntity<?> approveInvoice(@RequestBody List<InvoiceInvestorDto> invoiceInvestorDtoList){
        ResponseEntity invoiceApproveResp = null;
        try {
            String message = invoiceService.approveInvoice(invoiceInvestorDtoList);
            invoiceApproveResp = new ResponseEntity<>(message,HttpStatus.OK);
        }catch (ApiException apiException) {
            invoiceApproveResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
        } catch (DataIntegrityViolationException d) {
            invoiceApproveResp = new ResponseEntity<>(new Message("Data Integrity Violation"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return invoiceApproveResp;
    }

    //This api will be used by circle to reject invoices
    @PostMapping("/reject")
    public ResponseEntity<?> rejectInvoice(@RequestBody List<InvoiceInvestorDto> invoiceInvestorDtoList){
        ResponseEntity invoiceApproveResp = null;
        try {
            String message = invoiceService.rejectInvoice(invoiceInvestorDtoList);
            invoiceApproveResp = new ResponseEntity<>(message,HttpStatus.OK);
        }catch (ApiException apiException) {
            invoiceApproveResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
        } catch (DataIntegrityViolationException d) {
            invoiceApproveResp = new ResponseEntity<>(new Message("Data Integrity Violation"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return invoiceApproveResp;
    }

}
