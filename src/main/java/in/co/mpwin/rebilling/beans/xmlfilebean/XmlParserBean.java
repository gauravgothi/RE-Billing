package in.co.mpwin.rebilling.beans.xmlfilebean;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

//@Entity
@Getter
@Setter @ToString
@NoArgsConstructor
@AllArgsConstructor
//@Table(name = "parsed_data_d1")
public class XmlParserBean {

    /*@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;*/
    private DataEntityD1 dataEntityD1;
    private DataEntityD301 dataEntityD301;

    // Constructors, getters, and setters
}
