public class ColumnObject extends TrueCell{
    public int size;
    public int name;

    public ColumnObject(int n) {
    	super(null);
        size = 0;
        name = n;
        C = this;
    }

    public void cover() {
    	L.R = R;
        R.L = L;
        TrueCell i = D;
        while(i != this) {
    		TrueCell j = i.R;
    		while(j != i) {
    			j.D.U = j.U;
                j.U.D = j.D;
                j.C.size -= 1;
                j = j.R;
    		}
    		i = i.D;
    	}
    }

    public void uncover() {
    	TrueCell i = U;
    	while(i != this) {
    		TrueCell j = i.L;
    		while(j != i) {
    			j.C.size += 1;
    			j.D.U = j;
    			j.U.D = j;
    			j = j.L;
    		}
    		i = i.U;
    	}
    	R.L = this;
    	L.R = this;
    }
}