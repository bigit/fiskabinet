package ru.antelit.fiskabinet.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;
import ru.antelit.fiskabinet.domain.Kkm;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
public class KkmDto {
    private Integer id;
    private String innerName;

    @NotNull(message = "Не указана модель")
    @Range(min = 1, message = "Не указан модель")
    private Integer modelId;

    private Integer vendorId;

    @NotBlank(message = "Не указан заводской номер")
    @Pattern(regexp = "\\d+", message = "Номер может состоять только из цифр")
    private String serialNumber;

//  @Pattern(regexp = "\\d+", message = "Номер может состоять только из цифр")
    private String fnNumber;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fnEnd;

    private Integer ofdId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate ofdEnd;

    private Integer tradepointId;
    private Integer orgId;

    public static KkmDto create(Kkm kkm) {
        KkmDtoBuilder builder = new KkmDtoBuilder();
        builder.id(kkm.getId())
                .innerName(kkm.getInnerName());

        if (kkm.getSerialNumber() != null) {
            builder.serialNumber(kkm.getSerialNumber());
        }

        if (kkm.getKkmModel() != null) {
            builder
                    .modelId(kkm.getKkmModel().getId())
                    .vendorId(kkm.getKkmModel().getVendor().getId());
        } else {
            builder.modelId(0)
                    .vendorId(0);
        }

        if (kkm.getOfdProvider() != null) {
            builder.ofdId = kkm.getOfdProvider().getId();
        }

        if (kkm.getOfdSubEnd() != null) {
            builder.ofdEnd(kkm.getOfdSubEnd());
        }

        if (kkm.getOrganization() != null) {
            builder.orgId(kkm.getOrganization().getId());
        }
        builder.fnNumber(kkm.getFnNumber())
                .fnEnd(kkm.getFnEnd());
        if (kkm.getTradepoint() != null) {
                builder.tradepointId(kkm.getTradepoint().getId());
        }
        return builder.build();
    }
}
