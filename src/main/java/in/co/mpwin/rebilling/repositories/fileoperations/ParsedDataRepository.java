package in.co.mpwin.rebilling.repositories.fileoperations;

import in.co.mpwin.rebilling.beans.fileoperations.ParsedDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParsedDataRepository extends JpaRepository<ParsedDataEntity, Long> {
}
