package in.co.mpwin.rebilling.services.readingservice;

import in.co.mpwin.rebilling.beans.readingbean.MeterReadingBean;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.AuditControlServices;
import in.co.mpwin.rebilling.repositories.metermaster.MeterMasterRepo;
import in.co.mpwin.rebilling.repositories.readingrepo.MeterReadingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class MeterReadingService {
    @Autowired
    MeterReadingRepo meterReadingRepo;

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
            meterReadingBeanList = meterReadingRepo.findAllByReadingDateAndMeterNoAndStatus(month, meterNo, status);
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
                new AuditControlServices().setInitialAuditControlParameters(meterReadingBean);

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

    public MeterReadingBean updateCurrentState(String currentState, String month, String meterNo, String status) {
        MeterReadingBean meterReadingBean = new MeterReadingBean();
        try {
            if (!meterReadingRepo.findAllByReadingDateAndMeterNoAndStatus(month, meterNo, status).isEmpty())
                meterReadingBean = meterReadingRepo.updateCurrentState(currentState, month, meterNo, status);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return meterReadingBean;
    }

    public MeterReadingBean updateEndDate(Date endDate, String month, String meterNo, String status) {
        MeterReadingBean meterReadingBean = new MeterReadingBean();
        try {
            if (!meterReadingRepo.findAllByReadingDateAndMeterNoAndStatus(month, meterNo, status).isEmpty())
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


}
