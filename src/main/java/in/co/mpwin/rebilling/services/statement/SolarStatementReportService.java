package in.co.mpwin.rebilling.services.statement;

import in.co.mpwin.rebilling.beans.statement.SolarStatementBean;
import in.co.mpwin.rebilling.services.readingservice.MeterReadingService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class SolarStatementReportService {
    private static final Logger logger = LoggerFactory.getLogger(SolarStatementReportService.class);

    public byte[] exportSolarStatement(String reportFormat, List<SolarStatementBean> solarStatementBeanList) throws FileNotFoundException, JRException {

        final String methodName = "exportSolarStatement() : ";
        logger.info(methodName + " called with parameters solarStatementBeanList={}",solarStatementBeanList);
        //Load file and compile it
        logger.info(methodName + " load file solarReport.jrxml");
        Resource resource = new ClassPathResource("solarReport.jrxml");
        File file = null;

        InputStream is = null;
        try {
            is = resource.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //File file = ResourceUtils.getFile("/resources/solarReport.jrxml");
        //File file1 = ResourceUtils.getFile("classpath:solarReport_todSubReport.jrxml");
        //File file2 = ResourceUtils.getFile("classpath:solarReport_todSubReport.jasper");

        logger.info(methodName + " loaded file solarReport.jrxml now compiling input stream");
        JasperReport jasperReport = JasperCompileManager.compileReport(is);
        //JasperCompileManager.compileReportToFile(file1.getAbsolutePath(), file2.getAbsolutePath());

        //InputStream subReportInputStream = new FileInputStream(file2);

        //JasperReport subJasperReport = (JasperReport) JRLoader.loadObject(subReportInputStream);
        //get the datasource
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(solarStatementBeanList);
        logger.info(methodName + " got the data source.");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("SolarBeanParam",dataSource);
        parameters.put("SUBREPORT_DIR","");

        logger.info(methodName + " filling the report.");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters,dataSource);

        if (reportFormat.equalsIgnoreCase("pdf")){
            logger.info(methodName + " exporting the report.");
            return JasperExportManager.exportReportToPdf(jasperPrint);
        }

        logger.error(methodName + "return blank file or wrong data provided.");
        return new byte[0];
    }
}
