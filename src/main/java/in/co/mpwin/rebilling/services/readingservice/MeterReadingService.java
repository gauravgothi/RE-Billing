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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MeterReadingService {
    private static final Logger logger = LoggerFactory.getLogger(MeterReadingService.class);
    @Autowired
    private MeterReadingRepo meterReadingRepo;
    @Autowired @Lazy
    private  FivePercentService fivePercentService;
    @Autowired
    private MeterMasterService meterMasterService;
    @Autowired private DateMethods dateMethods;

    @Autowired private ModelMapper modelMapper;
    @Autowired
    MeterMasterRepo meterMasterRepo;

    public List<MeterReadingBean> getAllReadingByStatus(String status) {
        final String methodName = "getAllReadingByStatus() : ";
        logger.info(methodName + " called with parameters status={}",status);
        List<MeterReadingBean> meterReadingBeanList = new ArrayList<>();
        try {
            meterReadingBeanList = meterReadingRepo.findAllByStatus(status);
        } catch(NullPointerException ex)
        {
            logger.error(methodName+" throw NullPointerException");
            throw ex;
        }catch (ApiException apiException){
            logger.error(methodName+" throw apiException");
            throw apiException;
        }catch (DataIntegrityViolationException d){
            logger.error(methodName+" throw DataIntegrityViolationException");
            throw d;
        } catch (Exception e) {
            logger.error(methodName+" throw Exception");
            throw e;
        }
        logger.info(methodName + " return with MeterReadingBean list of size : {}",meterReadingBeanList.size());
        return meterReadingBeanList;
    }

    public List<MeterReadingBean> getAllReadingByMonthAndStatus(String month, String status) {
        final String methodName = "getAllReadingByMonthAndStatus() : ";
        logger.info(methodName + " called with parameters month={}, status={}",month,status);
        List<MeterReadingBean> meterReadingBeanList = new ArrayList<>();
        try {
            meterReadingBeanList = meterReadingRepo.findAllByReadingDateAndStatus(month, status);
        } catch (ApiException apiException){
            logger.error(methodName+" throw apiException");
            throw apiException;
        }catch (DataIntegrityViolationException d){
            logger.error(methodName+" throw DataIntegrityViolationException");
            throw d;
        } catch (Exception e) {
            logger.error(methodName+" throw Exception");
            throw e;
        }
        logger.info(methodName + " return with MeterReadingBean list of size : {}",meterReadingBeanList.size());
        return meterReadingBeanList;
    }

    public List<MeterReadingBean> getAllReadingByMonthAndMeterNoAndStatus(String month, String meterNo, String status) {
        final String methodName = "getAllReadingByMonthAndMeterNoAndStatus() : ";
        logger.info(methodName + " called with parameters month={},meterNo={}, status={}",month,meterNo, status);
        List<MeterReadingBean> meterReadingBeanList = new ArrayList<>();
        try {
            meterReadingBeanList = meterReadingRepo.findAllByReadingDateMonthAndMeterNoAndStatus(month, meterNo, status);
        }catch(ApiException apiException){
            logger.error(methodName+" throw apiException");
            throw apiException;
        }catch (DataIntegrityViolationException d){
            logger.error(methodName+" throw DataIntegrityViolationException");
            throw d;
        } catch (Exception e) {
            logger.error(methodName+" throw Exception");
            throw e;
        }
        logger.info(methodName + " return with MeterReadingBean list of size : {}",meterReadingBeanList.size());
        return meterReadingBeanList;
    }

    @Transactional
    public MeterReadingBean createMeterReading(MeterReadingBean passMRB) {
        final String methodName = "createMeterReading() : ";
        logger.info(methodName + " called with parameters passMRB={}",passMRB);
        MeterReadingBean meterReadingBean = new MeterReadingBean();
        try {
            //set time to zero 00:00:00 fixed on 28.11.2023
            passMRB.setReadingDate(new DateMethods().zeroTime(passMRB.getReadingDate(),0,0,0,0));
            passMRB.setEndDate(new DateMethods().zeroTime(passMRB.getEndDate(),0,0,0,0));
            //check for meter exist, active and mapped with respect to meter master
            Long validationCount = meterMasterRepo.countByMeterNumberAndStatusAndIsMapped(passMRB.getMeterNo(),
                    "active", "yes");
            //check next reading date wrt to input reading date and compare value
            MeterReadingBean nextReading = meterReadingRepo.findJustNext(passMRB.getMeterNo(), passMRB.getReadingDate());

            //check previous reading date wrt to input reading date and compare value
            MeterReadingBean previousReading = meterReadingRepo.findJustBefore(passMRB.getMeterNo(), passMRB.getReadingDate());
            //reading insertion validation
            if (validationCount <= 0) throw new ApiException(HttpStatus.BAD_REQUEST, "Meter is not actively mapped");
            //check export active energy
            if (nextReading != null && nextReading.getEActiveEnergy().compareTo(passMRB.getEActiveEnergy()) < 0)
                throw new ApiException(HttpStatus.BAD_REQUEST, " export active energy(kwh) is greater than reading already present at "+nextReading.getReadingDate());
            if (previousReading != null && previousReading.getEActiveEnergy().compareTo(passMRB.getEActiveEnergy()) > 0)
                throw new ApiException(HttpStatus.BAD_REQUEST, "export active energy(kwh) is lesser than reading already present at "+previousReading.getReadingDate());
            //check import active energy
            if (nextReading != null && nextReading.getIActiveEnergy().compareTo(passMRB.getIActiveEnergy()) < 0)
                throw new ApiException(HttpStatus.BAD_REQUEST, " import active energy(kwh) is greater than reading already present at "+nextReading.getReadingDate());
            if (previousReading != null && previousReading.getIActiveEnergy().compareTo(passMRB.getIActiveEnergy()) > 0)
                throw new ApiException(HttpStatus.BAD_REQUEST, "import active energy(kwh) is lesser than reading already present at "+previousReading.getReadingDate());

            //check export kvah apparent energy
            if (nextReading != null && nextReading.getEKvah().compareTo(passMRB.getEKvah()) < 0)
                throw new ApiException(HttpStatus.BAD_REQUEST, " export apparent energy(kvah) is greater than reading already present at "+nextReading.getReadingDate());
            if (previousReading != null && previousReading.getEKvah().compareTo(passMRB.getEKvah()) > 0)
                throw new ApiException(HttpStatus.BAD_REQUEST, "export apparent energy(kvah) is lesser than reading already present at "+previousReading.getReadingDate());
            //check import kvah apparent energy
            if (nextReading != null && nextReading.getIKvah().compareTo(passMRB.getIKvah()) < 0)
                throw new ApiException(HttpStatus.BAD_REQUEST, " import apparent energy(kvah) is greater than reading already present at "+nextReading.getReadingDate());
            if (previousReading != null && previousReading.getIKvah().compareTo(passMRB.getIKvah()) > 0)
                throw new ApiException(HttpStatus.BAD_REQUEST, "import apparent energy(kvah) is lesser than reading already present at "+previousReading.getReadingDate());

            //check export tod1
            if (nextReading != null && nextReading.getETod1().compareTo(passMRB.getETod1()) < 0)
                throw new ApiException(HttpStatus.BAD_REQUEST, " export TOD1 is greater than reading already present at "+nextReading.getReadingDate());
            if (previousReading != null && previousReading.getETod1().compareTo(passMRB.getETod1()) > 0)
                throw new ApiException(HttpStatus.BAD_REQUEST, "export TOD1 is lesser than reading already present at "+previousReading.getReadingDate());
            //check import tod1
            if (nextReading != null && nextReading.getITod1().compareTo(passMRB.getITod1()) < 0)
                throw new ApiException(HttpStatus.BAD_REQUEST, " import TOD1 is greater than reading already present at "+nextReading.getReadingDate());
            if (previousReading != null && previousReading.getITod1().compareTo(passMRB.getITod1()) > 0)
                throw new ApiException(HttpStatus.BAD_REQUEST, "import TOD1 is lesser than reading already present at "+previousReading.getReadingDate());

            //check export tod2
            if (nextReading != null && nextReading.getETod2().compareTo(passMRB.getETod2()) < 0)
                throw new ApiException(HttpStatus.BAD_REQUEST, " export TOD2 is greater than reading already present at "+nextReading.getReadingDate());
            if (previousReading != null && previousReading.getETod2().compareTo(passMRB.getETod2()) > 0)
                throw new ApiException(HttpStatus.BAD_REQUEST, "export TOD2 is lesser than reading already present at "+previousReading.getReadingDate());
            //check import tod2
            if (nextReading != null && nextReading.getITod2().compareTo(passMRB.getITod2()) < 0)
                throw new ApiException(HttpStatus.BAD_REQUEST, " import TOD2 is greater than reading already present at "+nextReading.getReadingDate());
            if (previousReading != null && previousReading.getITod2().compareTo(passMRB.getITod2()) > 0)
                throw new ApiException(HttpStatus.BAD_REQUEST, "import TOD2 is lesser than reading already present at "+previousReading.getReadingDate());

            //check export tod3
            if (nextReading != null && nextReading.getETod3().compareTo(passMRB.getETod3()) < 0)
                throw new ApiException(HttpStatus.BAD_REQUEST, " export TOD3 is greater than reading already present at "+nextReading.getReadingDate());
            if (previousReading != null && previousReading.getETod3().compareTo(passMRB.getETod3()) > 0)
                throw new ApiException(HttpStatus.BAD_REQUEST, "export TOD3 is lesser than reading already present at "+previousReading.getReadingDate());
            //check import tod3
            if (nextReading != null && nextReading.getITod3().compareTo(passMRB.getITod3()) < 0)
                throw new ApiException(HttpStatus.BAD_REQUEST, " import TOD3 is greater than reading already present at "+nextReading.getReadingDate());
            if (previousReading != null && previousReading.getITod3().compareTo(passMRB.getITod3()) > 0)
                throw new ApiException(HttpStatus.BAD_REQUEST, "import TOD3 is lesser than reading already present at "+previousReading.getReadingDate());

            //check export tod4
            if (nextReading != null && nextReading.getETod4().compareTo(passMRB.getETod4()) < 0)
                throw new ApiException(HttpStatus.BAD_REQUEST, " export TOD4 is greater than reading already present at "+nextReading.getReadingDate());
            if (previousReading != null && previousReading.getETod4().compareTo(passMRB.getETod4()) > 0)
                throw new ApiException(HttpStatus.BAD_REQUEST, "export TOD4 is lesser than reading already present at "+previousReading.getReadingDate());
            //check import tod4
            if (nextReading != null && nextReading.getITod4().compareTo(passMRB.getITod4()) < 0)
                throw new ApiException(HttpStatus.BAD_REQUEST, " import TOD4 is greater than reading already present at "+nextReading.getReadingDate());
            if (previousReading != null && previousReading.getITod4().compareTo(passMRB.getITod4()) > 0)
                throw new ApiException(HttpStatus.BAD_REQUEST, "import TOD4 is lesser than reading already present at "+previousReading.getReadingDate());

            //check export REACTIVE(IMP) - ACTIVE(IMP) (QUAD 1)
            if (nextReading != null && nextReading.getEReactiveQuad1().compareTo(passMRB.getEReactiveQuad1()) < 0)
                throw new ApiException(HttpStatus.BAD_REQUEST, " export REACTIVE(IMP) - ACTIVE(IMP) (QUAD 1) is greater than reading already present at "+nextReading.getReadingDate());
            if (previousReading != null && previousReading.getEReactiveQuad1().compareTo(passMRB.getEReactiveQuad1()) > 0)
                throw new ApiException(HttpStatus.BAD_REQUEST, "export REACTIVE(IMP) - ACTIVE(IMP) (QUAD 1) is lesser than reading already present at "+previousReading.getReadingDate());
            //check import REACTIVE(IMP) - ACTIVE(IMP) (QUAD 1)
            if (nextReading != null && nextReading.getIReactiveQuad1().compareTo(passMRB.getIReactiveQuad1()) < 0)
                throw new ApiException(HttpStatus.BAD_REQUEST, " import REACTIVE(IMP) - ACTIVE(IMP) (QUAD 1) is greater than reading already present at "+nextReading.getReadingDate());
            if (previousReading != null && previousReading.getIReactiveQuad1().compareTo(passMRB.getIReactiveQuad1()) > 0)
                throw new ApiException(HttpStatus.BAD_REQUEST, "import REACTIVE(IMP) - ACTIVE(IMP) (QUAD 1) is lesser than reading already present at "+previousReading.getReadingDate());

            //check export REACTIVE(EXP) - ACTIVE(IMP) (QUAD 2)
            if (nextReading != null && nextReading.getEReactiveQuad2().compareTo(passMRB.getEReactiveQuad2()) < 0)
                throw new ApiException(HttpStatus.BAD_REQUEST, " export REACTIVE(EXP) - ACTIVE(IMP) (QUAD 2) is greater than reading already present at "+nextReading.getReadingDate());
            if (previousReading != null && previousReading.getEReactiveQuad2().compareTo(passMRB.getEReactiveQuad2()) > 0)
                throw new ApiException(HttpStatus.BAD_REQUEST, "export REACTIVE(EXP) - ACTIVE(IMP) (QUAD 2) is lesser than reading already present at "+previousReading.getReadingDate());
            //check import REACTIVE(EXP) - ACTIVE(IMP) (QUAD 2)
            if (nextReading != null && nextReading.getIReactiveQuad2().compareTo(passMRB.getIReactiveQuad2()) < 0)
                throw new ApiException(HttpStatus.BAD_REQUEST, " import REACTIVE(EXP) - ACTIVE(IMP) (QUAD 2) is greater than reading already present at "+nextReading.getReadingDate());
            if (previousReading != null && previousReading.getIReactiveQuad2().compareTo(passMRB.getIReactiveQuad2()) > 0)
                throw new ApiException(HttpStatus.BAD_REQUEST, "import REACTIVE(EXP) - ACTIVE(IMP) (QUAD 2) is lesser than reading already present at "+previousReading.getReadingDate());

            //check export REACTIVE(IMP) - ACTIVE(EXP) (QUAD 3)
            if (nextReading != null && nextReading.getEReactiveQuad3().compareTo(passMRB.getEReactiveQuad3()) < 0)
                throw new ApiException(HttpStatus.BAD_REQUEST, " export REACTIVE(IMP) - ACTIVE(EXP) (QUAD 3) is greater than reading already present at "+nextReading.getReadingDate());
            if (previousReading != null && previousReading.getEReactiveQuad3().compareTo(passMRB.getEReactiveQuad3()) > 0)
                throw new ApiException(HttpStatus.BAD_REQUEST, "export REACTIVE(IMP) - ACTIVE(EXP) (QUAD 3) is lesser than reading already present at "+previousReading.getReadingDate());
            //check import REACTIVE(IMP) - ACTIVE(EXP) (QUAD 3)
            if (nextReading != null && nextReading.getIReactiveQuad3().compareTo(passMRB.getIReactiveQuad3()) < 0)
                throw new ApiException(HttpStatus.BAD_REQUEST, " import REACTIVE(IMP) - ACTIVE(EXP) (QUAD 3) is greater than reading already present at "+nextReading.getReadingDate());
            if (previousReading != null && previousReading.getIReactiveQuad3().compareTo(passMRB.getIReactiveQuad3()) > 0)
                throw new ApiException(HttpStatus.BAD_REQUEST, "import REACTIVE(IMP) - ACTIVE(EXP) (QUAD 3) is lesser than reading already present at "+previousReading.getReadingDate());

            //check export REACTIVE(EXP) - ACTIVE (EXP) (QUAD 4)
            if (nextReading != null && nextReading.getEReactiveQuad4().compareTo(passMRB.getEReactiveQuad4()) < 0)
                throw new ApiException(HttpStatus.BAD_REQUEST, " export REACTIVE(EXP) - ACTIVE (EXP) (QUAD 4) is greater than reading already present at "+nextReading.getReadingDate());
            if (previousReading != null && previousReading.getEReactiveQuad4().compareTo(passMRB.getEReactiveQuad4()) > 0)
                throw new ApiException(HttpStatus.BAD_REQUEST, "export REACTIVE(EXP) - ACTIVE (EXP) (QUAD 4) is lesser than reading already present at "+previousReading.getReadingDate());
            //check import REACTIVE(EXP) - ACTIVE (EXP) (QUAD 4)
            if (nextReading != null && nextReading.getIReactiveQuad4().compareTo(passMRB.getIReactiveQuad4()) < 0)
                throw new ApiException(HttpStatus.BAD_REQUEST, " import REACTIVE(EXP) - ACTIVE (EXP) (QUAD 4) is greater than reading already present at "+nextReading.getReadingDate());
            if (previousReading != null && previousReading.getIReactiveQuad4().compareTo(passMRB.getIReactiveQuad4()) > 0)
                throw new ApiException(HttpStatus.BAD_REQUEST, "import REACTIVE(EXP) - ACTIVE (EXP) (QUAD 4) is lesser than reading already present at "+previousReading.getReadingDate());

            //insertion in DB
            if (validationCount > 0 &&
                    (nextReading == null || nextReading.getEActiveEnergy().compareTo(passMRB.getEActiveEnergy()) >= 0) &&
                    (previousReading == null || previousReading.getEActiveEnergy().compareTo(passMRB.getEActiveEnergy()) <= 0)
            ) {
                //Set the Audit control parameters, Globally
                new AuditControlServices().setInitialAuditControlParameters(passMRB);

                meterReadingBean = meterReadingRepo.save(passMRB);
            }
        } catch(ApiException apiException){
            logger.error(methodName+" throw apiException");
            throw apiException;
        }catch (DataIntegrityViolationException d){
            logger.error(methodName+" throw DataIntegrityViolationException");
            throw d;
        } catch (Exception e) {
            logger.error(methodName+" throw Exception");
            throw e;
        }
        logger.info(methodName + " return with MeterReadingBean : {}",meterReadingBean);
        return meterReadingBean;
    }

    public List<MeterReadingBean> getAllReadingByCurrentStateAndStatus(String currentState, String status) {
        final String methodName = "getAllReadingByCurrentStateAndStatus() : ";
        logger.info(methodName + " called with parameters currentState={}, status={}",currentState,status);
        List<MeterReadingBean> meterReadingBeanList = new ArrayList<>();
        try {
            meterReadingBeanList = meterReadingRepo.findAllByCurrentStateAndStatus(currentState, status);
        } catch(ApiException apiException){
            logger.error(methodName+" throw apiException");
            throw apiException;
        }catch (DataIntegrityViolationException d){
            logger.error(methodName+" throw DataIntegrityViolationException");
            throw d;
        } catch (Exception e) {
            logger.error(methodName+" throw Exception");
            throw e;
        }
        logger.info(methodName + " return with MeterReadingBean list of size : {}",meterReadingBeanList.size());
        return meterReadingBeanList;
    }

    public List<MeterReadingBean> getAllReadingByCurrentStateAndMeterNoAndStatus(String currentState, String meterNo, String status) {
        final String methodName = "getAllReadingByCurrentStateAndMeterNoAndStatus() : ";
        logger.info(methodName + " called with parameters currentState={},meterNo={}, status={}",currentState,meterNo,status);
        List<MeterReadingBean> meterReadingBeanList = new ArrayList<>();
        try {
            meterReadingBeanList = meterReadingRepo.findAllByCurrentStateAndMeterNoAndStatus(currentState, meterNo, status);
        }catch(ApiException apiException){
            logger.error(methodName+" throw apiException");
            throw apiException;
        }catch (DataIntegrityViolationException d){
            logger.error(methodName+" throw DataIntegrityViolationException");
            throw d;
        }catch (Exception e) {
            logger.error(methodName+" throw Exception");
            throw e;
        }
        logger.info(methodName + " return with MeterReadingBean list of size : {}",meterReadingBeanList.size());
        return meterReadingBeanList;
    }

    public List<MeterReadingBean> updateCurrentState(String currentState,String updateState, String month, String meterNo, String status) {
        final String methodName = "updateCurrentState() : ";
        logger.info(methodName + " called with parameters currentState={},updateState={}, month={}, meterNo={}, status={}",currentState,updateState,month,meterNo,status);
        List<MeterReadingBean> meterReadingBeanList = new ArrayList<>();
        try {
                String username = new TokenInfo().getCurrentUsername();
                Timestamp updateTime = new DateMethods().getServerTime();
                meterReadingBeanList = meterReadingRepo.findAllByEndDateMonthAndMeterNoAndStatus(month, meterNo, status);
                List<MeterReadingBean> beansWithStateInitialRead = meterReadingBeanList.stream().filter(read -> read.getCurrentState().equals(currentState)).collect(Collectors.toList());
                beansWithStateInitialRead.forEach(read -> { read.setCurrentState(updateState);
                                                        read.setUpdatedBy(username);
                                                        read.setUpdatedOn(updateTime);
                                                     });
                beansWithStateInitialRead = (List<MeterReadingBean>) meterReadingRepo.saveAll(beansWithStateInitialRead);
            logger.info(methodName + " return with MeterReadingBean:{}",beansWithStateInitialRead);
                return beansWithStateInitialRead;
        } catch(ApiException apiException){
            logger.error(methodName+" throw apiException");
            throw apiException;
        }catch (DataIntegrityViolationException d){
            logger.error(methodName+" throw DataIntegrityViolationException");
            throw d;
        }catch (Exception e) {
            logger.error(methodName+" throw Exception");
            throw e;
        }

    }

    public MeterReadingBean updateEndDate(Date endDate, String month, String meterNo, String status) {
        final String methodName = "updateEndDate() : ";
        logger.info(methodName + " called with parameters endDate={}, month={}, meterNo={}, status={}",endDate,month,meterNo,status);
        MeterReadingBean meterReadingBean = new MeterReadingBean();
        try {
            if (!meterReadingRepo.findAllByReadingDateMonthAndMeterNoAndStatus(month, meterNo, status).isEmpty())
                meterReadingBean = meterReadingRepo.updateEndDate(endDate, month, meterNo, status);
        } catch(ApiException apiException){
            logger.error(methodName+" throw apiException");
            throw apiException;
        }catch (DataIntegrityViolationException d){
            logger.error(methodName+" throw DataIntegrityViolationException");
            throw d;
        }catch (Exception e) {
            logger.error(methodName+" throw Exception");
            throw e;
        }
        logger.info(methodName + " return with MeterReadingBean:{}",meterReadingBean);
        return meterReadingBean;
    }

    public MeterReadingBean getReadingByMeterNoAndReadingDateAndStatus(String meterNo, Date readingDate, String status) {
        final String methodName = "getReadingByMeterNoAndReadingDateAndStatus() : ";
        logger.info(methodName + " called with parameters meterNo={}, readingDate={}, status={}",meterNo,readingDate,status);
        MeterReadingBean meterReadingBean = new MeterReadingBean();
        try {
            meterReadingBean = meterReadingRepo.findByMeterNoAndReadingDateAndStatus(meterNo, readingDate, status);
        }catch(ApiException apiException){
            logger.error(methodName+" throw apiException");
            throw apiException;
        }catch (DataIntegrityViolationException d){
            logger.error(methodName+" throw DataIntegrityViolationException");
            throw d;
        }catch (Exception e) {
            logger.error(methodName+" throw Exception");
            throw e;
        }
        logger.info(methodName + " return with MeterReadingBean:{}",meterReadingBean);
        return meterReadingBean;
    }

    public MeterReadingBean GetLastReadingByMeterNoAndStatus(String oldMeterNumber, String str) {
        final String methodName = "GetLastReadingByMeterNoAndStatus() : ";
        logger.info(methodName + " called with parameters oldMeterNumber={}, str={}",oldMeterNumber,str);
        MeterReadingBean meterReadingBean= meterReadingRepo.findLastReadByMeterNoAndStatus(oldMeterNumber, str);
        logger.info(methodName + " return with MeterReadingBean:{}",meterReadingBean);
        return meterReadingBean;
    }


    public List<MeterReadingBean> getAmrAcceptedReadings(String monthYear) {
        final String methodName = "getAmrAcceptedReadings() : ";
        logger.info(methodName + " called with parameters monthYear={}",monthYear);
        try {
            List<MeterReadingBean> list = meterReadingRepo.findAcceptOrForceAcceptReadingsByAmr(monthYear)
                    .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST,"Reading is not present for month" + monthYear));
            logger.info(methodName + " return with MeterReadingBean list of size:{}",list.size());
            return list;
        }catch(ApiException apiException){
            logger.error(methodName+" throw apiException");
            throw apiException;
        }catch (DataIntegrityViolationException d){
            logger.error(methodName+" throw DataIntegrityViolationException");
            throw d;
        }catch (Exception e) {
            logger.error(methodName+" throw Exception");
            throw e;
        }
    }

    public List<MeterReadingBean> getHtAcceptedReadings(String monthYear) {
        final String methodName = "getHtAcceptedReadings() : ";
        logger.info(methodName + " called with parameters monthYear={}",monthYear);
        try {   //Get Meter List Belongs to developer only then find reading status HT_ACCEPT of those meters only
                List<Map<String,String>> meterListOfDeveloper = meterMasterService.getMetersByUser();
                List<String> meterList = meterListOfDeveloper.stream().map(m -> m.get("meterNo")).collect(Collectors.toList());
            List<MeterReadingBean> list= meterReadingRepo.findHtAcceptedReadings(meterList,monthYear)
                        .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST,"Reading is not present for month" + monthYear));
            logger.info(methodName + " return with MeterReadingBean list of size:{}",list.size());
            return list;
        }catch(ApiException apiException){
            logger.error(methodName+" throw apiException");
            throw apiException;
        }catch (DataIntegrityViolationException d){
            logger.error(methodName+" throw DataIntegrityViolationException");
            throw d;
        }catch (Exception e) {
            logger.error(methodName+" throw Exception");
            throw e;
        }
    }

    @Transactional
    public void htUserAccept(List<MeterReadingBean> meterReadingBeanList) {
        final String methodName = "htUserAccept() : ";
        logger.info(methodName + " called with parameters meterReadingBeanList={}",meterReadingBeanList);
        try {
                if(meterReadingBeanList.size()==0) throw new ApiException(HttpStatus.BAD_REQUEST,"Select atleast one..");

                String username = new TokenInfo().getCurrentUsername();
                Timestamp updateTime = new DateMethods().getServerTime();
                // here we don't need to check existing current state because view reading have only amr_accept or force_accept or dev_reject
                meterReadingBeanList.forEach(read -> {  read.setCurrentState("ht_accept");
                                                        read.setUpdatedBy(username);
                                                        read.setUpdatedOn(updateTime);
                                                    });
                meterReadingRepo.saveAll(meterReadingBeanList);
                logger.info(methodName+" return with void");
        }catch(ApiException apiException){
            logger.error(methodName+" throw apiException");
            throw apiException;
        }catch (DataIntegrityViolationException d){
            logger.error(methodName+" throw DataIntegrityViolationException");
            throw d;
        }catch (Exception e) {
            logger.error(methodName+" throw Exception");
            throw e;
        }
    }

    @Transactional
    public void developerUserAccept(List<MeterReadingBean> meterReadingBeanList) {
        final String methodName = "developerUserAccept() : ";
        logger.info(methodName + " called with parameters meterReadingBeanList={}",meterReadingBeanList);
        try {
            if(meterReadingBeanList.size()==0) throw new ApiException(HttpStatus.BAD_REQUEST,"Select atleast one..");

            String username = new TokenInfo().getCurrentUsername();
            Timestamp updateTime = new DateMethods().getServerTime();
            // here we don't need to check existing current state because view reading have only ht_accept
            meterReadingBeanList.forEach(read -> {  read.setCurrentState("dev_accept");
                read.setUpdatedBy(username);
                read.setUpdatedOn(updateTime);
            });
            meterReadingRepo.saveAll(meterReadingBeanList);
            logger.info(methodName+" return with void");
        }catch(ApiException apiException){
            logger.error(methodName+" throw apiException");
            throw apiException;
        }catch (DataIntegrityViolationException d){
            logger.error(methodName+" throw DataIntegrityViolationException");
            throw d;
        }catch (Exception e) {
            logger.error(methodName+" throw Exception");
            throw e;
        }
    }

    @Transactional
    public void developerUserReject(List<MeterReadingBean> meterReadingBeanList) {
        final String methodName = "developerUserReject() : ";
        logger.info(methodName + " called with parameters meterReadingBeanList={}",meterReadingBeanList);
        try {
            if(meterReadingBeanList.size()==0) throw new ApiException(HttpStatus.BAD_REQUEST,"Select atleast one..");

            String username = new TokenInfo().getCurrentUsername();
            Timestamp updateTime = new DateMethods().getServerTime();
            // here we don't need to check existing current state because view reading have only ht_accept
            meterReadingBeanList.forEach(read -> {  read.setCurrentState("dev_reject");
                read.setUpdatedBy(username);
                read.setUpdatedOn(updateTime);
                read.setStatus("inactive_" +updateTime ); //added for dev_reject cases newly added 19.10.23

            });
            meterReadingRepo.saveAll(meterReadingBeanList);

            //added for dev_reject cases newly added 19.10.23
            meterReadingBeanList.forEach(read -> {
                String monthYear = new DateMethods().getMonthYear(read.getReadingDate());
                fivePercentService.discardFivePercent(read.getMeterNo(), monthYear,"inactive_" +updateTime);
            });
            logger.info(methodName+" return with void");
        }catch(ApiException apiException){
            logger.error(methodName+" throw apiException");
            throw apiException;
        }catch (DataIntegrityViolationException d){
            logger.error(methodName+" throw DataIntegrityViolationException");
            throw d;
        }catch (Exception e) {
            logger.error(methodName+" throw Exception");
            throw e;
        }
    }

    public List<Map<String,String>> getMeterListByCurrentStateIn(List<String> currentStateList) {
        final String methodName = "getMeterListByCurrentStateIn() : ";
        logger.info(methodName + " called with parameters currentStateList={}",currentStateList);
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
                logger.info(methodName + " return with MeterReadingBean list of size:{}",meterList.size());
                return meterList;


        }catch(ApiException apiException){
            logger.error(methodName+" throw apiException");
            throw apiException;
        }catch (DataIntegrityViolationException d){
            logger.error(methodName+" throw DataIntegrityViolationException");
            throw d;
        }catch (Exception e) {
            logger.error(methodName+" throw Exception");
            throw e;
        }
    }

    @Transactional
    public MeterConsumptionDto getMeterConsumptionByMonth(String meterNo, String monthYear) throws ParseException {
        final String methodName = " getMeterConsumptionByMonth() : ";
        logger.info(methodName + " called with parameters meterNo={}, monthYear={}",meterNo,monthYear);
        MeterConsumptionDto meterConsumptionDto = new MeterConsumptionDto();
        try {
                List<String> currentStates = List.of("ht_accept","dev_accept");
                Date startReadDate = dateMethods.getCurrentAndPreviousDate(monthYear).get(0);
                Date endReadDate = dateMethods.getCurrentAndPreviousDate(monthYear).get(1);
                List<MeterReadingBean> meterReadingBeanList = meterReadingRepo.findByMeterNoAndCurrentStatesInBetween
                        (meterNo,currentStates,startReadDate,endReadDate,"active");
                if (meterReadingBeanList.size()==0) throw new ApiException(HttpStatus.BAD_REQUEST,"Not any Reading(ht accept or developer accept) Present in month");
                if (meterReadingBeanList.size()>2) throw new ApiException(HttpStatus.BAD_REQUEST,"More than 2 reading present for meter in given month");
                if (meterReadingBeanList.size()<2) throw new ApiException(HttpStatus.BAD_REQUEST,"Less than 2 reading present for meter in given month");

                if (meterReadingBeanList.size()==2){
                    MeterMasterBean meterMasterBean = meterMasterRepo.findByMeterNumberAndStatus(meterNo,"active");
                    if(meterMasterBean == null) throw new ApiException(HttpStatus.BAD_REQUEST,"meter must be active");

                    meterConsumptionDto = meterConsumptionDto.setMeterConsumptionDto(meterReadingBeanList.get(0),meterReadingBeanList.get(1),meterMasterBean.getMf());
                    meterConsumptionDto.setCategory(meterMasterBean.getCategory());
                    meterConsumptionDto.setMonthYear(monthYear);

            }
        }catch(ApiException apiException){
            logger.error(methodName +" throw apiException");
            throw apiException;
        }catch (DataIntegrityViolationException d){
            logger.error(methodName+" throw DataIntegrityViolationException");
            throw d;
        }catch (Exception e) {
            logger.error(methodName+" throw Exception");
            throw e;
        }
        logger.info(methodName + " return with meterConsumptionDto:{}",meterConsumptionDto);
        return meterConsumptionDto;
    }
    public MeterReadingBean GetLastReadingByMeterNoAndStatus(String meterNo, Date date) {
        final String methodName = "GetLastReadingByMeterNoAndStatus() : ";
        logger.info(methodName + " called with parameters meterNo={}, date={}",meterNo,date);
       // first we check reading on punching date, if reading exist on punching date then throw api exception.
        MeterReadingBean currentReadingBean = meterReadingRepo.findByMeterNoAndReadingDateWithoutTimeAndAndStatus(meterNo,date,"active");
        if(currentReadingBean!=null)
            throw new ApiException(HttpStatus.BAD_REQUEST,"Reading("+currentReadingBean.getEActiveEnergy()+") of meter no. for this month on date "+currentReadingBean.getReadingDate()+" is already present.");

        MeterReadingBean meterReadingBean= meterReadingRepo.findJustBefore(meterNo,date);
        logger.info(methodName + " return with meterReadingBean:{}",meterReadingBean);
        return meterReadingBean;
    }


    public List<MeterReadingBean> getAllReadingByMeterNo(String meterNo) {
        final String methodName = "getAllReadingByMeterNo() : ";
        logger.info(methodName + " called with parameters meterNo={}",meterNo);
        try {
            List<MeterReadingBean> meterReadingBeanList = meterReadingRepo.findAllByMeterNoAndStatusOrderByReadingDateDesc(meterNo,"active");
            if(meterReadingBeanList.size()==0) throw new ApiException(HttpStatus.BAD_REQUEST,"No reading found");
            logger.info(methodName + " return with  meterReadingBeanList of size:{}", meterReadingBeanList.size());
            return meterReadingBeanList;
        }catch(ApiException apiException){
            logger.error(methodName+" throw apiException");
            throw apiException;
        }catch (DataIntegrityViolationException d){
            logger.error(methodName+" throw DataIntegrityViolationException");
            throw d;
        }catch (Exception e) {
            logger.error(methodName+" throw Exception");
            throw e;
        }
    }

}


