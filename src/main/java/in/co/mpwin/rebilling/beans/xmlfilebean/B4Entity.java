package in.co.mpwin.rebilling.beans.xmlfilebean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class B4Entity {
    private String value;
    private String unit;
    private String paramcode;
    private String tod;


    @Override
    public String toString() {
        return "B4Entity{" +
                "value='" + value + '\'' +
                ", unit='" + unit + '\'' +
                ", paramcode='" + paramcode + '\'' +
                ", tod='" + tod + '\'' +
                '}';
    }
}
