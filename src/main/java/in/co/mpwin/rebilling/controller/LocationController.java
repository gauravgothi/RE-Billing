package in.co.mpwin.rebilling.controller;

import in.co.mpwin.rebilling.beans.LocationMaster;
import in.co.mpwin.rebilling.beans.metermaster.MeterPtr;
import in.co.mpwin.rebilling.services.LocationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/location")
@CrossOrigin(origins="*")
public class LocationController {

    @Autowired
    LocationService locationService;

    @Autowired
    private ModelMapper mapper;

    @RequestMapping(method= RequestMethod.GET,value="")
    public ResponseEntity<LocationMaster> getAllLocationMaster()    {
        String status = "0";
        ResponseEntity locationResp = null;
        try{
            List<LocationMaster> locationList = locationService.findAllByStatus(status);
            locationResp = locationResponse(new HashSet<>(locationList));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return locationResp;
    }

    @RequestMapping(method= RequestMethod.GET,value="/region")
    public ResponseEntity<LocationMaster> getRegions()    {
        String status = "0";
        ResponseEntity regionResp = null;
        try{
            List<LocationMaster> regionList = locationService.findDistinctRegion(status);

            Set<String> regions = new HashSet<>();
            for (LocationMaster l:regionList){
                regions.add(l.getRegion());
            }
            regionResp = locationResponse(regions);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return regionResp;
    }

    @RequestMapping(method= RequestMethod.GET,value="/region/{region}")
    public ResponseEntity<LocationMaster> getDistinctCircleByRegionAndStatus(@PathVariable("region") String region)    {
        String status = "0";
        ResponseEntity circleResp = null;
        try{
            List<LocationMaster> circleList = locationService.getDistinctCircleByRegionAndStatus(region, status);

            Set<String> circles = new HashSet<>();
            for (LocationMaster l:circleList){
                circles.add(l.getCircle());
            }
            circleResp = locationResponse(circles);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return circleResp;
    }

    @RequestMapping(method= RequestMethod.GET,value="/circle/{circle}")
    public ResponseEntity<LocationMaster> getDistinctDivisionByCircleAndStatus(@PathVariable("circle") String circle)    {
        String status = "0";
        ResponseEntity divisionResp = null;
        try{
            List<LocationMaster> divisionList = locationService.getDistinctDivisionByCircleAndStatus(circle, status);

            Set<String> divisions = new HashSet<>();
            for (LocationMaster l:divisionList){
                divisions.add(l.getDivision());
            }

            divisionResp = locationResponse(divisions);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return divisionResp;
    }

    @RequestMapping(method= RequestMethod.GET,value="/division/{division}")
    public ResponseEntity<LocationMaster> getDistinctDcNameByDivisionAndStatus(@PathVariable("division") String division)    {
        String status = "0";
        ResponseEntity dcNameResp = null;
        try{
            List<LocationMaster> dcNameList = locationService.getDistinctDcNameByDivisionAndStatus(division, status);

            Set<String> dcNames = new HashSet<>();
            for (LocationMaster l:dcNameList){
                dcNames.add(l.getDcName());
            }

            dcNameResp = locationResponse(dcNames);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dcNameResp;
    }

    private ResponseEntity<?> locationResponse(Set<?> inputList)  {
        ResponseEntity response = null;
        if(!inputList.isEmpty())
        {
            response = new ResponseEntity<>(inputList, HttpStatus.OK);
        }else if(inputList.isEmpty())
        {
            response =new ResponseEntity<>(inputList + " is not present under selected item ",HttpStatus.NO_CONTENT);
        } else
        {
            response=new ResponseEntity<>("Invalid Request",HttpStatus.BAD_REQUEST);
        }

        return response;
    }


}
