import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class DancingSudoku {
	private static int boardSize = 0;
	private static int partitionSize = 0;
	
	public static void main(String[] args){
		String filename = args[0];
		File inputFile = new File(filename);
		Scanner input = null;
		int[][] vals = null;

		int temp = 0;
    	int count = 0;
    	
	    try {
			input = new Scanner(inputFile);
			temp = input.nextInt();
			boardSize = temp;
			partitionSize = (int) Math.sqrt(boardSize);
			System.out.println("Boardsize: " + temp + "x" + temp);
			vals = new int[boardSize][boardSize];			
			
			System.out.println("Input:");
	    	int i = 0;
	    	int j = 0;
	    	while (input.hasNext()){
	    		temp = input.nextInt();
	    		count++;
	    		System.out.printf("%3d", temp);
	    		vals[i][j] = temp;
				j++;
				if (j == boardSize) {
					j = 0;
					i++;
					System.out.println();
				}
				if (j == boardSize) {
					break;
				}
	    	}
	    	input.close();
	    } catch (FileNotFoundException exception) {
	    	System.out.println("Input file not found: " + filename);
	    }
	    
	    if (count != boardSize*boardSize) 
	    	throw new RuntimeException("Incorrect number of inputs.");
	    
	    long startTime = System.nanoTime();
	    DancingSolver dancingSolver = new DancingSolver();
	    if(filename.contains("Empty")) {
	    	vals = dancingSolver.solve(vals, boardSize, true);
	    } else {
	    	vals = dancingSolver.solve(vals, boardSize, false);
	    }
	    long endTime   = System.nanoTime();
	    long totalTime = endTime - startTime;
	    
	    System.out.println("");
	    System.out.println("sovled in: " + totalTime/1000000 + "ms");
	    System.out.println("");

		if (vals == null) {
			System.out.println("No solution found.");
			return;
		} else {
			System.out.println("Output:");
			for (int i = 0; i < boardSize; i++) {
				for (int j = 0; j < boardSize; j++) {
					System.out.printf("%3d", vals[i][j]);
				}
				System.out.println();
			}
			
			StringBuilder builder = new StringBuilder();
			for(int i = 0; i < boardSize; i++) {
				for(int j = 0; j < boardSize; j++) {
				  builder.append(vals[i][j]+"");
				  if(j < boardSize - 1)
				     builder.append(" ");
				}
				builder.append("\n");
			}
			
			int index = filename.indexOf('.');
			String solution = filename.substring(0, index);
			
			BufferedWriter writer;
			try {
				writer = new BufferedWriter(
						new FileWriter(solution + "Solution" + ".txt"));
				writer.write(String.valueOf(boardSize));
				writer.write("\n");
				writer.write(builder.toString());
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void printBoard(int[][] vals, int size) {
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                System.out.print(vals[row][column] + " ");
            }
            System.out.println();
        }
    }
}
