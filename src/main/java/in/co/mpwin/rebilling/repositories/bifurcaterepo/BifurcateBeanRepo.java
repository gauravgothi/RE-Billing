package in.co.mpwin.rebilling.repositories.bifurcaterepo;

import in.co.mpwin.rebilling.beans.bifurcation.BifurcateBean;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public interface BifurcateBeanRepo extends CrudRepository<BifurcateBean,Long> {

    @Query(value ="SELECT * FROM ecell.re_bifurcated_reading Where hmeterno=:hMeterNumber AND hreading_date=:hReadingDate AND status=:status ORDER BY id ASC",nativeQuery = true)
    List<BifurcateBean> findAllByHMeterNumberAndHReadingDateAndStatus(
            @Param("hMeterNumber") String hMeterNumber,@Param("hReadingDate") Date hReadingDate,@Param("status") String status);

    @Query(value = "SELECT DISTINCT new in.co.mpwin.rebilling.beans.bifurcation.BifurcateBean(d.lInvestorCode,d.lInvestorName) FROM BifurcateBean d WHERE d.hDevUsername =:developerUsername",nativeQuery = false)
    List<BifurcateBean> findDistinctInvestorCodeByDeveloperUsername(@Param("developerUsername") String developerUsername);

    @Query(value = "SELECT COUNT(*)>0 FROM ecell.re_bifurcated_reading WHERE linv_code=:investorCode AND hmonth=:monthYear AND status=:status",nativeQuery = true)
    boolean isExistsInvestorInBifurcateBean(@Param("investorCode") String investorCode,@Param("monthYear") String monthYear,@Param("status") String status);

    @Query(value = "SELECT * FROM ecell.re_bifurcated_reading WHERE linv_code=:investorCode AND hmonth=:hmonth AND status=:status",nativeQuery = true)
    BifurcateBean findByLInvestorCodeAndHmonthAndStatus(@Param("investorCode") String lInvestorCode,@Param("hmonth") String hmonth,@Param("status") String status);

//    @Query(value = "SELECT DISTINCT (d.linv_code,d.linv_name) FROM ecell.re_bifurcated_reading d WHERE d.hdev_username =:developerUsername",nativeQuery = true)
//    List<Tuple> findDistinctInvestorCodeByDeveloperUsername(@Param("developerUsername") String developerUsername);

}
