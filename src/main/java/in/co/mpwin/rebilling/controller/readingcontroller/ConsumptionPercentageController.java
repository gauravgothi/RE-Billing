package in.co.mpwin.rebilling.controller.readingcontroller;

import in.co.mpwin.rebilling.dto.ConsumptionPercentageDto;
import in.co.mpwin.rebilling.dto.ConsumptionPercentageDto2;
import in.co.mpwin.rebilling.beans.readingbean.FivePercentBean;
import in.co.mpwin.rebilling.miscellanious.DateMethods;
import in.co.mpwin.rebilling.miscellanious.Message;
import in.co.mpwin.rebilling.services.readingservice.ConsumptionPercentageService;
import in.co.mpwin.rebilling.services.readingservice.ConsumerPercentageService2;
import in.co.mpwin.rebilling.services.readingservice.FivePercentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/report")
@CrossOrigin(origins="*")
public class ConsumptionPercentageController {
    @Autowired
    ConsumptionPercentageService consumptionPercentageService;
    @Autowired
    ConsumerPercentageService2 consumerPercentageService2;
    @Autowired
    FivePercentService fivePercentService;

    @GetMapping("/5percent/month/{month}")
    public ResponseEntity<?> getConsumptionReport(@PathVariable("month") String month){
        ResponseEntity reportDtoResp = null;
        List<ConsumptionPercentageDto> consumptionPercentageDtoList = null;
        try {
            consumptionPercentageDtoList = consumptionPercentageService.calculatePercentageReport(month);
            List<FivePercentBean> fivePercentReport =  consumptionPercentageService.percentageBeanToDto(consumptionPercentageDtoList);
            reportDtoResp = new ResponseEntity<>(fivePercentReport, HttpStatus.OK);
        } catch (ParseException e) {
            reportDtoResp = new ResponseEntity<>(new Message("Month is not in valid format(Mmm-yyyy)"),HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
            reportDtoResp = new ResponseEntity<>(new Message(e.getMessage().substring(0,e.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
        }
        return reportDtoResp;
    }

    @GetMapping("/5percent/{startDate}/{endDate}")
    public ResponseEntity<?> getConsumptionReport2(@PathVariable("startDate") String startDate,@PathVariable("endDate") String endDate){
        ResponseEntity reportDtoResp = null;
        List<ConsumptionPercentageDto2> consumptionPercentageDtoList = null;
        try {
                Date sDate = new SimpleDateFormat("dd-MM-yyyy").parse(startDate);
                Date eDate = new SimpleDateFormat("dd-MM-yyyy").parse(endDate);
                String month = new DateMethods().getMonthYear(eDate);

                consumptionPercentageDtoList = consumerPercentageService2.calculatePercentageReport2(sDate,eDate);
                List<FivePercentBean> fivePercentReport =  consumerPercentageService2.consumptionPercentageDto2ToFivePercentageBean(consumptionPercentageDtoList,month);
                fivePercentService.insertFivePercentReport(fivePercentReport);//report save to table
                reportDtoResp = new ResponseEntity<>(fivePercentReport, HttpStatus.OK);
           // reportDtoResp = new ResponseEntity<>(consumptionPercentageDtoList, HttpStatus.OK);
        } catch (ParseException e) {
            reportDtoResp = new ResponseEntity<>(new Message("Month is not in valid format(Mmm-yyyy)"),HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
            reportDtoResp = new ResponseEntity<>(new Message(e.getMessage().substring(0,e.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
        }
        return reportDtoResp;
    }

}
