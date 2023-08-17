package in.co.mpwin.rebilling.miscellanious;

import in.co.mpwin.rebilling.beans.developermaster.DeveloperMasterBean;
import in.co.mpwin.rebilling.beans.feedermaster.FeederMasterBean;
import in.co.mpwin.rebilling.beans.machinemaster.MachineMasterBean;
import in.co.mpwin.rebilling.beans.mapping.InvestorMachineMappingBean;
import in.co.mpwin.rebilling.beans.mapping.MeterFeederPlantMappingBean;
import in.co.mpwin.rebilling.beans.metermaster.MeterMasterBean;
import in.co.mpwin.rebilling.beans.plantmaster.PlantMasterBean;
import in.co.mpwin.rebilling.services.plantmaster.PlantMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import in.co.mpwin.rebilling.beans.investormaster.InvestorMasterBean;

import org.springframework.stereotype.Service;

@Service
public class AuditControlServices {
    public void setInitialAuditControlParameters(Object obj) {

        if(obj instanceof MeterMasterBean){
            this.setInitialAuditControlParametersOfMeter((MeterMasterBean)obj);
        }
        else if(obj instanceof FeederMasterBean){
            this.setInitialAuditControlParametersOfFeeder((FeederMasterBean)obj);

        } else if(obj instanceof PlantMasterBean)
        {
            this.setInitialAuditControlParametersOfPlant((PlantMasterBean)obj);
        }else if(obj instanceof MachineMasterBean)
        {
            this.setInitialAuditControlParametersOfMachine((MachineMasterBean)obj);

        } else if (obj instanceof DeveloperMasterBean) {
            this.setInitialAuditControlParametersOfDeveloper((DeveloperMasterBean)obj);

        }else if (obj instanceof InvestorMasterBean) {
            this.setInitialAuditControlParametersOfInvestor((InvestorMasterBean)obj);

        } else if(obj instanceof InvestorMachineMappingBean){
            this.setInitialAuditControlParametersOfInvestor((InvestorMachineMappingBean)obj);
        } else if(obj instanceof MeterFeederPlantMappingBean){
            this.setInitialAuditControlParametersOfInvestor((MeterFeederPlantMappingBean)obj);
        }

    }




    private void setInitialAuditControlParametersOfInvestor(MeterFeederPlantMappingBean meterFeederPlantMappingBean) {
        meterFeederPlantMappingBean.setCreatedOn(new DateMethods().getServerTime());
        meterFeederPlantMappingBean.setUpdatedOn(new DateMethods().getServerTime());
        meterFeederPlantMappingBean.setCreatedBy(new TokenInfo().getCurrentUsername());
        meterFeederPlantMappingBean.setUpdatedBy(new TokenInfo().getCurrentUsername());
        meterFeederPlantMappingBean.setStatus("active");
        meterFeederPlantMappingBean.setRemark("NA");
    }
    private void setInitialAuditControlParametersOfInvestor(InvestorMachineMappingBean investorMachineMappingBean) {
        investorMachineMappingBean.setCreatedOn(new DateMethods().getServerTime());
        investorMachineMappingBean.setUpdatedOn(new DateMethods().getServerTime());
        investorMachineMappingBean.setCreatedBy(new TokenInfo().getCurrentUsername());
        investorMachineMappingBean.setUpdatedBy(new TokenInfo().getCurrentUsername());
        investorMachineMappingBean.setStatus("active");
        investorMachineMappingBean.setRemark("NA");
    }

    private void setInitialAuditControlParametersOfMachine(MachineMasterBean machineMasterBean) {
        machineMasterBean.setCreatedOn(new DateMethods().getServerTime());
        machineMasterBean.setUpdatedOn(new DateMethods().getServerTime());
        machineMasterBean.setCreatedBy(new TokenInfo().getCurrentUsername());
        machineMasterBean.setUpdatedBy(new TokenInfo().getCurrentUsername());
        machineMasterBean.setStatus("active");
        machineMasterBean.setRemark("NA");

    }

    private void setInitialAuditControlParametersOfPlant(PlantMasterBean plantMasterBean) {
        plantMasterBean.setCreatedOn(new DateMethods().getServerTime());
        plantMasterBean.setUpdatedOn(new DateMethods().getServerTime());
        plantMasterBean.setCreatedBy(new TokenInfo().getCurrentUsername());
        plantMasterBean.setUpdatedBy(new TokenInfo().getCurrentUsername());
        plantMasterBean.setStatus("active");
        plantMasterBean.setRemark("NA");
    }

    private void setInitialAuditControlParametersOfInvestor(InvestorMasterBean investorMasterBean) {
        investorMasterBean.setCreatedOn(new DateMethods().getServerTime());
        investorMasterBean.setUpdatedOn(new DateMethods().getServerTime());
        investorMasterBean.setCreatedBy(new TokenInfo().getCurrentUsername());
        investorMasterBean.setUpdatedBy(new TokenInfo().getCurrentUsername());
        investorMasterBean.setStatus("active");
        investorMasterBean.setRemark("NA");
    }

    private void setInitialAuditControlParametersOfDeveloper(DeveloperMasterBean developerMasterBean) {
        developerMasterBean.setCreatedOn(new DateMethods().getServerTime());
        developerMasterBean.setUpdatedOn(new DateMethods().getServerTime());
        developerMasterBean.setCreatedBy(new TokenInfo().getCurrentUsername());
        developerMasterBean.setUpdatedBy(new TokenInfo().getCurrentUsername());
        developerMasterBean.setStatus("active");
        developerMasterBean.setRemark("NA");
    }

    private void setInitialAuditControlParametersOfFeeder(FeederMasterBean feederMasterBean) {
        feederMasterBean.setCreatedOn(new DateMethods().getServerTime());
        feederMasterBean.setUpdatedOn(new DateMethods().getServerTime());
        feederMasterBean.setCreatedBy(new TokenInfo().getCurrentUsername());
        feederMasterBean.setUpdatedBy(new TokenInfo().getCurrentUsername());
        feederMasterBean.setStatus("active");
        feederMasterBean.setRemark("NA");
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
//        MeterMasterBean meterMasterBean = new MeterMasterBean();
//        meterMasterBean.setMeterNumber("TESTER");
//        new AuditControlServices().setInitialAuditControlParameters(meterMasterBean);
//        System.out.println(meterMasterBean.getCreatedOn());


//        PlantMasterService plantMasterService = new PlantMasterService();
//        System.out.println("max id of plant table is ="+plantMasterService.getMaxId());
        //System.out.println("max id of plant table is =" + plantMasterService.getAllPlantMasterBean("active"));
    }
}
