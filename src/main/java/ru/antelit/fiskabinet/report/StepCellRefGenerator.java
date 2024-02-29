package ru.antelit.fiskabinet.report;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jxls.command.CellRefGenerator;
import org.jxls.common.CellRef;
import org.jxls.common.Context;

@AllArgsConstructor
@Log4j2
public class StepCellRefGenerator implements CellRefGenerator {

    private final String sheetName;
    private final Integer startRow;
    private final Integer startCol;
    private final Integer length;
    private final Integer offset;

    public StepCellRefGenerator(String sheetName, Integer startRow, Integer startCol, Integer length) {
        this.sheetName = sheetName;
        this.startRow = startRow;
        this.startCol = startCol;
        this.length = length;
        this.offset = 0;
    }

    @Override
    public CellRef generateCellRef(int index, Context context) {
        int row = startRow + (index / length) * 2;
        int col = startCol + index % length;
        CellRef cellRef = new CellRef( sheetName, row, offset + col * 3);
        log.info("CellRef: {}", cellRef.toString());
        return cellRef;
    }
}
