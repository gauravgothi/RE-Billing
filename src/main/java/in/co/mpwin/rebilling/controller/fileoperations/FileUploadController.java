package in.co.mpwin.rebilling.controller.fileoperations;

import in.co.mpwin.rebilling.miscellanious.UploadPath;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
public class FileUploadController {
    @PostMapping("/upload")
    public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile file ) {

        ResponseEntity resp = null;

        String fileName = file.getOriginalFilename();
        String uploadedPath = UploadPath.createUploadedPath();
        try {
            System.out.println("uploadedPath + fileName : "+uploadedPath + fileName);
            file.transferTo( new File(uploadedPath + fileName));
            //resp = new ResponseEntity<>(uploadedPath + fileName, HttpStatus.OK);
            resp = new ResponseEntity<>("File uploaded successfully", HttpStatus.OK);
            //call method to read the XML file

        } catch (Exception e) {
            resp = new ResponseEntity<>("Can't upload the file",HttpStatus.INTERNAL_SERVER_ERROR);
            return resp;
        }
        return resp;
    }
}
