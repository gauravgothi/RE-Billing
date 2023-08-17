package in.co.mpwin.rebilling.services.fileserivce;

import in.co.mpwin.rebilling.beans.readingbean.MeterReadingBean;
import in.co.mpwin.rebilling.beans.xmlfilebean.XmlParserBean;
import org.springframework.stereotype.Service;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class XmlSaxParserService {

    public XmlParserBean parseXml(String xmlData) throws ParserConfigurationException, SAXException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();

        XmlParserBean parsedData = new XmlParserBean();
        InputStream inputStream = new ByteArrayInputStream(xmlData.getBytes());

        DefaultHandler handler = new DefaultHandler() {
            private StringBuilder elementValue;

            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                elementValue = new StringBuilder();

                if ("D1".equals(qName)) {
                    //create object if list of objects to be parsed
                } else if ("G22".equals(qName)) {
                    parsedData.setG22Code(attributes.getValue("CODE"));
                    parsedData.setG22Name(attributes.getValue("NAME"));
                }
            }

            @Override
            public void characters(char[] ch, int start, int length) throws SAXException {
                elementValue.append(ch, start, length);
            }

            @Override
            public void endElement(String uri, String localName, String qName) throws SAXException {
                String value = elementValue.toString().trim();

                switch (qName) {
                    case "G1":
                        parsedData.setG1(value);
                        System.out.println("setG1 : "+parsedData.getG1());
                        break;
                    case "G2":
                        parsedData.setG2(parseDate(value));
                        break;
                    case "G3":
                        parsedData.setG3(parseDate(value));
                        break;
                    case "G4":
                        parsedData.setG4(parseDate(value));
                        break;
                    case "G7":
                        parsedData.setG7(Integer.parseInt(value));
                        break;
                    case "G8":
                        parsedData.setG8(Integer.parseInt(value));
                        break;
                    case "G9":
                        parsedData.setG9(Double.parseDouble(value));
                        break;
                    case "G10":
                        parsedData.setG10(Double.parseDouble(value));
                        break;
                    case "G11":
                        parsedData.setG11(Double.parseDouble(value));
                        break;
                    case "G12":
                        parsedData.setG12(Double.parseDouble(value));
                        break;
                    case "G13":
                        parsedData.setG13(value);
                        break;
                    case "G15":
                        parsedData.setG15(value);
                        break;
                    case "G17":
                        parsedData.setG17(value);
                        break;
                    case "G19":
                        parsedData.setG19(Integer.parseInt(value));
                        break;
                    case "G20":
                        parsedData.setG20(Integer.parseInt(value));
                        break;
                    case "G27":
                        parsedData.setG27(value);
                        break;
                    case "G30":
                        parsedData.setG30(Double.parseDouble(value));
                        break;
                    case "G31":
                        parsedData.setG31(value);
                        break;
                    case "G32":
                        parsedData.setG32(Integer.parseInt(value));
                        break;
                    case "G33":
                        parsedData.setG33(value);
                        break;
                    case "G1209":
                        parsedData.setG1209(Integer.parseInt(value));
                        break;
                    case "G1210":
                        parsedData.setG1210(parseDate(value));
                        break;
                    case "G1211":
                        parsedData.setG1211(Integer.parseInt(value));
                        break;
                    case "G1212":
                        parsedData.setG1212(Integer.parseInt(value));
                        break;
                    case "G1216":
                        parsedData.setG1216(value);
                        break;
                    case "G1233":
                        parsedData.setG1233(Integer.parseInt(value));
                        break;
                    case "G1219":
                        parsedData.setG1219(Integer.parseInt(value));
                        break;
                    case "G1220":
                        parsedData.setG1220(Integer.parseInt(value));
                        break;
                    case "G1221":
                        parsedData.setG1221(Integer.parseInt(value));
                        break;
                    case "G1222":
                        parsedData.setG1222(Double.parseDouble(value));
                        break;
                    case "G1223":
                        parsedData.setG1223(Integer.parseInt(value));
                        break;
                    case "G1236":
                        parsedData.setG1236(value);
                        break;
                    case "G1237":
                        parsedData.setG1237(Integer.parseInt(value));
                        break;
                    case "G1239":
                        parsedData.setG1239(Integer.parseInt(value));
                        break;
                    case "G1240":
                        parsedData.setG1240(Integer.parseInt(value));
                        break;
                    case "D1":
                        //add method if list of object
                        break;
                }
            }

            private Date parseDate(String dateStr) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    return dateFormat.parse(dateStr);
                } catch (Exception e) {
                    return null;
                }
            }
        };

        try {
            saxParser.parse(inputStream, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return parsedData;
    }

    public MeterReadingBean convertXmlParserBeanToMeterReadingBean(XmlParserBean xmlParserBean)
    {
        MeterReadingBean meterReadingBean=new MeterReadingBean();
        try {
            meterReadingBean.setMeterNo(xmlParserBean.getG1());//1
            meterReadingBean.setMf(xmlParserBean.getG9());//2
            meterReadingBean.setReadingDate(xmlParserBean.getG2());//3
            meterReadingBean.setEndDate(xmlParserBean.getG2());//4
            meterReadingBean.setReadingType("NR");//5
            meterReadingBean.setReadSource("file");//6

            meterReadingBean.setETod1(xmlParserBean.getG9());//7
            meterReadingBean.setETod2(xmlParserBean.getG9());//8
            meterReadingBean.setETod3(xmlParserBean.getG9());//9
            meterReadingBean.setETod4(xmlParserBean.getG9());//10
            meterReadingBean.setEActiveEnergy(xmlParserBean.getG9());//11
            meterReadingBean.setEReactiveQuad1(xmlParserBean.getG9());//12
            meterReadingBean.setEReactiveQuad2(xmlParserBean.getG9());//13
            meterReadingBean.setEReactiveQuad3(xmlParserBean.getG9());//14
            meterReadingBean.setEReactiveQuad4(xmlParserBean.getG9());//15
            meterReadingBean.setEAdjustment(xmlParserBean.getG9());//16
            meterReadingBean.setEMaxDemand(xmlParserBean.getG9());//17
            meterReadingBean.setEKvah(xmlParserBean.getG9());//18
            meterReadingBean.setEAssesment(xmlParserBean.getG9());//19

            meterReadingBean.setITod1(xmlParserBean.getG9());//20
            meterReadingBean.setITod2(xmlParserBean.getG9());//21
            meterReadingBean.setITod3(xmlParserBean.getG9());//22
            meterReadingBean.setITod4(xmlParserBean.getG9());//23
            meterReadingBean.setIActiveEnergy(xmlParserBean.getG9());//24
            meterReadingBean.setIReactiveQuad1(xmlParserBean.getG9());//25
            meterReadingBean.setIReactiveQuad2(xmlParserBean.getG9());//26
            meterReadingBean.setIReactiveQuad3(xmlParserBean.getG9());//27
            meterReadingBean.setIReactiveQuad4(xmlParserBean.getG9());//28
            meterReadingBean.setIAdjustment(xmlParserBean.getG9());//29
            meterReadingBean.setIMaxDemand(xmlParserBean.getG9());//30
            meterReadingBean.setIKvah(xmlParserBean.getG9());//31
            meterReadingBean.setIAssesment(xmlParserBean.getG9());//32

            meterReadingBean.setCurrentState("Current setate");//33
            meterReadingBean.setCreatedBy("SHADAB");//34
            meterReadingBean.setUpdatedBy("KHAN");//35
            meterReadingBean.setCreatedOn(null);//36
            meterReadingBean.setUpdatedOn(null);//37
            meterReadingBean.setStatus("status");//38
            meterReadingBean.setRemark("test reamrk");//39
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return meterReadingBean;
    }
}
