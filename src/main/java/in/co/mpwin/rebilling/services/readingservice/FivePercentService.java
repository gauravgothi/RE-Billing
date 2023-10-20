package in.co.mpwin.rebilling.services.readingservice;

import in.co.mpwin.rebilling.beans.readingbean.FivePercentBean;
import in.co.mpwin.rebilling.beans.readingbean.MeterReadingBean;
import in.co.mpwin.rebilling.dto.ConsumptionPercentageDto2;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.repositories.readingrepo.FivePercentRepo;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FivePercentService {

    @Autowired(required = false) private in.co.mpwin.rebilling.beans.readingbean.FivePercentBean fivePercentBean;
    @Autowired private FivePercentRepo fivePercentRepo;

    @Autowired private MeterReadingService meterReadingService;

    public void insertFivePercentReport(List<FivePercentBean> fivePercentBeans){
        try {   //First convert the FivePercentBean to FivePercentBean using modelmapper
                for(FivePercentBean bean : fivePercentBeans) {
                    if (bean.getResult().equals("pass") || bean.getResult().equals("fail")) {
                        fivePercentRepo.save(bean);
                    }
                }
        }catch (DataIntegrityViolationException d){
            //throw new ApiException(HttpStatus.BAD_REQUEST,"Record already exist");
        }
        catch (Exception e){
           // e.printStackTrace();
        }
    }

    public Boolean isAlreadyExistRecord(String plantCode,String monthYear){
        try {
            List<FivePercentBean> alreadyExistBean = fivePercentRepo.findByPlantCodeAndMonthYear(plantCode,monthYear);
            return alreadyExistBean.size()>0?true:false;
        }catch (DataIntegrityViolationException e){
            throw e;
        }catch (Exception e){
            throw e;
        }
    }

    public List<FivePercentBean> getByPlantCodeAndMonth(String plantCode, String monthYear){
        List<FivePercentBean> alreadyPresent =null;
        try {
            alreadyPresent = fivePercentRepo.findByPlantCodeAndMonthYear(plantCode,monthYear);

        }catch (DataIntegrityViolationException e){
            //throw e;
        }catch (Exception e){
           // throw e;
        }
        return alreadyPresent;
    }

    public List<FivePercentBean> getByMonth(String monthYear){
        List<FivePercentBean> alreadyPresent = null;
        try {
            alreadyPresent = fivePercentRepo.findByMonthYear(monthYear);

        }catch (DataIntegrityViolationException e){
            //throw e;
        }catch (Exception e){
            // throw e;
        }
        return alreadyPresent;
    }

    public List<FivePercentBean> getByMonthAndRemarkEqualTo(String monthYear, String remark) {
        List<FivePercentBean> alreadyPresent = null;
        try {
            alreadyPresent = fivePercentRepo.findByMonthAndRemarkEqualTo(monthYear,remark);

        }catch (DataIntegrityViolationException e){
            //throw e;
        }catch (Exception e){
            // throw e;
        }
        return alreadyPresent;
    }

    @Transactional
    //Pass Five Percent Bean
    public void amrUserAccept(List<FivePercentBean> fivePercentBeanList){
        try {
            for (FivePercentBean b : fivePercentBeanList){
                //if five percent report remark is calculated then only accept reading otherwise give exception
                if((b.getResult().equals("withheld")))
                    continue;
                String[] mainMeters = b.getMainMeterNumber().split("#");
                String[] checkMeters = b.getCheckMeterNumber().split("#");
                if(b.getResult().equals("pass") && (b.getRemark().equals("calculated"))){
                    //update the reading current state
                    for (String mainMeter : mainMeters)
                        meterReadingService.updateCurrentState("initial_read","amr_accept",b.getMonthYear(),mainMeter,"active");
                    for (String checkMeter : checkMeters)
                        meterReadingService.updateCurrentState("initial_read","amr_accept",b.getMonthYear(),checkMeter,"active");
                }
                else if (b.getResult().equals("fail") && (b.getRemark().equals("calculated"))){
                    //update the reading current state
                    for (String mainMeter : mainMeters)
                        meterReadingService.updateCurrentState("initial_read","amr_reject",b.getMonthYear(),mainMeter,"active");
                    for (String checkMeter : checkMeters)
                        meterReadingService.updateCurrentState("initial_read","amr_reject",b.getMonthYear(),checkMeter,"active");
                }
                fivePercentRepo.setRemarkById("amr_approved",b.getId());
            }
        }catch (ApiException apiException){
            throw apiException;
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Transactional
    //Pass Five Percent Bean with action of meter_selected_flag
    public void amrUserForceAccept(List<FivePercentBean> fivePercentBeanList){
        try {
            for (FivePercentBean b : fivePercentBeanList){
                String[] mainMeters = b.getMainMeterNumber().split("#");
                String[] checkMeters = b.getCheckMeterNumber().split("#");
                if (!(b.getMeterSelectedFlag().equals("main") || b.getMeterSelectedFlag().equals("check")))
                    throw new ApiException(HttpStatus.BAD_REQUEST,"Select either main or check");
                if(b.getResult().equals("fail") && b.getMeterSelectedFlag().equals("main")){
                    //update the reading current state of main meter only
                    for (String mainMeter : mainMeters) {
                        meterReadingService.updateCurrentState("amr_reject", "amr_force_accept", b.getMonthYear(), mainMeter, "active");
                    }
                    fivePercentRepo.setMeterSelectedFlagById("main",b.getId());
                }
                else if (b.getResult().equals("fail") && b.getMeterSelectedFlag().equals("check")){
                    //update the reading current state of main check only
                    for (String checkMeter : checkMeters) {
                        meterReadingService.updateCurrentState("amr_reject", "amr_force_accept", b.getMonthYear(), checkMeter, "active");
                    }
                    fivePercentRepo.setMeterSelectedFlagById("check",b.getId());
                }
                fivePercentRepo.setRemarkById("amr_force_approved",b.getId());
            }
        }catch (ApiException apiException){
            throw apiException;
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public List<FivePercentBean> getAmrUserAcceptByMonthAndResult(String monthYear,String result){
        List<FivePercentBean> fivePercentBeanList = null;
        try {
            fivePercentBeanList = fivePercentRepo.findByMonthAndResult(monthYear,result);

        }catch (DataIntegrityViolationException e){
            //throw e;
        }catch (Exception e){
            // throw e;
        }
        return fivePercentBeanList;
    }

    public List<FivePercentBean> getAmrUserAcceptFailResultForAction(String monthYear,String result,String remark){
        List<FivePercentBean> fivePercentBeanList = null;
        try {
            fivePercentBeanList = fivePercentRepo.findByMonthAndResultAndRemark(monthYear,result,remark);
            if (fivePercentBeanList.size()==0)
                throw new ApiException(HttpStatus.BAD_REQUEST,"Not any Failed reading left for force approval");

        }catch (ApiException apiException){
            throw apiException;
        }catch (DataIntegrityViolationException d){
            throw d;
        }catch (Exception e){
             throw e;
        }
        return fivePercentBeanList;
    }

    public List<FivePercentBean> discardFivePercent(String meterNumber,String monthYear,String status){
        List<FivePercentBean> fivePercentBeanList = null;
        try {
                fivePercentRepo.discardFivePercentBean(meterNumber,monthYear,status);
        }catch (ApiException apiException){
            throw apiException;
        }catch (DataIntegrityViolationException d){
            throw d;
        }catch (Exception e){
            throw e;
        }
        return fivePercentBeanList;
    }

}
