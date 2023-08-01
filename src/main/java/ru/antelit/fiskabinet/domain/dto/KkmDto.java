package ru.antelit.fiskabinet.domain.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.antelit.fiskabinet.domain.Kkm;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
public class KkmDto {
    private Integer id;
    private String innerName;

    @NotNull
    private Integer modelId;

    @NotNull
    private Integer vendorId;

    private String serialNumber;

    private String fnNumber;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fnEnd;

    private Integer tradepointId;

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
        builder.fnNumber(kkm.getFnNumber())
                .fnEnd(kkm.getFnEnd())
                .tradepointId(kkm.getTradepoint().getId());

        return builder.build();
    }
}
