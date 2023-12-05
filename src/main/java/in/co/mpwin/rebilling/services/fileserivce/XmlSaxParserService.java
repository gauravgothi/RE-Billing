package in.co.mpwin.rebilling.services.fileserivce;

import in.co.mpwin.rebilling.beans.readingbean.MeterReadingBean;
import in.co.mpwin.rebilling.beans.xmlfilebean.*;
import in.co.mpwin.rebilling.miscellanious.DateMethods;
import in.co.mpwin.rebilling.services.feedermaster.FeederMasterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class XmlSaxParserService {

    private static final Logger logger = LoggerFactory.getLogger(XmlSaxParserService.class);
    public XmlParserBean parseXml(String xmlData) throws ParserConfigurationException, SAXException, IOException {
        final String methodName = "parseXml() : ";
        logger.info(methodName + "called with parameters xmlData={}",xmlData);
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();

        XmlParserBean parsedData = new XmlParserBean();
        InputStream inputStream = new ByteArrayInputStream(xmlData.getBytes());

        DefaultHandler handler = new DefaultHandler() {
            StringBuilder elementValue = new StringBuilder();
            //private StringBuilder elementValue;
            DataEntityD1 currentDataEntityD1 = null;
            DataEntityD301 currentDataEntityD301 = null;
            B3Entity currentB3Entity = null;
            B4Entity currentB4Entity = null;
            B5Entity currentB5Entity = null;
            B6Entity currentB6Entity = null;

            boolean insideD301 = false;
            boolean insideD1 = false;

            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

                if ("D1".equals(qName)) {
                    //create object if list of objects to be parsed
                    insideD1 = true;
                    currentDataEntityD1 = new DataEntityD1();
                } else if ("D3-01".equals(qName)) {
                    insideD301 = true;
                    currentDataEntityD301 = new DataEntityD301();
                    currentDataEntityD301.setMechanism(attributes.getValue("MECHANISM"));
                    currentDataEntityD301.setDatetime(attributes.getValue("DATETIME"));
                } else if (insideD1 && "G22".equals(qName)) {
                    currentDataEntityD1.setG22Code(attributes.getValue("CODE"));
                    currentDataEntityD1.setG22Name(attributes.getValue("NAME"));
                } else if (insideD301 && "B3".equals(qName)) {
                    currentB3Entity = new B3Entity();
                    currentB3Entity.setValue(attributes.getValue("VALUE"));
                    currentB3Entity.setUnit(attributes.getValue("UNIT"));
                    currentB3Entity.setParamcode(attributes.getValue("PARAMCODE"));
                    currentDataEntityD301.addB3(currentB3Entity);
                } else if (insideD301 && "B4".equals(qName)) {
                    currentB4Entity = new B4Entity();
                    currentB4Entity.setValue(attributes.getValue("VALUE"));
                    currentB4Entity.setUnit(attributes.getValue("UNIT"));
                    currentB4Entity.setParamcode(attributes.getValue("PARAMCODE"));
                    currentB4Entity.setTod(attributes.getValue("TOD"));
                    currentDataEntityD301.addB4(currentB4Entity);
                } else if (insideD301 && "B5".equals(qName)) {
                    currentB5Entity = new B5Entity();
                    currentB5Entity.setValue(attributes.getValue("VALUE"));
                    currentB5Entity.setUnit(attributes.getValue("UNIT"));
                    currentB5Entity.setParamcode(attributes.getValue("PARAMCODE"));
                    currentB5Entity.setOccdate(attributes.getValue("OCCDATE"));
                    currentDataEntityD301.addB5(currentB5Entity);
                } else if (insideD301 && "B6".equals(qName)) {
                    currentB6Entity = new B6Entity();
                    currentB6Entity.setValue(attributes.getValue("VALUE"));
                    currentB6Entity.setUnit(attributes.getValue("UNIT"));
                    currentB6Entity.setParamcode(attributes.getValue("PARAMCODE"));
                    currentB6Entity.setTod(attributes.getValue("TOD"));
                    currentB6Entity.setOccdate(attributes.getValue("OCCDATE"));
                    currentDataEntityD301.addB6(currentB6Entity);
                }
            }

            @Override
            public void characters(char[] ch, int start, int length) throws SAXException {

                if (insideD1 || insideD301) {
                    elementValue.append(ch, start, length);
                }
            }

            @Override
            public void endElement(String uri, String localName, String qName) throws SAXException {
                String value = elementValue.toString().trim();
                if (insideD1) {
                    switch (qName) {
                        case "G1":
                            currentDataEntityD1.setG1(value);
                            System.out.println("setG1 : " + currentDataEntityD1.getG1());
                            break;
                        case "G2":
                            currentDataEntityD1.setG2(parseDate(value));
                            break;
                        case "G3":
                            currentDataEntityD1.setG3(parseDate(value));
                            break;
                        case "G4":
                            currentDataEntityD1.setG4(parseDate(value));
                            break;
                        case "G7":
                            currentDataEntityD1.setG7(Integer.parseInt(value));
                            break;
                        case "G8":
                            currentDataEntityD1.setG8(Integer.parseInt(value));
                            break;
                        case "G9":
                            currentDataEntityD1.setG9(new BigDecimal(value));
                            break;
                        case "G10":
                            currentDataEntityD1.setG10(new BigDecimal(value));
                            break;
                        case "G11":
                            currentDataEntityD1.setG11(new BigDecimal(value));
                            break;
                        case "G12":
                            currentDataEntityD1.setG12(new BigDecimal(value));
                            break;
                        case "G13":
                            currentDataEntityD1.setG13(value);
                            break;
                        case "G15":
                            currentDataEntityD1.setG15(value);
                            break;
                        case "G17":
                            currentDataEntityD1.setG17(value);
                            break;
                        case "G19":
                            currentDataEntityD1.setG19(Integer.parseInt(value));
                            break;
                        case "G20":
                            currentDataEntityD1.setG20(Integer.parseInt(value));
                            break;
                        case "G27":
                            currentDataEntityD1.setG27(value);
                            break;
                        case "G30":
                            currentDataEntityD1.setG30(new BigDecimal(value));
                            break;
                        case "G31":
                            currentDataEntityD1.setG31(value);
                            break;
                        case "G32":
                            currentDataEntityD1.setG32(Integer.parseInt(value));
                            break;
                        case "G33":
                            currentDataEntityD1.setG33(value);
                            break;
                        case "G1209":
                            currentDataEntityD1.setG1209(Integer.parseInt(value));
                            break;
                        case "G1210":
                            currentDataEntityD1.setG1210(parseDate(value));
                            break;
                        case "G1211":
                            currentDataEntityD1.setG1211(Integer.parseInt(value));
                            break;
                        case "G1212":
                            currentDataEntityD1.setG1212(Integer.parseInt(value));
                            break;
                        case "G1216":
                            currentDataEntityD1.setG1216(value);
                            break;
                        case "G1233":
                            currentDataEntityD1.setG1233(Integer.parseInt(value));
                            break;
                        case "G1219":
                            currentDataEntityD1.setG1219(Integer.parseInt(value));
                            break;
                        case "G1220":
                            currentDataEntityD1.setG1220(Integer.parseInt(value));
                            break;
                        case "G1221":
                            currentDataEntityD1.setG1221(Integer.parseInt(value));
                            break;
                        case "G1222":
                            currentDataEntityD1.setG1222(new BigDecimal(value));
                            break;
                        case "G1223":
                            currentDataEntityD1.setG1223(Integer.parseInt(value));
                            break;
                        case "G1236":
                            currentDataEntityD1.setG1236(value);
                            break;
                        case "G1237":
                            currentDataEntityD1.setG1237(Integer.parseInt(value));
                            break;
                        case "G1239":
                            currentDataEntityD1.setG1239(Integer.parseInt(value));
                            break;
                        case "G1240":
                            currentDataEntityD1.setG1240(Integer.parseInt(value));
                            break;
                        case "D1":
                            insideD1 = false;
                            // Add the EntityD1 to the object
                            parsedData.setDataEntityD1(currentDataEntityD1);
                            currentDataEntityD1 = null;
                            break;
                    }
                    elementValue.setLength(0);
                } else if (insideD301) {
                    switch (qName){
                        case "D3-01":
                            insideD301 = false;
                            parsedData.setDataEntityD301(currentDataEntityD301);
                            currentDataEntityD301 = null;
                            break;
                        case "B3":
                            currentDataEntityD301.addB3(currentB3Entity);
                            break;
                        case "B4":
                            currentDataEntityD301.addB4(currentB4Entity);
                            break;
                        case "B5":
                            currentDataEntityD301.addB5(currentB5Entity);
                            break;
                        case "B6":
                            currentDataEntityD301.addB6(currentB6Entity);
                            break;
                    }

                }
            }

//            @Override
//            public void endDocument() throws SAXException {
//                // Add the parsed DataEntity to the list
//                if (currentDataEntity != null) {
//                    dataList.add(currentDataEntity);
//                }
//            }

                private Date parseDate (String dateStr){
                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                        return dateFormat.parse(dateStr);
                    } catch (Exception e) {
                        return null;
                    }
                }


        };

        try{
                saxParser.parse(inputStream, handler);
            }catch(SAXParseException e) {
            logger.error(methodName+" throw SAXParseException");
                throw e;
            }catch(SAXException e) {
            logger.error(methodName+" throw SAXException");
                throw e;
            }catch(IOException e) {
            logger.error(methodName+" throw IOException");
                throw e;
            } catch(Exception e) {
            logger.error(methodName+" throw Exception");
                throw e;
            }

        logger.info(methodName + " return with XmlParserBean parsedData");
        return parsedData;
    }

        public MeterReadingBean convertXmlParserBeanToMeterReadingBean (XmlParserBean xmlParserBean) throws ParseException {
            final String methodName = "convertXmlParserBeanToMeterReadingBean() : ";
            logger.info(methodName + "called with parameters xmlParserBean={}", xmlParserBean);
            MeterReadingBean meterReadingBean = new MeterReadingBean();
            try {
                meterReadingBean.setMeterNo(xmlParserBean.getDataEntityD1().getG1());//1
                meterReadingBean.setMf(new BigDecimal(0.00));//2
                meterReadingBean.setReadingDate(new SimpleDateFormat("dd-MM-yyyy")
                        .parse(xmlParserBean.getDataEntityD301().getDatetime()));//3
                //make end date by -1 day in reading date
                meterReadingBean.setEndDate(new DateMethods().getOneDayBefore(meterReadingBean.getReadingDate()));//4
                meterReadingBean.setReadingType("NR");//5
                meterReadingBean.setReadSource("file");//6

                for (B3Entity b3Entity : xmlParserBean.getDataEntityD301().getB3Entities())
                {
                    switch (b3Entity.getParamcode()){
                        case "P7-1-6-1-0":
                            meterReadingBean.setEActiveEnergy(new BigDecimal(b3Entity.getValue()));
                            break;
                        case "P7-1-5-1-0":
                            meterReadingBean.setIActiveEnergy(new BigDecimal(b3Entity.getValue()));
                            break;
                        case "P7-3-5-0-0":
                            meterReadingBean.setIKvah(new BigDecimal(b3Entity.getValue()));
                            break;
                        case "P7-3-6-0-0":
                            meterReadingBean.setEKvah(new BigDecimal(b3Entity.getValue()));
                            break;
                        case "P7-2-1-0-0":
                            meterReadingBean.setIReactiveQuad1(new BigDecimal(b3Entity.getValue()));
                            break;
                        case "P7-2-4-0-0":
                            meterReadingBean.setIReactiveQuad2(new BigDecimal(b3Entity.getValue()));
                            break;
                        case "P7-2-2-0-0":
                            meterReadingBean.setIReactiveQuad3(new BigDecimal(b3Entity.getValue()));
                            break;
                        case "P7-2-3-0-0":
                            meterReadingBean.setIReactiveQuad4(new BigDecimal(b3Entity.getValue()));
                            break;
                    }
                }

                for (B4Entity b4Entity : xmlParserBean.getDataEntityD301().getB4Entities())
                {
                    switch (b4Entity.getParamcode()){
                        case "P7-1-5-1-0":
                            if (b4Entity.getTod().equals("1"))
                                meterReadingBean.setITod1(new BigDecimal(b4Entity.getValue()));
                            else if (b4Entity.getTod().equals("2"))
                                meterReadingBean.setITod2(new BigDecimal(b4Entity.getValue()));
                            else if (b4Entity.getTod().equals("3"))
                                meterReadingBean.setITod3(new BigDecimal(b4Entity.getValue()));
                            else if (b4Entity.getTod().equals("4"))
                                meterReadingBean.setITod4(new BigDecimal(b4Entity.getValue()));
                            break;
                        case "P7-1-6-1-0":
                            if (b4Entity.getTod().equals("1"))
                                meterReadingBean.setETod1(new BigDecimal(b4Entity.getValue()));
                            else if (b4Entity.getTod().equals("2"))
                                meterReadingBean.setETod2(new BigDecimal(b4Entity.getValue()));
                            else if (b4Entity.getTod().equals("3"))
                                meterReadingBean.setETod3(new BigDecimal(b4Entity.getValue()));
                            else if (b4Entity.getTod().equals("4"))
                                meterReadingBean.setETod4(new BigDecimal(b4Entity.getValue()));
                            break;

                    }
                }

                for (B5Entity b5Entity : xmlParserBean.getDataEntityD301().getB5Entities())
                {
                    switch (b5Entity.getParamcode()){
                        case "P7-6-5-0-0":
                            meterReadingBean.setIMaxDemand(new BigDecimal(b5Entity.getValue()));
                            break;
                        case "P7-6-6-0-0":
                            meterReadingBean.setEMaxDemand(new BigDecimal(b5Entity.getValue()));
                            break;

                    }
                }

                meterReadingBean.setEReactiveQuad1(new BigDecimal(0.00));//12
                meterReadingBean.setEReactiveQuad2(new BigDecimal(0.00));//13
                meterReadingBean.setEReactiveQuad3(new BigDecimal(0.00));//14
                meterReadingBean.setEReactiveQuad4(new BigDecimal(0.00));//15
                meterReadingBean.setEAdjustment(new BigDecimal(0.00));//16
                meterReadingBean.setIAdjustment(new BigDecimal(0.00));//17
                meterReadingBean.setEAssesment(new BigDecimal(0.00));//18
                meterReadingBean.setIAssesment(new BigDecimal(0.00));//19


                meterReadingBean.setCurrentState("initial_read");//33
                meterReadingBean.setStatus("active");//38
                meterReadingBean.setRemark("NA");//39
            } catch (Exception e) {
                logger.error(methodName+" throw Exception");
                throw e;
            }
            logger.info(methodName + " return with meterReadingBean : {}", meterReadingBean);
            return meterReadingBean;
        }
}
