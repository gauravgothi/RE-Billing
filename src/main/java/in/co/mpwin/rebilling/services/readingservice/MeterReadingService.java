package in.co.mpwin.rebilling.services.readingservice;

import in.co.mpwin.rebilling.beans.metermaster.MeterMasterBean;
import in.co.mpwin.rebilling.beans.readingbean.MeterReadingBean;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.AuditControlServices;
import in.co.mpwin.rebilling.repositories.metermaster.MeterMasterRepo;
import in.co.mpwin.rebilling.repositories.readingrepo.MeterReadingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MeterReadingService {
    @Autowired
    MeterReadingRepo meterReadingRepo;

    @Autowired
    MeterMasterRepo meterMasterRepo;

    /*@Autowired
    MeterReadingBean meterReadingBean;*/


    public List<MeterReadingBean> getAllReadingByStatus(String status) {
        List<MeterReadingBean> meterReadingBeanList = new ArrayList<>();
        try {
            meterReadingBeanList = meterReadingRepo.findAllByStatus(status);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return meterReadingBeanList;
    }

    public List<MeterReadingBean> getAllReadingByMonthAndStatus(String month, String status){
        List<MeterReadingBean> meterReadingBeanList = new ArrayList<>();
        try {
            meterReadingBeanList = meterReadingRepo.findAllByReadingDateAndStatus(month,status);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return meterReadingBeanList;
    }

    public List<MeterReadingBean> getAllReadingByMonthAndMeterNoAndStatus(String month,String meterNo, String status) {
        List<MeterReadingBean> meterReadingBeanList = new ArrayList<>();
        try {
            meterReadingBeanList = meterReadingRepo.findAllByReadingDateAndMeterNoAndStatus(month,meterNo,status);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return meterReadingBeanList;
    }

    /*public MeterReading createMeterReading(MeterReading meterReading){
        MeterReading meterReading1 = new MeterReading();
        try {
            //Set the Audit control parameters, Globally
            new AuditControlServices().setInitialAuditControlParameters(meterReading);
            meterReading1 = meterReadingRepo.save(meterReading);
        }catch (Exception exception){
            exception.printStackTrace();
            return null;
        }
        return meterReading1;
    }*/

    @Transactional
    public MeterReadingBean createMeterReading(MeterReadingBean passMRB) {
        MeterReadingBean meterReadingBean=new MeterReadingBean();
        try {
            //check for meter exist, active and mapped with respect to meter master
            Long validationCount = meterMasterRepo.countByMeterNumberAndStatusAndIsMapped(passMRB.getMeterNo(),
                                                                                            "active","yes");
            if (validationCount>0){

                //Set the Audit control parameters, Globally
                new AuditControlServices().setInitialAuditControlParameters(meterReadingBean);

                meterReadingBean = meterReadingRepo.save(passMRB);
            }
        }

        catch (DataIntegrityViolationException d)
        {
            throw d;
        }
        catch (Exception e) {
            throw e;

            // Handle the exception or log the error as needed
        }
        return meterReadingBean;
    }


    public List<MeterReadingBean> getAllReadingByCurrentStateAndStatus(String currentState, String status) {
        List<MeterReadingBean> meterReadingBeanList = new ArrayList<>();
        try {
            meterReadingBeanList = meterReadingRepo.findAllByCurrentStateAndStatus(currentState,status);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return meterReadingBeanList;
    }

    public List<MeterReadingBean> getAllReadingByCurrentStateAndMeterNoAndStatus(String currentState,String meterNo, String status) {
        List<MeterReadingBean> meterReadingBeanList = new ArrayList<>();
        try {
           meterReadingBeanList = meterReadingRepo.findAllByCurrentStateAndMeterNoAndStatus(currentState,meterNo,status);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return meterReadingBeanList;
    }

    public MeterReadingBean updateCurrentState(String currentState,String month,String meterNo,String status){
        MeterReadingBean meterReadingBean = new MeterReadingBean();
        try {
            if(!meterReadingRepo.findAllByReadingDateAndMeterNoAndStatus(month,meterNo,status).isEmpty())
                meterReadingBean = meterReadingRepo.updateCurrentState(currentState,month,meterNo,status);
        }catch (Exception exception){
            exception.printStackTrace();
        }return meterReadingBean;
    }

    public MeterReadingBean updateEndDate(Date endDate,String month,String meterNo,String status){
        MeterReadingBean meterReadingBean = new MeterReadingBean();
        try {
            if(!meterReadingRepo.findAllByReadingDateAndMeterNoAndStatus(month,meterNo,status).isEmpty())
              meterReadingBean = meterReadingRepo.updateEndDate(endDate,month,meterNo,status);
        }catch (Exception exception){
            exception.printStackTrace();
        }return meterReadingBean;
    }

//    public MeterReadingBean createReadingByPunching(MeterReadingBean meterReadingBean){
//        try {
//                // Before inserting check for unique constraint
//                MeterReadingBean bean = meterReadingRepo.findByMeterNoAndReadingDateAndReadingTypeAndStatus(meterReadingBean.getMeterNo(),
//                                                                                meterReadingBean.getReadingDate(),
//                                                                                meterReadingBean.getReadingType(),
//                                                                                meterReadingBean.getStatus());
//
//                //if reading is not already exist then only insert or punch reading
//                if (bean == null)   {
//                    //check for meter exist, active and mapped with respect to meter master
//                    MeterMasterBean meter = meterMasterRepo.findByMeterNumberAndStatus(meterReadingBean.getMeterNo(),"active");
//                    if(!meter.equals(null) && meter.getIsMapped().equals("yes") && meter.getMf().equals(meterReadingBean.getMf()))
//                           {
//                            //Set the Audit control parameters, Globally
//                            new AuditControlServices().setInitialAuditControlParameters(meterReadingBean);
//
//                            return meterReadingRepo.save(meterReadingBean);
//
//                        } else
//                            throw new ApiException(HttpStatus.BAD_REQUEST,"Meter Number must be present, active and mapped state in meter master.");
//
//                }else
//                    throw new ApiException(HttpStatus.BAD_REQUEST,"Meter reading is already exist");
//
//        }catch (ApiException apiException){
//            throw apiException;
//        }catch (DataIntegrityViolationException d){
//            throw d;
//        }catch (Exception e){
//            throw e;
//        }
//    }
}
