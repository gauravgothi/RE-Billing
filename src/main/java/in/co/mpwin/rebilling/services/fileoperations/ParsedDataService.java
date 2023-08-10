package in.co.mpwin.rebilling.services.fileoperations;

import in.co.mpwin.rebilling.beans.fileoperations.ParsedDataEntity;
import in.co.mpwin.rebilling.repositories.fileoperations.ParsedDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ParsedDataService {
    private final ParsedDataRepository parsedDataRepository;
    private final XmlSaxParserService xmlSaxParserService;

    @Autowired
    public ParsedDataService(ParsedDataRepository parsedDataRepository, XmlSaxParserService xmlSaxParserService) {
        this.parsedDataRepository = parsedDataRepository;
        this.xmlSaxParserService = xmlSaxParserService;
    }

    @Transactional
    public void saveParsedDataFromXml(String xmlData) {
        try {
            ParsedDataEntity parsedData = xmlSaxParserService.parseXml(xmlData);
//            System.out.println("Test data printed "+parsedData.getG1());
            parsedDataRepository.save(parsedData);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception or log the error as needed
        }
    }
}
