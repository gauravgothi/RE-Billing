package in.co.mpwin.rebilling.controller.readingcontroller;

import in.co.mpwin.rebilling.dto.ConsumptionPercentageDto;
import in.co.mpwin.rebilling.miscellanious.Message;
import in.co.mpwin.rebilling.services.readingservice.ConsumptionPercentageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/report")
@CrossOrigin(origins="*")
public class ConsumptionPercentageController {
    @Autowired
    ConsumptionPercentageService consumptionPercentageService;

    @GetMapping("/month/{month}")
    public ResponseEntity<?> getConsumptionReport(@PathVariable("month") String month){
        ResponseEntity reportDtoResp = null;
        List<ConsumptionPercentageDto> report = null;
        try {
            report = consumptionPercentageService.calculatePercentageReport(month);
            reportDtoResp = new ResponseEntity<>(report, HttpStatus.OK);
        } catch (ParseException e) {
            reportDtoResp = new ResponseEntity<>(new Message("Month is not in valid format(Mmm-yyyy)"),HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
            reportDtoResp = new ResponseEntity<>(new Message(e.getMessage().substring(0,e.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
        }
        return reportDtoResp;
    }
}
