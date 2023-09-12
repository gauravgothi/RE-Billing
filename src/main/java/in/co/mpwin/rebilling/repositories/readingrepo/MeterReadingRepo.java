package in.co.mpwin.rebilling.repositories.readingrepo;

import in.co.mpwin.rebilling.beans.xmlfilebean.XmlParserBean;
import in.co.mpwin.rebilling.beans.readingbean.MeterReadingBean;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface MeterReadingRepo extends CrudRepository<MeterReadingBean,Long> {

    MeterReadingBean save(XmlParserBean xmlParserBean);

    List<MeterReadingBean> findAllByStatus(String status);

    @Query(value = "SELECT * FROM ecell.re_meter_reading_trx WHERE TO_CHAR(reading_date, 'Mon-YYYY') =:mon AND meter_no =:meter AND status=:status",nativeQuery = true)
    List<MeterReadingBean> findAllByReadingDateMonthAndMeterNoAndStatus(@Param("mon") String readingDateMonth,@Param("meter")String meterNo, @Param("status") String status);

    @Query(value = "SELECT * FROM ecell.re_meter_reading_trx WHERE TO_CHAR(end_date, 'Mon-YYYY') =:mon AND meter_no =:meter AND status=:status",nativeQuery = true)
    List<MeterReadingBean> findAllByEndDateMonthAndMeterNoAndStatus(@Param("mon") String EndDateMonth,@Param("meter")String meterNo, @Param("status") String status);

    @Query(value = "SELECT * FROM ecell.re_meter_reading_trx WHERE TO_CHAR(reading_date, 'Mon-YYYY') =:month AND status=:status",nativeQuery = true)
    List<MeterReadingBean> findAllByReadingDateAndStatus(@Param("month") String readingDate,@Param("status") String status);

    List<MeterReadingBean> findAllByCurrentStateAndMeterNoAndStatus(String currentState,String meterNo, String status);

    List<MeterReadingBean> findAllByCurrentStateAndStatus(String currentState, String status);

    @Query(value = "SELECT DISTINCT meter_no FROM ecell.re_meter_reading_trx where current_state in (:currentStates) and status =:status",nativeQuery = true)
    List<String> findAllDistinctMeterNoByCurrentStateInAndStatus(@Param("currentStates") List<String> currentState,String status);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE ecell.re_meter_reading_trx t SET t.current_state=:current_state WHERE TO_CHAR(t.end_date, 'Mon-YYYY') =:monthYear AND t.meter_no=:meter AND t.status=:status",nativeQuery = true)
    MeterReadingBean updateCurrentState(@Param("current_state") String currentState,@Param("monthYear") String monthYear, @Param("meter") String meterNo,@Param("status") String status);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE ecell.re_meter_reading_trx t SET t.end_date=:endDate WHERE TO_CHAR(t.reading_date, 'Mon-YYYY') =:monthYear AND t.meter_no=:meterNo AND t.status=:status",nativeQuery = true)
    MeterReadingBean updateEndDate(@Param("endDate") Date endDate, @Param("monthYear") String monthYear, @Param("meterNo") String meterNo, @Param("status") String status);

    MeterReadingBean save(MeterReadingBean meterReadingBean);

    //unique constraint

    MeterReadingBean findByMeterNoAndReadingDateAndStatus(String meterNo, Date readingDate, String status);

    MeterReadingBean findByMeterNoAndReadingDateAndReadingTypeAndStatus(String meterNo, Date readingDate, String readingType, String status);



    @Query(value = "SELECT * FROM ecell.re_meter_reading_trx WHERE meter_no=:meterNo AND status=:status ORDER BY reading_date DESC LIMIT 1", nativeQuery = true)
    MeterReadingBean findLastReadByMeterNoAndStatus(@Param("meterNo") String meterNo, @Param("status") String status);

    @Query(value = "SELECT * FROM ecell.re_meter_reading_trx WHERE meter_no =:meterNo and reading_date >:readingDate and status='active' order by reading_date limit 1;",nativeQuery = true)
    MeterReadingBean findJustNext(@Param("meterNo") String meterNo,@Param("readingDate") Date readingDate);

    @Query(value = "SELECT * FROM ecell.re_meter_reading_trx WHERE meter_no =:meterNo and reading_date <:readingDate and status='active' order by reading_date desc limit 1;",nativeQuery = true)
    MeterReadingBean findJustBefore(@Param("meterNo") String meterNo,@Param("readingDate") Date readingDate);

    @Query(value = "SELECT * FROM ecell.re_meter_reading_trx t WHERE TO_CHAR(t.end_date, 'Mon-YYYY') =:monthYear AND t.current_state in ('amr_accept','amr_force_accept') AND t.status='active';",nativeQuery = true)
    Optional<List<MeterReadingBean>> getAcceptOrForceAcceptReadingsByAmr(@Param("monthYear") String monthYear);

    @Query(value = "SELECT * FROM ecell.re_meter_reading_trx where meter_no =:meterNo AND current_state IN (:currentStates) AND status =:status AND reading_date BETWEEN :sd AND :ed ORDER BY reading_date ASC",nativeQuery = true)
    List<MeterReadingBean> findByMeterNoAndCurrentStatesInBetween(@Param("meterNo") String meterNo,@Param("currentStates") List<String> currentStates,@Param("sd") Date sd,@Param("ed") Date ed,@Param("status") String staus);
}
