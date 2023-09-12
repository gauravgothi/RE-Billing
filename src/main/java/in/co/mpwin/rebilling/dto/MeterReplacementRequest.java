package in.co.mpwin.rebilling.dto;

import in.co.mpwin.rebilling.beans.readingbean.MeterReadingBean;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MeterReplacementRequest {

    private MeterReadingBean oldMeterBean;
    private MeterReadingBean newMeterBean;

}
