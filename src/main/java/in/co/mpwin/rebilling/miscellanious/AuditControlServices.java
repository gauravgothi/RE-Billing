package in.co.mpwin.rebilling.miscellanious;

import in.co.mpwin.rebilling.beans.metermaster.MeterMasterBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuditControlServices {
    public void setInitialAuditControlParameters(Object obj) {

        if(obj instanceof MeterMasterBean){
            this.setInitialAuditControlParametersOfMeter((MeterMasterBean)obj);
        }
        else {

        }
    }

    public void setInitialAuditControlParametersOfMeter(MeterMasterBean meterMasterBean) {

            meterMasterBean.setCreatedOn(new DateMethods().getServerTime());
            meterMasterBean.setUpdatedOn(new DateMethods().getServerTime());
            meterMasterBean.setCreatedBy(new TokenInfo().getCurrentUsername());
            meterMasterBean.setUpdatedBy(new TokenInfo().getCurrentUsername());
            meterMasterBean.setStatus("active");
            meterMasterBean.setRemark("NA");
            meterMasterBean.setIsMapped("no");
    }

    public static void main(String arg[])
    {
        MeterMasterBean meterMasterBean = new MeterMasterBean();
        meterMasterBean.setMeterNumber("TESTER");
        new AuditControlServices().setInitialAuditControlParameters(meterMasterBean);
        System.out.println(meterMasterBean.getCreatedOn());
    }
}
