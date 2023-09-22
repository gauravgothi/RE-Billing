package in.co.mpwin.rebilling.controller.readingcontroller;

import in.co.mpwin.rebilling.beans.readingbean.MeterReadingBean;
import in.co.mpwin.rebilling.dto.MeterConsumptionDto;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.Message;
import in.co.mpwin.rebilling.services.readingservice.MeterReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/meter_reading")
@CrossOrigin(origins="*")
public class MeterReadingController {
    @Autowired
    MeterReadingService meterReadingService;


    @RequestMapping(method= RequestMethod.GET,value="/status/{status}")
    public ResponseEntity<?> getAllReadingByStatus(@PathVariable(value = "status") String status){

        ResponseEntity meterReadingResp = null;
        try {
            List<MeterReadingBean> meterReadingBeanList = meterReadingService.getAllReadingByStatus(status);

            if (!meterReadingBeanList.isEmpty())
                meterReadingResp = new ResponseEntity<>(meterReadingBeanList, HttpStatus.OK);
            else if (meterReadingBeanList.isEmpty())
                meterReadingResp = new ResponseEntity<>(new Message("Meter Reading list is not available."),HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
        }
        return meterReadingResp;
    }

    @RequestMapping(method= RequestMethod.GET,value="/month/{month}/status/{status}")
    public ResponseEntity<?> getAllReadingByMonthAndStatus(@PathVariable(name = "month") String month, @PathVariable(value = "status") String status){

        ResponseEntity meterReadingResp = null;
        try {
            List<MeterReadingBean> meterReadingBeanList = meterReadingService.getAllReadingByMonthAndStatus(month,status);

            if (!meterReadingBeanList.isEmpty())
                meterReadingResp = new ResponseEntity<>(meterReadingBeanList, HttpStatus.OK);
            else if (meterReadingBeanList.isEmpty())
                meterReadingResp = new ResponseEntity<>(new Message("Meter Reading list is not available."),HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
        }
        return meterReadingResp;
    }

    @RequestMapping(method= RequestMethod.GET,value="/month/{month}/meterNo/{meterNo}/status/{status}")
    public ResponseEntity<?> getAllReadingByMonthAndMeterNoAndStatus(@PathVariable(name = "month") String month,
                                                                     @PathVariable(name = "meterNo") String meterNo,
                                                                     @PathVariable(value = "status") String status){

        ResponseEntity meterReadingResp = null;
        try {
            List<MeterReadingBean> meterReadingBeanList = meterReadingService.getAllReadingByMonthAndMeterNoAndStatus(month,meterNo,status);

            if (!meterReadingBeanList.isEmpty())
                meterReadingResp = new ResponseEntity<>(meterReadingBeanList, HttpStatus.OK);
            else if (meterReadingBeanList.isEmpty())
                meterReadingResp = new ResponseEntity<>(new Message("Meter Reading list is not available."),HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
        }
        return meterReadingResp;
    }

    @RequestMapping(method= RequestMethod.GET,value="/currentState/{currentState}/status/{status}")
    public ResponseEntity<?> getAllReadingByCurrentStateAndStatus(@PathVariable(name = "currentState") String currentState, @PathVariable(value = "status") String status){

        ResponseEntity meterReadingResp = null;
        try {
            List<MeterReadingBean> meterReadingBeanList = meterReadingService.getAllReadingByCurrentStateAndStatus(currentState,status);

            if (!meterReadingBeanList.isEmpty())
                meterReadingResp = new ResponseEntity<>(meterReadingBeanList, HttpStatus.OK);
            else if (meterReadingBeanList.isEmpty())
                meterReadingResp = new ResponseEntity<>(new Message("Meter Reading list is not available."),HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
        }
        return meterReadingResp;
    }

    @RequestMapping(method= RequestMethod.GET,value="/currentState/{currentState}/meterNo/{meterNo}/status/{status}")
    public ResponseEntity<?> getAllReadingByCurrentStateAndMeterNoAndStatus(@PathVariable(name = "currentState") String currentState,
                                                                  @PathVariable(name = "meterNo") String meterNo,
                                                                  @PathVariable(value = "status") String status){

        ResponseEntity meterReadingResp = null;
        try {
            List<MeterReadingBean> meterReadingBeanList = meterReadingService.getAllReadingByCurrentStateAndMeterNoAndStatus(currentState,meterNo,status);

            if (!meterReadingBeanList.isEmpty())
                meterReadingResp = new ResponseEntity<>(meterReadingBeanList, HttpStatus.OK);
            else if (meterReadingBeanList.isEmpty())
                meterReadingResp = new ResponseEntity<>(new Message("Meter Reading list is not available."),HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
        }
        return meterReadingResp;
    }

    @RequestMapping(method = RequestMethod.PUT,value = {"/currentState/{currentState}/updateState/{updateState}/{month}/{meterNo}/{status}",
                                                        "/endDate/{endDate}/{month}/{meterNo}/{status}"})
    public ResponseEntity<?> updateCurrentStateOrEndDate(@PathVariable(name = "currentState") Optional<String> currentState,
                                                         @PathVariable(name = "updateState") Optional<String> updateState,
                                                @PathVariable(name = "endDate") Optional<String> endDate,
                                                @PathVariable(name = "month") String month,
                                                @PathVariable(name = "meterNo") String meterNo,
                                                @PathVariable(value = "status") String status){

        ResponseEntity updateResp = null;
        try {
            if(currentState.isPresent() && updateState.isPresent() && !endDate.isPresent()){

                List<MeterReadingBean> beans = meterReadingService.updateCurrentState(String.valueOf(currentState),
                                                                                    String.valueOf(updateState),month,meterNo,status);
                updateResp = new ResponseEntity<>(beans,HttpStatus.OK);

            } else if (endDate.isPresent() && !currentState.isPresent()) {

                Date endDate2 = new SimpleDateFormat("dd-MMM-yyyy").parse(String.valueOf(endDate));
                MeterReadingBean bean = meterReadingService.updateEndDate(endDate2,month,meterNo,status);
                updateResp = new ResponseEntity<>(bean,HttpStatus.OK);
            }else
                updateResp = new ResponseEntity<>(new Message("check inputs, Nothing is updated"),HttpStatus.BAD_REQUEST);

        }catch (Exception exception){
            exception.printStackTrace();
            updateResp = new ResponseEntity<>(new Message("something went wrong! "),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return updateResp;
    }

    @RequestMapping(method = RequestMethod.POST,value = "")
    public ResponseEntity<?> createMeterReading(@RequestBody @Valid MeterReadingBean meterReadingBean){
        ResponseEntity createReadResp =  null;
        try {
            //set the meter reading source to web punched if service not called through fileuploadservice
            meterReadingBean.setReadSource("web");
            MeterReadingBean bean = meterReadingService.createMeterReading(meterReadingBean);

            if (bean!=null)
                createReadResp = new ResponseEntity<>(new Message(bean.getId() + " reading saved successfully"),HttpStatus.OK);

        }catch (ApiException apiException){
            createReadResp = new ResponseEntity<>(new Message(apiException.getMessage()),apiException.getHttpStatus());
        }catch (DataIntegrityViolationException d)
        {
            Throwable rootCause = d.getRootCause();
            String msg=rootCause.getMessage().substring(0,rootCause.getMessage().indexOf("Detail:"));
            createReadResp = new ResponseEntity<>(new Message(msg),HttpStatus.INTERNAL_SERVER_ERROR);
            return createReadResp;

        } catch (Exception e){
            e.printStackTrace();
            createReadResp = new ResponseEntity<>(new Message(" something went wrong or some exception occurred "),HttpStatus.INTERNAL_SERVER_ERROR);

        }
        return createReadResp;
    }

    //this is used by HT to view AMR accepted or force accepted meter readings
    @GetMapping("/amr_accepted/monthYear/{monthYear}")
    public ResponseEntity<?> getAmrAcceptedReadings(@PathVariable("monthYear") String monthYear){
        ResponseEntity readingDtosResp = null;
        try {
                List<MeterReadingBean> readings = meterReadingService.getAmrAcceptedReadings(monthYear);
            readingDtosResp = new ResponseEntity<>(readings,HttpStatus.OK);
        }catch (ApiException apiException){
            readingDtosResp = new ResponseEntity<>(new Message(apiException.getMessage()),apiException.getHttpStatus());
        }
        catch (Exception e){
            e.printStackTrace();
            readingDtosResp = new ResponseEntity<>(new Message(e.getMessage().substring(0,e.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
        }
        return readingDtosResp;
    }

    //this is used by HT to accept AMR accepted or force accepted meter readings
    @PostMapping("/amr_accepted/htAccept")
    public ResponseEntity<?> htUserAccept(@RequestBody List<MeterReadingBean> meterReadingBeanList){
        ResponseEntity readingActionResp = null;
        try {
                meterReadingService.htUserAccept(meterReadingBeanList);
                readingActionResp = new ResponseEntity<>(new Message("Readings approved successfully.."),HttpStatus.OK);

        }catch (ApiException apiException){
            readingActionResp = new ResponseEntity<>(new Message(apiException.getMessage()),apiException.getHttpStatus());
        }
        catch (Exception e){
            e.printStackTrace();
            readingActionResp = new ResponseEntity<>(new Message(e.getMessage().substring(0,e.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
        }
        return readingActionResp;
    }

    //For Ht report meter consumption report month wise,
    // This controller will fetch ht accept meters of a month
    //url is meter_reading/currentStates/ht_accept,
    @GetMapping("/currentStates/{currentStates}")
    public ResponseEntity<?> getMeterListByCurrentStateIn(@PathVariable("currentStates") List<String> currentStateList){
        ResponseEntity meterListResp = null;
        try {
                List<Map<String,String>> meterList = meterReadingService.getMeterListByCurrentStateIn(currentStateList);
                meterListResp = new ResponseEntity<>(meterList,HttpStatus.OK);
        }catch (ApiException apiException){
            meterListResp = new ResponseEntity<>(new Message(apiException.getMessage()),apiException.getHttpStatus());
        }
        catch (Exception e){
            e.printStackTrace();
            meterListResp = new ResponseEntity<>(new Message(e.getMessage().substring(0,e.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
        }
        return meterListResp;
    }

    //Passing Meter Number, Month in MMM-yyyy format and Current states of current reading to get consumption details
    @GetMapping("/meterConsumption/meterNo/{meterNo}/monthYear/{monthYear}")
    public ResponseEntity<?> getMeterConsumptionByMonth(@PathVariable("meterNo") String meterNo,
                                                        @PathVariable("monthYear") String monthYear){
        ResponseEntity meterConsumptionResp = null;
        try {
            MeterConsumptionDto meterConsumptionDto = meterReadingService.getMeterConsumptionByMonth(meterNo,monthYear);
            meterConsumptionResp = new ResponseEntity<>(meterConsumptionDto,HttpStatus.OK);
        }catch (ApiException apiException){
            meterConsumptionResp = new ResponseEntity<>(new Message(apiException.getMessage()),apiException.getHttpStatus());
        }
        catch (Exception e){
            e.printStackTrace();
            meterConsumptionResp = new ResponseEntity<>(new Message(e.getMessage().substring(0,e.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
        }
        return meterConsumptionResp;
    }

    //this is used by DEVELOPER to view HT_ACCEPT or HT approved meter reading of a month
    @GetMapping("/ht_accepted/monthYear/{monthYear}")
    public ResponseEntity<?> getHtAcceptedReadings(@PathVariable("monthYear") String monthYear){
        ResponseEntity readingDtosResp = null;
        try {
            List<MeterReadingBean> readings = meterReadingService.getHtAcceptedReadings(monthYear);
            readingDtosResp = new ResponseEntity<>(readings,HttpStatus.OK);
        }catch (ApiException apiException){
            readingDtosResp = new ResponseEntity<>(new Message(apiException.getMessage()),apiException.getHttpStatus());
        }
        catch (Exception e){
            e.printStackTrace();
            readingDtosResp = new ResponseEntity<>(new Message(e.getMessage().substring(0,e.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
        }
        return readingDtosResp;
    }

    //this is used by Developer to accept HT accepted meter reading
    @PostMapping("/ht_accepted/developer-accept")
    public ResponseEntity<?> developerUserAccept(@RequestBody List<MeterReadingBean> meterReadingBeanList){
        ResponseEntity readingActionResp = null;
        try {
            meterReadingService.developerUserAccept(meterReadingBeanList);
            readingActionResp = new ResponseEntity<>(new Message("Readings approved successfully.."),HttpStatus.OK);

        }catch (ApiException apiException){
            readingActionResp = new ResponseEntity<>(new Message(apiException.getMessage()),apiException.getHttpStatus());
        }
        catch (Exception e){
            e.printStackTrace();
            readingActionResp = new ResponseEntity<>(new Message(e.getMessage().substring(0,e.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
        }
        return readingActionResp;
    }

    //this is used by Developer to reject HT accepted meter reading
    @PostMapping("/ht_accepted/developer-reject")
    public ResponseEntity<?> developerUserReject(@RequestBody List<MeterReadingBean> meterReadingBeanList){
        ResponseEntity readingActionResp = null;
        try {
            meterReadingService.developerUserReject(meterReadingBeanList);
            readingActionResp = new ResponseEntity<>(new Message("Readings rejected successfully.."),HttpStatus.OK);

        }catch (ApiException apiException){
            readingActionResp = new ResponseEntity<>(new Message(apiException.getMessage()),apiException.getHttpStatus());
        }
        catch (Exception e){
            e.printStackTrace();
            readingActionResp = new ResponseEntity<>(new Message(e.getMessage().substring(0,e.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
        }
        return readingActionResp;
    }

}
