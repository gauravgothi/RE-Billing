package in.co.mpwin.rebilling.controller.thirdparty;

import in.co.mpwin.rebilling.beans.invoice.InvoiceBean;
import in.co.mpwin.rebilling.beans.thirdparty.ThirdPartyBean;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.Message;
import in.co.mpwin.rebilling.services.thirdparty.ThirdPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/thirdparty")
@CrossOrigin(origins="*")
public class ThirdPartyController {

    @Autowired private ThirdPartyService thirdPartyService;
    // save third party bean into database
    @PostMapping("/save")
    public ResponseEntity<?> saveThirdPartyBean(@RequestBody ThirdPartyBean tpBean)
    {
        ResponseEntity resp = null;
        try {
            ThirdPartyBean saveBean = thirdPartyService.saveThirdPartyBean(tpBean);
            resp = new ResponseEntity<>( saveBean, HttpStatus.OK);
        }catch (ApiException apiException) {
            resp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
        } catch (DataIntegrityViolationException d) {
           resp = new ResponseEntity<>(new Message("Data Integrity Violation"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            resp = new ResponseEntity<>(new Message("Exception: "+e.getMessage().substring(0,200)), HttpStatus.BAD_REQUEST);
        }
        return resp;
    }

    // get list of third party saved in database

    @GetMapping("/list")
    public ResponseEntity<?> getThirdPartyBeans() {
        ResponseEntity resp = null;
        try {
            List<ThirdPartyBean> tpLists = thirdPartyService.getThirdPartyBeans();
            resp = new ResponseEntity<>(tpLists, HttpStatus.OK);
            } catch (ApiException apiException) {
                resp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
            } catch (DataIntegrityViolationException d) {
                resp = new ResponseEntity<>(new Message("Data Integrity Violation"), HttpStatus.BAD_REQUEST);
            } catch (Exception e) {
                e.printStackTrace();
                resp = new ResponseEntity<>(new Message("Exception: " + e.getMessage().substring(0, 200)), HttpStatus.BAD_REQUEST);
            }
        return resp;
    }
}
