package in.co.mpwin.rebilling.controller.invoicecontroller;

import in.co.mpwin.rebilling.beans.invoice.InvoiceBean;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.Message;
import in.co.mpwin.rebilling.services.invoiceservice.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invoice")
@CrossOrigin(origins="*")
public class InvoiceController {

    @Autowired private InvoiceService invoiceService;



    @GetMapping("/generate/investor/{investor}/monthYear/{monthYear}")
    public ResponseEntity<?> generateInvoice(@PathVariable ("investor") String investor,@PathVariable ("monthYear") String monthYear){
        ResponseEntity invoiceGenerateResp = null;
        try {
              InvoiceBean invoiceBean = invoiceService.generateInvoice(investor,monthYear);
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

    @PostMapping("/generate/save")
    public ResponseEntity<?> saveInvoice(@RequestBody InvoiceBean invoiceBean){
        ResponseEntity invoiceSaveResp = null;
        try {
                InvoiceBean savedInvoice = invoiceService.saveInvoice(invoiceBean);
                invoiceSaveResp = new ResponseEntity<>(savedInvoice,HttpStatus.OK);
        }catch (ApiException apiException) {
            invoiceSaveResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
        } catch (DataIntegrityViolationException d) {
            invoiceSaveResp = new ResponseEntity<>(new Message("Data Integrity Violation"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return invoiceSaveResp;
    }
}
