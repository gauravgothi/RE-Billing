package in.co.mpwin.rebilling.services.fileserivce;

import in.co.mpwin.rebilling.beans.xmlfilebean.XmlParserBean;
import in.co.mpwin.rebilling.beans.readingbean.MeterReadingBean;
import in.co.mpwin.rebilling.miscellanious.Message;
import in.co.mpwin.rebilling.miscellanious.UploadPath;
import in.co.mpwin.rebilling.services.readingservice.MeterReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileUploadService {

    @Autowired
    XmlSaxParserService xmlSaxParserService;
    @Autowired
    MeterReadingService meterReadingService;
    /*@Autowired
    MeterReadingBean meterReadingBean;
    @Autowired
    XmlParserBean xmlParserBean;*/
    public ResponseEntity<?> handleFileUpload(@RequestParam("xmlFile") MultipartFile xmlFile ) {

        ResponseEntity resp = null;

        String fileName = xmlFile.getOriginalFilename();
        String uploadedPath = UploadPath.createUploadedPath();
        try {

            //copy XML file into the Server
            Files.copy(xmlFile.getInputStream(), Paths.get(uploadedPath+fileName), StandardCopyOption.REPLACE_EXISTING);
            File copiedFile=new File(uploadedPath+fileName);


            if(copiedFile.exists()) {

                //call method to read the XML file parser
                byte[] bytes = xmlFile.getBytes();
                String xmlData = new String(bytes, "UTF-8");
                XmlParserBean xmlParserBean = xmlSaxParserService.parseXml(xmlData);

                if(xmlParserBean.getG1().equals(null))
                {
                    resp = new ResponseEntity<>(new Message("XML file is not in correct format"), HttpStatus.OK);
                }
                else {
                    //saving data into meter reading table
                    MeterReadingBean passMRB=xmlSaxParserService.convertXmlParserBeanToMeterReadingBean(xmlParserBean);

                    //Saving XML file data into Meterreading Table
                    MeterReadingBean meterReadingBean = meterReadingService.createMeterReading(passMRB);
                    if (meterReadingBean.getMeterNo().equals(null)) {
                        resp = new ResponseEntity<>(new Message("Reading data can't insert into table"), HttpStatus.OK);
                    } else {
                        resp = new ResponseEntity<>(new Message("File uploaded successfully"), HttpStatus.OK);
                    }
                }
            }
            else{
                resp = new ResponseEntity<>(new Message("File can't be saved"), HttpStatus.BAD_REQUEST);
            }


        }
        catch (DataIntegrityViolationException d)
        {
            Throwable rootCause = d.getRootCause();
            String msg=rootCause.getMessage().substring(0,rootCause.getMessage().indexOf("Detail:"));

            resp = new ResponseEntity<>(new Message(msg),HttpStatus.INTERNAL_SERVER_ERROR);
            return resp;
        }
        catch (Exception e) {
            resp = new ResponseEntity<>(new Message("Error in File uploading"),HttpStatus.INTERNAL_SERVER_ERROR);
            return resp;
        }
        return resp;
    }
}
