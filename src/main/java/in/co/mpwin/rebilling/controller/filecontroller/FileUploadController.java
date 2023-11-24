package in.co.mpwin.rebilling.controller.filecontroller;

import in.co.mpwin.rebilling.controller.developermaster.DeveloperMasterController;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.Message;
import in.co.mpwin.rebilling.services.fileserivce.FileUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.io.IOException;

@RestController
@RequestMapping("/xml")
@CrossOrigin(origins="*")
public class FileUploadController {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);
    @Autowired
    FileUploadService fus;

    @PostMapping("/parsed-data/save")
    public ResponseEntity<?> handleFileUpload(@RequestParam("xmlFile") MultipartFile file ) throws IOException {
        final String methodName = "handleFileUpload() : ";
        logger.info(methodName + "called. with parameters MultipartFile file name: {}",file.getOriginalFilename());
        ResponseEntity resp = null;
        try {
            resp=fus.handleFileUpload(file);
            logger.info(methodName + "return. handleFileUpload(file) response: {}",resp.getBody());
        }catch (ApiException apiException){
            resp = new ResponseEntity<>(new Message(apiException.getMessage()),apiException.getHttpStatus());
            logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
        }catch(SAXParseException saxParseException) {
            resp = new ResponseEntity<>(new Message(saxParseException.getMessage()),HttpStatus.BAD_REQUEST);
            logger.error(methodName+" SAXParseException Exception occurred: {}", saxParseException.getMessage());
        }catch(SAXException saxException) {
            resp = new ResponseEntity<>(new Message(saxException.getMessage()),HttpStatus.BAD_REQUEST);
            logger.error(methodName+" SAXException Exception occurred: {}", saxException.getMessage());
        }catch (IOException ioException) {
            resp = new ResponseEntity(new Message(ioException.getMessage()),HttpStatus.BAD_REQUEST);
            logger.error(methodName+"IOException Exception occurred: {}", ioException.getMessage());
        } catch (DataIntegrityViolationException d) {
            resp = new ResponseEntity<>(new Message(d.getMessage()),HttpStatus.BAD_REQUEST);
            logger.error(methodName+"Data Integrity Violation Exception occurred: {}", d.getMessage());
        } catch (Exception e) {
            resp = new ResponseEntity<>(new Message(e.getMessage()),HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);
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
