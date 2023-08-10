package in.co.mpwin.rebilling.controller.readingoperations;

import in.co.mpwin.rebilling.services.transactional.AmrValidatedReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/amr_validated_reading")
@CrossOrigin(origins="*")
public class AmrValidatedReadingController {
    @Autowired
    AmrValidatedReadingService amrValidatedReadingService;

    //public ResponseEntity<?> getAllValidatedReadingBy
}
