package in.co.mpwin.rebilling.services.readingservice;

import in.co.mpwin.rebilling.beans.readingbean.FivePercentBean;
import in.co.mpwin.rebilling.beans.readingbean.MeterReadingBean;
import in.co.mpwin.rebilling.dto.ConsumptionPercentageDto2;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.repositories.readingrepo.FivePercentRepo;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(FivePercentService.class);
    @Autowired(required = false) private in.co.mpwin.rebilling.beans.readingbean.FivePercentBean fivePercentBean;
    @Autowired private FivePercentRepo fivePercentRepo;

    @Autowired private MeterReadingService meterReadingService;

    public void insertFivePercentReport(List<FivePercentBean> fivePercentBeans){
        final String methodName = "insertFivePercentReport() : ";
        logger.info(methodName + " called with parameters fivePercentBeans={}",fivePercentBeans);
        try {   //First convert the FivePercentBean to FivePercentBean using modelmapper
                logger.info(methodName + "looping fivePercentBeans list and save each bean");
                for(FivePercentBean bean : fivePercentBeans) {
                    if (bean.getResult().equals("pass") || bean.getResult().equals("fail")) {
                        logger.info(methodName + " saving five percent bean of plant = {}",bean.getPlantCode());
                        fivePercentRepo.save(bean);
                    }
                }
        }catch (DataIntegrityViolationException d){
            //throw new ApiException(HttpStatus.BAD_REQUEST,"Record already exist");
            logger.error(methodName+" caught DataIntegrityViolationException but no action in catch");
        }
        catch (Exception e){
           // e.printStackTrace();
            logger.error(methodName+" caught common Exception but no action in catch");
        }
    }

    public Boolean isAlreadyExistRecord(String plantCode,String monthYear){
        final String methodName = "isAlreadyExistRecord() : ";
        logger.info(methodName + " called with parameters plant Code={} and monthYear = {}",plantCode,monthYear);
        try {
            List<FivePercentBean> alreadyExistBean = fivePercentRepo.findByPlantCodeAndMonthYear(plantCode,monthYear);
            return alreadyExistBean.size()>0?true:false;
        }catch (DataIntegrityViolationException e){
            logger.error(methodName+" throw DataIntegrityViolationException.");
            throw e;
        }catch (Exception e){
            logger.error(methodName+" throw common Exception.");
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
        final String methodName = "getByMonthAndRemarkEqualTo() : ";
        logger.info(methodName + " called with parameters remark={} and monthYear = {}",remark,monthYear);
        List<FivePercentBean> alreadyPresent = null;
        try {
            alreadyPresent = fivePercentRepo.findByMonthAndRemarkEqualTo(monthYear,remark);

        }catch (DataIntegrityViolationException e){
            logger.error(methodName+" caught DataIntegrityViolationException but no action in catch");
            //throw e;
        }catch (Exception e){
            logger.error(methodName+" caught common Exception but no action in catch");
            // throw e;
        }
        return alreadyPresent;
    }

    @Transactional
    //Pass Five Percent Bean
    public void amrUserAccept(List<FivePercentBean> fivePercentBeanList){
        final String methodName = "amrUserAccept() : ";
        logger.info(methodName + " called with parameters fivePercentBeanList of size = {}",fivePercentBeanList.size());
        try {
            logger.info(methodName + "looping on five percent bean list..");
            for (FivePercentBean b : fivePercentBeanList){
                //if five percent report remark is calculated then only accept reading otherwise give exception
                if((b.getResult().equals("withheld"))) {
                    continue;
                }
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
                logger.info(methodName + " return success and set remark amr approved of bean id = {}",b.getId());
                fivePercentRepo.setRemarkById("amr_approved",b.getId());
            }
        }catch (ApiException apiException){
            logger.error(methodName+" throw ApiException");
            throw apiException;
        }
        catch (Exception e){
            logger.error(methodName+" throw common ApiException");
            throw e;
        }
    }

    @Transactional
    //Pass Five Percent Bean with action of meter_selected_flag
    public void amrUserForceAccept(List<FivePercentBean> fivePercentBeanList){
        final String methodName = "amrUserForceAccept() : ";
        logger.info(methodName + " called with parameters fivePercentBeanList of size = {}",fivePercentBeanList.size());
        try {
            logger.info(methodName + "looping on five percent bean list..");
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
                logger.info(methodName + " return success and set remark amr force approved of bean id = {}",b.getId());
                fivePercentRepo.setRemarkById("amr_force_approved",b.getId());
            }
        }catch (ApiException apiException){
            logger.error(methodName+" throw ApiException");
            throw apiException;
        }
        catch (Exception e){
            logger.error(methodName+" throw common ApiException");
            throw e;
        }
    }

    public List<FivePercentBean> getAmrUserAcceptByMonthAndResult(String monthYear,String result){
        final String methodName = "getAmrUserAcceptByMonthAndResult() : ";
        logger.info(methodName + " called with parameters monthYear = {} and result",monthYear,result);
        List<FivePercentBean> fivePercentBeanList = null;
        try {
            fivePercentBeanList = fivePercentRepo.findByMonthAndResult(monthYear,result);

        }catch (DataIntegrityViolationException e){
            //throw e;
            logger.error(methodName+" caught DataIntegrityViolationException but no action in catch");
        }catch (Exception e){
            // throw e;
            logger.error(methodName+" caught common Exception but no action in catch");
        }
        return fivePercentBeanList;
    }

    public List<FivePercentBean> getAmrUserAcceptFailResultForAction(String monthYear,String result,String remark){
        final String methodName = "getAmrUserAcceptFailResultForAction() : ";
        logger.info(methodName + " called with parameters monthYear = {}, result={} and remark ={}",monthYear,result,remark);
        List<FivePercentBean> fivePercentBeanList = null;
        try {
            fivePercentBeanList = fivePercentRepo.findByMonthAndResultAndRemark(monthYear,result,remark);
            if (fivePercentBeanList.size()==0)
                throw new ApiException(HttpStatus.BAD_REQUEST,"Not any Failed reading left for force approval");

        }catch (ApiException apiException){
            logger.error(methodName+" throw ApiException");
            throw apiException;
        }catch (DataIntegrityViolationException d){
            logger.error(methodName+" throw DataIntegrityViolationException");
            throw d;
        }catch (Exception e){
            logger.error(methodName+" throw common Exception");
             throw e;
        }
        return fivePercentBeanList;
    }

    public List<FivePercentBean> discardFivePercent(String meterNumber,String monthYear,String status){
        final String methodName = "discardFivePercent() : ";
        logger.info(methodName + " called with parameters meterNumber = {}, monthYear={} and remark={}",meterNumber,monthYear,status);
        List<FivePercentBean> fivePercentBeanList = null;
        try {
                fivePercentRepo.discardFivePercentBean(meterNumber,monthYear,status);
        }catch (ApiException apiException){
            logger.error(methodName+" throw ApiException");
            throw apiException;
        }catch (DataIntegrityViolationException d){
            logger.error(methodName+" throw DataIntegrityViolationException");
            throw d;
        }catch (Exception e){
            logger.error(methodName+" throw common Exception");
            throw e;
        }
        logger.info(methodName + "return success with five percent bean list of size ={}",fivePercentBeanList.size());
        return fivePercentBeanList;
    }

}
