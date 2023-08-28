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
    /*@Transactional
    @Modifying
    @Query(value = "INSERT into re_meter_reading_trx (" +
            "meter_no," +
            "mf," +
            "reading_date," +
            "end_date," +
            "reading_type," +
            "read_source," +
            "e_tod1," +
            "e_tod2," +
            "e_tod3," +
            "e_tod4," +
            "e_active_energy," +
            "e_reactive_quad1," +
            "e_reactive_quad2," +
            "e_reactive_quad3," +
            "e_reactive_quad4," +
            "e_adjustment," +
            "e_max_demand," +
            "e_kvah," +
            "e_assesment," +
            "i_tod1," +
            "i_tod2," +
            "i_tod3," +
            "i_tod4," +
            "i_active_energy," +
            "i_reactive_quad1," +
            "i_reactive_quad2," +
            "i_reactive_quad3," +
            "i_reactive_quad4," +
            "i_adjustment," +
            "i_max_demand," +
            "i_kvah," +
            "i_assesment," +
            "current_state," +
            "created_by," +
            "updated_by," +
            "created_on," +
            "updated_on," +
            "status," +
            "remark) " +
            "values(" +
            ":#{#xpb.meter}," +
            ":#{#xpb.g9}," +
            ":#{#xpb.g2}," +
            ":#{#xpb.g2}," +
            ":#{#xpb.g13}," +
            ":#{#xpb.g13}," +
            ":#{#xpb.g9}," +
            ":#{#xpb.g9}," +
            ":#{#xpb.g9}," +
            ":#{#xpb.g9}," +
            ":#{#xpb.g9}," +
            ":#{#xpb.g9}," +
            ":#{#xpb.g9}," +
            ":#{#xpb.g9}," +
            ":#{#xpb.g9}," +
            ":#{#xpb.g9}," +
            ":#{#xpb.g9}," +
            ":#{#xpb.g9}," +
            ":#{#xpb.g9}," +
            ":#{#xpb.g9}," +
            ":#{#xpb.g9}," +
            ":#{#xpb.g9}," +
            ":#{#xpb.g9}," +
            ":#{#xpb.g9}," +
            ":#{#xpb.g9}," +
            ":#{#xpb.g9}," +
            ":#{#xpb.g9}," +
            ":#{#xpb.g9}," +
            ":#{#xpb.g9}," +
            ":#{#xpb.g9}," +
            ":#{#xpb.g9}," +
            ":#{#xpb.g9}," +
            ":#{#xpb.g13}," +
            ":#{#xpb.g13}," +
            ":#{#xpb.g2}," +
            ":#{#xpb.g2}," +
            ":#{#xpb.g13}," +
            ":#{#xpb.g13})", nativeQuery = true)
    public int createMeterReading(@Param("xpb") MeterReadingBean xmlParserBean);*/

    List<MeterReadingBean> findAllByStatus(String status);

    @Query(value = "SELECT * FROM ecell.re_meter_reading_trx WHERE TO_CHAR(reading_date, 'Mon-YYYY') =:month AND meter_no =:meter AND status=:status",nativeQuery = true)
    List<MeterReadingBean> findAllByReadingDateAndMeterNoAndStatus(@Param("month") String readingDate,@Param("meter")String meterNo, @Param("status") String status);

    @Query(value = "SELECT * FROM ecell.re_meter_reading_trx WHERE TO_CHAR(reading_date, 'Mon-YYYY') =:month AND status=:status",nativeQuery = true)
    List<MeterReadingBean> findAllByReadingDateAndStatus(@Param("month") String readingDate,@Param("status") String status);

    List<MeterReadingBean> findAllByCurrentStateAndMeterNoAndStatus(String currentState,String meterNo, String status);

    List<MeterReadingBean> findAllByCurrentStateAndStatus(String currentState, String status);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE ecell.re_meter_reading_trx t SET t.current_state=:current_state WHERE TO_CHAR(t.reading_date, 'Mon-YYYY') =:month AND t.meter_no=:meter AND t.status=:status",nativeQuery = true)
    MeterReadingBean updateCurrentState(@Param("current_state") String currentState,@Param("month") String month, @Param("meter") String meterNo,@Param("status") String status);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE ecell.re_meter_reading_trx t SET t.end_date=:endDate WHERE TO_CHAR(t.reading_date, 'Mon-YYYY') =:month AND t.meter_no=:meterNo AND t.status=:status",nativeQuery = true)
    MeterReadingBean updateEndDate(@Param("endDate") Date endDate, @Param("month") String month, @Param("meterNo") String meterNo, @Param("status") String status);

    MeterReadingBean save(MeterReadingBean meterReadingBean);

    //unique constraint

    MeterReadingBean findByMeterNoAndReadingDateAndStatus(String meterNo, Date readingDate, String status);

    MeterReadingBean findByMeterNoAndReadingDateAndReadingTypeAndStatus(String meterNo, Date readingDate, String readingType, String status);



    @Query(value = "SELECT * FROM ecell.re_meter_reading_trx WHERE meter_no=:meterNo AND status=:status ORDER BY reading_date DESC LIMIT 1", nativeQuery = true)
    MeterReadingBean findLastReadByMeterNoAndStatus( @Param("meterNo") String meterNo, @Param("status") String status);


    @Query(value = "SELECT * FROM ecell.re_meter_reading_trx WHERE meter_no =:meterNo and reading_date >:readingDate and status='active' order by reading_date limit 1;",nativeQuery = true)
    MeterReadingBean findJustNext(String meterNo, Date readingDate);

    @Query(value = "SELECT * FROM ecell.re_meter_reading_trx WHERE meter_no =:meterNo and reading_date <:readingDate and status='active' order by reading_date desc limit 1;",nativeQuery = true)
    MeterReadingBean findJustBefore(String meterNo, Date readingDate);

}
