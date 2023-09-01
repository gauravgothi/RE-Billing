package in.co.mpwin.rebilling.services.readingservice;

import in.co.mpwin.rebilling.beans.readingbean.FivePercentBean;
import in.co.mpwin.rebilling.dto.ConsumptionPercentageDto2;
import in.co.mpwin.rebilling.repositories.readingrepo.FivePercentRepo;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FivePercentService {

    @Autowired(required = false) private in.co.mpwin.rebilling.beans.readingbean.FivePercentBean fivePercentBean;
    @Autowired private FivePercentRepo fivePercentRepo;

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
            List<in.co.mpwin.rebilling.beans.readingbean.FivePercentBean> alreadyExistBean = fivePercentRepo.findByPlantCodeAndMonthYear(plantCode,monthYear);
            return alreadyExistBean.size()>0?true:false;
        }catch (DataIntegrityViolationException e){
            throw e;
        }catch (Exception e){
            throw e;
        }
    }

    public List<in.co.mpwin.rebilling.beans.readingbean.FivePercentBean> getByPlantCodeAndMonth(String plantCode, String monthYear){
        List<in.co.mpwin.rebilling.beans.readingbean.FivePercentBean> alreadyPresent =null;
        try {
            alreadyPresent = fivePercentRepo.findByPlantCodeAndMonthYear(plantCode,monthYear);

        }catch (DataIntegrityViolationException e){
            //throw e;
        }catch (Exception e){
           // throw e;
        }
        return alreadyPresent;
    }

    public List<in.co.mpwin.rebilling.beans.readingbean.FivePercentBean> getByMonth(String monthYear){
        List<in.co.mpwin.rebilling.beans.readingbean.FivePercentBean> alreadyPresent = null;
        try {
            alreadyPresent = fivePercentRepo.findByMonthYear(monthYear);

        }catch (DataIntegrityViolationException e){
            //throw e;
        }catch (Exception e){
            // throw e;
        }
        return alreadyPresent;
    }
}
