package in.co.mpwin.rebilling.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter @ToString
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceInvestorDto {

    private String plantCode;
    private String plantName;
    private String meterNumber;
    private String investorCode;
    private String ppwaNo;
    private String investorName;
    private BigDecimal bifurcateTotalKwhConsumption;
    private BigDecimal bifurcateInvestorKwhConsumption;
    private String invoiceNumber;
    private String invoiceDate;
    private BigDecimal invoiceAmount;
    private String invoiceStage;

}
