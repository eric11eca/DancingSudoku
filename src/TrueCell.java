public class TrueCell {
	public ColumnObject C;
    public TrueCell L, R, U, D;
    public int row, col, num;

    public TrueCell(ColumnObject c) {
    	C = c;
    	L = this; 
    	R = this;
    	U = this;
    	D = this;
    }
}