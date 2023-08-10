package in.co.mpwin.rebilling.services.transactional;

import in.co.mpwin.rebilling.beans.readingoperations.AmrValidatedReading;
import in.co.mpwin.rebilling.miscellanious.AuditControlServices;
import in.co.mpwin.rebilling.repositories.transactional.AmrValidatedReadingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AmrValidatedReadingService {
    @Autowired
    AmrValidatedReadingRepo amrValidatedReadingRepo;

    public List<AmrValidatedReading> getAllAmrValidatedReading(String status){
        List<AmrValidatedReading> amrValidatedReadingList = new ArrayList<>();
        try {
            amrValidatedReadingList = amrValidatedReadingRepo.findAllByStatus(status);
        } catch (Exception e){
            e.printStackTrace();
        }
        return amrValidatedReadingList;
    }

    public AmrValidatedReading createAmrValidatedReading(AmrValidatedReading amrValidatedReading){
        AmrValidatedReading amrValidatedReading1 = new AmrValidatedReading();
        try {
            //Set the Audit control parameters, Globally
            new AuditControlServices().setInitialAuditControlParameters(amrValidatedReading);
            amrValidatedReading1 = amrValidatedReadingRepo.save(amrValidatedReading);
        }catch (Exception exception){
            exception.printStackTrace();
            return null;
        }
        return amrValidatedReading1;
    }
}
