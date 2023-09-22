package in.co.mpwin.rebilling.repositories.developermaster;

import in.co.mpwin.rebilling.beans.developermaster.DeveloperMasterBean;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DeveloperMasterRepo extends CrudRepository<DeveloperMasterBean,Long> {

    List<DeveloperMasterBean> findAllByStatus(String status);
    List<DeveloperMasterBean> findByLocationIdAndStatus(String locationId,String status);

    DeveloperMasterBean save(DeveloperMasterBean developerMasterBean);

    DeveloperMasterBean findByIdAndStatus(Long id, String status);

    @Query(value = "SELECT d.id FROM ecell.re_developer_master AS d WHERE d.developer_username=:username AND d.status='active'",nativeQuery = true)
    Long findIdByDeveloperUsername(@Param("username") String username);
}
