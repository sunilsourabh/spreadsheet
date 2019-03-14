package com.javaworks.SpreadSheet;
import Model.Cell;
import Repository.ICellRepository;
import Service.ICellDependencyService;
import Utils.CellUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class SpreadSheet {

    @Autowired
    private ICellRepository _cellRepository;

    @Autowired
    private ICellDependencyService _cellDependencyService;

    public SpreadSheet() {

    }

    /**
     * Convert line entry in csv file into list of Cells converted and computed together
     * @param rownumber
     * @param rowEntry
     */
    public void transformToCells(final int rownumber, String rowEntry){
        int colnumber =0;
        for(String cellVal:rowEntry.split(","))
        {

            Cell cell = getOrCreate(cellVal, rownumber, colnumber);
            if(cell.get_isExpression()) {
                _cellDependencyService.manageDependencies(cell);
                if (cell.allDependenciesResolved()) {
                    _cellDependencyService.evaluate(cell);
                    _cellDependencyService.resolveDependencies(cell);
                }
            }
            else {
                cell.set_isResolved(true);
                _cellDependencyService.resolveDependencies(cell);
            }
            colnumber++;
        }

    }

    /**
     * This method is used to create a new cell based on the input, if the cell has been pre-created as dependency then take from Hashmap
     * @param cellVal Current cell value, could be number or an expression
     * @param rownumber current row number
     * @param colCounter current column number
     * @return
     */
    private Cell getOrCreate(String cellVal, int rownumber, int colCounter){

        String key = CellUtils.Utils.buildKey(rownumber, colCounter);
        Cell cell;
        if(_cellRepository.containsCellKey(key)) {
            cell = _cellRepository.getSheetCell(key);
            Cell.setValue(cell,cellVal);


        }
        else {
            cell = Cell.create(rownumber,
                    colCounter,
                    CellUtils.Utils.getRowAlphaExprKey(rownumber, colCounter), cellVal);

            _cellRepository.addToSheetCell(key, cell);
        }
        return cell;
    }
    public List<Cell>[] getSheetCells()
    {
        return _cellRepository.get_sheetCells();
    }


}
