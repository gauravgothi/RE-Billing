package in.co.mpwin.rebilling.controller.readingcontroller;

import in.co.mpwin.rebilling.beans.readingbean.MeterReadingBean;
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

    @RequestMapping(method = RequestMethod.PUT,value = {"/currentState/{currentState}/{month}/{meterNo}/{status}",
                                                        "/endDate/{endDate}/{month}/{meterNo}/{status}"})
    public ResponseEntity<?> updateCurrentStateOrEndDate(@PathVariable(name = "currentState") Optional<String> currentState,
                                                @PathVariable(name = "endDate") Optional<String> endDate,
                                                @PathVariable(name = "month") String month,
                                                @PathVariable(name = "meterNo") String meterNo,
                                                @PathVariable(value = "status") String status){

        ResponseEntity updateResp = null;
        try {
            if(currentState.isPresent() && !endDate.isPresent()){

                MeterReadingBean bean = meterReadingService.updateCurrentState(String.valueOf(currentState),month,meterNo,status);
                updateResp = new ResponseEntity<>(bean,HttpStatus.OK);

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
//            else if (bean==null)
//                createReadResp = new ResponseEntity<>(new Message(" Reading is already present for given month and meter OR" +
//                        "Meter number is not in active and mapped"),HttpStatus.BAD_REQUEST);
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
}
