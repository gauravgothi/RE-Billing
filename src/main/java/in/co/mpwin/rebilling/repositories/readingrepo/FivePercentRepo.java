package in.co.mpwin.rebilling.repositories.readingrepo;

import in.co.mpwin.rebilling.beans.readingbean.FivePercentBean;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FivePercentRepo extends CrudRepository<FivePercentBean,Long> {
//    @Query(value = "insert into ecell.re_5perc_report_trx " +
//            "(check_assessment,check_consumption,check_current_reading,check_meter_number,check_mf,check_previous_reading,check_reading_difference,check_total_consumption,developer_id,developer_name,developer_site_address,feeder_code,feeder_name,investor_id,investor_name,main_assessment,main_consumption,main_current_reading,main_meter_number,main_mf,main_previous_reading,main_reading_difference,main_total_consumption,month_year,percentage,plant_code,remark,result)" +
//            " values" +
//            " (:#{#b.checkAssessment},:#{#b.checkConsumption},:#{#b.checkCurrentReading},:#{#b.checkMeterNumber},:#{#b.checkMf},:#{#b.checkPreviousReading},:#{#b.checkReadingDifference},:#{#b.checkTotalConsumption},:#{#b.developerId},:#{#b.developerName},:#{#b.developerSiteAddress},:#{#b.feederCode},:#{#b.feederName},:#{#b.investorId},:#{#b.investorName},:#{#b.mainAssessment},:#{#b.mainConsumption},:#{#b.mainCurrentReading},:#{#b.mainMeterNumber},:#{#b.mainMf},:#{#b.mainPreviousReading},:#{#b.mainReadingDifference},:#{#b.mainTotalConsumption},:#{#b.monthYear},:#{#b.percentage},:#{#b.plantCode},:#{#b.remark},:#{#b.result}) "+
//            " on conflict on constraint re_5perc_report_trx_ukey do nothing",nativeQuery = true)
    FivePercentBean save(FivePercentBean b);

//    Boolean existByPlantCodeAndMonthYear(String plantCode,String monthYear);

    List<FivePercentBean> findByPlantCodeAndMonthYear(String plantCode, String monthYear);

    List<FivePercentBean> findByMonthYear(String monthYear);
}
