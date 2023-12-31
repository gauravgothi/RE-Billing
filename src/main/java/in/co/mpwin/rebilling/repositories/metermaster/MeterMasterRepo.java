package in.co.mpwin.rebilling.repositories.metermaster;

import in.co.mpwin.rebilling.beans.metermaster.MeterMasterBean;
import in.co.mpwin.rebilling.interfaces.metermaster.MeterMasterInterface;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface MeterMasterRepo extends CrudRepository<MeterMasterBean, Long> {
//    @Query(value = "select * from re_meter_master where meterno =:meterno and status=:status", nativeQuery = true)
//    public MeterMasterBean getMeterDetailsByMeterNo(@Param("meterno") String meterno,
//                                             @Param("status") String status);
      public MeterMasterBean findByMeterNumberAndStatus(String meterNumber, String status);

//    @Query(value = "select * from re_meter_master where status = :status", nativeQuery = true)
//    public ArrayList<MeterMasterBean> getAllMeterByStatus(@Param("status") String status);
      public ArrayList<MeterMasterBean> findAllByStatus(String status);


    /*@Transactional
    @Modifying
    @Query(value = "INSERT into re_meter_master (" +
            "meterno," +
            "make," +
            "category," +
            "type," +
            "meter_class," +
            "meter_ctr," +
            "meter_ptr," +
            "me_ctr," +
            "me_ptr," +
            "dial_bmf," +
            "equip_class," +
            "phase," +
            "metergrp," +
            "mf," +
            "install_date," +
            "created_by," +
            "updated_by," +
            "created_on," +
            "updated_on," +
            "status," +
            "remark) " +
            "values(" +
            ":meterno," +
            ":make," +
            ":category," +
            ":type," +
            ":meter_class," +
            ":meter_ctr," +
            ":meter_ptr," +
            ":me_ctr," +
            ":me_ptr," +
            ":dial_bmf," +
            ":equip_class," +
            ":phase," +
            ":metergrp," +
            ":mf," +
            ":install_date," +
            ":created_by," +
            ":updated_by," +
            ":created_on," +
            ":updated_on," +
            ":status," +
            ":remark)", nativeQuery = true)
    public int createMeterMaster(@Param("meterno") String METERNO,
                          @Param("make") String MAKE,
                          @Param("category") String CATEGORY,
                          @Param("type") String TYPE,
                          @Param("meter_class") String METER_CLASS,
                          @Param("meter_ctr") String METER_CTR,
                          @Param("meter_ptr") String METER_PTR,
                          @Param("me_ctr") String ME_CTR,
                          @Param("me_ptr") String ME_PTR,
                          @Param("dial_bmf") String DIAL_BMF,
                          @Param("equip_class") String EQUIP_CLASS,
                          @Param("phase") String PHASE,
                          @Param("metergrp") String METERGRP,
                          @Param("mf") String MF,
                          @Param("install_date") Date install_date,
                          @Param("created_by") String created_by,
                          @Param("updated_by") String updated_by,

                          @Param("created_on") Timestamp created_on,
                          @Param("updated_on") Timestamp updated_on,
                          @Param("status") String status,
                          @Param("remark") String remark
                        );*/
    @Transactional
    //@Modifying
//    @Query(value = "INSERT into re_meter_master (" +
//            "meterno," +
//            "make," +
//            "category," +
//            "type," +
//            "meter_class," +
//            "meter_ctr," +
//            "meter_ptr," +
//            "me_ctr," +
//            "me_ptr," +
//            "dial_bmf," +
//            "equip_class," +
//            "phase," +
//            "metergrp," +
//            "mf," +
//            "install_date," +
//            "created_by," +
//            "updated_by," +
//            "created_on," +
//            "updated_on," +
//            "status," +
//            "remark) " +
//            "values(" +
//            ":#{#mmb.meterno}," +
//            ":#{#mmb.make}," +
//            ":#{#mmb.category}," +
//            ":#{#mmb.type}," +
//            ":#{#mmb.meter_class}," +
//            ":#{#mmb.meter_ctr}," +
//            ":#{#mmb.meter_ptr}," +
//            ":#{#mmb.me_ctr}," +
//            ":#{#mmb.me_ptr}," +
//            ":#{#mmb.dial_bmf}," +
//            ":#{#mmb.equip_class}," +
//            ":#{#mmb.phase}," +
//            ":#{#mmb.metergrp}," +
//            ":#{#mmb.mf}," +
//            ":#{#mmb.install_date}," +
//            ":#{#mmb.created_by}," +
//            ":#{#mmb.updated_by}," +
//            ":#{#mmb.created_on}," +
//            ":#{#mmb.updated_on}," +
//            ":#{#mmb.status}," +
//            ":#{#mmb.remark})", nativeQuery = true)
//    public int createMeterMaster(@Param("mmb") MeterMasterBean mmb);
          public MeterMasterBean save(MeterMasterBean meterMasterBean);

        Long countByMeterNumberAndStatusAndIsMapped(String meterNumber, String status, String isMapped);


//    @Query(value = "select case when exists (select * from re_meter_master as t where t.meter_number =:meterno"
//            + " and t.make =:make and t.status =:status) then true else false end",nativeQuery = true)
//    Boolean existsByMeterNumberAndMakeAndStatus(@Param("meterno") String meterNumber,@Param("make") String make,@Param("status") String status);

    List<MeterMasterBean> findByMeterNumberAndMakeAndStatus(String meterNumber, String make, String status);



    @Modifying
    @Transactional
    @Query(value = "UPDATE ecell.re_meter_master SET status=:status, is_Mapped=:isMapped where meter_number=:meterNo", nativeQuery = true)
    void updateMeterStatusAndMappingByMeterNo(@Param("meterNo") String meterNo, @Param("status") String status, @Param("isMapped") String isMapped);

  List<MeterMasterBean> findByCategoryAndStatusAndIsMapped(String category, String status,String isMapped);

    List<MeterMasterBean> findByStatusAndIsMapped(String status,String isMapped);

    List<MeterMasterBean> findByStatusAndIsMappedOrderById(String status,String isMapped);


//    @Query(value = "select * from ecell.re_meter_master where meter_number in (:mappedMeters) AND status=:status ORDER BY id ASC", nativeQuery = true)
//    List<MeterMasterBean> findByMeterNumberInAndStatus(@Param("mappedMeters") List<String> mappedMeters, @Param("status") String status);

    @Query(value = "select * from ecell.re_meter_master where meter_number in (:mappedMeters) AND status=:status AND is_mapped=:mapped ORDER BY id ASC", nativeQuery = true)
    List<MeterMasterBean> findByMeterNumberInAndStatusAndIsMapped(@Param("mappedMeters") List<String> mappedMeters,@Param("status") String status,@Param("mapped") String mapped);
    @Query(value = "select * from ecell.re_meter_master where meter_number=:meterNo AND status=:status AND is_mapped=:mapped ORDER BY id ASC", nativeQuery = true)
    MeterMasterBean findByMeterNumberAndStatusAndIsMapped(@Param("meterNo") String meterNo,@Param("status") String status, @Param("mapped") String mapped);

    List<MeterMasterBean> findByStatusAndIsMappedAndCategory(String status, String isMapped, String category);
}
