package in.co.mpwin.rebilling.beans.xmlfilebean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
