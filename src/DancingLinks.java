import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class DancingLinks {
	public int[][] solution;
	public boolean empty = false;
	
	private int size = 9;
    private ColumnObject header;
    private Stack<TrueCell> resultSet;
    
    public DancingLinks(ExactCell[][] cover, int size) {
    	this.size = size;
        header = initDancingBoard(cover);
        resultSet = new Stack<>();
    }
    
    public void search(int k) {
        if (header.R == header) {
        	solution = mapToSudoku(resultSet);
        	if (empty) throw new RuntimeException();
        } else {
            ColumnObject c = chooseColumnObject();
            c.cover();
            
            TrueCell i = c.D;
            while(i != c) {
            	resultSet.push(i);
                for (TrueCell j = i.R; j != i; j = j.R) {
                    j.C.cover();
                }
                
                search(k + 1);
                i = resultSet.pop();
                c = i.C;
                
                TrueCell j = i.L;
                while(j != i) {
                	j.C.uncover();
                	j = j.L;
                }
                i = i.D;
            }
            c.uncover();
        }
    }
    
    private ColumnObject initDancingBoard(ExactCell[][] coverMatrix) {
        int numCol = coverMatrix[0].length;
        //System.out.println(numCol);

        ColumnObject head = new ColumnObject(-1);
        List<ColumnObject> columnObjects = new ArrayList<>();

        for (int i = 0; i < numCol; i++) {
            ColumnObject n = new ColumnObject(i);
            columnObjects.add(n);
            linkToRightSide(head, n);
            head = n;
        }
        head = head.R.C;

        for (ExactCell[] row : coverMatrix) {
            TrueCell prev = null;
            //System.out.println(row);
            for (int i = 0; i < numCol; i++) {
            	ExactCell cell = row[i];
                if (cell != null && cell.val == 1) {
                    ColumnObject c = columnObjects.get(i);
                    TrueCell node = new TrueCell(c);
                    node.row = cell.row;
                    node.col = cell.col;
                    node.num = cell.num;
                    if (prev == null)
                        prev = node;
                    linkToDownSide(c.U, node);
                    linkToRightSide(prev, node);
                    prev = node;
                    c.size += 1;
                }
            }
        }
        head.size = numCol;
        return head;
    }
    
    private void linkToRightSide(TrueCell curr, TrueCell right) {
        right.L = curr;
        curr.R.L = right;
        right.R = curr.R;
        curr.R = right;
    }
    
    private void linkToDownSide(TrueCell curr, TrueCell down) {
        down.U = curr;
        curr.D.U = down;
        down.D = curr.D;
        curr.D = down;
    }

    private ColumnObject chooseColumnObject() {
        int s = Integer.MAX_VALUE;
        ColumnObject c = null;
        ColumnObject j = (ColumnObject) header.R;
        while(j != header) {
        	if (j.size < s) {
                s = j.size;
                c = j;
        	}
        	j = (ColumnObject) j.R;
        }
        return c;
    }

    private int[][] mapToSudoku(Stack<TrueCell> answer) {
        int[][] sudoku = new int[size][size];
        List<TrueCell> answers = new ArrayList<>(answer);
        for (TrueCell cell : answers) {
        	//System.out.println(String.format("(%s,%s) = %s", cell.row, cell.col, cell.num));
        	sudoku[cell.row-1][cell.col-1] = cell.num;
        }
        return sudoku;
    }
}
