package in.co.mpwin.rebilling.beans.xmlfilebean;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class B6Entity {
    private String value;
    private String unit;
    private String paramcode;
    private String tod;
    private String occdate;


    @Override
    public String toString() {
        return "B6Entity{" +
                "value='" + value + '\'' +
                ", unit='" + unit + '\'' +
                ", paramcode='" + paramcode + '\'' +
                ", tod='" + tod + '\'' +
                ", occdate='" + occdate + '\'' +
                '}';
    }
}
