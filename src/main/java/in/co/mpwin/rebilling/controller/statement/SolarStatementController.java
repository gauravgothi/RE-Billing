package in.co.mpwin.rebilling.controller.statement;

import in.co.mpwin.rebilling.beans.statement.SolarStatementBean;
import in.co.mpwin.rebilling.beans.thirdparty.ThirdPartyBean;
import in.co.mpwin.rebilling.controller.invoicecontroller.InvoiceController;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.Message;
import in.co.mpwin.rebilling.services.statement.SolarStatementReportService;
import in.co.mpwin.rebilling.services.statement.SolarStatementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(SolarStatementController.class);

    @Autowired
    SolarStatementService solarStatementService;

    @Autowired
    SolarStatementReportService solarStatementReportService;

    @GetMapping("/solar-generate-save-download/meterNo/{meterNo}/monthYear/{monthYear}")
    public ResponseEntity<?> getSolarStatement(@PathVariable("meterNo") String meterNo,@PathVariable("monthYear") String monthYear)
    {
        final String methodName = "getSolarStatement() : ";
        logger.info(methodName + "called. with parameters meterNo: {}, monthYear: {}",meterNo,monthYear);
        ResponseEntity statementResp = null;
        try {
                List<SolarStatementBean> solarStatementBeanList = solarStatementService.getSolarStatement(meterNo,monthYear);
                byte[] solarStatementInPdf = solarStatementReportService.exportSolarStatement("pdf",solarStatementBeanList);
                statementResp = new ResponseEntity<>( solarStatementInPdf, HttpStatus.OK);
            logger.info(methodName + "return. byte array of solar statement pdf:");
        }catch (ApiException apiException) {
            statementResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
            logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
        } catch (DataIntegrityViolationException d) {
            statementResp = new ResponseEntity<>(new Message("Data Integrity Violation"), HttpStatus.BAD_REQUEST);
            logger.error(methodName+"Data Integrity Violation Exception occurred: {}", d.getMessage());
        } catch (Exception e) {
            statementResp = new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);
        }
        return statementResp;
    }

    @GetMapping("/solar-generate-save/meterNo/{meterNo}/monthYear/{monthYear}")
    public ResponseEntity<?> generateSolarStatement(@PathVariable("meterNo") String meterNo,@PathVariable("monthYear") String monthYear)
    {
        final String methodName = "generateSolarStatement() : ";
        logger.info(methodName + "called. with parameters meterNo: {}, monthYear: {}",meterNo,monthYear);
        ResponseEntity statementBeanListResp = null;
        try {
            List<SolarStatementBean> solarStatementBeanList = solarStatementService.getSolarStatement(meterNo,monthYear);
            statementBeanListResp = new ResponseEntity<>( solarStatementBeanList, HttpStatus.OK);
            logger.info(methodName + "return solar statement bean list of size ={}",solarStatementBeanList.size());
        }catch (ApiException apiException) {
            statementBeanListResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
            logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
        } catch (DataIntegrityViolationException d) {
            statementBeanListResp = new ResponseEntity<>(new Message("Data Integrity Violation"), HttpStatus.BAD_REQUEST);
            logger.error(methodName+"Data Integrity Violation Exception occurred: {}", d.getMessage());
        } catch (Exception e) {
            statementBeanListResp = new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);
        }
        return statementBeanListResp;
    }

    @GetMapping("/solar-download/meterNo/{meterNo}/monthYear/{monthYear}")
    public ResponseEntity<?> downloadSolarStatement(@PathVariable("meterNo") String meterNo,@PathVariable("monthYear") String monthYear)
    {
        final String methodName = "downloadSolarStatement() : ";
        logger.info(methodName + "called. with parameters meterNo: {}, monthYear: {}",meterNo,monthYear);
        ResponseEntity statementResp = null;
        try {
            List<SolarStatementBean> solarStatementBeanList = solarStatementService.downloadSolarStatement(meterNo,monthYear);
            byte[] solarStatementInPdf = solarStatementReportService.exportSolarStatement("pdf",solarStatementBeanList);
            statementResp = new ResponseEntity<>( solarStatementInPdf, HttpStatus.OK);
            logger.info(methodName + "return. byte array of solar statement pdf:");
        }catch (ApiException apiException) {
            statementResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
            logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
        } catch (DataIntegrityViolationException d) {
            statementResp = new ResponseEntity<>(new Message("Data Integrity Violation"), HttpStatus.BAD_REQUEST);
            logger.error(methodName+"Data Integrity Violation Exception occurred: {}", d.getMessage());
        } catch (Exception e) {
            statementResp = new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);
        }
        return statementResp;
    }

}
