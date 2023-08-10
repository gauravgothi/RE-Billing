package in.co.mpwin.rebilling.services.fileoperations;

import in.co.mpwin.rebilling.beans.fileoperations.ParsedDataEntity;
import in.co.mpwin.rebilling.miscellanious.Message;
import in.co.mpwin.rebilling.miscellanious.UploadPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public class FileUploadService {

    public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile file ) {
    ResponseEntity resp = null;

    String fileName = file.getOriginalFilename();
    String uploadedPath = UploadPath.createUploadedPath();
        try {
        System.out.println("uploadedPath + fileName : "+uploadedPath + fileName);
        file.transferTo( new File(uploadedPath + fileName));
        //resp = new ResponseEntity<>(uploadedPath + fileName, HttpStatus.OK);
        System.out.println("isExist : "+file.getResource().exists());
        if(file.getResource().exists()) {
            resp = new ResponseEntity<>(new Message("File uploaded successfully"), HttpStatus.OK);

            //call method to read the XML file

        }
        else{
            resp = new ResponseEntity<>(new Message("File can't be uploaded"), HttpStatus.BAD_REQUEST);
        }


    } catch (Exception e) {
        resp = new ResponseEntity<>(new Message("Error in File uploading"),HttpStatus.INTERNAL_SERVER_ERROR);
        return resp;
    }
        return resp;
}
}
