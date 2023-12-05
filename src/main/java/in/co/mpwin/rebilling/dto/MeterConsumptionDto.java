package in.co.mpwin.rebilling.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import in.co.mpwin.rebilling.beans.readingbean.MeterReadingBean;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Digits;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

@Getter
@Setter @ToString
@NoArgsConstructor
@AllArgsConstructor
public class MeterConsumptionDto {

    @Column(name="meter_no")@NotNull
    private String meterNo;
    @Column(name="mf")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal mf;
    @Column(name="category")@NotNull
    private String category;
    @Column(name = "reading_date")@NotNull@JsonFormat(timezone = "IST")
    private Date previousReadingDate;
    @Column(name = "reading_date")@NotNull@JsonFormat(timezone = "IST")
    private Date currentReadingDate;
    @Column(name = "month_year")@NotNull
    private String monthYear;

//    @Column(name = "end_date")@NotNull@JsonFormat(timezone = "IST")
//    private Date endDate;
//    @Column(name="reading_type")@NotNull
//    private String readingType;
//    @Column(name="read_source")@NotNull
//    private String readSource;

    @Column(name="e_previous_active_energy")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal ePreviousActiveEnergy;
    @Column(name="e_previous_assesment")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal ePreviousAssesment;
    @Column(name="e_previous_kvah")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal ePreviousKvah;
    @Column(name="e_previous_max_demand")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal ePreviousMaxDemand;
    @Column(name="e_previous_tod1")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal ePreviousTod1;
    @Column(name="e_previous_tod2")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal ePreviousTod2;
    @Column(name="e_previous_tod3")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal ePreviousTod3;
    @Column(name="e_previous_tod4")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal ePreviousTod4;
    @Column(name="e_previous_reactive_quad1")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal ePreviousReactiveQuad1;
    @Column(name="e_previous_reactive_quad2")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal ePreviousReactiveQuad2;
    @Column(name="e_previous_reactive_quad3")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal ePreviousReactiveQuad3;
    @Column(name="e_previous_reactive_quad4")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal ePreviousReactiveQuad4;
    @Column(name="e_current_active_energy")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal eCurrentActiveEnergy;
    @Column(name="e_current_assesment")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal eCurrentAssesment;
    @Column(name="e_current_kvah")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal eCurrentKvah;
    @Column(name="e_current_max_demand")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal eCurrentMaxDemand;
    @Column(name="e_current_tod1")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal eCurrentTod1;
    @Column(name="e_current_tod2")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal eCurrentTod2;
    @Column(name="e_current_tod3")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal eCurrentTod3;
    @Column(name="e_current_tod4")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal eCurrentTod4;
    @Column(name="e_current_reactive_quad1")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal eCurrentReactiveQuad1;
    @Column(name="e_current_reactive_quad2")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal eCurrentReactiveQuad2;
    @Column(name="e_current_reactive_quad3")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal eCurrentReactiveQuad3;
    @Column(name="e_current_reactive_quad4")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal eCurrentReactiveQuad4;
    @Column(name="e_diff_active_energy")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal eDiffActiveEnergy;
    @Column(name="e_diff_assesment")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal eDiffAssesment;
    @Column(name="e_diff_kvah")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal eDiffKvah;
    @Column(name="e_diff_max_demand")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal eDiffMaxDemand;
    @Column(name="e_diff_tod1")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal eDiffTod1;
    @Column(name="e_diff_tod2")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal eDiffTod2;
    @Column(name="e_diff_tod3")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal eDiffTod3;
    @Column(name="e_diff_tod4")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal eDiffTod4;
    @Column(name="e_diff_reactive_quad1")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal eDiffReactiveQuad1;
    @Column(name="e_diff_reactive_quad2")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal eDiffReactiveQuad2;
    @Column(name="e_diff_reactive_quad3")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal eDiffReactiveQuad3;
    @Column(name="e_diff_reactive_quad4")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal eDiffReactiveQuad4;

