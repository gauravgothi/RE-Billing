package in.co.mpwin.rebilling.controller.readingcontroller;

import in.co.mpwin.rebilling.dto.ConsumptionPercentageDto;
import in.co.mpwin.rebilling.dto.ConsumptionPercentageDto2;
import in.co.mpwin.rebilling.beans.readingbean.FivePercentBean;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.DateMethods;
import in.co.mpwin.rebilling.miscellanious.Message;
import in.co.mpwin.rebilling.repositories.readingrepo.FivePercentRepo;
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
    @Autowired
    private FivePercentRepo fivePercentRepo;

    @GetMapping("/5percent/month/{month}")
    public ResponseEntity<?> getConsumptionReport(@PathVariable("month") String month){
        ResponseEntity reportDtoResp = null;
        List<ConsumptionPercentageDto2> consumptionPercentageDtoList = null;
        try {
                Date previousReadDate = new DateMethods().getCurrentAndPreviousDate(month).get(0);
                Date currentReadDate = new DateMethods().getCurrentAndPreviousDate(month).get(1);

                consumptionPercentageDtoList = consumerPercentageService2.calculatePercentageReport2(previousReadDate,currentReadDate);
                List<FivePercentBean> fivePercentReport =  consumerPercentageService2.consumptionPercentageDto2ToFivePercentageBean(consumptionPercentageDtoList,month);
                fivePercentService.insertFivePercentReport(fivePercentReport);//report save to table
                if (fivePercentReport.size() == 0)
                    reportDtoResp = new ResponseEntity<>(new Message("Five Percent Report of Given month Already approved and " +
                            " no any meter left in withheld"), HttpStatus.BAD_REQUEST);
                else
                    reportDtoResp = new ResponseEntity<>(fivePercentReport, HttpStatus.OK);
                //reportDtoResp = new ResponseEntity<>(consumptionPercentageDtoList, HttpStatus.OK);
        } catch (ApiException apiException){
            reportDtoResp = new ResponseEntity<>(new Message(apiException.getMessage()),apiException.getHttpStatus());
        }catch (ArithmeticException arithmeticException){
            reportDtoResp = new ResponseEntity<>(new Message("Arithmetic Exception Occured"),HttpStatus.BAD_REQUEST);
        } catch (ParseException e) {
            reportDtoResp = new ResponseEntity<>(new Message("Month is not in valid format(Mmm-yyyy)"),HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
            reportDtoResp = new ResponseEntity<>(new Message(e.getMessage().substring(0,e.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
        }
        return reportDtoResp;
    }

    @GetMapping("/5percent/startDate/{startDate}/endDate/{endDate}")
    public ResponseEntity<?> getConsumptionReport2(@PathVariable("startDate") String startDate,@PathVariable("endDate") String endDate){
        ResponseEntity reportDtoResp = null;
        List<ConsumptionPercentageDto2> consumptionPercentageDtoList = null;
        try {
                Date sDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
                Date eDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
                String month = new DateMethods().getMonthYear(eDate);

                consumptionPercentageDtoList = consumerPercentageService2.calculatePercentageReport2(sDate,eDate);
                List<FivePercentBean> fivePercentReport =  consumerPercentageService2.consumptionPercentageDto2ToFivePercentageBean(consumptionPercentageDtoList,month);
                fivePercentService.insertFivePercentReport(fivePercentReport);//report save to table
                reportDtoResp = new ResponseEntity<>(fivePercentReport, HttpStatus.OK);
           //reportDtoResp = new ResponseEntity<>(consumptionPercentageDtoList, HttpStatus.OK);
        } catch (ParseException e) {
            reportDtoResp = new ResponseEntity<>(new Message("Month is not in valid format(Mmm-yyyy)"),HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
            //reportDtoResp = new ResponseEntity<>(new Message(e.getMessage().substring(0,e.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
        }
        return reportDtoResp;
    }

    //Initial approval of pass and fail reading by amr user
    @PostMapping("/5percent/approve")
    public ResponseEntity<?> amrUserAction(@RequestBody List<FivePercentBean> fivePercentBeanList){
        ResponseEntity reportDtoResp = null;
        try {
                fivePercentService.amrUserAccept(fivePercentBeanList);

        }catch (ApiException apiException){
            reportDtoResp = new ResponseEntity<>(new Message(apiException.getMessage()),apiException.getHttpStatus());
        }
        catch (Exception e){
            e.printStackTrace();
            reportDtoResp = new ResponseEntity<>(new Message(e.getMessage().substring(0,e.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new Message("Record saved successfully"),HttpStatus.OK);
    }

    // this is used by amr to load fail approved reading on initial approval stage...... only for viewing pass and fail
    @GetMapping("/5percent/approve/monthYear/{monthYear}/result/{result}")
    public ResponseEntity<?> getAmrUserAcceptByMonthAndResult(@PathVariable String monthYear,@PathVariable String result){
        ResponseEntity reportDtoResp = null;
        try {
            List<FivePercentBean> passOrFailResults =   fivePercentService.getAmrUserAcceptByMonthAndResult(monthYear,result);
            reportDtoResp = new ResponseEntity<>(passOrFailResults,HttpStatus.OK);
        }catch (ApiException apiException){
            reportDtoResp = new ResponseEntity<>(new Message(apiException.getMessage()),apiException.getHttpStatus());
        }
        catch (Exception e){
            e.printStackTrace();
            reportDtoResp = new ResponseEntity<>(new Message(e.getMessage().substring(0,e.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
        }
        return reportDtoResp ;
    }

    // this is used by amr For performing action on failed readings....... by simply loading non action reading
    @GetMapping("/5percent/approve/monthYear/{monthYear}/result/fail/remark/calculated")
    public ResponseEntity<?> getAmrUserAcceptFailResultForAction(@PathVariable String monthYear){
        ResponseEntity reportDtoResp = null;
        try {
            List<FivePercentBean> passOrFailResults =   fivePercentService.getAmrUserAcceptFailResultForAction(monthYear,"fail","amr_approved");

            reportDtoResp = new ResponseEntity<>(passOrFailResults,HttpStatus.OK);
        }catch (ApiException apiException){
            reportDtoResp = new ResponseEntity<>(new Message(apiException.getMessage()),apiException.getHttpStatus());
        }
        catch (Exception e){
            e.printStackTrace();
            reportDtoResp = new ResponseEntity<>(new Message(e.getMessage().substring(0,e.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
        }
        return reportDtoResp ;
    }

    //After loading fail approved reading this is used by AMR to force accept failed readings
    @PostMapping("/5percent/forceAccept")
    public ResponseEntity<?> amrUserForceAction(@RequestBody List<FivePercentBean> fivePercentBeanList){
        ResponseEntity reportDtoResp = null;
        try {   //list must have meter selected flag either main or check
                fivePercentService.amrUserForceAccept(fivePercentBeanList);
                reportDtoResp = new ResponseEntity<>(new Message("Record saved successfully"),HttpStatus.OK);
        }catch (ApiException apiException){
            reportDtoResp = new ResponseEntity<>(new Message(apiException.getMessage()),apiException.getHttpStatus());
        }
        catch (Exception e){
            e.printStackTrace();
            reportDtoResp = new ResponseEntity<>(new Message(e.getMessage().substring(0,e.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
        }
        return reportDtoResp;
    }

}
