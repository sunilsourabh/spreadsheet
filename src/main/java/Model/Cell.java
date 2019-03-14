package Model;

import Utils.CellUtils;

import java.util.HashSet;

public class Cell
{
    private double _cellValue;
    private Boolean _isExpression;
    private int _rowId;
    private int _colId;
    private String _expression;
    private HashSet<Cell> _dependencies;
    private boolean _isResolved;

    public String getToken() {
        return token;
    }

    private String token;

    public void set_isResolved(boolean _isResolved) {
        this._isResolved = _isResolved;
    }



    public boolean is_isResolved() {
        return _isResolved;
    }


    public double get_cellValue() {
        return _cellValue;
    }

    public void set_cellValue(double _cellValue) {
        this._cellValue = _cellValue;
    }

    public Boolean get_isExpression() {
        return _isExpression;
    }

    public void set_isExpression(Boolean _isExpression) {
        this._isExpression = _isExpression;
    }

    public int get_rowId() {
        return _rowId;
    }

    public void set_rowId(int _rowId) {
        this._rowId = _rowId;
    }

    public int get_colId() {
        return _colId;
    }

    public void set_colId(int _colId) {
        this._colId = _colId;
    }

    public String get_expression() {
        if(_expression== null) return  "";
        return _expression;
    }

    public void set_expression(String _expression) {
        this._expression = _expression;
    }

    public HashSet<Cell> get_dependencies() {
        return _dependencies;
    }

    public void set_dependencies(HashSet<Cell> _dependencies) {
        this._dependencies = _dependencies;
    }

    public void addDependency(Cell dependentCell){
        if(this._dependencies==null)
            this._dependencies = new HashSet<>();
        this._dependencies.add(dependentCell);
    }

    public static Cell create(int row, int col,String token, Object element){
        Cell cell = new Cell();
        cell.set_rowId(row);
        cell.set_colId(col);
        cell.token = token;
        setValue(cell,element);
        return cell;
    }

    public static void setValue(Cell cell, Object element){
        Boolean isExpr= CellUtils.Utils.isExpression(element);
        cell.set_isExpression(isExpr);
        if (isExpr){
            cell.set_expression(element.toString());
        }else {
            cell.set_cellValue(Integer.valueOf(element.toString()));
            cell.set_isResolved(true);
        }
    }

    public boolean allDependenciesResolved(){

        if(_dependencies== null) return false;
        if(_dependencies.isEmpty()) return false;
        return !_dependencies.stream().anyMatch(dep -> dep.is_isResolved()==false);
    }



    @Override
    public String toString() {
        return  CellUtils.Utils.buildKey(get_rowId(), get_colId());
    }



}
