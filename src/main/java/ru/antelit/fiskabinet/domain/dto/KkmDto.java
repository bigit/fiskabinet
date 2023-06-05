package ru.antelit.fiskabinet.domain.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.antelit.fiskabinet.domain.Kkm;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
public class KkmDto {
    private Integer id;
    private String innerName;
    private Integer modelId;
    private Integer vendorId;
    private Long fnNumber;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date fnEnd;
    private Integer tradepointId;

    public static KkmDto create(Kkm kkm) {
        return new KkmDtoBuilder()
                .id(kkm.getId())
                .innerName(kkm.getInnerName())
                .modelId(kkm.getKkmModel().getId())
                .vendorId(kkm.getKkmModel().getVendor().getId())
                .fnNumber(kkm.getFnNumber())
                .fnEnd(kkm.getFnEnd())
                .tradepointId(kkm.getTradepoint().getId())
                .build();
    }
}
