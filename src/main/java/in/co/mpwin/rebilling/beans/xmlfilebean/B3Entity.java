package in.co.mpwin.rebilling.beans.xmlfilebean;

import lombok.*;

@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
public class B3Entity {
    private String value;
    private String unit;
    private String paramcode;


    @Override
    public String toString() {
        return "B3Entity{" +
                "value='" + value + '\'' +
                ", unit='" + unit + '\'' +
                ", paramcode='" + paramcode + '\'' +
                '}';
    }
}
