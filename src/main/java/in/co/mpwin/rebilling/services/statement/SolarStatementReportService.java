package in.co.mpwin.rebilling.services.statement;

import in.co.mpwin.rebilling.beans.statement.SolarStatementBean;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class SolarStatementReportService {

    public byte[] exportSolarStatement(String reportFormat, List<SolarStatementBean> solarStatementBeanList) throws FileNotFoundException, JRException {

        //Load file and compile it
        File file = ResourceUtils.getFile("classpath:solarReport.jrxml");
        //File file1 = ResourceUtils.getFile("classpath:solarReport_todSubReport.jrxml");
        //File file2 = ResourceUtils.getFile("classpath:solarReport_todSubReport.jasper");

        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        //JasperCompileManager.compileReportToFile(file1.getAbsolutePath(), file2.getAbsolutePath());

        //InputStream subReportInputStream = new FileInputStream(file2);

        //JasperReport subJasperReport = (JasperReport) JRLoader.loadObject(subReportInputStream);
        //get the datasource
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(solarStatementBeanList);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("SolarBeanParam",dataSource);
        parameters.put("SUBREPORT_DIR","");

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters,dataSource);

        if (reportFormat.equalsIgnoreCase("pdf")){
            return JasperExportManager.exportReportToPdf(jasperPrint);
        }

        return new byte[0];
    }
}
