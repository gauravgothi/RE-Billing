package in.co.mpwin.rebilling.dto;

import in.co.mpwin.rebilling.beans.developermaster.DeveloperMasterBean;
import in.co.mpwin.rebilling.beans.feedermaster.FeederMasterBean;
import in.co.mpwin.rebilling.beans.investormaster.InvestorMasterBean;
import in.co.mpwin.rebilling.beans.machinemaster.MachineMasterBean;
import in.co.mpwin.rebilling.beans.metermaster.MeterMasterBean;
import in.co.mpwin.rebilling.beans.plantmaster.PlantMasterBean;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CompleteMappingDto {

    private String meterNumber;
    private String mainMeterNumber;
    private String checkMeterNumber;
    private String standbyMeterNumber;
    //private MeterMasterBean meterMasterBean;
    private FeederMasterBean feederMasterBean;
    private DeveloperMasterBean developerMasterBean;
    private PlantMasterBean plantMasterBean;
    private List<InvestorMasterBean> investorMasterBeanList;
    private List<Map<String,List<MachineMasterBean>>> machinesOfInvestors;

}
