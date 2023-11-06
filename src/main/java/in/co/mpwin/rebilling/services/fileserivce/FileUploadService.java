package in.co.mpwin.rebilling.services.fileserivce;

import in.co.mpwin.rebilling.beans.xmlfilebean.XmlParserBean;
import in.co.mpwin.rebilling.beans.readingbean.MeterReadingBean;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
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
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;

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
    public ResponseEntity<?> handleFileUpload(MultipartFile xmlFile ) throws IOException, ParserConfigurationException, ParseException, SAXException {

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

                if(xmlParserBean.getDataEntityD1().getG1().equals(null))
                {
                    resp = new ResponseEntity<>(new Message("XML file is not in correct format"), HttpStatus.BAD_REQUEST);
                }
                else {
                    //saving data into meter reading entity or bean
                    MeterReadingBean passMRB=xmlSaxParserService.convertXmlParserBeanToMeterReadingBean(xmlParserBean);

                    //Saving XML file data into Meterreading Table
                    MeterReadingBean meterReadingBean = meterReadingService.createMeterReading(passMRB);
                    if (meterReadingBean.getMeterNo().equals(null)) {
                        resp = new ResponseEntity<>(new Message("Reading data can't insert into table"), HttpStatus.BAD_REQUEST);
                    } else {
                        resp = new ResponseEntity<>(new Message("File uploaded successfully"), HttpStatus.OK);
                    }
                }
            }
            else{
                resp = new ResponseEntity<>(new Message("File can't be saved"), HttpStatus.BAD_REQUEST);
            }


        }catch (ApiException apiException){
            throw apiException;
        }catch (IOException ioException)
        {   throw ioException;
        }
        catch (DataIntegrityViolationException d)
        {
            throw d;
        }
        catch (Exception e) {
            throw e;
        }
        return resp;
    }
}
