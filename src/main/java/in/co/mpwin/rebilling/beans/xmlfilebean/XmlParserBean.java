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

    private String g1;
    private Date g2;
    private Date g3;
    private Date g4;
    private Integer g7;
    private Integer g8;
    private BigDecimal g9;
    private BigDecimal g10;
    private BigDecimal g11;
    private BigDecimal g12;
    private String g13;
    private String g15;
    private String g17;
    private Integer g19;
    private Integer g20;
    private String g22Code;
    private String g22Name;
    private String g27;
    private BigDecimal g30;
    private String g31;
    private Integer g32;
    private String g33;
    private Integer g1209;
    private Date g1210;
    private Integer g1211;
    private Integer g1212;
    private String g1216;
    private Integer g1233;
    private Integer g1219;
    private Integer g1220;
    private Integer g1221;
    private BigDecimal g1222;
    private Integer g1223;
    private String g1236;
    private Integer g1237;
    private Integer g1239;
    private Integer g1240;

    // Constructors, getters, and setters
}
