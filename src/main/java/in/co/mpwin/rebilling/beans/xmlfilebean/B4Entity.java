package in.co.mpwin.rebilling.beans.xmlfilebean;

import lombok.*;

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
