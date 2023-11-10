package in.co.mpwin.rebilling.dto;

import in.co.mpwin.rebilling.beans.developermaster.DeveloperMasterBean;
import in.co.mpwin.rebilling.beans.feedermaster.FeederMasterBean;
import in.co.mpwin.rebilling.beans.investormaster.InvestorMasterBean;
import in.co.mpwin.rebilling.beans.machinemaster.MachineMasterBean;
import in.co.mpwin.rebilling.beans.plantmaster.PlantMasterBean;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InvestorMachineMappingDto {
    private DeveloperMasterBean developerMasterBean;
    private PlantMasterBean plantMasterBean;

    private InvestorMasterBean investorMasterBean;
    private List<MachineMasterBean> machineMasterBeanList;
}
