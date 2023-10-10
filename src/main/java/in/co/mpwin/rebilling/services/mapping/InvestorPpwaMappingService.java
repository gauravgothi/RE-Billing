package in.co.mpwin.rebilling.services.mapping;

import in.co.mpwin.rebilling.beans.invoice.InvoiceBean;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.AuditControlServices;
import in.co.mpwin.rebilling.repositories.mapping.InvestorPpwaMappingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvestorPpwaMappingService {

    @Autowired private InvestorPpwaMappingRepo investorPpwaMappingRepo;

    public List<String> getInvestorCodeListByPpwaNo(String ppwaNo,String status) {
        try {
                List<String> investorCodeList = investorPpwaMappingRepo.findAllInvestorCodeByPpwaNo(ppwaNo,status);
                if (investorCodeList.size() == 0)
                    throw new ApiException(HttpStatus.BAD_REQUEST,"No Investor present for give ppwa number "+ppwaNo);
            return investorCodeList;
        }catch (ApiException apiException){
            throw apiException;
        }catch (DataIntegrityViolationException d){
            throw d;
        }catch (Exception e){
            throw e;
        }
    }

    public String getPpwaNoByInvestorCode(String investorCode,String status) {
        try {
            String ppwaNo = investorPpwaMappingRepo.findPpwaNoByInvestorCode(investorCode,status);
            if (ppwaNo == null)
                return "NA";
            return ppwaNo;
        }catch (ApiException apiException){
            throw apiException;
        }catch (DataIntegrityViolationException d){
            throw d;
        }catch (Exception e){
            throw e;
        }
    }
}
