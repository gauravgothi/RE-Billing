package in.co.mpwin.rebilling.repositories.readingrepo;


import in.co.mpwin.rebilling.beans.xmlfilebean.XmlParserBean;
import in.co.mpwin.rebilling.beans.readingbean.MeterReadingBean;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

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

}
