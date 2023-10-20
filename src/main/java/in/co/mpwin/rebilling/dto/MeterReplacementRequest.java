package in.co.mpwin.rebilling.dto;

import in.co.mpwin.rebilling.beans.readingbean.MeterReadingBean;
import lombok.*;

@Getter
@Setter @ToString
@AllArgsConstructor
@NoArgsConstructor
public class MeterReplacementRequest {

    private MeterReadingBean oldMeterBean;
    private MeterReadingBean newMeterBean;

}
