package in.co.mpwin.rebilling.services.readingservice;

import in.co.mpwin.rebilling.beans.readingbean.AmrValidatedReading;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.AuditControlServices;
import in.co.mpwin.rebilling.repositories.readingrepo.AmrValidatedReadingRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AmrValidatedReadingService {
    private static final Logger logger = LoggerFactory.getLogger(AmrValidatedReadingService.class);
    @Autowired
    AmrValidatedReadingRepo amrValidatedReadingRepo;

    public List<AmrValidatedReading> getAllAmrValidatedReading(String status){
        final String methodName = "getAllAmrValidatedReading() : ";
        logger.info(methodName + " called with parameters status={}",status);
        List<AmrValidatedReading> amrValidatedReadingList = new ArrayList<>();
        try {
            amrValidatedReadingList = amrValidatedReadingRepo.findAllByStatus(status);
        }  catch(NullPointerException ex)
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
        logger.info(methodName + " return with MeterReadingBean list of size : {}",amrValidatedReadingList.size());
        return amrValidatedReadingList;
    }

    public AmrValidatedReading createAmrValidatedReading(AmrValidatedReading amrValidatedReading){
        final String methodName = "createAmrValidatedReading() : ";
        logger.info(methodName + " called with parameters amrValidatedReading={}",amrValidatedReading);
        AmrValidatedReading amrValidatedReading1 = new AmrValidatedReading();
        try {
            //Set the Audit control parameters, Globally
            new AuditControlServices().setInitialAuditControlParameters(amrValidatedReading);
            amrValidatedReading1 = amrValidatedReadingRepo.save(amrValidatedReading);
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
        logger.info(methodName + " return with  amrValidatedReading : {}", amrValidatedReading1);
        return amrValidatedReading1;
    }
}
