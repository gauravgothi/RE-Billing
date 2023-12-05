package in.co.mpwin.rebilling.services.mapping;

import in.co.mpwin.rebilling.beans.invoice.InvoiceBean;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.AuditControlServices;
import in.co.mpwin.rebilling.repositories.mapping.InvestorPpwaMappingRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvestorPpwaMappingService {
    private static final Logger logger = LoggerFactory.getLogger(InvestorPpwaMappingService.class);

    @Autowired private InvestorPpwaMappingRepo investorPpwaMappingRepo;

    public List<String> getInvestorCodeListByPpwaNo(String ppwaNo,String status) {
        final String methodName = "getInvestorCodeListByPpwaNo() : ";
        logger.info(methodName + "called with parameters ppwaNo={}, status={}",ppwaNo,status);
        try {
                List<String> investorCodeList = investorPpwaMappingRepo.findAllInvestorCodeByPpwaNo(ppwaNo,status);
                if (investorCodeList.size() == 0)
                    throw new ApiException(HttpStatus.BAD_REQUEST,"No Investor present for give ppwa number "+ppwaNo);
            logger.info(methodName + " return with investorCodeList of size : {}",investorCodeList.size());
            return investorCodeList;
            } catch (ApiException apiException){
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

    public String getPpwaNoByInvestorCode(String investorCode,String status) {
        final String methodName = "getPpwaNoByInvestorCode() : ";
        logger.info(methodName + "called with parameters investorCode={}, status={}",investorCode,status);
        try {
            String ppwaNo = investorPpwaMappingRepo.findPpwaNoByInvestorCode(investorCode,status);
            if (ppwaNo == null) {
                logger.info(methodName + " return with ppwaNo : NA");
                return "NA";
            }
            logger.info(methodName + " return with ppwaNo : {}",ppwaNo);
            return ppwaNo;
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
}