    @Column(name="e_consumption_active_energy")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal eConsumptionActiveEnergy;
    @Column(name="e_consumption_assesment")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal eConsumptionAssesment;
    @Column(name="e_consumption_kvah")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal eConsumptionKvah;
    @Column(name="e_consumption_max_demand")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal eConsumptionMaxDemand;
    @Column(name="e_consumption_tod1")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal eConsumptionTod1;
    @Column(name="e_consumption_tod2")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal eConsumptionTod2;
    @Column(name="e_consumption_tod3")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal eConsumptionTod3;
    @Column(name="e_consumption_tod4")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal eConsumptionTod4;
    @Column(name="e_consumption_reactive_quad1")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal eConsumptionReactiveQuad1;
    @Column(name="e_consumption_reactive_quad2")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal eConsumptionReactiveQuad2;
    @Column(name="e_consumption_reactive_quad3")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal eConsumptionReactiveQuad3;
    @Column(name="e_consumption_reactive_quad4")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal eConsumptionReactiveQuad4;

    @Column(name="e_adjustment")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal eAdjustment;
    @Column(name="i_adjustment")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iAdjustment;

    @Column(name="i_previous_active_energy")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iPreviousActiveEnergy;
    @Column(name="i_previous_assesment")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iPreviousAssesment;
    @Column(name="i_previous_kvah")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iPreviousKvah;
    @Column(name="i_previous_max_demand")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iPreviousMaxDemand;
    @Column(name="i_previous_tod1")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iPreviousTod1;
    @Column(name="i_previous_tod2")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iPreviousTod2;
    @Column(name="i_previous_tod3")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iPreviousTod3;
    @Column(name="i_previous_tod4")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iPreviousTod4;
    @Column(name="i_previous_reactive_quad1")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iPreviousReactiveQuad1;
    @Column(name="i_previous_reactive_quad2")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iPreviousReactiveQuad2;
    @Column(name="i_previous_reactive_quad3")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iPreviousReactiveQuad3;
    @Column(name="i_previous_reactive_quad4")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iPreviousReactiveQuad4;
    @Column(name="i_current_active_energy")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iCurrentActiveEnergy;
    @Column(name="i_current_assesment")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iCurrentAssesment;
    @Column(name="i_current_kvah")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iCurrentKvah;
    @Column(name="i_current_max_demand")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iCurrentMaxDemand;
    @Column(name="i_current_tod1")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iCurrentTod1;
    @Column(name="i_current_tod2")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iCurrentTod2;
    @Column(name="i_current_tod3")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iCurrentTod3;
    @Column(name="i_current_tod4")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iCurrentTod4;
    @Column(name="i_current_reactive_quad1")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iCurrentReactiveQuad1;
    @Column(name="i_current_reactive_quad2")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iCurrentReactiveQuad2;
    @Column(name="i_current_reactive_quad3")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iCurrentReactiveQuad3;
    @Column(name="i_current_reactive_quad4")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iCurrentReactiveQuad4;
    @Column(name="i_diff_active_energy")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iDiffActiveEnergy;
    @Column(name="i_diff_assesment")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iDiffAssesment;
    @Column(name="i_diff_kvah")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iDiffKvah;
    @Column(name="i_diff_max_demand")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iDiffMaxDemand;
    @Column(name="i_diff_tod1")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iDiffTod1;
    @Column(name="i_diff_tod2")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iDiffTod2;
    @Column(name="i_diff_tod3")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iDiffTod3;
    @Column(name="i_diff_tod4")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iDiffTod4;
    @Column(name="i_diff_reactive_quad1")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iDiffReactiveQuad1;
    @Column(name="i_diff_reactive_quad2")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iDiffReactiveQuad2;
    @Column(name="i_diff_reactive_quad3")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iDiffReactiveQuad3;
    @Column(name="i_diff_reactive_quad4")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iDiffReactiveQuad4;

    @Column(name="i_consumption_active_energy")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iConsumptionActiveEnergy;
    @Column(name="i_consumption_assesment")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iConsumptionAssesment;
    @Column(name="i_consumption_kvah")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iConsumptionKvah;
    @Column(name="i_consumption_max_demand")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iConsumptionMaxDemand;
    @Column(name="i_consumption_tod1")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iConsumptionTod1;
    @Column(name="i_consumption_tod2")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iConsumptionTod2;
    @Column(name="i_consumption_tod3")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iConsumptionTod3;
    @Column(name="i_consumption_tod4")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iConsumptionTod4;
    @Column(name="i_consumption_reactive_quad1")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iConsumptionReactiveQuad1;
    @Column(name="i_consumption_reactive_quad2")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iConsumptionReactiveQuad2;
    @Column(name="i_consumption_reactive_quad3")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iConsumptionReactiveQuad3;
    @Column(name="i_consumption_reactive_quad4")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal iConsumptionReactiveQuad4;

