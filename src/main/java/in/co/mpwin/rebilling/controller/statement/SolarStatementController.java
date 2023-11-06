package in.co.mpwin.rebilling.controller.statement;

import in.co.mpwin.rebilling.beans.statement.SolarStatementBean;
import in.co.mpwin.rebilling.beans.thirdparty.ThirdPartyBean;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.Message;
import in.co.mpwin.rebilling.services.statement.SolarStatementReportService;
import in.co.mpwin.rebilling.services.statement.SolarStatementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/statement")
@CrossOrigin(origins="*")
public class SolarStatementController {

    @Autowired
    SolarStatementService solarStatementService;

    @Autowired
    SolarStatementReportService solarStatementReportService;

    @GetMapping("/solar/meterNo/{meterNo}/monthYear/{monthYear}")
    public ResponseEntity<?> getSolarStatement(@PathVariable("meterNo") String meterNo,@PathVariable("monthYear") String monthYear)
    {
        ResponseEntity statementResp = null;
        try {
                List<SolarStatementBean> solarStatementBeanList = solarStatementService.getSolarStatement(meterNo,monthYear);
                byte[] solarStatementInPdf = solarStatementReportService.exportSolarStatement("pdf",solarStatementBeanList);
                statementResp = new ResponseEntity<>( solarStatementInPdf, HttpStatus.OK);

        }catch (ApiException apiException) {
            statementResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
        } catch (DataIntegrityViolationException d) {
            statementResp = new ResponseEntity<>(new Message("Data Integrity Violation"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            statementResp = new ResponseEntity<>(new Message("Exception: "+e.getMessage().substring(0,200)), HttpStatus.BAD_REQUEST);
        }
        return statementResp;
    }

}
