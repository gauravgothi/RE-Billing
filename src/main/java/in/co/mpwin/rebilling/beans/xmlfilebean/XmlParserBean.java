package in.co.mpwin.rebilling.beans.xmlfilebean;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

//@Entity
@Getter
@Setter
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
