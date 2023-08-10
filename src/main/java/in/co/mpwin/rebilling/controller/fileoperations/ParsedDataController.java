package in.co.mpwin.rebilling.controller.fileoperations;

import in.co.mpwin.rebilling.miscellanious.Message;
import in.co.mpwin.rebilling.services.fileoperations.ParsedDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/xml")
public class ParsedDataController {
    private final ParsedDataService parsedDataService;

    @Autowired
    public ParsedDataController(ParsedDataService parsedDataService) {
        this.parsedDataService = parsedDataService;
    }

    @PostMapping("/parsed-data/save")
    public ResponseEntity<?> saveParsedData(@RequestParam("xmlFile") List<MultipartFile> xmlFileList) {
        int listSize = xmlFileList.size();
        List<Message> responseList = new ArrayList<>();
        for (int i = 0; listSize > i ; i++) {
            try {
                // Read bytes from the MultipartFile's InputStream
                byte[] bytes = xmlFileList.get(i).getBytes();
                // Create a String from the byte array using the appropriate encoding
                // For example, using UTF-8 encoding:
                String xmlData = new String(bytes, "UTF-8");
                parsedDataService.saveParsedDataFromXml(xmlData);
                responseList.add(i,new Message(xmlFileList.get(i).getOriginalFilename() + " : " + " Data successfully saved to the database."));
                //responseList.add(i, ResponseEntity.status(HttpStatus.CREATED).body((xmlFileList.get(i).getOriginalFilename()) + " : " + "Data successfully saved to the database."));
            } catch (Exception e) {
                e.printStackTrace();
                responseList.add(i,new Message(xmlFileList.get(i).getOriginalFilename() + " : " + " Error saving data to the database."));
                //responseList.add(i, ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body((xmlFileList.get(i).getOriginalFilename()) + " : " + "Error saving data to the database."));
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving data to the database.");
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }
}