    //Grand KWH = ((Current KWH - Previous KWH) * MF ) + KWH Assesment - KWH Adjustment
    @Column(name="grand_kwh_export")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal grandKwhExport;
    @Column(name="grand_kwh_import")@NotNull //@Digits(integer = 14,fraction = 6)
    private BigDecimal grandKwhImport;

    public MeterConsumptionDto setMeterConsumptionDto(MeterReadingBean previous, MeterReadingBean current,BigDecimal mf) {

        MeterConsumptionDto meterConsumptionDto = new MeterConsumptionDto();
        meterConsumptionDto.setMeterNo(current.getMeterNo());
        meterConsumptionDto.setMf(mf);
        meterConsumptionDto.setPreviousReadingDate(previous.getReadingDate());
        meterConsumptionDto.setCurrentReadingDate(current.getReadingDate());

        meterConsumptionDto.setEAdjustment(current.getEAdjustment());
        meterConsumptionDto.setIAdjustment(current.getIAdjustment());

        meterConsumptionDto.setEPreviousActiveEnergy(previous.getEActiveEnergy());
        meterConsumptionDto.setEPreviousAssesment(previous.getEAssesment());
        meterConsumptionDto.setEPreviousKvah(previous.getEKvah());
        meterConsumptionDto.setEPreviousMaxDemand(previous.getEMaxDemand());
        meterConsumptionDto.setEPreviousTod1(previous.getETod1());
        meterConsumptionDto.setEPreviousTod2(previous.getETod2());
        meterConsumptionDto.setEPreviousTod3(previous.getETod3());
        meterConsumptionDto.setEPreviousTod4(previous.getETod4());
        meterConsumptionDto.setEPreviousReactiveQuad1(previous.getEReactiveQuad1());
        meterConsumptionDto.setEPreviousReactiveQuad2(previous.getEReactiveQuad2());
        meterConsumptionDto.setEPreviousReactiveQuad3(previous.getEReactiveQuad3());
        meterConsumptionDto.setEPreviousReactiveQuad4(previous.getEReactiveQuad4());

        meterConsumptionDto.setECurrentActiveEnergy(current.getEActiveEnergy());
        meterConsumptionDto.setECurrentAssesment(current.getEAssesment());
        meterConsumptionDto.setECurrentKvah(current.getEKvah());
        meterConsumptionDto.setECurrentMaxDemand(current.getEMaxDemand());
        meterConsumptionDto.setECurrentTod1(current.getETod1());
        meterConsumptionDto.setECurrentTod2(current.getETod2());
        meterConsumptionDto.setECurrentTod3(current.getETod3());
        meterConsumptionDto.setECurrentTod4(current.getETod4());
        meterConsumptionDto.setECurrentReactiveQuad1(current.getEReactiveQuad1());
        meterConsumptionDto.setECurrentReactiveQuad2(current.getEReactiveQuad2());
        meterConsumptionDto.setECurrentReactiveQuad3(current.getEReactiveQuad3());
        meterConsumptionDto.setECurrentReactiveQuad4(current.getEReactiveQuad4());
        meterConsumptionDto.setECurrentActiveEnergy(current.getEActiveEnergy());

        meterConsumptionDto.setEDiffActiveEnergy(current.getEActiveEnergy().subtract(previous.getEActiveEnergy()));
        meterConsumptionDto.setEDiffAssesment(current.getEAssesment());
        meterConsumptionDto.setEDiffKvah(current.getEKvah().subtract(previous.getEKvah()));
        meterConsumptionDto.setEDiffMaxDemand(BigDecimal.valueOf(0));
        meterConsumptionDto.setEDiffTod1(current.getETod1().subtract(previous.getETod1()));
        meterConsumptionDto.setEDiffTod2(current.getETod2().subtract(previous.getETod2()));
        meterConsumptionDto.setEDiffTod3(current.getETod3().subtract(previous.getETod3()));
        meterConsumptionDto.setEDiffTod4(current.getETod4().subtract(previous.getETod4()));
        meterConsumptionDto.setEDiffReactiveQuad1(current.getEReactiveQuad1().subtract(previous.getEReactiveQuad1()));
        meterConsumptionDto.setEDiffReactiveQuad2(current.getEReactiveQuad2().subtract(previous.getEReactiveQuad2()));
        meterConsumptionDto.setEDiffReactiveQuad3(current.getEReactiveQuad3().subtract(previous.getEReactiveQuad3()));
        meterConsumptionDto.setEDiffReactiveQuad4(current.getEReactiveQuad4().subtract(previous.getEReactiveQuad4()));

        meterConsumptionDto.setEConsumptionActiveEnergy(meterConsumptionDto.getEDiffActiveEnergy().multiply(mf).setScale(2, RoundingMode.HALF_DOWN));
        meterConsumptionDto.setEConsumptionAssesment(meterConsumptionDto.getEDiffAssesment());
        meterConsumptionDto.setEConsumptionKvah(meterConsumptionDto.getEDiffKvah().multiply(mf).setScale(2, RoundingMode.HALF_DOWN));
        meterConsumptionDto.setEConsumptionMaxDemand(meterConsumptionDto.getECurrentMaxDemand().multiply(mf).setScale(2, RoundingMode.HALF_DOWN));
        meterConsumptionDto.setEConsumptionTod1(meterConsumptionDto.getEDiffTod1().multiply(mf).setScale(2, RoundingMode.HALF_DOWN));
        meterConsumptionDto.setEConsumptionTod2(meterConsumptionDto.getEDiffTod2().multiply(mf).setScale(2, RoundingMode.HALF_DOWN));
        meterConsumptionDto.setEConsumptionTod3(meterConsumptionDto.getEDiffTod3().multiply(mf).setScale(2, RoundingMode.HALF_DOWN));
        meterConsumptionDto.setEConsumptionTod4(meterConsumptionDto.getEDiffTod4().multiply(mf).setScale(2, RoundingMode.HALF_DOWN));
        meterConsumptionDto.setEConsumptionReactiveQuad1(meterConsumptionDto.getEDiffReactiveQuad1().multiply(mf).setScale(2, RoundingMode.HALF_DOWN));
        meterConsumptionDto.setEConsumptionReactiveQuad2(meterConsumptionDto.getEDiffReactiveQuad2().multiply(mf).setScale(2, RoundingMode.HALF_DOWN));
        meterConsumptionDto.setEConsumptionReactiveQuad3(meterConsumptionDto.getEDiffReactiveQuad3().multiply(mf).setScale(2, RoundingMode.HALF_DOWN));
        meterConsumptionDto.setEConsumptionReactiveQuad4(meterConsumptionDto.getEDiffReactiveQuad4().multiply(mf).setScale(2, RoundingMode.HALF_DOWN));

        meterConsumptionDto.setGrandKwhExport((meterConsumptionDto.getEConsumptionActiveEnergy()
                                        .add(meterConsumptionDto.getEConsumptionAssesment()))
                                        .subtract(meterConsumptionDto.getEAdjustment()));

        meterConsumptionDto.setIPreviousActiveEnergy(previous.getIActiveEnergy());
        meterConsumptionDto.setIPreviousAssesment(previous.getIAssesment());
        meterConsumptionDto.setIPreviousKvah(previous.getIKvah());
        meterConsumptionDto.setIPreviousMaxDemand(previous.getIMaxDemand());
        meterConsumptionDto.setIPreviousTod1(previous.getITod1());
        meterConsumptionDto.setIPreviousTod2(previous.getITod2());
        meterConsumptionDto.setIPreviousTod3(previous.getITod3());
        meterConsumptionDto.setIPreviousTod4(previous.getITod4());
        meterConsumptionDto.setIPreviousReactiveQuad1(previous.getIReactiveQuad1());
        meterConsumptionDto.setIPreviousReactiveQuad2(previous.getIReactiveQuad2());
        meterConsumptionDto.setIPreviousReactiveQuad3(previous.getIReactiveQuad3());
        meterConsumptionDto.setIPreviousReactiveQuad4(previous.getIReactiveQuad4());

        meterConsumptionDto.setICurrentActiveEnergy(current.getIActiveEnergy());
        meterConsumptionDto.setICurrentAssesment(current.getIAssesment());
        meterConsumptionDto.setICurrentKvah(current.getIKvah());
        meterConsumptionDto.setICurrentMaxDemand(current.getIMaxDemand());
        meterConsumptionDto.setICurrentTod1(current.getITod1());
        meterConsumptionDto.setICurrentTod2(current.getITod2());
        meterConsumptionDto.setICurrentTod3(current.getITod3());
        meterConsumptionDto.setICurrentTod4(current.getITod4());
        meterConsumptionDto.setICurrentReactiveQuad1(current.getIReactiveQuad1());
        meterConsumptionDto.setICurrentReactiveQuad2(current.getIReactiveQuad2());
        meterConsumptionDto.setICurrentReactiveQuad3(current.getIReactiveQuad3());
        meterConsumptionDto.setICurrentReactiveQuad4(current.getIReactiveQuad4());

        meterConsumptionDto.setIDiffActiveEnergy(current.getIActiveEnergy().subtract(previous.getIActiveEnergy()));
        meterConsumptionDto.setIDiffAssesment(current.getIAssesment());
        meterConsumptionDto.setIDiffKvah(current.getIKvah().subtract(previous.getIKvah()));
        meterConsumptionDto.setIDiffMaxDemand(BigDecimal.valueOf(0));
        meterConsumptionDto.setIDiffTod1(current.getITod1().subtract(previous.getITod1()));
        meterConsumptionDto.setIDiffTod2(current.getITod2().subtract(previous.getITod2()));
        meterConsumptionDto.setIDiffTod3(current.getITod3().subtract(previous.getITod3()));
        meterConsumptionDto.setIDiffTod4(current.getITod4().subtract(previous.getITod4()));
        meterConsumptionDto.setIDiffReactiveQuad1(current.getIReactiveQuad1().subtract(previous.getIReactiveQuad1()));
        meterConsumptionDto.setIDiffReactiveQuad2(current.getIReactiveQuad2().subtract(previous.getIReactiveQuad2()));
        meterConsumptionDto.setIDiffReactiveQuad3(current.getIReactiveQuad3().subtract(previous.getIReactiveQuad3()));
        meterConsumptionDto.setIDiffReactiveQuad4(current.getIReactiveQuad4().subtract(previous.getIReactiveQuad4()));

        meterConsumptionDto.setIConsumptionActiveEnergy(meterConsumptionDto.getIDiffActiveEnergy().multiply(mf).setScale(2, RoundingMode.HALF_DOWN));
        meterConsumptionDto.setIConsumptionAssesment(meterConsumptionDto.getIDiffAssesment());
        meterConsumptionDto.setIConsumptionKvah(meterConsumptionDto.getIDiffKvah().multiply(mf).setScale(2, RoundingMode.HALF_DOWN));
        meterConsumptionDto.setIConsumptionMaxDemand(meterConsumptionDto.getICurrentMaxDemand().multiply(mf).setScale(2, RoundingMode.HALF_DOWN));
        meterConsumptionDto.setIConsumptionTod1(meterConsumptionDto.getIDiffTod1().multiply(mf).setScale(2, RoundingMode.HALF_DOWN));
        meterConsumptionDto.setIConsumptionTod2(meterConsumptionDto.getIDiffTod2().multiply(mf).setScale(2, RoundingMode.HALF_DOWN));
        meterConsumptionDto.setIConsumptionTod3(meterConsumptionDto.getIDiffTod3().multiply(mf).setScale(2, RoundingMode.HALF_DOWN));
        meterConsumptionDto.setIConsumptionTod4(meterConsumptionDto.getIDiffTod4().multiply(mf).setScale(2, RoundingMode.HALF_DOWN));
        meterConsumptionDto.setIConsumptionReactiveQuad1(meterConsumptionDto.getIDiffReactiveQuad1().multiply(mf).setScale(2, RoundingMode.HALF_DOWN));
        meterConsumptionDto.setIConsumptionReactiveQuad2(meterConsumptionDto.getIDiffReactiveQuad2().multiply(mf).setScale(2, RoundingMode.HALF_DOWN));
        meterConsumptionDto.setIConsumptionReactiveQuad3(meterConsumptionDto.getIDiffReactiveQuad3().multiply(mf).setScale(2, RoundingMode.HALF_DOWN));
        meterConsumptionDto.setIConsumptionReactiveQuad4(meterConsumptionDto.getIDiffReactiveQuad4().multiply(mf).setScale(2, RoundingMode.HALF_DOWN));

        meterConsumptionDto.setGrandKwhImport((meterConsumptionDto.getIConsumptionActiveEnergy()
                .add(meterConsumptionDto.getIConsumptionAssesment()))
                .subtract(meterConsumptionDto.getIAdjustment()));

        return meterConsumptionDto;
    }
}
