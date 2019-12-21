import java.util.Arrays;

public class DancingSolver {
	public int[][] solution;
	
    private int boardSize;
    private int blockSize;
    private int colIndex;
    private ExactCell[][] coverMatrix;
    private DancingLinks dancing;

    public int[][] solve(int[][] board, int size, boolean empty) {
    	boardSize = size;
    	blockSize = (int) Math.sqrt(boardSize);
    	colIndex = 0;
    	createCoverMatrix();
    	fillUpTheRest(board);
        dancing = new DancingLinks(coverMatrix, boardSize);
        if(empty) dancing.empty = true;
        try{
        	dancing.search(0);
        } catch(RuntimeException e) {
        	System.out.println(e.getMessage());
        	return dancing.solution;
        }
        return dancing.solution;
    }

    private int rowIndex(int row, int col, int num) {
    	int rowNum = (int) ((row - 1) * Math.pow(boardSize, 2)) 
    			+ (col - 1) * boardSize + num;
        return rowNum-1;
    }

    private void createCoverMatrix() {
    	int row = (int) Math.pow(boardSize, 3);
    	int col = (int) (Math.pow(boardSize, 2)*4);
    	coverMatrix = new ExactCell[row][col];
    	cellLineConstraints();
        blockConstraints();
    }
    
    private void fillUpTheRest(int[][] board) {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                int num = board[i][j];
                if (num != 0) {
                    for (int n = 1; n <= boardSize; n++) {
                        if (n != num) {
                        	int index = rowIndex(i+1, j+1, n);
                            Arrays.fill(coverMatrix[index], null);
                        }
                    }
                }
            }
        }
    }
    
    private void cellLineConstraints() {
    	int rowConstrain = (int) Math.pow(boardSize, 2);
    	int colConstrain = rowConstrain*2;
        for (int i = 1; i <= boardSize; i++) {
            for (int j = 1; j <= boardSize; j++) {
                for (int n = 1; n <= boardSize; n++) {
                	ExactCell c1 = new ExactCell(1, i, j, n);
                	ExactCell c2 = new ExactCell(1, i, n, j);
                	ExactCell c3 = new ExactCell(1, n, i, j);
                    coverMatrix[rowIndex(i, j, n)][colIndex] = c1;
                    coverMatrix[rowIndex(i, n, j)][colIndex+rowConstrain] = c2;
                    coverMatrix[rowIndex(n, i, j)][colIndex+colConstrain] = c3;
                }
                colIndex++;
            }
        }
        colIndex = (int) (Math.pow(boardSize, 2)*3);
    }


    private void blockConstraints() {
        for (int i = 1; i <= boardSize; i += blockSize) {
            for (int j = 1; j <= boardSize; j += blockSize) {
                for (int n = 1; n <= boardSize; n++) {
                    for (int r = 0; r < blockSize; r++) {
                        for (int c = 0; c < blockSize; c++) {
                            int index = rowIndex(i + r, j + c, n);
                            ExactCell c1 = new ExactCell(1, i+r, j+c, n);
                            coverMatrix[index][colIndex] = c1;
                        }
                    }
                    colIndex += 1;
                }
            }
        }
    }
}