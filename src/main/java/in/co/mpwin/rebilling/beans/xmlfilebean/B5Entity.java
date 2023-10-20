package in.co.mpwin.rebilling.beans.xmlfilebean;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class B5Entity {
    private String value;
    private String unit;
    private String paramcode;
    private String occdate;


    @Override
    public String toString() {
        return "B5Entity{" +
                "value='" + value + '\'' +
                ", unit='" + unit + '\'' +
                ", paramcode='" + paramcode + '\'' +
                ", occdate='" + occdate + '\'' +
                '}';
    }
}
