package in.co.mpwin.rebilling.services.metermaster;

import in.co.mpwin.rebilling.beans.mapping.MeterFeederPlantMappingBean;
import in.co.mpwin.rebilling.beans.metermaster.MeterMasterBean;
import in.co.mpwin.rebilling.dao.metermaster.MeterMasterDao;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.DateMethods;
import in.co.mpwin.rebilling.miscellanious.TokenInfo;
import in.co.mpwin.rebilling.repositories.metermaster.MeterMasterRepo;
import in.co.mpwin.rebilling.services.developermaster.DeveloperMasterService;
import in.co.mpwin.rebilling.services.mapping.MeterFeederPlantMappingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MeterMasterService {
    private static final Logger logger = LoggerFactory.getLogger(MeterMasterService.class);
    @Autowired private MeterMasterDao  meterMasterDao;

    @Autowired private MeterFeederPlantMappingService mfpService;

    @Autowired private  DeveloperMasterService developerMasterService;
    @Autowired private MeterMasterRepo meterMasterRepo;

    public MeterMasterBean getMeterDetailsByMeterNo(String meterno, String status) {
        final String methodName = "getMeterDetailsByMeterNo() : ";
        logger.info(methodName + "called with parameters meterno={}, status={}",meterno,status);
        MeterMasterBean meterMasterBean = new MeterMasterBean();
        try {
            meterMasterBean = meterMasterDao.getMeterDetailsByMeterNo(meterno,status);
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
        logger.info(methodName + " return with  MeterMasterBean : {}", meterMasterBean);
        return meterMasterBean;
    }

    public ArrayList<MeterMasterBean> getAllMeterByStatus(String status) {
        final String methodName = "getAllMeterByStatus() : ";
        logger.info(methodName + "called with parameters status={}",status);
        ArrayList<MeterMasterBean> meterList = new ArrayList<MeterMasterBean>();
        try {
            meterList = meterMasterDao.getAllMeterByStatus(status);
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
        logger.info(methodName + " return with  MeterMasterBean list of size : {}", meterList.size());
        return meterList;
    }

    /*public int createMeterMaster(
            String METERNO,
            String MAKE,
            String CATEGORY,
            String TYPE,
            String METER_CLASS,
            String METER_CTR,
            String METER_PTR,
            String ME_CTR,
            String ME_PTR,
            String DIAL_BMF,
            String EQUIP_CLASS,
            String PHASE,
            String METERGRP,
            String MF,
            Date install_date,
            String created_by,
            String updated_by,
            Timestamp created_on,
            Timestamp updated_on,
            String status,
            String remark
    )   {
        int resp_code = 0;
        try {
            resp_code = meterMasterDao.createMeterMaster(METERNO,
                    MAKE,
                    CATEGORY,
                    TYPE,
                    METER_CLASS,
                    METER_CTR,
                    METER_PTR,
                    ME_CTR,
                    ME_PTR,
                    DIAL_BMF,
                    EQUIP_CLASS,
                    PHASE,
                    METERGRP,
                    MF,
                    install_date,
                    created_by,
                    updated_by,
                    created_on,
                    updated_on,
                    status,
                    remark);
        }
        catch (Exception e) {
            System.out.print(e);
            e.printStackTrace();
        }
        return resp_code;
    }*/


    public MeterMasterBean createMeterMaster(MeterMasterBean meterMasterBean) {
        final String methodName = "createMeterMaster() : ";
        logger.info(methodName + "called with parameters meterMasterBean={}",meterMasterBean);
        //int result = -1;
        MeterMasterBean mmb = new MeterMasterBean();
        try {
                mmb = meterMasterDao.createMeterMaster(meterMasterBean);
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
        logger.info(methodName + " return with  MeterMasterBean : {}", mmb);
        return mmb;
    }

    public  MeterMasterBean updateMeterStatusAndMappingByMeterNo(String meterNumber, String status, String isMapped) {
        final String methodName = "updateMeterStatusAndMappingByMeterNo() : ";
        logger.info(methodName + "called with parameters meterNumber={}, status={}, isMapped={} ",meterNumber,status,isMapped);
       try {
           MeterMasterBean meterBean = meterMasterRepo.findByMeterNumberAndStatus(meterNumber, status);
           meterBean.setIsMapped(isMapped);
           meterBean.setUpdatedBy(new TokenInfo().getCurrentUsername());
           meterBean.setUpdatedOn(new DateMethods().getServerTime());
           MeterMasterBean updatedMeterBean = meterMasterRepo.save(meterBean);
           logger.info(methodName + " return with  Updated MeterMasterBean : {}", updatedMeterBean);
           return updatedMeterBean;
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

    }

    public List<MeterMasterBean> getMeterDetailsByCategory(String category, String status, String isMapped) {
        final String methodName = "getMeterDetailsByCategory() : ";
        logger.info(methodName + "called with parameters category={}, status={}, isMapped={} ",category,status,isMapped);
        try {
            List<MeterMasterBean> listMeters = meterMasterRepo.findByCategoryAndStatusAndIsMapped(category, status, isMapped);
            logger.info(methodName + " return with  MeterMasterBean list of size : {}", listMeters.size());
            return listMeters;
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
    }

    public List<Map<String,String>> getMetersByUser(){
        final String methodName = "getMetersByUser() : ";
        logger.info(methodName + "called with parameters empty ");
        List<Map<String,String>> meterList = new ArrayList<>();
        try {
                String roleName = new TokenInfo().getCurrentUserRole();
                if (!roleName.equalsIgnoreCase("DEVELOPER"))
                    throw new ApiException(HttpStatus.BAD_REQUEST,"Current User must be developer to access this.");
                String username = new TokenInfo().getCurrentUsername();
                String developerId = String.valueOf(developerMasterService.getDeveloperIdByUsername(username));
                List<MeterFeederPlantMappingBean> mfpBeans = mfpService.getMappingByDeveloperId(developerId,"active");
                if (mfpBeans.isEmpty())
                    throw new ApiException(HttpStatus.BAD_REQUEST,"Not any Meter Feeder Plant mapping is present for user.");
                List<String> meters = mfpBeans.stream().flatMap(m-> Stream.of(m.getMainMeterNo(),m.getCheckMeterNo())).distinct().collect(Collectors.toList());
                for (String meter : meters){
                    Map<String,String> m = new HashMap<>();
                    String meterCategory = meterMasterRepo.findByMeterNumberAndStatus(meter,"active").getCategory();
                    m.put("meterNo",meter);
                    m.put("meterCategory",meterCategory);
                    meterList.add(m);
                }
                logger.info(methodName + " return with  MeterMasterBean list of size : {}", meterList.size());
                    return meterList;
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
    }

    public List<Map<String,String>> getMeters(){
        final String methodName = "getMeters() : ";
        logger.info(methodName + "called with parameters empty ");
        List<Map<String,String>> meterList = new ArrayList<>();
        try {

            List<MeterMasterBean> meters = meterMasterRepo.findByStatusAndIsMapped("active","yes");
            for (MeterMasterBean bean : meters){
                Map<String,String> m = new HashMap<>();
                m.put("meterNo", bean.getMeterNumber());
                m.put("meterCategory", bean.getCategory());
                meterList.add(m);
            }
            logger.info(methodName + " return with  MeterMasterBean list of size : {}", meterList.size());
            return meterList;
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
    }

    public List<MeterMasterBean> getMeterByStatusAndIsMappped(String status, String mapped) {
        final String methodName = "getMeterByStatusAndIsMappped() : ";
        logger.info(methodName + "called with parameters status={}, mapped={} ",status,mapped);
        try{
        List<MeterMasterBean> ismappedMeters =  meterMasterRepo.findByStatusAndIsMappedOrderById(status,mapped);
            if (ismappedMeters.isEmpty())
            {
                throw new ApiException(HttpStatus.BAD_REQUEST, "new meter list are not found in meter master.");
            }
                logger.info(methodName + " return with  MeterMasterBean list of size : {}", ismappedMeters.size());
                return ismappedMeters;
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
    }

    public List<MeterMasterBean> getUnmappedMeterBeans(String status, String isMapped, String category) {
        final String methodName = "getUnmappedMeterBeans() : ";
        logger.info(methodName + "called with parameters status={}, isMapped={}, category={} ",status,isMapped,category);
        List<MeterMasterBean> unmappedMeters = meterMasterRepo.findByStatusAndIsMappedAndCategory(status,isMapped,category);
        logger.info(methodName + " return with  MeterMasterBean list of size : {}", unmappedMeters.size());
        return unmappedMeters;
    }
}
