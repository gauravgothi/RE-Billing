package in.co.mpwin.rebilling.controller.readingcontroller;

import in.co.mpwin.rebilling.beans.readingbean.MeterReadingBean;
import in.co.mpwin.rebilling.dto.BifurcateConsumptionDto;
import in.co.mpwin.rebilling.dto.MeterConsumptionDto;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.DateMethods;
import in.co.mpwin.rebilling.miscellanious.Message;
import in.co.mpwin.rebilling.services.readingservice.MeterReadingPunchingService;
import in.co.mpwin.rebilling.services.readingservice.MeterReadingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/meter_reading")
@CrossOrigin(origins="*")
public class MeterReadingController {
    private static final Logger logger = LoggerFactory.getLogger(MeterReadingController.class);
    @Autowired
    MeterReadingService meterReadingService;
    @Autowired
    MeterReadingPunchingService meterReadingPunchingService;

    @RequestMapping(method= RequestMethod.GET,value="/status/{status}")
    public ResponseEntity<?> getAllReadingByStatus(@PathVariable(value = "status") String status){
        final String methodName = "getAllReadingByStatus() : ";
        logger.info(methodName + "called. with parameters status: {}",status);
        ResponseEntity meterReadingResp = null;
        try {
            List<MeterReadingBean> meterReadingBeanList = meterReadingService.getAllReadingByStatus(status);

            if (!meterReadingBeanList.isEmpty()){
                meterReadingResp = new ResponseEntity<>(meterReadingBeanList, HttpStatus.OK);
                logger.info(methodName + "return. meterReadingBeanList of size : {}",meterReadingBeanList.size());
            }
            else if (meterReadingBeanList.isEmpty()) {
                meterReadingResp = new ResponseEntity<>(new Message("Meter Reading list is not available."), HttpStatus.BAD_REQUEST);
                logger.info(methodName + "return. meterReadingBeanList of size zero.");
            }
        }catch (Exception e){
            meterReadingResp = new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);
        }
        return meterReadingResp;
    }

    @RequestMapping(method= RequestMethod.GET,value="/month/{month}/status/{status}")
    public ResponseEntity<?> getAllReadingByMonthAndStatus(@PathVariable(name = "month") String month, @PathVariable(value = "status") String status){
        final String methodName = "getAllReadingByMonthAndStatus() : ";
        logger.info(methodName + "called. with parameters month : {} and status: {}",month,status);
        ResponseEntity meterReadingResp = null;
        try {
            List<MeterReadingBean> meterReadingBeanList = meterReadingService.getAllReadingByMonthAndStatus(month,status);

            if (!meterReadingBeanList.isEmpty()) {
                meterReadingResp = new ResponseEntity<>(meterReadingBeanList, HttpStatus.OK);
                logger.info(methodName + "return. meterReadingBeanList of size : {}",meterReadingBeanList.size());
            }
            else if (meterReadingBeanList.isEmpty()) {
                meterReadingResp = new ResponseEntity<>(new Message("Meter Reading list is not available."), HttpStatus.BAD_REQUEST);
                logger.info(methodName + "return. meterReadingBeanList of size zero.");
            }
        }catch (Exception e){
            meterReadingResp = new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);
        }
        return meterReadingResp;
    }

    @RequestMapping(method= RequestMethod.GET,value="/month/{month}/meterNo/{meterNo}/status/{status}")
    public ResponseEntity<?> getAllReadingByMonthAndMeterNoAndStatus(@PathVariable(name = "month") String month,
                                                                     @PathVariable(name = "meterNo") String meterNo,
                                                                     @PathVariable(value = "status") String status){

        final String methodName = "getAllReadingByMonthAndMeterNoAndStatus() : ";
        logger.info(methodName + "called. with parameters month : {}, meterNo :{} and status: {}",month,meterNo,status);
        ResponseEntity meterReadingResp = null;
        try {
            List<MeterReadingBean> meterReadingBeanList = meterReadingService.getAllReadingByMonthAndMeterNoAndStatus(month,meterNo,status);

            if (!meterReadingBeanList.isEmpty()) {
                meterReadingResp = new ResponseEntity<>(meterReadingBeanList, HttpStatus.OK);
                logger.info(methodName + "return. meterReadingBeanList of size : {}",meterReadingBeanList.size());
            }
            else if (meterReadingBeanList.isEmpty()) {
                meterReadingResp = new ResponseEntity<>(new Message("Meter Reading list is not available."), HttpStatus.BAD_REQUEST);
                logger.info(methodName + "return. meterReadingBeanList of size zero.");
            }
        }catch (Exception e){
            meterReadingResp = new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);
        }
        return meterReadingResp;
    }

    @RequestMapping(method= RequestMethod.GET,value="/currentState/{currentState}/status/{status}")
    public ResponseEntity<?> getAllReadingByCurrentStateAndStatus(@PathVariable(name = "currentState") String currentState, @PathVariable(value = "status") String status){
        final String methodName = "getAllReadingByCurrentStateAndStatus() : ";
        logger.info(methodName + "called. with parameters currentState : {} and status: {}",currentState,status);
        ResponseEntity meterReadingResp = null;
        try {
            List<MeterReadingBean> meterReadingBeanList = meterReadingService.getAllReadingByCurrentStateAndStatus(currentState,status);

            if (!meterReadingBeanList.isEmpty()) {
                meterReadingResp = new ResponseEntity<>(meterReadingBeanList, HttpStatus.OK);
                logger.info(methodName + "return. meterReadingBeanList of size : {}",meterReadingBeanList.size());
            }
            else if (meterReadingBeanList.isEmpty()) {
                meterReadingResp = new ResponseEntity<>(new Message("Meter Reading list is not available."), HttpStatus.BAD_REQUEST);
                logger.info(methodName + "return. meterReadingBeanList of size zero.");
            }
        }catch (Exception e){
            meterReadingResp = new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);
        }
        return meterReadingResp;
    }

    @RequestMapping(method= RequestMethod.GET,value="/currentState/{currentState}/meterNo/{meterNo}/status/{status}")
    public ResponseEntity<?> getAllReadingByCurrentStateAndMeterNoAndStatus(@PathVariable(name = "currentState") String currentState,
                                                                  @PathVariable(name = "meterNo") String meterNo,
                                                                  @PathVariable(value = "status") String status){
        final String methodName = "getAllReadingByCurrentStateAndMeterNoAndStatus() : ";
        logger.info(methodName + "called. with parameters currentState : {},meterNo :{} and status: {}",currentState,meterNo,status);
        ResponseEntity meterReadingResp = null;
        try {
            List<MeterReadingBean> meterReadingBeanList = meterReadingService.getAllReadingByCurrentStateAndMeterNoAndStatus(currentState,meterNo,status);

            if (!meterReadingBeanList.isEmpty()) {
                meterReadingResp = new ResponseEntity<>(meterReadingBeanList, HttpStatus.OK);
                logger.info(methodName + "return. meterReadingBeanList of size : {}",meterReadingBeanList.size());
            }
            else if (meterReadingBeanList.isEmpty()) {
                meterReadingResp = new ResponseEntity<>(new Message("Meter Reading list is not available."), HttpStatus.BAD_REQUEST);
                logger.info(methodName + "return. meterReadingBeanList of size zero.");
            }
        }catch (Exception e){
            meterReadingResp = new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);
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

        final String methodName = "updateCurrentStateOrEndDate() : ";
        logger.info(methodName + "called. with parameters currentState : {} OR updateState : {} OR endDate : {},month : {}, meterNo :{} and status: {}",currentState,updateState,endDate,month,meterNo,status);
        ResponseEntity updateResp = null;
        try {
            if(currentState.isPresent() && updateState.isPresent() && !endDate.isPresent()){

                List<MeterReadingBean> beans = meterReadingService.updateCurrentState(String.valueOf(currentState),
                                                                                    String.valueOf(updateState),month,meterNo,status);
                updateResp = new ResponseEntity<>(beans,HttpStatus.OK);
                logger.info(methodName + "return. success of update current state of MeterReadingBean list size : {}",beans.size());

            } else if (endDate.isPresent() && !currentState.isPresent()) {

                Date endDate2 = new SimpleDateFormat("dd-MMM-yyyy").parse(String.valueOf(endDate));
                MeterReadingBean bean = meterReadingService.updateEndDate(endDate2,month,meterNo,status);
                updateResp = new ResponseEntity<>(bean,HttpStatus.OK);
                logger.info(methodName + "return. success of update end date of MeterReadingBean: {}",bean.toString());
            }else {
                updateResp = new ResponseEntity<>(new Message("check inputs, Nothing is updated"), HttpStatus.BAD_REQUEST);
                logger.info(methodName + "return. response message : {}", updateResp.getBody());
            }
        }catch (Exception e){
            updateResp = new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);
        }
        return updateResp;
    }

    @RequestMapping(method = RequestMethod.POST,value = "")
    public ResponseEntity<?> createMeterReading(@RequestBody @Valid MeterReadingBean meterReadingBean){
        final String methodName = "createMeterReading() : ";
        logger.info(methodName + "called. with request body of meterReadingBean :{}",meterReadingBean.toString());
        ResponseEntity createReadResp =  null;
        try {
            //set the meter reading source to web punched if service not called through fileuploadservice
            meterReadingBean.setReadSource("web");

            MeterReadingBean bean = meterReadingService.createMeterReading(meterReadingBean);

            if (bean!=null)
                createReadResp = new ResponseEntity<>(new Message(bean.getId() + " reading saved successfully"),HttpStatus.OK);
            logger.info(methodName + "return. success response : {}",createReadResp.getBody());
        }catch (ApiException apiException){
            createReadResp = new ResponseEntity<>(new Message(apiException.getMessage()),apiException.getHttpStatus());
            logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
        }catch (DataIntegrityViolationException d) {
            createReadResp = new ResponseEntity<>(new Message(d.getMessage()),HttpStatus.BAD_REQUEST);
            logger.error(methodName+"Data Integrity Violation Exception occurred: {}", d.getMessage());
        } catch (Exception e){
            createReadResp = new ResponseEntity<>(new Message(e.getMessage()),HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);
        }
        return createReadResp;
    }

    //this is used by HT to view AMR accepted or force accepted or dev_reject meter readings
    @GetMapping("/amr_accepted/monthYear/{monthYear}")
    public ResponseEntity<?> getAmrAcceptedReadings(@PathVariable("monthYear") String monthYear){
        final String methodName = "getAmrAcceptedReadings() : ";
        logger.info(methodName + "called. with parameters monthYear :{}",monthYear);
        ResponseEntity readingDtosResp = null;
        try {
                List<MeterReadingBean> readings = meterReadingService.getAmrAcceptedReadings(monthYear);
                if (readings.size() == 0)
                    throw new ApiException(HttpStatus.BAD_REQUEST,"Not any reading left");
                readingDtosResp = new ResponseEntity<>(readings,HttpStatus.OK);
            logger.info(methodName + "return. amr accepted readings of size : {}",readings.size());
        }catch (ApiException apiException){
            readingDtosResp = new ResponseEntity<>(new Message(apiException.getMessage()),apiException.getHttpStatus());
            logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
        } catch (Exception e){
            readingDtosResp = new ResponseEntity<>(new Message(e.getMessage()),HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);
        }
        return readingDtosResp;
    }

    //this is used by HT to accept AMR accepted or force accepted meter readings
    @PostMapping("/amr_accepted/htAccept")
    public ResponseEntity<?> htUserAccept(@RequestBody List<MeterReadingBean> meterReadingBeanList){
        final String methodName = "htUserAccept() : ";
        logger.info(methodName + "called. with request body of meterReadingBeanList of size: {}",meterReadingBeanList.size());
        ResponseEntity readingActionResp = null;
        try {
                meterReadingService.htUserAccept(meterReadingBeanList);
                readingActionResp = new ResponseEntity<>(new Message("Readings approved successfully.."),HttpStatus.OK);
                logger.info(methodName + "return.readings approved by ht user : success");
        }catch (ApiException apiException){
            readingActionResp = new ResponseEntity<>(new Message(apiException.getMessage()),apiException.getHttpStatus());
            logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
        }
        catch (Exception e){
            readingActionResp = new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);
        }
        return readingActionResp;
    }

    //For Ht report meter consumption report month wise,
    // This controller will fetch ht accept meters of a month
    //url is meter_reading/currentStates/ht_accept,
    @GetMapping("/meterConsumption/meterList")
    public ResponseEntity<?> getMeterListByCurrentStateIn(){
        final String methodName = "getMeterListByCurrentStateIn() : ";
        logger.info(methodName + "called. with parameters: empty");
        ResponseEntity meterListResp = null;
        try {
                List<String> currentStateList = List.of("ht_accept","dev_accept","dev_reject");
                List<Map<String,String>> meterList = meterReadingService.getMeterListByCurrentStateIn(currentStateList);
                meterListResp = new ResponseEntity<>(meterList,HttpStatus.OK);
                logger.info(methodName + "return. List<Map<String,String>> meterList of size : {}",meterList.size());
        }catch (ApiException apiException){
            meterListResp = new ResponseEntity<>(new Message(apiException.getMessage()),apiException.getHttpStatus());
            logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
        }
        catch (Exception e){
            meterListResp = new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);
        }
        return meterListResp;
    }

    //Passing Meter Number, Month in MMM-yyyy format and Current states of current reading to get consumption details
    @GetMapping("/meterConsumption/meterNo/{meterNo}/monthYear/{monthYear}")
    public ResponseEntity<?> getMeterConsumptionByMonth(@PathVariable("meterNo") String meterNo,
                                                        @PathVariable("monthYear") String monthYear){
        final String methodName = "getMeterConsumptionByMonth() : ";
        logger.info(methodName + "called. with parameters: meterNo : {} and monthYear : {}",meterNo,monthYear);
        ResponseEntity meterConsumptionResp = null;
        try {
            MeterConsumptionDto meterConsumptionDto = meterReadingService.getMeterConsumptionByMonth(meterNo,monthYear);
            meterConsumptionResp = new ResponseEntity<>(meterConsumptionDto,HttpStatus.OK);
            logger.info(methodName + "return. meterConsumptionDto : {}",meterConsumptionDto.toString());
        }catch (ApiException apiException){
            meterConsumptionResp = new ResponseEntity<>(new Message("Arithmetic Exception" + apiException.getMessage()),apiException.getHttpStatus());
            logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
        }
        catch (Exception e){
            meterConsumptionResp = new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);
        }
        return meterConsumptionResp;
    }

    //this is used by DEVELOPER to view HT_ACCEPT or HT approved meter reading of a month
    @GetMapping("/ht_accepted/monthYear/{monthYear}")
    public ResponseEntity<?> getHtAcceptedReadings(@PathVariable("monthYear") String monthYear){
        final String methodName = "getHtAcceptedReadings() : ";
        logger.info(methodName + "called. with parameters monthYear : {}",monthYear);
        ResponseEntity readingDtosResp = null;
        try {
            List<MeterReadingBean> readings = meterReadingService.getHtAcceptedReadings(monthYear);
            readingDtosResp = new ResponseEntity<>(readings,HttpStatus.OK);
            logger.info(methodName + "return. meter readings of size : {}",readings.size());
        }catch (ApiException apiException){
            readingDtosResp = new ResponseEntity<>(new Message(apiException.getMessage()),apiException.getHttpStatus());
            logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
        }
        catch (Exception e){
            readingDtosResp = new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);
        }
        return readingDtosResp;
    }

    //this is used by Developer to accept HT accepted meter reading
    @PostMapping("/ht_accepted/developer-accept")
    public ResponseEntity<?> developerUserAccept(@RequestBody List<MeterReadingBean> meterReadingBeanList){
        final String methodName = "developerUserAccept() : ";
        logger.info(methodName + "called. with request body of meterReadingBeanList of size: {}",meterReadingBeanList.size());
        ResponseEntity readingActionResp = null;
        try {
            meterReadingService.developerUserAccept(meterReadingBeanList);
            readingActionResp = new ResponseEntity<>(new Message("Readings approved successfully.."),HttpStatus.OK);
            logger.info(methodName + "return.success of readings accepted by developer of size : {}",meterReadingBeanList.size());
        }catch (ApiException apiException){
            readingActionResp = new ResponseEntity<>(new Message(apiException.getMessage()),apiException.getHttpStatus());
            logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
        }
        catch (Exception e){
            readingActionResp = new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);
        }
        return readingActionResp;
    }

    //this is used by Developer to reject HT accepted meter reading
    @PostMapping("/ht_accepted/developer-reject")
    public ResponseEntity<?> developerUserReject(@RequestBody List<MeterReadingBean> meterReadingBeanList){
        final String methodName = "developerUserReject() : ";
        logger.info(methodName + "called. with request body of meterReadingBeanList of size: {}",meterReadingBeanList.size());
        ResponseEntity readingActionResp = null;
        try {
            meterReadingService.developerUserReject(meterReadingBeanList);
            readingActionResp = new ResponseEntity<>(new Message("Readings rejected successfully.."),HttpStatus.OK);
            logger.info(methodName + "return.success of readings rejected by developer of size : {}",meterReadingBeanList.size());
        }catch (ApiException apiException){
            readingActionResp = new ResponseEntity<>(new Message(apiException.getMessage()),apiException.getHttpStatus());
            logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
        }
        catch (Exception e){
            readingActionResp = new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);
        }
        return readingActionResp;
    }

    @GetMapping("/meterNo/{meterNo}/history")
    public ResponseEntity<?> getAllReadingByMeterNo(@PathVariable("meterNo") String meterNo)
    {
        final String methodName = "getAllReadingByMeterNo() : ";
        logger.info(methodName + "called. with parameters meterNo: {}",meterNo);
        ResponseEntity readingHistoryResp = null;
        try {
            List<MeterReadingBean> meterReadingHistoryList = meterReadingService.getAllReadingByMeterNo(meterNo);
            readingHistoryResp = new ResponseEntity<>(meterReadingHistoryList,HttpStatus.OK);
            logger.info(methodName + "return.readings by meterNo : {} of size : {}",meterNo,meterReadingHistoryList.size());
        }catch (ApiException apiException){
            readingHistoryResp = new ResponseEntity<>(new Message(apiException.getMessage()),apiException.getHttpStatus());
            logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
        }catch (DataIntegrityViolationException d){
            readingHistoryResp = new ResponseEntity<>(new Message("Database Error"), HttpStatus.BAD_REQUEST);
            logger.error(methodName+"Data Integrity Violation Exception occurred: {}", d.getMessage());
        } catch (Exception e){
            readingHistoryResp = new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);
        }
        return readingHistoryResp;
    }
    //api for SR meter reading for new plant installation.
    @RequestMapping(method = RequestMethod.POST, value ="/SR")
    public ResponseEntity<?> saveSRMeterReading(@RequestBody MeterReadingBean meterReadingBean) {
        final String methodName = "saveSRMeterReading() : ";
        logger.info(methodName + "called. with request body of meterReadingBean: {}",meterReadingBean.toString());
        ResponseEntity res = null;
        try {
            MeterReadingBean mrb = meterReadingPunchingService.saveSRMeterReading(meterReadingBean);
            if(mrb!=null) {
                res = new ResponseEntity<>(new Message("SR Reading saved successfully."), HttpStatus.OK);
                logger.info(methodName + "return.success response message : {}",res.getBody());
            }
            if(mrb==null)
                res = new ResponseEntity<>(new Message("SR Reading not saved due to some error."),HttpStatus.BAD_REQUEST);
                logger.info(methodName + "return.fail response message : {}",res.getBody());
            }catch (ApiException apiException){
            res = new ResponseEntity<>(new Message(apiException.getMessage()),apiException.getHttpStatus());
            logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
            }catch (DataIntegrityViolationException d){
            res = new ResponseEntity<>(new Message(d.getMessage()),HttpStatus.BAD_REQUEST);
            logger.error(methodName+"Data Integrity Violation Exception occurred: {}", d.getMessage());
            }catch(NullPointerException ex){
            res = new ResponseEntity<>(new Message(ex.getMessage()),HttpStatus.BAD_REQUEST);
            logger.error(methodName+" NullPointerException occurred: {}", ex.getMessage());
            }catch (Exception ex){
            res = new ResponseEntity<>(new Message("some exception occurred: "+ex.getMessage()),HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}", ex.getMessage(),ex);
            }
        return res;
    }

}
