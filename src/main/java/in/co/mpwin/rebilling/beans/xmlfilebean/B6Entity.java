package in.co.mpwin.rebilling.beans.xmlfilebean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
