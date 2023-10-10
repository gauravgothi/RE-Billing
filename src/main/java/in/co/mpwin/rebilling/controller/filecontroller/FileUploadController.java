package in.co.mpwin.rebilling.controller.filecontroller;

import in.co.mpwin.rebilling.miscellanious.Message;
import in.co.mpwin.rebilling.services.fileserivce.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/xml")
@CrossOrigin(origins="*")
public class FileUploadController {
    @Autowired
    FileUploadService fus;

    @PostMapping("/parsed-data/save")
    public ResponseEntity<?> handleFileUpload(@RequestParam("xmlFile") MultipartFile file ) {

        ResponseEntity resp = null;
        try {
            resp=fus.handleFileUpload(file);
        }
        catch (Exception e) {
            resp = new ResponseEntity<>(new Message("Can't upload the file"),HttpStatus.INTERNAL_SERVER_ERROR);
            return resp;
        }
        return resp;
        /*
        String fileName = file.getOriginalFilename();
        String uploadedPath = UploadPath.createUploadedPath();
        try {
            /*System.out.println("uploadedPath + fileName : "+uploadedPath + fileName);
            file.transferTo( new File(uploadedPath + fileName));
            //resp = new ResponseEntity<>(uploadedPath + fileName, HttpStatus.OK);
            resp = new ResponseEntity<>("File uploaded successfully", HttpStatus.OK);
            //call method to read the XML file

        } catch (Exception e) {
            resp = new ResponseEntity<>("Can't upload the file",HttpStatus.INTERNAL_SERVER_ERROR);
            return resp;
        }
        return resp;
    */
    }
}
