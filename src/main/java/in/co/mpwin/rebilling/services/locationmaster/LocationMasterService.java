package in.co.mpwin.rebilling.services.locationmaster;

import in.co.mpwin.rebilling.beans.locationmaster.LocationMaster;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.repositories.locationmaster.LocationMasterRepo;
import in.co.mpwin.rebilling.services.investormaster.InvestorMasterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocationMasterService {
    private static final Logger logger = LoggerFactory.getLogger(LocationMasterService.class);

    @Autowired
    LocationMasterRepo locationMasterRepo;

    public List<LocationMaster> findAllByStatus(String status){
        final String methodName = "findAllByStatus() : ";
        logger.info(methodName + "called with parameters status={}",status);
        List<LocationMaster> locationMasterList = new ArrayList<>();
        try {
            locationMasterList= locationMasterRepo.findAllByStatus(status);
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
        logger.info(methodName + " return with LocationMaster list of size : {}",locationMasterList.size());
        return locationMasterList;
    }

    public LocationMaster getLocationByDivisionCode(String divisionCode,String status){
        final String methodName = "getLocationByDivisionCode() : ";
        logger.info(methodName + "called with parameters divisionCode={}, status={}",divisionCode,status);
        LocationMaster location = new LocationMaster();
        try {
            location= locationMasterRepo.findByDivisionCodeAndStatus(divisionCode,status);
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
        logger.info(methodName + " return with location : {}",location);
        return location;
    }

    public LocationMaster getLocationById(String id,String status){
        final String methodName = "getLocationById() : ";
        logger.info(methodName + "called with parameters id={}, status={}",id,status);
        LocationMaster location = new LocationMaster();
        try {
            location= locationMasterRepo.findByIdAndStatus(id,status);
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
        logger.info(methodName + " return with location : {}",location);
        return location;
    }

    public List<String> getDeveloperListByCircleNameAndStatus(String username, String status) {
        return locationMasterRepo.findDeveloperUsernamesByLocationIdAndStatus(username,status);
    }

    public String getIdByCircleNameAndStatus(String circleName, String status) {
        return locationMasterRepo.findIdByCircleNameAndStatus(circleName,status);

    }

    public List<String> getAllRegion(String status) {
        final String methodName = "getAllRegion() : ";
        logger.info(methodName + "called with parameters status={}",status);
        List<String> list = locationMasterRepo.findDistictRegionByStatus(status);
        if(list.isEmpty() || list == null)
            throw new ApiException(HttpStatus.BAD_REQUEST,"Region list are not found in location master.");
        logger.info(methodName + " return with Region list of size : {}",list.size());
            return list;
    }

    public List<String> getAllCircleByRegion(String regionName, String status) {
        final String methodName = "getAllCircleByRegion() : ";
        logger.info(methodName + "called with parameters regionName={}, status={}",regionName,status);
        List<String> list = locationMasterRepo.findDistictCircleByRegionAndStatus(regionName,status);
        if(list.isEmpty() || list == null)
            throw new ApiException(HttpStatus.BAD_REQUEST,"Circle list are not found in location master.");
        logger.info(methodName + " return with circle list of size : {}",list.size());
        return list;
    }

    public List<LocationMaster> getAllDivisonByCircle(String circleName, String status) {
        final String methodName = "getAllDivisonByCircle() : ";
        logger.info(methodName + "called with parameters circleName={}, status={}",circleName,status);
        List<LocationMaster> list = locationMasterRepo.findDivisionBYCircleAndStatus(circleName,status);
        if(list.isEmpty() || list == null)
            throw new ApiException(HttpStatus.BAD_REQUEST,"Division list are not found in location master.");
        logger.info(methodName + " return with LocationMaster list of size : {}",list.size());
        return list;

    }
}
