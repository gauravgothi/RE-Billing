package in.co.mpwin.rebilling.beans.thirdparty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeveloperPlantDto {

    private String developerId;
    private String developerName;

    private String developerType;

    private String plantCode;
    private String plantName;
    private String plantCapacity;
    private String siteLocation;
    private String plantRegion;
    private String plantCircle;
    private String plantDivision;
    private String plantCommissionDate;
    private String mfpId;
    private String mainMeterNo;
    private String checkMeterNo;
    private String standbyMeterNo;
    private  String feederNumber;
    private String feederInjectingSubstationName;
    private String feederCircuitVoltage;
}
