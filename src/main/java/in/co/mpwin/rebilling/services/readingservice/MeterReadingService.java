package in.co.mpwin.rebilling.services.readingservice;

import in.co.mpwin.rebilling.beans.metermaster.MeterMasterBean;
import in.co.mpwin.rebilling.beans.readingbean.MeterReadingBean;
import in.co.mpwin.rebilling.dto.MeterConsumptionDto;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.AuditControlServices;
import in.co.mpwin.rebilling.miscellanious.DateMethods;
import in.co.mpwin.rebilling.miscellanious.TokenInfo;
import in.co.mpwin.rebilling.repositories.metermaster.MeterMasterRepo;
import in.co.mpwin.rebilling.repositories.readingrepo.MeterReadingRepo;
import in.co.mpwin.rebilling.services.metermaster.MeterMasterService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MeterReadingService {
    @Autowired
    private MeterReadingRepo meterReadingRepo;
    @Autowired
    private MeterMasterService meterMasterService;
    @Autowired private DateMethods dateMethods;

    @Autowired private ModelMapper modelMapper;
    @Autowired
    MeterMasterRepo meterMasterRepo;

    public List<MeterReadingBean> getAllReadingByStatus(String status) {
        List<MeterReadingBean> meterReadingBeanList = new ArrayList<>();
        try {
            meterReadingBeanList = meterReadingRepo.findAllByStatus(status);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return meterReadingBeanList;
    }

    public List<MeterReadingBean> getAllReadingByMonthAndStatus(String month, String status) {
        List<MeterReadingBean> meterReadingBeanList = new ArrayList<>();
        try {
            meterReadingBeanList = meterReadingRepo.findAllByReadingDateAndStatus(month, status);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return meterReadingBeanList;
    }

    public List<MeterReadingBean> getAllReadingByMonthAndMeterNoAndStatus(String month, String meterNo, String status) {
        List<MeterReadingBean> meterReadingBeanList = new ArrayList<>();
        try {
            meterReadingBeanList = meterReadingRepo.findAllByReadingDateMonthAndMeterNoAndStatus(month, meterNo, status);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return meterReadingBeanList;
    }

    @Transactional
    public MeterReadingBean createMeterReading(MeterReadingBean passMRB) {
        MeterReadingBean meterReadingBean = new MeterReadingBean();
        try {
            //check for meter exist, active and mapped with respect to meter master
            Long validationCount = meterMasterRepo.countByMeterNumberAndStatusAndIsMapped(passMRB.getMeterNo(),
                    "active", "yes");
            //check next reading date wrt to input reading date and compare value
            MeterReadingBean nextReading = meterReadingRepo.findJustNext(passMRB.getMeterNo(), passMRB.getReadingDate());

            //check previous reading date wrt to input reading date and compare value
            MeterReadingBean previousReading = meterReadingRepo.findJustBefore(passMRB.getMeterNo(), passMRB.getReadingDate());
            //reading insertion validation
            if (validationCount <= 0) throw new ApiException(HttpStatus.BAD_REQUEST, "Meter is not actively mapped");
            if (nextReading != null && nextReading.getEActiveEnergy().compareTo(passMRB.getEActiveEnergy()) < 0)
                throw new ApiException(HttpStatus.BAD_REQUEST, "you are entering reading which is greater than Next reading in Database");
            if (previousReading != null && previousReading.getEActiveEnergy().compareTo(passMRB.getEActiveEnergy()) > 0)
                throw new ApiException(HttpStatus.BAD_REQUEST, "you are entering reading which is less than previous reading in Database");
            //insertion in DB
            if (validationCount > 0 &&
                    (nextReading == null || nextReading.getEActiveEnergy().compareTo(passMRB.getEActiveEnergy()) >= 0) &&
                    (previousReading == null || previousReading.getEActiveEnergy().compareTo(passMRB.getEActiveEnergy()) <= 0)
            ) {
                //Set the Audit control parameters, Globally
                new AuditControlServices().setInitialAuditControlParameters(passMRB);

                meterReadingBean = meterReadingRepo.save(passMRB);
            }
        } catch (ApiException apiException) {
            throw apiException;
        } catch (DataIntegrityViolationException d) {
            throw d;
        } catch (Exception e) {
            throw e;
            // Handle the exception or log the error as needed
        }
        return meterReadingBean;
    }

    public List<MeterReadingBean> getAllReadingByCurrentStateAndStatus(String currentState, String status) {
        List<MeterReadingBean> meterReadingBeanList = new ArrayList<>();
        try {
            meterReadingBeanList = meterReadingRepo.findAllByCurrentStateAndStatus(currentState, status);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return meterReadingBeanList;
    }

    public List<MeterReadingBean> getAllReadingByCurrentStateAndMeterNoAndStatus(String currentState, String meterNo, String status) {
        List<MeterReadingBean> meterReadingBeanList = new ArrayList<>();
        try {
            meterReadingBeanList = meterReadingRepo.findAllByCurrentStateAndMeterNoAndStatus(currentState, meterNo, status);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return meterReadingBeanList;
    }

    public List<MeterReadingBean> updateCurrentState(String currentState,String updateState, String month, String meterNo, String status) {
        List<MeterReadingBean> meterReadingBeanList = new ArrayList<>();
        try {
                String username = new TokenInfo().getCurrentUsername();
                Timestamp updateTime = new DateMethods().getServerTime();
                meterReadingBeanList = meterReadingRepo.findAllByEndDateMonthAndMeterNoAndStatus(month, meterNo, status);
                List<MeterReadingBean> beansWithStateInitialRead = meterReadingBeanList.stream().filter(read -> read.getCurrentState().equals("initial_read")).collect(Collectors.toList());
                beansWithStateInitialRead.forEach(read -> { read.setCurrentState(updateState);
                                                        read.setUpdatedBy(username);
                                                        read.setUpdatedOn(updateTime);
                                                     });
                beansWithStateInitialRead = (List<MeterReadingBean>) meterReadingRepo.saveAll(beansWithStateInitialRead);
                return beansWithStateInitialRead;
        } catch (Exception exception) {
           throw  exception;
        }
    }

    public MeterReadingBean updateEndDate(Date endDate, String month, String meterNo, String status) {
        MeterReadingBean meterReadingBean = new MeterReadingBean();
        try {
            if (!meterReadingRepo.findAllByReadingDateMonthAndMeterNoAndStatus(month, meterNo, status).isEmpty())
                meterReadingBean = meterReadingRepo.updateEndDate(endDate, month, meterNo, status);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return meterReadingBean;
    }

    public MeterReadingBean getReadingByMeterNoAndReadingDateAndStatus(String meterNo, Date readingDate, String status) {
        MeterReadingBean meterReadingBean = new MeterReadingBean();
        try {
            meterReadingBean = meterReadingRepo.findByMeterNoAndReadingDateAndStatus(meterNo, readingDate, status);
        } catch (NullPointerException exception) {
            return null;
        } catch (Exception exception) {
            return null;
        }
        return meterReadingBean;
    }

    public MeterReadingBean GetLastReadingByMeterNoAndStatus(String oldMeterNumber, String str) {
        return meterReadingRepo.findLastReadByMeterNoAndStatus(oldMeterNumber, str);
    }


    public List<MeterReadingBean> getAmrAcceptedReadings(String monthYear) {
        try {
               return meterReadingRepo.findAcceptOrForceAcceptReadingsByAmr(monthYear)
                    .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST,"Reading is not present for month" + monthYear));
        }catch (ApiException apiException){
            throw apiException;
        }catch (Exception exception){
            throw exception;
        }
    }

    public List<MeterReadingBean> getHtAcceptedReadings(String monthYear) {
        try {   //Get Meter List Belongs to developer only then find reading status HT_ACCEPT of those meters only
                List<Map<String,String>> meterListOfDeveloper = meterMasterService.getMetersByUser();
                List<String> meterList = meterListOfDeveloper.stream().map(m -> m.get("meterNo")).collect(Collectors.toList());
                return meterReadingRepo.findHtAcceptedReadings(meterList,monthYear)
                        .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST,"Reading is not present for month" + monthYear));
        }catch (ApiException apiException){
            throw apiException;
        }catch (Exception exception){
            throw exception;
        }
    }

    @Transactional
    public void htUserAccept(List<MeterReadingBean> meterReadingBeanList) {
        try {
                if(meterReadingBeanList.size()==0) throw new ApiException(HttpStatus.BAD_REQUEST,"Select atleast one..");

                String username = new TokenInfo().getCurrentUsername();
                Timestamp updateTime = new DateMethods().getServerTime();
                // here we dont need to check existing current state because view reading have only amr_accept or force_accept
                meterReadingBeanList.forEach(read -> {  read.setCurrentState("ht_accept");
                                                        read.setUpdatedBy(username);
                                                        read.setUpdatedOn(updateTime);
                                                    });
                meterReadingRepo.saveAll(meterReadingBeanList);
        }catch (ApiException apiException){
            throw apiException;
        }catch (Exception exception){
            throw exception;
        }
    }

    @Transactional
    public void developerUserAccept(List<MeterReadingBean> meterReadingBeanList) {
        try {
            if(meterReadingBeanList.size()==0) throw new ApiException(HttpStatus.BAD_REQUEST,"Select atleast one..");

            String username = new TokenInfo().getCurrentUsername();
            Timestamp updateTime = new DateMethods().getServerTime();
            // here we dont need to check existing current state because view reading have only ht_accept
            meterReadingBeanList.forEach(read -> {  read.setCurrentState("dev_accept");
                read.setUpdatedBy(username);
                read.setUpdatedOn(updateTime);
            });
            meterReadingRepo.saveAll(meterReadingBeanList);
        }catch (ApiException apiException){
            throw apiException;
        }catch (Exception exception){
            throw exception;
        }
    }

    @Transactional
    public void developerUserReject(List<MeterReadingBean> meterReadingBeanList) {
        try {
            if(meterReadingBeanList.size()==0) throw new ApiException(HttpStatus.BAD_REQUEST,"Select atleast one..");

            String username = new TokenInfo().getCurrentUsername();
            Timestamp updateTime = new DateMethods().getServerTime();
            // here we dont need to check existing current state because view reading have only ht_accept
            meterReadingBeanList.forEach(read -> {  read.setCurrentState("dev_reject");
                read.setUpdatedBy(username);
                read.setUpdatedOn(updateTime);
            });
            meterReadingRepo.saveAll(meterReadingBeanList);
        }catch (ApiException apiException){
            throw apiException;
        }catch (Exception exception){
            throw exception;
        }
    }

    public List<Map<String,String>> getMeterListByCurrentStateIn(List<String> currentStateList) {
        try {
                List<Map<String,String>> meterList = new ArrayList<>();
                List<String> meters = meterReadingRepo.findAllDistinctMeterNoByCurrentStateInAndStatus(currentStateList,"active");
                if(meters.size()==0) throw new ApiException(HttpStatus.BAD_REQUEST,"Not any meter left.");
                for (String meter : meters){
                        Map<String,String> m = new HashMap<>();
                        String meterCategory = meterMasterRepo.findByMeterNumberAndStatus(meter,"active").getCategory();
                        m.put("meterNo",meter);
                        m.put("meterCategory",meterCategory);
                        meterList.add(m);
                }

                return meterList;
        }catch (ApiException apiException){
            throw apiException;
        }catch (Exception exception){
            throw exception;
        }
    }

    @Transactional
    public MeterConsumptionDto getMeterConsumptionByMonth(String meterNo, String monthYear) throws ParseException {
        MeterConsumptionDto meterConsumptionDto = new MeterConsumptionDto();
        try {
                List<String> currentStates = List.of("ht_accept");
                Date startReadDate = dateMethods.getCurrentAndPreviousDate(monthYear).get(0);
                Date endReadDate = dateMethods.getCurrentAndPreviousDate(monthYear).get(1);
                List<MeterReadingBean> meterReadingBeanList = meterReadingRepo.findByMeterNoAndCurrentStatesInBetween
                        (meterNo,currentStates,startReadDate,endReadDate,"active");
                if (meterReadingBeanList.size()==0) throw new ApiException(HttpStatus.BAD_REQUEST,"Not any Reading Present in month");
                if (meterReadingBeanList.size()>2) throw new ApiException(HttpStatus.BAD_REQUEST,"More than 2 reading in given month");
                if (meterReadingBeanList.size()<2) throw new ApiException(HttpStatus.BAD_REQUEST,"Less than 2 reading in given month");

                if (meterReadingBeanList.size()==2){
                    MeterMasterBean meterMasterBean = meterMasterRepo.findByMeterNumberAndStatus(meterNo,"active");
                    if(meterMasterBean == null) throw new ApiException(HttpStatus.BAD_REQUEST,"meter must be active");

                    meterConsumptionDto = meterConsumptionDto.setMeterConsumptionDto(meterReadingBeanList.get(0),meterReadingBeanList.get(1),meterMasterBean.getMf());
                    meterConsumptionDto.setCategory(meterMasterBean.getCategory());

            }
        }catch (ApiException apiException){
            throw apiException;
        }catch (Exception exception){
            throw exception;
        }
        return meterConsumptionDto;
    }
}


